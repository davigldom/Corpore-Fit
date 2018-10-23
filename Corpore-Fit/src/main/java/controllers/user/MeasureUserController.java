/*
 * MeasureUserController.java
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
import services.MeasureService;
import services.UserService;
import controllers.AbstractController;
import domain.Measure;

@Controller
@RequestMapping("/measure/user")
public class MeasureUserController extends AbstractController {

	@Autowired
	ActorService	actorService;

	@Autowired
	UserService		userService;

	@Autowired
	MeasureService	measureService;


	// Constructors -----------------------------------------------------------

	public MeasureUserController() {
		super();
	}

	// Create User
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createUser() {
		ModelAndView result;
		final Measure measure = this.measureService.create();
		result = this.createEditModelAndView(measure);
		return result;
	}

	// Edit User
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Measure measure, final BindingResult binding) {
		ModelAndView result;
		measure = this.measureService.reconstruct(measure, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(measure);
		else
			try {
				this.measureService.save(measure);
				result = new ModelAndView("redirect:/measure/list.do?userId=" + this.userService.findByPrincipal().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(measure, "actor.commit.error");
			}
		return result;
	}

	// Delete an user
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int measureId) {
		ModelAndView result;
		try {
			final Measure measure = this.measureService.findOne(measureId);
			Assert.isTrue(this.userService.findByPrincipal().getMeasures().contains(measure));
			Assert.isTrue(this.userService.findByPrincipal().getMeasures().contains(measure));
			this.userService.findByPrincipal().getMeasures().remove(measure);
			this.measureService.delete(measure);
			result = new ModelAndView("redirect:/measure/list.do?userId=" + this.userService.findByPrincipal().getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("measure/list");
			result.addObject("measures", this.userService.findByPrincipal().getMeasures());
			result.addObject("isCreator", true);
			result.addObject("requestURI","/measure/list.do");
			result.addObject("message", "actor.commit.error");

		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Measure measure) {
		ModelAndView result;
		result = this.createEditModelAndView(measure, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Measure measure, final String message) {
		ModelAndView result;
		result = new ModelAndView("measure/edit");
		result.addObject("measure", measure);
		result.addObject(this.userService.findByPrincipal().getId());
		result.addObject("message", message);
		return result;
	}

}
