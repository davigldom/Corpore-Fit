
package converters;

import java.net.URLEncoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.DaySchedule;

@Component
@Transactional
public class DayScheduleToStringConverter implements Converter<DaySchedule, String> {

	@Override
	public String convert(final DaySchedule day) {
		String result;
		StringBuilder builder;

		if (day == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(String.valueOf(day.getDay()), "UTF-8"));
				builder.append(" - ");
				builder.append(URLEncoder.encode(day.getMorningStart(), "UTF-8"));
				builder.append(" - ");
				builder.append(URLEncoder.encode(day.getMorningEnd(), "UTF-8"));
				builder.append(" - ");
				builder.append(URLEncoder.encode(day.getAfternoonStart(), "UTF-8"));
				builder.append(" - ");
				builder.append(URLEncoder.encode(day.getAfternoonEnd(), "UTF-8"));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}

		return result;
	}

}
