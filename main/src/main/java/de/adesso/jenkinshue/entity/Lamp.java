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
import javax.validation.constraints.NotBlank;
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHueUniqueId() {
		return hueUniqueId;
	}

	public void setHueUniqueId(String hueUniqueId) {
		this.hueUniqueId = hueUniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getWorkingStart() {
		return workingStart;
	}

	public void setWorkingStart(Date workingStart) {
		this.workingStart = workingStart;
	}

	public Date getWorkingEnd() {
		return workingEnd;
	}

	public void setWorkingEnd(Date workingEnd) {
		this.workingEnd = workingEnd;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public Scenario getLastShownScenario() {
		return lastShownScenario;
	}

	public void setLastShownScenario(Scenario lastShownScenario) {
		this.lastShownScenario = lastShownScenario;
	}

	public List<ScenarioConfig> getScenarioConfigs() {
		return scenarioConfigs;
	}

	public void setScenarioConfigs(List<ScenarioConfig> scenarioConfigs) {
		this.scenarioConfigs = scenarioConfigs;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
