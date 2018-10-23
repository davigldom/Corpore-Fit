
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ActivityBookRepository;
import domain.ActivityBook;

@Component
@Transactional
public class StringToActivityBookConverter implements Converter<String, ActivityBook> {

	@Autowired
	ActivityBookRepository	activityBookRepository;


	@Override
	public ActivityBook convert(final String text) {
		ActivityBook result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.activityBookRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
