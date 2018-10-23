
package controllers.nutritionist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.DietService;
import services.NutritionistService;
import controllers.AbstractController;
import domain.Actor;
import domain.Diet;
import domain.Nutritionist;
import domain.User;

@Controller
@RequestMapping("/actor")
public class ActorNutritionistController extends AbstractController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private NutritionistService	nutritionistService;

	@Autowired
	private DietService			dietService;


	//Edit Nutritionist
	@RequestMapping(value = "/nutritionist/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") Nutritionist nutritionist, final BindingResult binding) {
		ModelAndView result;
		nutritionist = this.nutritionistService.reconstruct(nutritionist, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(nutritionist);
		else
			try {
				this.nutritionistService.save(nutritionist);
				result = new ModelAndView("redirect:/actor/display-principal.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(nutritionist, "actor.commit.error");
			}
		return result;
	}

	//Delete an nutritionist
	@RequestMapping(value = "/nutritionist/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final int actorId) {
		ModelAndView result;
		final Nutritionist nutritionist = this.nutritionistService.findOne(actorId);
		Assert.isTrue(nutritionist.equals(this.actorService.findByPrincipal()));
		try {
			for (final Diet d : nutritionist.getDiets())
				for (final User u : this.dietService.getFollowers(d.getId()))
					u.setDiet(null);
			for (final User u : this.nutritionistService.getAssigners(nutritionist.getId()))
				u.setNutritionist(null);
			this.nutritionistService.delete(nutritionist);
			result = new ModelAndView("redirect:../../j_spring_security_logout");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(nutritionist, "actor.commit.error");
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
