
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
@RequestMapping("/diet/user")
public class DietUserController extends AbstractController {

	@Autowired
	ActorService		actorService;

	@Autowired
	UserService			userService;

	@Autowired
	DietService			dietService;

	@Autowired
	NutritionistService	nutritionistService;


	//List nutritionist diets
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int nutritionistId) {
		ModelAndView result;
		final Nutritionist nutritionist = this.nutritionistService.findOne(nutritionistId);
		final Collection<Diet> diets = nutritionist.getDiets();
		Assert.notNull(nutritionist);
		Assert.isTrue(nutritionist.isValidated());
		result = new ModelAndView("diet/list");
		result.addObject("diets", diets);
		return result;
	}

	//Display diet
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int dietId) {
		ModelAndView result;
		final Diet diet = this.dietService.findOne(dietId);
		Assert.isTrue(diet.getNutritionist().isValidated());
		final User user = this.userService.findByPrincipal();
		boolean isFollowing = false;
		boolean nutritionistAssigned=false;
		Assert.notNull(diet);
		if(user.getNutritionist()!=null&&user.getNutritionist().equals(diet.getNutritionist())){
			nutritionistAssigned=true;
		}
		if (user.getDiet() != null)
			isFollowing = user.getDiet().equals(diet);
		result = new ModelAndView("diet/display");
		result.addObject("diet", diet);
		result.addObject("isFollowing", isFollowing);
		result.addObject("nutritionistAssigned", nutritionistAssigned);
		result.addObject("dietId", dietId);
		return result;
	}

	//Follow diet
	@RequestMapping(value = "/follow-diet", method = RequestMethod.GET)
	public ModelAndView follow(@RequestParam final int dietId) {
		ModelAndView result;
		final Diet diet = this.dietService.findOne(dietId);
		final User user = this.userService.findByPrincipal();
		Assert.isTrue(user.getNutritionist().equals(diet.getNutritionist()));
		Assert.notNull(diet);
		user.setDiet(diet);
		this.userService.save(user);
		result = new ModelAndView("redirect:/diet/user/display.do?dietId=" + dietId);
		return result;
	}

	//Unfollow diet
	@RequestMapping(value = "/unfollow-diet", method = RequestMethod.GET)
	public ModelAndView unfollow(@RequestParam final int dietId) {
		ModelAndView result;
		final Diet diet = this.dietService.findOne(dietId);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(diet);
		user.setDiet(null);
		this.userService.save(user);
		result = new ModelAndView("redirect:/diet/user/display.do?dietId=" + dietId);
		return result;
	}

	//Display my diet
	@RequestMapping(value = "/display-following", method = RequestMethod.GET)
	public ModelAndView displayFollowing() {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		final Diet diet = user.getDiet();
		if (diet == null)
			result = new ModelAndView("diet/none");
		else
			result = new ModelAndView("redirect:/diet/user/display.do?dietId=" + diet.getId());

		return result;
	}

}
