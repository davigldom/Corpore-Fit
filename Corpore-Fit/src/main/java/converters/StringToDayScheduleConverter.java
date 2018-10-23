
package converters;

import java.net.URLDecoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.DayName;
import domain.DaySchedule;

@Component
@Transactional
public class StringToDayScheduleConverter implements Converter<String, DaySchedule> {

	@Override
	public DaySchedule convert(final String text) {
		DaySchedule result = null;
		String parts[];

		if (text == null)
			result = null;
		else if (text == "")
			result = null;
		else
			try {
				parts = text.split("\\,");
				result = new DaySchedule();
				result.setDay(DayName.valueOf(URLDecoder.decode(parts[0], "UTF-8")));
				result.setMorningStart(URLDecoder.decode(parts[1], "UTF-8"));
				result.setMorningEnd(URLDecoder.decode(parts[2], "UTF-8"));
				result.setAfternoonStart(URLDecoder.decode(parts[3], "UTF-8"));
				result.setAfternoonEnd(URLDecoder.decode(parts[4], "UTF-8"));
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}

		return result;
	}
}
