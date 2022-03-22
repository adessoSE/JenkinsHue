package de.adesso.jenkinshue.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Job implements Serializable {
	
	private static final long serialVersionUID = 8376216421882284583L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotBlank
	private String name;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
