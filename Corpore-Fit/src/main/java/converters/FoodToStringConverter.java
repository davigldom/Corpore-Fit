
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Food;

@Component
@Transactional
public class FoodToStringConverter implements Converter<Food, String> {

	@Override
	public String convert(final Food food) {
		String result;
		if (food == null)
			result = null;
		else
			result = String.valueOf(food.getId());
		return result;
	}

}
