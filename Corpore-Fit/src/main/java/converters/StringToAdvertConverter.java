
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.AdvertRepository;
import domain.Advert;

@Component
@Transactional
public class StringToAdvertConverter implements Converter<String, Advert> {

	@Autowired
	AdvertRepository	advertRepository;


	@Override
	public Advert convert(final String text) {
		Advert result;
		int id;
		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.advertRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
