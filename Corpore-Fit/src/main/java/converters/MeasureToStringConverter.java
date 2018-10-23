
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Measure;

@Component
@Transactional
public class MeasureToStringConverter implements Converter<Measure, String> {

	@Override
	public String convert(final Measure measure) {
		String result;
		if (measure == null)
			result = null;
		else
			result = String.valueOf(measure.getId());
		return result;
	}

}
