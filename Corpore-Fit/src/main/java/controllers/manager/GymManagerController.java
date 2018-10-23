
package controllers.manager;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.GymService;
import services.ManagerService;
import controllers.AbstractController;
import domain.Gym;
import domain.GymRating;
import domain.Manager;

@Controller
@RequestMapping("/gym/manager")
public class GymManagerController extends AbstractController {

	@Autowired
	GymService		gymService;

	@Autowired
	ManagerService	managerService;


	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Gym gym;
		Collection<GymRating> grGym;
		double averageRating = 0.0;
		boolean hasGym = true;

		Manager principal;
		principal = this.managerService.findByPrincipal();

		if (principal.getGym() == null) {
			hasGym = false;
			gym = null;
		} else {
			gym = principal.getGym();
			grGym = new ArrayList<GymRating>(gym.getGymRatings());
			double allRatings = 0.0;
			for (final GymRating g : grGym) {
				allRatings += g.getRating();
				averageRating = allRatings / grGym.size();
			}
		}

		result = new ModelAndView("gym/display");
		result.addObject("gym", gym);
		result.addObject("hasGym", hasGym);
		result.addObject("principal", principal);
		result.addObject("averageRating", averageRating);

		return result;
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Gym gym;
		gym = this.gymService.create();
		result = this.createEditModelAndView(gym);

		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int gymId) {
		ModelAndView result;
		Gym gym;

		gym = this.gymService.findOneToEdit(gymId);
		Assert.notNull(gym);
		result = this.createEditModelAndView(gym);

		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Gym gym, final BindingResult binding) {
		ModelAndView result;

		gym = this.gymService.reconstruct(gym, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(gym);
		else
			try {
				this.gymService.save(gym);
				result = new ModelAndView("redirect:/gym/manager/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(gym, "gym.commit.error");
			}
		return result;
	}

	// Ancilliary methods
	protected ModelAndView createEditModelAndView(final Gym gym) {
		ModelAndView result;
		result = this.createEditModelAndView(gym, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Gym gym, final String message) {
		ModelAndView result;
		result = new ModelAndView("gym/edit");
		//		result.addObject(actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase(), actor);
		//		result.addObject("authority", actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());
		result.addObject("message", message);
		result.addObject("gym", gym);
		return result;
	}
}
