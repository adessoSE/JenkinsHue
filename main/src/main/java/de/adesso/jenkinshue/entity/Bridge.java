package de.adesso.jenkinshue.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;


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
public class Bridge implements Serializable {
	
	private static final long serialVersionUID = 8390628384846190340L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotBlank
	private String ip;
	
	private String hueUserName;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHueUserName() {
		return hueUserName;
	}

	public void setHueUserName(String hueUserName) {
		this.hueUserName = hueUserName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
