
package controllers.nutritionist;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.NutritionistService;
import controllers.AbstractController;
import domain.Actor;
import domain.DaySchedule;
import domain.Nutritionist;

@Controller
@RequestMapping("/schedule/nutritionist")
public class ScheduleNutritionistController extends AbstractController {

	@Autowired
	private NutritionistService	nutritionistService;


	//Save a day schedule
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final String day, final String morningStart, final String morningEnd, final String afternoonStart, final String afternoonEnd) {
		ModelAndView result;
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		Hibernate.initialize(nutritionist.getSchedule());
		final List<DaySchedule> schedule = nutritionist.getSchedule();
		Assert.notNull(schedule);
		try {
			this.nutritionistService.save(this.nutritionistService.addDaySchedule(day, morningStart, morningEnd, afternoonStart, afternoonEnd));
			result = new ModelAndView("redirect:/schedule/display.do?nutritionistId=" + nutritionist.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("nutritionist/schedule");
			result.addObject("schedule", schedule);
			result.addObject("username", nutritionist.getUserAccount().getUsername());
			result.addObject("message", "nutritionist.schedule.error");
			result.addObject("username", nutritionist.getUserAccount().getUsername());
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;
		result = this.createEditModelAndView(actor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String message) {
		ModelAndView result;
		result = new ModelAndView("actor/edit");
		result.addObject(actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase(), actor);
		result.addObject("authority", actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());
		result.addObject("message", message);
		result.addObject("actor", actor);
		return result;
	}
}
