
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Mark;

@Component
@Transactional
public class MarkToStringConverter implements Converter<Mark, String> {

	@Override
	public String convert(final Mark mark) {
		String result;
		if (mark == null)
			result = null;
		else
			result = String.valueOf(mark.getId());
		return result;
	}

}
