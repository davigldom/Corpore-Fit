
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Appointment;

@Component
@Transactional
public class AppointmentToStringConverter implements Converter<Appointment, String> {

	@Override
	public String convert(final Appointment appointment) {
		String result;
		if (appointment == null)
			result = null;
		else
			result = String.valueOf(appointment.getId());
		return result;
	}

}
