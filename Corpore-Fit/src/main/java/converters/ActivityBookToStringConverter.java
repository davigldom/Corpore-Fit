
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.ActivityBook;

@Component
@Transactional
public class ActivityBookToStringConverter implements Converter<ActivityBook, String> {

	@Override
	public String convert(final ActivityBook activityBook) {
		String result;
		if (activityBook == null)
			result = null;
		else
			result = String.valueOf(activityBook.getId());
		return result;
	}

}
