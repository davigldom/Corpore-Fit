
package controllers.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AppointmentService;
import services.NutritionistService;
import services.UserService;
import controllers.AbstractController;
import domain.Appointment;
import domain.DaySchedule;
import domain.Nutritionist;

@Controller
@RequestMapping("/appointment/user")
public class AppointmentUserController extends AbstractController {

	@Autowired
	AppointmentService	appointmentService;

	@Autowired
	NutritionistService	nutritionistService;

	@Autowired
	UserService			userService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final String date) throws ParseException {
		ModelAndView result;

		final Appointment appointment = this.appointmentService.create();

		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		final Date parsed = format.parse(date);
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(parsed);

		result = this.createEditModelAndView(appointment, calendar);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("appointment/list");
		result.addObject("appointments", this.userService.findByPrincipal().getAppointments());
		result.addObject("requestURI", "/appointment/user/list.do");
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int appointmentId) {
		ModelAndView result;

		final Appointment ap = this.appointmentService.findOne(appointmentId);

		Assert.isTrue(ap.getUser().equals(this.userService.findByPrincipal()));

		result = new ModelAndView("appointment/display");
		result.addObject("appointment", ap);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Appointment appointment, final BindingResult binding) {
		ModelAndView result;

		appointment = this.appointmentService.reconstruct(appointment, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(appointment, appointment.getTime());
		else
			try {
				this.appointmentService.make(appointment);
				result = new ModelAndView("redirect:/appointment/user/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(appointment, appointment.getTime(), "appointment.commit.error");
			}
		return result;
	}

	// Delete gym
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int appointmentId) {
		ModelAndView result;

		final Appointment appointment = this.appointmentService.findOne(appointmentId);
		try {

			this.appointmentService.delete(appointment);

			result = new ModelAndView("redirect:/appointment/user/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("appointment/list");
			result.addObject("appointments", this.userService.findByPrincipal().getAppointments());
			result.addObject("requestURI", "/appointment/user/list.do");
			result.addObject("message", "appointment.commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Appointment appointment, final Calendar date) {
		ModelAndView result;
		result = this.createEditModelAndView(appointment, date, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Appointment appointment, final Calendar date, final String message) {
		ModelAndView result;

		result = new ModelAndView("appointment/edit");
		result.addObject("message", message);
		result.addObject("appointment", appointment);
		result.addObject("dates", this.getPossibleDates(appointment, date));
		return result;
	}

	protected Collection<Calendar> getPossibleDates(final Appointment appointment, final Calendar date) {
		final Collection<Calendar> dates = new ArrayList<Calendar>();
		final Collection<Calendar> appointmentDates = new ArrayList<Calendar>();
		final Nutritionist nutri = this.userService.findByPrincipal().getNutritionist();

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		final Calendar calendarStartMorning = Calendar.getInstance();
		final Calendar calendarStartAfternoon = Calendar.getInstance();
		final Calendar endCalendarMorning = Calendar.getInstance();
		final Calendar endCalendarAfternoon = Calendar.getInstance();

		for (final DaySchedule s : nutri.getSchedule())
			if (s.getDay().ordinal() + 1 == date.get(Calendar.DAY_OF_WEEK)) {
				Date dateStartMorning = null;
				try {
					dateStartMorning = formatter.parse("" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.YEAR) + " " + s.getMorningStart());
				} catch (final ParseException e) {
					e.printStackTrace();
				}
				Date dateEndMorning = null;
				try {
					dateEndMorning = formatter.parse("" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.YEAR) + " " + s.getMorningEnd());
				} catch (final ParseException e) {
					e.printStackTrace();
				}
				Date dateStartAfternoon = null;
				try {
					dateStartAfternoon = formatter.parse("" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.YEAR) + " " + s.getAfternoonStart());
				} catch (final ParseException e) {
					e.printStackTrace();
				}
				Date dateEndAfternoon = null;
				try {
					dateEndAfternoon = formatter.parse("" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.YEAR) + " " + s.getAfternoonEnd());
				} catch (final ParseException e) {
					e.printStackTrace();
				}

				calendarStartMorning.setTime(dateStartMorning);
				calendarStartAfternoon.setTime(dateStartAfternoon);
				endCalendarMorning.setTime(dateEndMorning);
				endCalendarAfternoon.setTime(dateEndAfternoon);
			}

		appointmentDates.addAll(this.appointmentService.findAllBetweenTimes(calendarStartMorning, endCalendarAfternoon, nutri.getId()));

		while (calendarStartMorning.before(endCalendarMorning)) {
			final Date result = calendarStartMorning.getTime();
			final Calendar time = Calendar.getInstance();
			time.setTime(result);
			dates.add(time);
			calendarStartMorning.add(Calendar.MINUTE, 30);
		}

		while (calendarStartAfternoon.before(endCalendarAfternoon)) {
			final Date result = calendarStartAfternoon.getTime();
			final Calendar time = Calendar.getInstance();
			time.setTime(result);
			dates.add(time);
			calendarStartAfternoon.add(Calendar.MINUTE, 30);
		}

		dates.removeAll(appointmentDates);

		return dates;
	}

}
