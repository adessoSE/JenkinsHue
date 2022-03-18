package de.adesso.jenkinshue.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;


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
public class Job implements Serializable {
	
	private static final long serialVersionUID = 8376216421882284583L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotBlank
	private String name;
	
}
