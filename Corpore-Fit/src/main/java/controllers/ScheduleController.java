
package controllers;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.NutritionistService;
import services.UserService;
import domain.DaySchedule;
import domain.Nutritionist;

@Controller
@RequestMapping("/schedule")
public class ScheduleController extends AbstractController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserService			userService;

	@Autowired
	private NutritionistService	nutritionistService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int nutritionistId) {
		ModelAndView result;
		final Nutritionist nutritionist = this.nutritionistService.findOne(nutritionistId);
		Hibernate.initialize(nutritionist.getSchedule());
		final List<DaySchedule> schedule = nutritionist.getSchedule();
		Assert.notNull(schedule);

		if (!this.actorService.isAdmin())
			if (!nutritionist.isValidated())
				Assert.isTrue(this.nutritionistService.findByPrincipal().equals(nutritionist));

		result = new ModelAndView("nutritionist/schedule");

		if (this.actorService.isUser() && this.userService.findByPrincipal().getNutritionist() != null && this.userService.findByPrincipal().getNutritionist().equals(nutritionist))
			result.addObject("canMakeAppointment", true);

		result.addObject("schedule", schedule);
		result.addObject("nutritionist", nutritionist);
		result.addObject("username", nutritionist.getUserAccount().getUsername());
		return result;
	}

}
