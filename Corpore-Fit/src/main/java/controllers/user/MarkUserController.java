/*
 * MarkUserController.java
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
import services.MarkService;
import services.UserService;
import controllers.AbstractController;
import domain.Mark;

@Controller
@RequestMapping("/mark/user")
public class MarkUserController extends AbstractController {

	@Autowired
	ActorService	actorService;

	@Autowired
	UserService		userService;

	@Autowired
	MarkService		markService;


	// Constructors -----------------------------------------------------------

	public MarkUserController() {
		super();
	}

	// Create User
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createUser() {
		ModelAndView result;
		Mark mark = this.markService.create();
		if(!this.userService.findByPrincipal().getMarks().isEmpty()){
			mark = this.markService.getLatestMark(this.userService.findByPrincipal());
		}
		result = this.createEditModelAndView(mark);
		return result;
	}

	// Edit User
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Mark mark, final BindingResult binding) {
		ModelAndView result;
		mark = this.markService.reconstruct(mark, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(mark);
		else
			try {
				this.markService.save(mark);
				result = new ModelAndView("redirect:/mark/list.do?userId=" + this.userService.findByPrincipal().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(mark, "actor.commit.error");
			}
		return result;
	}

	// Delete an user
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int markId) {
		ModelAndView result;
		try {
			final Mark mark = this.markService.findOne(markId);
			Assert.isTrue(this.userService.findByPrincipal().getMarks().contains(mark));
			this.userService.findByPrincipal().getMarks().remove(mark);
			this.markService.delete(mark);
			result = new ModelAndView("redirect:/mark/list.do?userId=" + this.userService.findByPrincipal().getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("mark/list");
			result.addObject("marks", this.userService.findByPrincipal().getMarks());
			result.addObject("isCreator", true);
			result.addObject("requestURI","/mark/list.do");
			result.addObject("message", "actor.commit.error");

		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Mark mark) {
		ModelAndView result;
		result = this.createEditModelAndView(mark, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Mark mark, final String message) {
		ModelAndView result;
		result = new ModelAndView("mark/edit");
		result.addObject("mark", mark);
		result.addObject(this.userService.findByPrincipal().getId());
		result.addObject("message", message);
		return result;
	}

}
