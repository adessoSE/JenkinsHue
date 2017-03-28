package de.adesso.jenkinshue.common.jenkins.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author wennier
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenkinsBuildDTO implements Serializable {
	
	private static final long serialVersionUID = -3161955824125607033L;
	
	private boolean building;
	
	private String result;
	
	private long number;
	
	private Date timestamp;
	
	public static String getTreeParameter() {
		return "building,result,number,timestamp";
	}

}
