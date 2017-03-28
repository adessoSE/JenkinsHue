package de.adesso.jenkinshue.common.dto.bridge;

import java.io.Serializable;

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
public class BridgeUpdateDTO implements Serializable {

	private static final long serialVersionUID = -4774937935941408893L;

	private long id;

	private String ip;

	private String hueUserName;

	private long userId;

}
