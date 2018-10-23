/*
 * MarkController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.DayService;
import services.ExerciseService;
import services.FriendRequestService;
import services.UserService;
import domain.Exercise;
import domain.Privacy;
import domain.User;

@Controller
@RequestMapping("/exercise")
public class ExerciseController extends AbstractController {

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

	public ExerciseController() {
		super();
	}

	@RequestMapping("/display")
	public ModelAndView display(@RequestParam int exerciseId) {
		ModelAndView result;
		boolean isCreator = false;
		User creator = this.dayService.findCreator(this.exerciseService
				.findDay(exerciseId).getId());

		if (this.actorService.isUser()) {
			if (this.userService.findByPrincipal().getId() == creator.getId()) {
				isCreator = true;
			}
		}

		if (!isCreator) {
			Assert.isTrue(!creator.getPrivacy().equals(Privacy.CLOSED));

			if (creator.getPrivacy().equals(Privacy.FRIENDS))
						Assert.isTrue(this.friendRequestService.areFriends(
								this.userService.findByPrincipal(), creator));
		}
		
		Exercise exercise = this.exerciseService.findOne(exerciseId);

		result = new ModelAndView("exercise/display");
		result.addObject("exercise", exercise);
		result.addObject("isCreator", isCreator);
		result.addObject("userId", creator.getId());

		return result;
	}
}
