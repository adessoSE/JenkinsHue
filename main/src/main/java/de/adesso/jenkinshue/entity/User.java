package de.adesso.jenkinshue.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


import de.adesso.jenkinshue.common.enumeration.Role;
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
@Table(name = "USER_")
public class User implements Serializable {
	
	private static final long serialVersionUID = 4518443565637177684L;

	@Id
	@GeneratedValue
	private long id;
	
	private String email;
	
	@NotBlank
	private String login;
	
	private String forename;
	
	private String surname;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team team;
	
	@OneToMany(mappedBy = "user")
	private List<Bridge> bridges;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private List<Role> roles;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<Bridge> getBridges() {
		return bridges;
	}

	public void setBridges(List<Bridge> bridges) {
		this.bridges = bridges;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
