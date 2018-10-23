
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Exercise;

@Component
@Transactional
public class ExerciseToStringConverter implements Converter<Exercise, String> {

	@Override
	public String convert(final Exercise exercise) {
		String result;
		if (exercise == null)
			result = null;
		else
			result = String.valueOf(exercise.getId());
		return result;
	}

}
