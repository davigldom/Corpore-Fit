
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Routine;

@Component
@Transactional
public class RoutineToStringConverter implements Converter<Routine, String> {

	@Override
	public String convert(final Routine routine) {
		String result;
		if (routine == null)
			result = null;
		else
			result = String.valueOf(routine.getId());
		return result;
	}

}
