package de.adesso.jenkinshue.util;

import java.util.ArrayList;
import java.util.List;

import de.adesso.jenkinshue.common.enumeration.Scenario;
import de.adesso.jenkinshue.constant.HueConstants;
import de.adesso.jenkinshue.entity.ScenarioConfig;

/**
 * 
 * @author wennier
 *
 */
public class ScenarioUtil {
	
	public List<ScenarioConfig> generateDefaultScenarioConfigs() {
		List<ScenarioConfig> list = new ArrayList<>();
		
		ScenarioConfig config16 = new ScenarioConfig();
		config16.setScenario(Scenario.BUILDING);
		config16.setLampOn(true);
		config16.setOnetimePulsationEnabled(true);
		config16.setColorChangeEnabled(true);
		config16.setColorHex("#0000ff");
		config16.setBrightnessChangeEnabled(true);
		config16.setBrightness(HueConstants.MAX_BRI);
		list.add(config16);
		
		ScenarioConfig config15 = new ScenarioConfig();
		config15.setScenario(Scenario.FAILURE);
		config15.setLampOn(true);
		config15.setColorChangeEnabled(true);
		config15.setColorHex("#ff0000");
		config15.setBrightnessChangeEnabled(true);
		config15.setBrightness(HueConstants.MAX_BRI);
		list.add(config15);
		
		ScenarioConfig config14 = new ScenarioConfig();
		config14.setScenario(Scenario.UNSTABLE);
		config14.setLampOn(true);
		config14.setColorChangeEnabled(true);
		config14.setColorHex("#ffff00");
		config14.setBrightnessChangeEnabled(true);
		config14.setBrightness(HueConstants.MAX_BRI);
		list.add(config14);
		
		ScenarioConfig config13 = new ScenarioConfig();
		config13.setScenario(Scenario.SUCCESS);
		config13.setLampOn(true);
		config13.setColorChangeEnabled(true);
		config13.setColorHex("#00ff00");
		config13.setBrightnessChangeEnabled(true);
		config13.setBrightness(HueConstants.MAX_BRI);
		list.add(config13);
		
		/*
		
		// Blau
		
		ScenarioConfig config12 = new ScenarioConfig();
		config12.setScenario(Scenario.BUILDING_AFTER_FAILURE);
		config12.setLampOn(true);
		config12.setOnetimePulsationEnabled(true);
		config12.setColorChangeEnabled(true);
		config12.setColorHex("#0000ff");
		config12.setBrightnessChangeEnabled(true);
		config12.setBrightness(HueConstants.MAX_BRI);
		list.add(config12);
		
		ScenarioConfig config11 = new ScenarioConfig();
		config11.setScenario(Scenario.BUILDING_AFTER_UNSTABLE);
		config11.setLampOn(true);
		config11.setOnetimePulsationEnabled(true);
		config11.setColorChangeEnabled(true);
		config11.setColorHex("#0000ff");
		config11.setBrightnessChangeEnabled(true);
		config11.setBrightness(HueConstants.MAX_BRI);
		list.add(config11);
		
		ScenarioConfig config10 = new ScenarioConfig();
		config10.setScenario(Scenario.BUILDING_AFTER_SUCCESS);
		config10.setLampOn(true);
		config10.setOnetimePulsationEnabled(false);
		config10.setColorChangeEnabled(true);
		config10.setColorHex("#0000ff");
		config10.setBrightnessChangeEnabled(true);
		config10.setBrightness(HueConstants.MAX_BRI);
		list.add(config10);
		
		// Rot
		
		ScenarioConfig config9 = new ScenarioConfig();
		config9.setScenario(Scenario.FAILURE_AFTER_SUCCESS);
		config9.setLampOn(true);
		config9.setOnetimePulsationEnabled(true);
		config9.setColorChangeEnabled(true);
		config9.setColorHex("#ff0000");
		config9.setBrightnessChangeEnabled(true);
		config9.setBrightness(HueConstants.MAX_BRI);
		list.add(config9);
		
		ScenarioConfig config8 = new ScenarioConfig();
		config8.setScenario(Scenario.FAILURE_AFTER_UNSTABLE);
		config8.setLampOn(true);
		config8.setOnetimePulsationEnabled(true);
		config8.setColorChangeEnabled(true);
		config8.setColorHex("#ff0000");
		config8.setBrightnessChangeEnabled(true);
		config8.setBrightness(HueConstants.MAX_BRI);
		list.add(config8);
		
		ScenarioConfig config7 = new ScenarioConfig();
		config7.setScenario(Scenario.FAILURE_AFTER_FAILURE);
		config7.setLampOn(true);
		config7.setOnetimePulsationEnabled(false);
		config7.setColorChangeEnabled(true);
		config7.setColorHex("#ff0000");
		config7.setBrightnessChangeEnabled(true);
		config7.setBrightness(HueConstants.MAX_BRI);
		list.add(config7);
		
		// Gelb
		
		ScenarioConfig config6 = new ScenarioConfig();
		config6.setScenario(Scenario.UNSTABLE_AFTER_SUCCESS);
		config6.setLampOn(true);
		config6.setOnetimePulsationEnabled(true);
		config6.setColorChangeEnabled(true);
		config6.setColorHex("#ffff00");
		config6.setBrightnessChangeEnabled(true);
		config6.setBrightness(HueConstants.MAX_BRI);
		list.add(config6);
		
		ScenarioConfig config5 = new ScenarioConfig();
		config5.setScenario(Scenario.UNSTABLE_AFTER_FAILURE);
		config5.setLampOn(true);
		config5.setOnetimePulsationEnabled(true);
		config5.setColorChangeEnabled(true);
		config5.setColorHex("#ffff00");
		config5.setBrightnessChangeEnabled(true);
		config5.setBrightness(HueConstants.MAX_BRI);
		list.add(config5);
		
		ScenarioConfig config4 = new ScenarioConfig();
		config4.setScenario(Scenario.UNSTABLE_AFTER_UNSTABLE);
		config4.setLampOn(true);
		config4.setOnetimePulsationEnabled(false);
		config4.setColorChangeEnabled(true);
		config4.setColorHex("#ffff00");
		config4.setBrightnessChangeEnabled(true);
		config4.setBrightness(HueConstants.MAX_BRI);
		list.add(config4);
		
		// Grün
		
		ScenarioConfig config3 = new ScenarioConfig();
		config3.setScenario(Scenario.SUCCESS_AFTER_FAILURE);
		config3.setLampOn(true);
		config3.setOnetimePulsationEnabled(false);
		config3.setColorChangeEnabled(true);
		config3.setColorHex("#00ff00");
		config3.setBrightnessChangeEnabled(true);
		config3.setBrightness(HueConstants.MAX_BRI);
		list.add(config3);
		
		ScenarioConfig config2 = new ScenarioConfig();
		config2.setScenario(Scenario.SUCCESS_AFTER_UNSTABLE);
		config2.setLampOn(true);
		config2.setOnetimePulsationEnabled(false);
		config2.setColorChangeEnabled(true);
		config2.setColorHex("#00ff00");
		config2.setBrightnessChangeEnabled(true);
		config2.setBrightness(HueConstants.MAX_BRI);
		list.add(config2);
		
		ScenarioConfig config1 = new ScenarioConfig();
		config1.setScenario(Scenario.SUCCESS_AFTER_SUCCESS);
		config1.setLampOn(true);
		config1.setOnetimePulsationEnabled(false);
		config1.setColorChangeEnabled(true);
		config1.setColorHex("#00ff00");
		config1.setBrightnessChangeEnabled(true);
		config1.setBrightness(HueConstants.MAX_BRI);
		list.add(config1);
		
		*/
		
		return list;
	}
	
	public List<Scenario> generateDefaultScenarioPriority() {
		List<Scenario> list = new ArrayList<>(12);
		
		list.add(Scenario.BUILDING_AFTER_FAILURE);
		list.add(Scenario.BUILDING_AFTER_UNSTABLE);
		list.add(Scenario.BUILDING_AFTER_SUCCESS);
		
		list.add(Scenario.FAILURE_AFTER_SUCCESS);
		list.add(Scenario.FAILURE_AFTER_UNSTABLE);
		list.add(Scenario.FAILURE_AFTER_FAILURE);
		
		list.add(Scenario.UNSTABLE_AFTER_SUCCESS);
		list.add(Scenario.UNSTABLE_AFTER_FAILURE);
		list.add(Scenario.UNSTABLE_AFTER_UNSTABLE);
		
		list.add(Scenario.SUCCESS_AFTER_FAILURE);
		list.add(Scenario.SUCCESS_AFTER_UNSTABLE);
		list.add(Scenario.SUCCESS_AFTER_SUCCESS);
		
		return list;
	}

}
