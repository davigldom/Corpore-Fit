
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.GymRepository;
import domain.Gym;

@Component
@Transactional
public class StringToGymConverter implements Converter<String, Gym> {

	@Autowired
	GymRepository	gymRepository;


	@Override
	public Gym convert(final String text) {
		Gym result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.gymRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
