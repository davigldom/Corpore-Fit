
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Activity;

@Component
@Transactional
public class ActivityToStringConverter implements Converter<Activity, String> {

	@Override
	public String convert(final Activity activity) {
		String result;
		if (activity == null)
			result = null;
		else
			result = String.valueOf(activity.getId());
		return result;
	}

}
