package de.adesso.jenkinshue.dozer;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wennier
 *
 */
@Service
public class Mapper extends DozerBeanMapper {

	public <E, D> List<E> mapList(List<D> dtos, Class<E> entityClass) {
		List<E> entities = new ArrayList<>();
		if (dtos != null) {
			for (D dto : dtos) {
				E entity = this.map(dto, entityClass);
				entities.add(entity);
			}
		}
		return entities;
	}
}
