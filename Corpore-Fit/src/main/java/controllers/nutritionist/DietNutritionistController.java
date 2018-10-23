
package controllers.nutritionist;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.DietService;
import services.NutritionistService;
import services.UserService;
import controllers.AbstractController;
import domain.Diet;
import domain.Nutritionist;
import domain.User;

@Controller
@RequestMapping("/diet/nutritionist")
public class DietNutritionistController extends AbstractController {

	@Autowired
	ActorService		actorService;

	@Autowired
	UserService			userService;

	@Autowired
	DietService			dietService;

	@Autowired
	NutritionistService	nutritionistService;


	//List own diets
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		final Collection<Diet> diets = nutritionist.getDiets();
		Assert.notNull(nutritionist);
		Assert.isTrue(nutritionist.isValidated(),"Nutritionist not validated");
		result = new ModelAndView("diet/list");
		result.addObject("diets", diets);
		return result;
	}

	//Create diet
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Diet diet = this.dietService.create();

		Assert.isTrue(this.nutritionistService.findByPrincipal().isValidated(),"Nutritionist not validated");
		Assert.notNull(diet);
		result = new ModelAndView("diet/edit");
		result.addObject("diet", diet);
		return result;
	}

	//Save diet
	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Diet diet, final BindingResult binding) {
		ModelAndView result;
		Assert.notNull(diet);
		diet = this.dietService.reconstruct(diet, binding);
		if (binding.hasErrors())
			result = new ModelAndView("diet/edit");
		else
			try {
				this.dietService.save(diet);
				result = new ModelAndView("redirect:/diet/nutritionist/list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("diet/edit");
				result.addObject("message", "actor.commit.error");
			}
		return result;
	}

	//Edit diet
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int dietId) {
		ModelAndView result;
		final Diet diet = this.dietService.findOneToEdit(dietId);
		Assert.notNull(diet);
		result = new ModelAndView("diet/edit");
		result.addObject("diet", diet);
		return result;
	}

	//Display diet
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int dietId) {
		ModelAndView result;
		final Diet diet = this.dietService.findOne(dietId);
		Assert.notNull(diet);
		Assert.isTrue(this.nutritionistService.findByPrincipal().equals(diet.getNutritionist()));
		result = new ModelAndView("diet/display");
		result.addObject("diet", diet);
		return result;
	}
	
	//Delete diet
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int dietId) {
		ModelAndView result;
		try {
			final Diet diet = this.dietService.findOneToEdit(dietId);
			for (User u : this.dietService.getFollowers(diet.getId())) {
				u.setDiet(null);
			}
			this.dietService.delete(diet);
			result = new ModelAndView("redirect:/diet/nutritionist/list.do");
		} catch (final Throwable oops) {
			if(this.nutritionistService.findByPrincipal().isValidated()){
			result = new ModelAndView("diet/list");
			result.addObject("diets", this.nutritionistService.findByPrincipal().getDiets());
			}else
				result = new ModelAndView("redirect:/diet/nutritionist/list.do");
			result.addObject("message", "actor.commit.error");

		}
		return result;
	}

}
