
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.MarkRepository;
import domain.Mark;

@Component
@Transactional
public class StringToMarkConverter implements Converter<String, Mark> {

	@Autowired
	MarkRepository	markRepository;


	@Override
	public Mark convert(final String text) {
		Mark result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.markRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
