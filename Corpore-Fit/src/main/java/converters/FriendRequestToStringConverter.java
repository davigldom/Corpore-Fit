
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.FriendRequest;

@Component
@Transactional
public class FriendRequestToStringConverter implements Converter<FriendRequest, String> {

	@Override
	public String convert(final FriendRequest friendRequest) {
		String result;
		if (friendRequest == null)
			result = null;
		else
			result = String.valueOf(friendRequest.getId());
		return result;
	}

}
