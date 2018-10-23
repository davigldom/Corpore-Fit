
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Gym;

@Component
@Transactional
public class GymToStringConverter implements Converter<Gym, String> {

	@Override
	public String convert(final Gym gym) {
		String result;
		if (gym == null)
			result = null;
		else
			result = String.valueOf(gym.getId());
		return result;
	}

}
