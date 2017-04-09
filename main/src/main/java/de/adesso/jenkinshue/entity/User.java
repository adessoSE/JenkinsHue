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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.adesso.jenkinshue.common.enumeration.Role;
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
	
}
