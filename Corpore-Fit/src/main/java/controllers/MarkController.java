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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FriendRequestService;
import services.UserService;
import domain.Mark;
import domain.Privacy;
import domain.User;

@Controller
@RequestMapping("/mark")
public class MarkController extends AbstractController {
	
	
	@Autowired
	ActorService actorService;

	@Autowired
	UserService userService;
	
	@Autowired
	FriendRequestService friendRequestService;

	// Constructors -----------------------------------------------------------

	public MarkController() {
		super();
	}

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam int userId) {
		ModelAndView result;
		boolean isCreator = false;
		
		if(this.actorService.isUser()){
			if(this.userService.findByPrincipal().getId()==userId){
				isCreator=true;
			}
		}
		
		User user = this.userService.findOne(userId);
		
		if (user.getPrivacy().equals(Privacy.CLOSED))
			Assert.isTrue(this.userService.findByPrincipal().getId() == userId);

		if (user.getPrivacy().equals(Privacy.FRIENDS))
			Assert.isTrue(this.userService.findByPrincipal().getId() == userId || this.friendRequestService.areFriends(this.userService.findByPrincipal(), user));

		
		Collection<Mark> marks = user.getMarks();
		
		result = new ModelAndView("mark/list");
		result.addObject("marks",marks);
		result.addObject("requestURI","/mark/list.do");
		result.addObject("isCreator", isCreator);
		result.addObject("userId", userId);

		return result;
	}

	
}
