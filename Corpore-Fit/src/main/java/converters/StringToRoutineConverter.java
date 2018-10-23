
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.RoutineRepository;
import domain.Routine;

@Component
@Transactional
public class StringToRoutineConverter implements Converter<String, Routine> {

	@Autowired
	RoutineRepository	routineRepository;


	@Override
	public Routine convert(final String text) {
		Routine result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.routineRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
