/*
 * RoutineUserController.java
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
import services.RoutineService;
import services.UserService;
import controllers.AbstractController;
import domain.Routine;

@Controller
@RequestMapping("/routine/user")
public class RoutineUserController extends AbstractController {

	@Autowired
	ActorService	actorService;

	@Autowired
	UserService		userService;

	@Autowired
	RoutineService	routineService;


	// Constructors -----------------------------------------------------------

	public RoutineUserController() {
		super();
	}

	// Create User
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createRoutine() {
		ModelAndView result;
		Assert.isNull(this.userService.findByPrincipal().getRoutine());
		final Routine routine = this.routineService.create();
		result = this.createEditModelAndView(routine);
		return result;
	}

	// Edit User
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Routine routine, final BindingResult binding) {
		ModelAndView result;
		routine = this.routineService.reconstruct(routine, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(routine);
		else
			try {
				this.routineService.save(routine);
				result = new ModelAndView("redirect:/routine/display.do?userId=" + this.userService.findByPrincipal().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(routine, "actor.commit.error");
			}
		return result;
	}

	// Delete an user
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int routineId) {
		ModelAndView result;
		try {
			final Routine routine = this.routineService.findOneToEdit(routineId);
			this.routineService.delete(routine);
			result = new ModelAndView("redirect:/actor/display.do?actorId=" + this.userService.findByPrincipal().getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("routine/display");
			result.addObject("isCreator", true);
			result.addObject("routine", this.userService.findByPrincipal().getRoutine());
			result.addObject("message", "actor.commit.error");

		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Routine routine) {
		ModelAndView result;
		result = this.createEditModelAndView(routine, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Routine routine, final String message) {
		ModelAndView result;
		result = new ModelAndView("routine/edit");
		result.addObject("routine", routine);
		result.addObject(this.userService.findByPrincipal().getId());
		result.addObject("message", message);
		return result;
	}

}
