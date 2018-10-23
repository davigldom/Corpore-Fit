
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.SocialNetwork;

@Component
@Transactional
public class SocialNetworkToStringConverter implements Converter<SocialNetwork, String> {

	@Override
	public String convert(final SocialNetwork socialNetwork) {
		String result;
		if (socialNetwork == null)
			result = null;
		else
			result = String.valueOf(socialNetwork.getId());
		return result;
	}

}
