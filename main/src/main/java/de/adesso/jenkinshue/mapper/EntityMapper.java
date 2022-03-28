package de.adesso.jenkinshue.mapper;

import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author wennier
 *
 */
@Service
public class EntityMapper {

	private final Mapper mapper;

	public EntityMapper() {
		mapper = DozerBeanMapperBuilder.buildDefault();
	}

	public <E, D> List<E> mapList(List<D> dtos, Class<E> entityClass) {
		if (dtos == null) {
			return Collections.emptyList();
		}
		return dtos.stream()
				.map(dto -> mapper.map(dto, entityClass))
				.collect(Collectors.toList());

	}

	public  <E, D> E map(D dto, Class<E> entityClass) {
		return mapper.map(dto, entityClass);
	}

}
