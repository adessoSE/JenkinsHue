package de.adesso.jenkinshue.util;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author wennier
 *
 * @param <A>
 * @param <B>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoObjects<A, B> implements Serializable {
	
	private static final long serialVersionUID = -5744827708220561701L;

	private A a;
	
	private B b;
	
}
