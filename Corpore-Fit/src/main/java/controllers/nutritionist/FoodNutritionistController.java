/*
 * FoodUserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.nutritionist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DailyDietService;
import services.FoodService;
import services.NutritionistService;
import controllers.AbstractController;
import domain.DailyDiet;
import domain.Food;
import domain.Nutritionist;

@Controller
@RequestMapping("/food/nutritionist")
public class FoodNutritionistController extends AbstractController {

	@Autowired
	private FoodService			foodService;

	@Autowired
	private DailyDietService	dayService;

	@Autowired
	private NutritionistService	nutritionistService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int dayId) {
		ModelAndView result;
		final Food food = this.foodService.create();
		Assert.isTrue(this.dayService.findCreator(dayId).equals(this.nutritionistService.findByPrincipal()));
		result = this.createEditModelAndView(food, dayId);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int foodId) {
		ModelAndView result;
		final Food food = this.foodService.findOneToEdit(foodId);
		result = this.createEditModelAndView(food, this.foodService.findDailyDiet(foodId).getId());
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Food food, final BindingResult binding, final int dayId) {
		ModelAndView result;
		food = this.foodService.reconstruct(food, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(food, dayId);
		else
			try {
				final Food saved = this.foodService.save(food);
				final DailyDiet day = this.dayService.findOneToEdit(dayId);
				if (food.getId() == 0) {
					day.getFoods().add(saved);
					this.dayService.save(day);
				}
				result = new ModelAndView("redirect:/diet/nutritionist/display.do?dietId=" + this.foodService.findDiet(saved.getId()).getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(food, dayId, "food.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int foodId) {
		ModelAndView result;
		final Food food = this.foodService.findOneToEdit(foodId);
		final int dietId = this.foodService.findDiet(food.getId()).getId();
		this.foodService.delete(food);
		result = new ModelAndView("redirect:/diet/nutritionist/display.do?dietId=" + dietId);
		return result;
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int foodId) {
		ModelAndView result;
		final boolean isCreator = false;
		final Nutritionist creator = this.dayService.findCreator(this.foodService.findDailyDiet(foodId).getId());

		final Food food = this.foodService.findOneToEdit(foodId);

		result = new ModelAndView("food/display");
		result.addObject("food", food);
		result.addObject("isCreator", isCreator);
		result.addObject("userId", creator.getId());
		result.addObject("dietId", this.foodService.findDiet(food.getId()).getId());

		return result;
	}

	protected ModelAndView createEditModelAndView(final Food food, final int dayId) {
		ModelAndView result;
		result = this.createEditModelAndView(food, dayId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Food food, final int dayId, final String message) {
		ModelAndView result;
		result = new ModelAndView("food/edit");
		result.addObject("food", food);
		result.addObject("dayId", dayId);
		if (food.getId() != 0)
			result.addObject("dietId", this.foodService.findDiet(food.getId()).getId());
		result.addObject("message", message);
		return result;
	}

}
