
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ExerciseRepository;
import domain.Exercise;

@Component
@Transactional
public class StringToExerciseConverter implements Converter<String, Exercise> {

	@Autowired
	ExerciseRepository	exerciseRepository;


	@Override
	public Exercise convert(final String text) {
		Exercise result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.exerciseRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
