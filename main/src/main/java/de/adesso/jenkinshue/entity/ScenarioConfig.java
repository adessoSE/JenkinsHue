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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author wennier
 *
 */
@Getter
@Setter
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

}
