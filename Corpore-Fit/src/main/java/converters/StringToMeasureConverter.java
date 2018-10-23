
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.MeasureRepository;
import domain.Measure;

@Component
@Transactional
public class StringToMeasureConverter implements Converter<String, Measure> {

	@Autowired
	MeasureRepository	measureRepository;


	@Override
	public Measure convert(final String text) {
		Measure result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.measureRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
