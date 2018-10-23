
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.DayRepository;
import domain.Day;

@Component
@Transactional
public class StringToDayConverter implements Converter<String, Day> {

	@Autowired
	DayRepository	dayRepository;


	@Override
	public Day convert(final String text) {
		Day result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.dayRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
