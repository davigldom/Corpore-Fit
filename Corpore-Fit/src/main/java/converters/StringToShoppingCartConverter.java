
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ShoppingCartRepository;
import domain.ShoppingCart;

@Component
@Transactional
public class StringToShoppingCartConverter implements Converter<String, ShoppingCart> {

	@Autowired
	ShoppingCartRepository	shoppingCartRepository;


	@Override
	public ShoppingCart convert(final String text) {
		ShoppingCart result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.shoppingCartRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
