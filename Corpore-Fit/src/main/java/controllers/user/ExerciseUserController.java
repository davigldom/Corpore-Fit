/*
 * ExerciseUserController.java
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
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.DayService;
import services.ExerciseService;
import services.FriendRequestService;
import services.UserService;
import controllers.AbstractController;
import domain.Day;
import domain.Exercise;

@Controller
@RequestMapping("/exercise/user")
public class ExerciseUserController extends AbstractController {

	@Autowired
	ActorService actorService;

	@Autowired
	UserService userService;

	@Autowired
	ExerciseService exerciseService;

	@Autowired
	DayService dayService;

	@Autowired
	FriendRequestService friendRequestService;

	// Constructors -----------------------------------------------------------

	public ExerciseUserController() {
		super();
	}

	// Create User
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int dayId) {
		ModelAndView result;
		Assert.isTrue(this.dayService.findCreator(dayId).equals(this.userService.findByPrincipal()));
		final Exercise exercise = this.exerciseService.create();
		result = this.createEditModelAndView(exercise, dayId);
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int exerciseId) {
		ModelAndView result;
		final Exercise exercise = this.exerciseService.findOneToEdit(exerciseId);
		result = this.createEditModelAndView(exercise, this.exerciseService.findDay(exerciseId).getId());
		return result;
	}

	// Edit User
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Exercise exercise, final BindingResult binding, int dayId) {
		ModelAndView result;
		exercise = this.exerciseService.reconstruct(exercise, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(exercise, dayId);
		else
			try {
				Exercise saved = this.exerciseService.save(exercise);
				Day day = this.dayService.findOneToEdit(dayId);
				if(exercise.getId()==0){
					day.getExercises().add(saved);
					this.dayService.save(day);	
				}
				result = new ModelAndView("redirect:/exercise/display.do?exerciseId="
						+ saved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(exercise,dayId,
						"exercise.commit.error");
			}
		return result;
	}

	// Delete an user
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int exerciseId) {
		ModelAndView result;
		try {
			final Exercise exercise = this.exerciseService
					.findOneToEdit(exerciseId);
			this.exerciseService.delete(exercise);
			result = new ModelAndView("redirect:/routine/display.do?userId="
					+ this.userService.findByPrincipal().getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("routine/display");
			result.addObject("isCreator", true);
			result.addObject("routine", this.userService.findByPrincipal().getRoutine());
			result.addObject("message", "actor.commit.error");
		}
		return result;
	}

	
	protected ModelAndView createEditModelAndView(final Exercise exercise, int dayId) {
		ModelAndView result;
		result = this.createEditModelAndView(exercise, dayId, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Exercise exercise, int dayId,
			final String message) {
		ModelAndView result;
		result = new ModelAndView("exercise/edit");
		result.addObject("exercise", exercise);
		result.addObject("dayId", dayId);
		result.addObject("userId", this.userService.findByPrincipal().getId());
		result.addObject("message", message);
		return result;
	}

}
