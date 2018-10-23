
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Nutritionist;

@Component
@Transactional
public class NutritionistToStringConverter implements Converter<Nutritionist, String> {

	@Override
	public String convert(final Nutritionist nutritionist) {
		String result;
		if (nutritionist == null)
			result = null;
		else
			result = String.valueOf(nutritionist.getId());
		return result;
	}

}
