
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.DailyDiet;

@Component
@Transactional
public class DailyDietToStringConverter implements Converter<DailyDiet, String> {

	@Override
	public String convert(final DailyDiet day) {
		String result;
		if (day == null)
			result = null;
		else
			result = String.valueOf(day.getId());
		return result;
	}

}
