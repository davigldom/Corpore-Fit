/*
 * FriendRequestUserController.java
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FriendRequestService;
import services.UserService;
import controllers.AbstractController;
import domain.FriendRequest;
import domain.User;

@Controller
@RequestMapping("/friend-request/user")
public class FriendRequestUserController extends AbstractController {

	@Autowired
	ActorService			actorService;

	@Autowired
	UserService				userService;

	@Autowired
	FriendRequestService	friendRequestService;


	// Constructors -----------------------------------------------------------

	public FriendRequestUserController() {
		super();
	}

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView sendRequest(@RequestParam final int userId) {
		ModelAndView result;
		final User receiver = this.userService.findOne(userId);

		Assert.isTrue(!receiver.isBanned());

		final FriendRequest fr = this.friendRequestService.create();
		fr.setReceiver(receiver);
		fr.setSender(this.userService.findByPrincipal());
		this.friendRequestService.save(fr);

		result = new ModelAndView("redirect:/friend-request/user/list-sent.do");
		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView acceptRequest(@RequestParam final int friendRequestId) {
		ModelAndView result;
		final FriendRequest fr = this.friendRequestService.findOne(friendRequestId);

		Assert.isTrue(!fr.getSender().isBanned());

		this.friendRequestService.accept(fr);

		result = new ModelAndView("redirect:/actor/user/list-friends.do");
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteRequest(@RequestParam final int friendRequestId) {
		ModelAndView result;
		final FriendRequest fr = this.friendRequestService.findOne(friendRequestId);
		this.friendRequestService.delete(fr);

		result = new ModelAndView("redirect:/friend-request/user/list-sent.do");
		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView rejectRequest(@RequestParam final int friendRequestId) {
		ModelAndView result;
		final FriendRequest fr = this.friendRequestService.findOne(friendRequestId);

		Assert.isTrue(!fr.getSender().isBanned());

		this.friendRequestService.reject(fr);

		result = new ModelAndView("redirect:/friend-request/user/list-received.do");
		return result;
	}

	@RequestMapping(value = "/remove-friend", method = RequestMethod.GET)
	public ModelAndView removeFriend(@RequestParam final int actorId) {
		ModelAndView result;
		final FriendRequest fr = this.friendRequestService.findByUsers(this.userService.findByPrincipal().getId(), actorId);
		this.friendRequestService.removeFriend(fr);

		result = new ModelAndView("redirect:/actor/user/list-friends.do");
		return result;
	}

	@RequestMapping(value = "/list-received", method = RequestMethod.GET)
	public ModelAndView listReceived() {
		ModelAndView result;

		result = new ModelAndView("friend-request/list-received");
		result.addObject("friendRequests", this.friendRequestService.getReceivedRequests());
		result.addObject("requestURI", "friend-request/user/list-received.do");
		return result;
	}

	@RequestMapping(value = "/list-sent", method = RequestMethod.GET)
	public ModelAndView listSent() {
		ModelAndView result;

		result = new ModelAndView("friend-request/list-sent");
		result.addObject("friendRequests", this.friendRequestService.getSentRequests());
		result.addObject("requestURI", "friend-request/user/list-sent.do");
		return result;
	}

}
