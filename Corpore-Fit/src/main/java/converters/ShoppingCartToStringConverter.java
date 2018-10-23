
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.ShoppingCart;

@Component
@Transactional
public class ShoppingCartToStringConverter implements Converter<ShoppingCart, String> {

	@Override
	public String convert(final ShoppingCart shoppingCart) {
		String result;
		if (shoppingCart == null)
			result = null;
		else
			result = String.valueOf(shoppingCart.getId());
		return result;
	}

}
