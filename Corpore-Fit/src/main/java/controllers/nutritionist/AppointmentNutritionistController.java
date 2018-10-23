
package controllers.nutritionist;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AppointmentService;
import services.NutritionistService;
import controllers.AbstractController;
import domain.Appointment;

@Controller
@RequestMapping("/appointment/nutritionist")
public class AppointmentNutritionistController extends AbstractController {

	@Autowired
	private AppointmentService	appointmentService;

	@Autowired
	private NutritionistService	nutritionistService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("appointment/list");
		result.addObject("appointments", this.nutritionistService.findByPrincipal().getAppointments());
		result.addObject("requestURI", "/appointment/nutritionist/list.do");
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int appointmentId) {
		ModelAndView result;

		final Appointment ap = this.appointmentService.findOne(appointmentId);

		Assert.isTrue(ap.getNutritionist().equals(this.nutritionistService.findByPrincipal()));

		result = new ModelAndView("appointment/display");
		result.addObject("appointment", ap);
		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int appointmentId, @RequestParam(required = false) final String cancelReason) {
		ModelAndView result;

		final Appointment appointment = this.appointmentService.findOne(appointmentId);
		try {
			appointment.setCancelReason(cancelReason);
			final Appointment ap = this.appointmentService.cancel(appointment);

			result = new ModelAndView("redirect:/appointment/nutritionist/display.do?appointmentId=" + ap.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("appointment/list");
			result.addObject("appointments", this.nutritionistService.findByPrincipal().getAppointments());
			result.addObject("message", "appointment.commit.error");
		}

		return result;
	}
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Appointment appointment, final Date date) {
		ModelAndView result;
		result = this.createEditModelAndView(appointment, date, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Appointment appointment, final Date date, final String message) {
		ModelAndView result;

		result = new ModelAndView("appointment/edit");
		result.addObject("message", message);
		result.addObject("appointment", appointment);
		//		result.addObject("dates", this.getPossibleDates(appointment, dateCalendar));
		return result;
	}

	//	protected Collection<Calendar> getPossibleDates(Appointment appointment, Calendar date){
	//		Collection<Calendar> dates = new ArrayList<Calendar>();
	//		Nutritionist nutri = appointment.getNutritionist();
	//		DaySchedule daySchedule = null;
	//		for (DaySchedule schedule : nutri.getDaySchedule()) {
	//			if(schedule.getDay().ordinal()==date.get(Calendar.DAY_OF_WEEK))
	//				daySchedule = schedule;
	//		}
	//		
	//		
	//		
	//		return dates;
	//	}

}
