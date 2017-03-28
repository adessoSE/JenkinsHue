package de.adesso.jenkinshue.common.dto;

import java.io.Serializable;
import java.util.List;

import de.adesso.jenkinshue.common.dto.lamp.LampNameDTO;
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
public class DashboardInformationDTO implements Serializable {
	
	private static final long serialVersionUID = -5162525487716011849L;
	
	private long bridgeCount;
	
	private long availableLampCount;
	
	private long teamLampCount;
	
	private long lampCount;
	
	private long teamUserCount;
	
	private long userCount;
	
	private long teamJobCount;
	
	private long jobCount;
	
	private List<LampNameDTO> teamLamps;

}
