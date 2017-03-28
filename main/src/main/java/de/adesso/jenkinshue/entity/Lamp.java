package de.adesso.jenkinshue.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.adesso.jenkinshue.common.enumeration.Scenario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author wennier
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Lamp implements Serializable {

	private static final long serialVersionUID = -4444902548871364238L;

	@Id
	@GeneratedValue
	private long id;

	@NotBlank
	@Column(unique = true)
	private String hueUniqueId;

	@NotBlank
	private String name;

	@NotNull
	@Temporal(TemporalType.TIME)
	private Date workingStart;

	@NotNull
	@Temporal(TemporalType.TIME)
	private Date workingEnd;

	@JoinColumn(name = "LAMP_ID")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Job> jobs;
	
	@Enumerated(EnumType.STRING)
	private Scenario lastShownScenario;

	@NotNull
	@JoinColumn(name = "LAMP_ID")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScenarioConfig> scenarioConfigs;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team team;

}
