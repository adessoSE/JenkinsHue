package de.adesso.jenkinshue.service;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.joda.time.DateTime;
import org.json.hue.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.philips.lighting.hue.listener.PHHTTPListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLight.PHLightAlertMode;
import com.philips.lighting.model.PHLightState;

import de.adesso.jenkinshue.Scheduler;
import de.adesso.jenkinshue.common.dto.bridge.BridgeDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampHueDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampNameDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTestDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampTurnOffDTO;
import de.adesso.jenkinshue.common.dto.lamp.LampWithHueUniqueId;
import de.adesso.jenkinshue.common.dto.scenario_config.ScenarioConfigDTO;
import de.adesso.jenkinshue.common.hue.dto.FoundBridgeDTO;
import de.adesso.jenkinshue.common.service.HueService;
import de.adesso.jenkinshue.constant.HueConstants;
import de.adesso.jenkinshue.entity.Bridge;
import de.adesso.jenkinshue.repository.BridgeRepository;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author wennier
 *
 */
@Log4j2
@Primary
@Service
public class HueServiceImpl implements HueService {

	private static final String PRESS_PUSH_LINK_BUTTON = "Push-Link-Taste drücken";
	private static final String CONNECTED = "Verbunden";
	private static final String CONNECTION_LOST = "Verbindung verloren";
	private static final String LAST_CONTACT = "letzter Kontakt";
	
	private static final String REPLACE_PORT_REGEX = ":.*";
	
	@Autowired
	private BridgeRepository bridgeRepository;

	private PHHueSDK phHueSDK;
	private PHSDKListener listener;

	private Map<String, String> bridgeStates = Collections.synchronizedMap(new HashMap<>());
	private List<PHBridge> bridges = Collections.synchronizedList(new ArrayList<PHBridge>());

	@PostConstruct
	public void init() {
		this.phHueSDK = PHHueSDK.getInstance();
		phHueSDK.setAppName("JenkinsHue");
		phHueSDK.setDeviceName("Server");

		initListener();

		phHueSDK.getNotificationManager().registerSDKListener(listener);
	}

	@PreDestroy
	public void destroy() {
		for (PHBridge bridge : bridges) {
			phHueSDK.disableHeartbeat(bridge);
			phHueSDK.disconnect(bridge);
			updateState(bridge, CONNECTION_LOST);
		}
	}
	
	private void updateState(PHBridge bridge, String state) {
		log.debug("updateState(" + removePortAndToLowerCase(bridge.getResourceCache().getBridgeConfiguration().getIpAddress()) + ", " + state + ") [bridge]");
		bridgeStates.put(removePortAndToLowerCase(bridge.getResourceCache().getBridgeConfiguration().getIpAddress()), state);
	}
	
	private void updateState(String ip, String state) {
		log.debug("updateState(" + removePortAndToLowerCase(ip) + ", " + state + ") [ip]");
		bridgeStates.put(removePortAndToLowerCase(ip), state);
	}
	

	public void initListener() {
		listener = new PHSDKListener() {

			@Override
			public void onAccessPointsFound(List<PHAccessPoint> accessPoint) {
				log.debug("onAccessPointsFound Es wird nie nach Bridges gesucht -> nicht benötigt");
			}

			@Override
			public void onAuthenticationRequired(PHAccessPoint accessPoint) {
				phHueSDK.startPushlinkAuthentication(accessPoint);
				log.debug("onAuthenticationRequired(" + accessPoint.getIpAddress() + ")"); // IP ohne Port
				updateState(accessPoint.getIpAddress(), PRESS_PUSH_LINK_BUTTON);
			}

			@Override
			public void onBridgeConnected(PHBridge bridge, String username) {
				String bridgeIpWithoutPort = removePortAndToLowerCase(bridge.getResourceCache().getBridgeConfiguration().getIpAddress());
				log.debug("onBridgeConnected(" + bridgeIpWithoutPort + ", " + username + ")");
				
				bridges.add(bridge);
				updateState(bridge, CONNECTED);
				
				Bridge b = bridgeRepository.findByIp(bridgeIpWithoutPort);
				b.setHueUserName(username);
				b = bridgeRepository.save(b);

				phHueSDK.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL); // 10000 Millis
			}

			@Override
			public void onCacheUpdated(List cacheNotificationsList, PHBridge bridge) {
				if (cacheNotificationsList.contains(PHMessageType.LIGHTS_CACHE_UPDATED)) {
					log.debug("Lights Cache Updated");
				}
			}

			@Override
			public void onConnectionLost(PHAccessPoint accessPoint) {
				updateState(accessPoint.getIpAddress(), CONNECTION_LOST + " (" + DateTime.now().toString("HH:mm") +  " Uhr)");
			}

			@Override
			public void onConnectionResumed(PHBridge bridge) {
				updateState(bridge, CONNECTED + " (" + LAST_CONTACT + ": " + DateTime.now().toString("HH:mm") +  " Uhr)");
			}

			@Override
			public void onError(int code, final String message) {
				log.debug(message + " " + code);

				if (code == PHHueError.BRIDGE_NOT_RESPONDING) {
					log.debug("BRIDGE_NOT_RESPONDING");
				} else if (code == PHMessageType.PUSHLINK_BUTTON_NOT_PRESSED) {
					log.debug("PUSHLINK_BUTTON_NOT_PRESSED");
				} else if (code == PHHueError.AUTHENTICATION_FAILED || code == PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
					log.debug("AUTHENTICATION_FAILED");
					for(Map.Entry<String, String> entry : bridgeStates.entrySet()) {
						if(entry.getValue().equals(PRESS_PUSH_LINK_BUTTON)) {
							bridgeStates.put(entry.getKey(), "Push-Link-Button nicht gedrückt. Bridge entfernen und neu hinzufügen!");
						}
					}
				} else if (code == PHMessageType.BRIDGE_NOT_FOUND) {
					log.debug("BRIDGE_NOT_FOUND");
				}
			}

			@Override
			public void onParsingErrors(List<PHHueParsingError> parsingErrorsList) {
				for (PHHueParsingError parsingError : parsingErrorsList) {
					log.debug("ParsingError : " + parsingError.getMessage());
				}
			}
		};
	}

	@Override
	public void connectToBridgeIfNotAlreadyConnected(BridgeDTO bridge) {
		for (PHBridge b : bridges) {
			if (ipAreEqual(b.getResourceCache().getBridgeConfiguration().getIpAddress(), bridge.getIp())) {
				return;
			}
		}

		PHAccessPoint accessPoint = new PHAccessPoint();
		accessPoint.setIpAddress(bridge.getIp());
		if (bridge.getHueUserName() != null && !bridge.getHueUserName().isEmpty()) {
			accessPoint.setUsername(bridge.getHueUserName());
		}
		phHueSDK.connect(accessPoint);
	}
	
	@Override
	public void disconnectFromBridge(BridgeDTO bridge) {
		for(int i = 0; i < bridges.size(); i++) {
			PHBridge b = bridges.get(i);
			if(ipAreEqual(bridge.getIp(), b.getResourceCache().getBridgeConfiguration().getIpAddress())) {
				bridgeStates.remove(removePortAndToLowerCase(b.getResourceCache().getBridgeConfiguration().getIpAddress()));
				phHueSDK.disableHeartbeat(b);
				phHueSDK.disconnect(b);
				bridges.remove(i);
				break;
			}
		}
	}

	@Override
	public void showRandomColorsOnAllLamps() {
		for (PHBridge bridge : bridges) {
			PHBridgeResourcesCache resourceCache = bridge.getResourceCache();
			List<PHLight> allLights = resourceCache.getAllLights();
			Random rand = new Random();
			for (PHLight light : allLights) {
				PHLightState lightState = new PHLightState();
				lightState.setBrightness(HueConstants.MAX_BRI);
				lightState.setSaturation(HueConstants.MAX_SAT);
				lightState.setHue(rand.nextInt(HueConstants.MAX_HUE + 1));
				bridge.updateLightState(light, lightState);
			}
		}
	}

	@Async
	@Override
	public void updateLamp(LampWithHueUniqueId lamp, ScenarioConfigDTO config) {
		if(config != null) {
			for (PHBridge bridge : bridges) {
				PHBridgeResourcesCache resourceCache = bridge.getResourceCache();
				List<PHLight> allLights = resourceCache.getAllLights();
	
				String url = "http://" + bridge.getResourceCache().getBridgeConfiguration().getIpAddress() + "/api/"
						+ bridge.getResourceCache().getBridgeConfiguration().getUsername() + "/lights";
				
				bridge.doHTTPGet(url, new PHHTTPListener() {
					@Override
					public void onHTTPResponse(String jsonResponse) {
						JSONObject object = new JSONObject(jsonResponse);
						for (PHLight light : allLights) {
							light.setUniqueId(object.optJSONObject(light.getIdentifier()).optString("uniqueid"));
							
							if (light.getUniqueId().equals(lamp.getHueUniqueId())) {
								updateLamp(bridge, light, config);
							}
						}
					}
				});
			}
		}
	}

	private void updateLamp(PHBridge bridge, PHLight light, ScenarioConfigDTO config) {
		PHLightState oldLightState = light.getLastKnownLightState();
		float oldX = oldLightState.getX();
		float oldY = oldLightState.getY();
		
		log.debug("---- old light state: -------");
		log.debug("x: " + oldLightState.getX());
		log.debug("y: " + oldLightState.getY());
		log.debug("on: " + oldLightState.isOn());
		log.debug("brightness: " + oldLightState.getBrightness());
		log.debug("-----------------------------");
		
		PHLightState lightState = new PHLightState();
		lightState.setOn(config.isLampOn());
		
		if(config.isLampOn()) {
			if (config.isOnetimePulsationEnabled()) {
				
				if (config.isOnetimePulsationColorChangeEnabled()) {
					updateLightStateColor(lightState, light.getModelNumber(), config.getOnetimePulsationColorHex());
				} else if(config.isColorChangeEnabled()) {
					updateLightStateColor(lightState, light.getModelNumber(), config.getColorHex());
				}
				
				lightState.setAlertMode(PHLightAlertMode.ALERT_SELECT);
				
				for(int i = 0; i < 3; i++) {
					bridge.updateLightState(light, lightState);
					try {
						Thread.sleep(1333);
					} catch (InterruptedException e) {
						log.error(e);
					}
				}
			}
			
			lightState = new PHLightState();
			lightState.setOn(config.isLampOn());
			
			if(config.isColorChangeEnabled()) {
				updateLightStateColor(lightState, light.getModelNumber(), config.getColorHex());
			} else {
				lightState.setX(oldX);
				lightState.setY(oldY);
			}
			
			if (config.isBrightnessChangeEnabled()) {
				lightState.setBrightness(config.getBrightness());
			}
		}
		
		bridge.updateLightState(light, lightState);
		
		log.debug("---- new light state: -------");
		log.debug("x: " + lightState.getX());
		log.debug("y: " + lightState.getY());
		log.debug("on: " + lightState.isOn());
		log.debug("brightness: " + lightState.getBrightness());
		log.debug("-----------------------------");
	}
	
	@Override
	public List<LampHueDTO> findAllLamps() {
		List<LampHueDTO> lamps = new ArrayList<>();
		
		final CountDownLatch latch;
		if(bridges.size() > 0) {
			latch = new CountDownLatch(bridges.size());
		} else {
			latch = null;
		}
		
		for (PHBridge bridge : bridges) {
			PHBridgeResourcesCache resourceCache = bridge.getResourceCache();
			List<PHLight> allLights = resourceCache.getAllLights();

			String url = "http://" + bridge.getResourceCache().getBridgeConfiguration().getIpAddress() + "/api/"
					+ bridge.getResourceCache().getBridgeConfiguration().getUsername() + "/lights";
			
			bridge.doHTTPGet(url, new PHHTTPListener() {
				@Override
				public void onHTTPResponse(String jsonResponse) {
					JSONObject object = new JSONObject(jsonResponse);
					for (PHLight light : allLights) {
						boolean lampOn = light.getLastKnownLightState().isOn();
						lamps.add(new LampHueDTO(object.optJSONObject(light.getIdentifier()).optString("uniqueid"), light.getName(), light.getLightType().toString(), light.getManufacturerName(), lampOn));
					}
					latch.countDown();
				}
			});
		}
		
		if(latch != null) {
			try {
				latch.await(Scheduler.LATCH_TIMEOUT, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return lamps;
	}
	
	@Override
	public List<FoundBridgeDTO> findAllBridges() {
		return Arrays.asList(new TestRestTemplate().getForObject("https://www.meethue.com/api/nupnp", FoundBridgeDTO[].class));
	}
	
	private void updateLightStateColor(PHLightState lightState, String lightModelNumber, String colorHex) {
		Color color = Color.decode(colorHex); // AWT ist auch OK
		float[] xy = PHUtilities.calculateXYFromRGB(color.getRed(), color.getGreen(), color.getBlue(), lightModelNumber);
		lightState.setX(xy[0]);
		lightState.setY(xy[1]);
	}

	@Override
	public void testScenario(LampTestDTO lamp) {
		if(lamp.getLamps() != null) {
			for(LampNameDTO l : lamp.getLamps()) {
				updateLamp(l, lamp.getScenarioConfig());
			}
		}
	}
	
	// TODO TESTEN
	@Override
	public void pulseOnce(String hueUniqueId) {
		updateLamp(new LampWithHueUniqueId() {
			@Override
			public String getHueUniqueId() {
				return hueUniqueId;
			}
		}, new ScenarioConfigDTO(-1, null, true, true, true, "#FFF", false, null, false, 0));
	}

	@Override
	public void turnOff(LampTurnOffDTO lamp) {
		if(lamp.getLamps() != null) {
			for(LampNameDTO l : lamp.getLamps()) {
				updateLamp(l, new ScenarioConfigDTO(-1, null, false, false, false, null, false, null, false, 0));
			}
			
		}
	}

	@Override
	public void updateBridgeState(List<BridgeDTO> bridgeDTOs) {
		for(BridgeDTO dto : bridgeDTOs) {
			String state = bridgeStates.get(removePortAndToLowerCase(dto.getIp()));
			if(state != null) {
				dto.setState(state);
			}
		}
	}

	@Override
	public boolean ipAreEqual(String ip1, String ip2) {
		if(removePortAndToLowerCase(ip1).equals(removePortAndToLowerCase(ip2))) {
			return true;
		} else {
			return false;
		}
	}
	
	public String removePortAndToLowerCase(String ip) {
		return ip.replaceAll(REPLACE_PORT_REGEX, "").toLowerCase();
	}

}
