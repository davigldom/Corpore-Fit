/*
 * FoodUserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DailyDietService;
import services.FoodService;
import services.UserService;
import controllers.AbstractController;
import domain.Food;
import domain.Nutritionist;

@Controller
@RequestMapping("/food/user")
public class FoodUserController extends AbstractController {

	@Autowired
	FoodService			foodService;

	@Autowired
	DailyDietService	dayService;

	@Autowired
	UserService			userService;


	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final int foodId) {
		ModelAndView result;
		final boolean isCreator = false;
		final Nutritionist creator = this.dayService.findCreator(this.foodService.findDailyDiet(foodId).getId());
		final Food food = this.foodService.findOne(foodId);

		result = new ModelAndView("food/display");
		result.addObject("food", food);
		result.addObject("isCreator", isCreator);
		result.addObject("userId", creator.getId());
		result.addObject("dietId", this.foodService.findDiet(food.getId()).getId());

		return result;
	}

}
