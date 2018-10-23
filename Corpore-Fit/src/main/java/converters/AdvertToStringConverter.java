
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Advert;

@Component
@Transactional
public class AdvertToStringConverter implements Converter<Advert, String> {

	@Override
	public String convert(final Advert advert) {
		String result;
		if (advert == null)
			result = null;
		else
			result = String.valueOf(advert.getId());
		return result;
	}

}
