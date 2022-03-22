package de.adesso.jenkinshue.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import de.adesso.jenkinshue.common.enumeration.Scenario;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * @author wennier
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioConfig implements Serializable {
	
	private static final long serialVersionUID = -114349844119378811L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Scenario scenario;
	
	private boolean lampOn;
	
	private boolean onetimePulsationEnabled;
	
	private boolean onetimePulsationColorChangeEnabled;
	
	private String onetimePulsationColorHex;
	
	private boolean colorChangeEnabled;
	
	private String colorHex;
	
	private boolean brightnessChangeEnabled;
	
	private int brightness;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public boolean isLampOn() {
		return lampOn;
	}

	public void setLampOn(boolean lampOn) {
		this.lampOn = lampOn;
	}

	public boolean isOnetimePulsationEnabled() {
		return onetimePulsationEnabled;
	}

	public void setOnetimePulsationEnabled(boolean onetimePulsationEnabled) {
		this.onetimePulsationEnabled = onetimePulsationEnabled;
	}

	public boolean isOnetimePulsationColorChangeEnabled() {
		return onetimePulsationColorChangeEnabled;
	}

	public void setOnetimePulsationColorChangeEnabled(boolean onetimePulsationColorChangeEnabled) {
		this.onetimePulsationColorChangeEnabled = onetimePulsationColorChangeEnabled;
	}

	public String getOnetimePulsationColorHex() {
		return onetimePulsationColorHex;
	}

	public void setOnetimePulsationColorHex(String onetimePulsationColorHex) {
		this.onetimePulsationColorHex = onetimePulsationColorHex;
	}

	public boolean isColorChangeEnabled() {
		return colorChangeEnabled;
	}

	public void setColorChangeEnabled(boolean colorChangeEnabled) {
		this.colorChangeEnabled = colorChangeEnabled;
	}

	public String getColorHex() {
		return colorHex;
	}

	public void setColorHex(String colorHex) {
		this.colorHex = colorHex;
	}

	public boolean isBrightnessChangeEnabled() {
		return brightnessChangeEnabled;
	}

	public void setBrightnessChangeEnabled(boolean brightnessChangeEnabled) {
		this.brightnessChangeEnabled = brightnessChangeEnabled;
	}

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

}
