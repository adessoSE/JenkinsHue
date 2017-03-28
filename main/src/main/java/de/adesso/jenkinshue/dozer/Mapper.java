package de.adesso.jenkinshue.dozer;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

/**
 * 
 * @author wennier
 *
 */
@Service
public class Mapper extends DozerBeanMapper {
	
	public Mapper() {
		super();
		addMapping(new LampMappingBuilder());
	}

}
