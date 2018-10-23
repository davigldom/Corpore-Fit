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
import services.SocialNetworkService;
import services.UserService;
import controllers.AbstractController;
import domain.SNetwork;
import domain.SocialNetwork;

@Controller
@RequestMapping("/social-network/user")
public class SocialNetworkUserController extends AbstractController {

	@Autowired
	ActorService			actorService;

	@Autowired
	UserService				userService;

	@Autowired
	SocialNetworkService	socialNetworkService;


	// Constructors -----------------------------------------------------------

	public SocialNetworkUserController() {
		super();
	}

	// Create User
	@RequestMapping(value = "/addFacebook", method = RequestMethod.GET)
	public ModelAndView addFacebook() {
		ModelAndView result;
		final SocialNetwork socialNetwork = this.socialNetworkService.create();
		socialNetwork.setsocialNetworkType(SNetwork.FACEBOOK);
		Assert.isNull(this.socialNetworkService.findByTypeAndUser(this.userService.findByPrincipal().getId(), socialNetwork.getSocialNetworkType()));
		result = this.createEditModelAndView(socialNetwork);
		return result;
	}

	@RequestMapping(value = "/addTwitter", method = RequestMethod.GET)
	public ModelAndView addTwitter() {
		ModelAndView result;
		final SocialNetwork socialNetwork = this.socialNetworkService.create();
		socialNetwork.setsocialNetworkType(SNetwork.TWITTER);
		Assert.isNull(this.socialNetworkService.findByTypeAndUser(this.userService.findByPrincipal().getId(), socialNetwork.getSocialNetworkType()));
		result = this.createEditModelAndView(socialNetwork);
		return result;
	}

	@RequestMapping(value = "/addInstagram", method = RequestMethod.GET)
	public ModelAndView addInstagram() {
		ModelAndView result;
		final SocialNetwork socialNetwork = this.socialNetworkService.create();
		socialNetwork.setsocialNetworkType(SNetwork.INSTAGRAM);
		Assert.isNull(this.socialNetworkService.findByTypeAndUser(this.userService.findByPrincipal().getId(), socialNetwork.getSocialNetworkType()));
		result = this.createEditModelAndView(socialNetwork);
		return result;
	}

	@RequestMapping(value = "/addYoutube", method = RequestMethod.GET)
	public ModelAndView addYoutube() {
		ModelAndView result;
		final SocialNetwork socialNetwork = this.socialNetworkService.create();
		socialNetwork.setsocialNetworkType(SNetwork.YOUTUBE);
		Assert.isNull(this.socialNetworkService.findByTypeAndUser(this.userService.findByPrincipal().getId(), socialNetwork.getSocialNetworkType()));
		result = this.createEditModelAndView(socialNetwork);
		return result;
	}

	// Edit User
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SocialNetwork socialNetwork, final BindingResult binding) {
		ModelAndView result;
		socialNetwork = this.socialNetworkService.reconstruct(socialNetwork, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(socialNetwork);
		else
			try {
				Assert.isTrue(this.userService.findByPrincipal().getSocialNetworks().size() < 4);
				Assert.isNull(this.socialNetworkService.findByTypeAndUser(this.userService.findByPrincipal().getId(), socialNetwork.getSocialNetworkType()));
				final SocialNetwork saved = this.socialNetworkService.save(socialNetwork);
				this.userService.findByPrincipal().getSocialNetworks().add(saved);
				this.userService.save(this.userService.findByPrincipal());

				result = new ModelAndView("redirect:/actor/display-principal.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(socialNetwork, "social.network.commit.error");
			}
		return result;
	}

	// Delete an user
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int socialNetworkId) {
		ModelAndView result;
		try {
			final SocialNetwork socialNetwork = this.socialNetworkService.findOne(socialNetworkId);
			Assert.isTrue(this.userService.findByPrincipal().getSocialNetworks().contains(socialNetwork));
			this.userService.findByPrincipal().getSocialNetworks().remove(socialNetwork);
			this.socialNetworkService.delete(socialNetwork);
			result = new ModelAndView("redirect:/actor/display-principal.do");
		} catch (final Throwable oops) {
			final SocialNetwork socialNetwork = this.socialNetworkService.findOne(socialNetworkId);
			result = this.createEditModelAndView(socialNetwork, "social.network.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialNetwork socialNetwork) {
		ModelAndView result;
		result = this.createEditModelAndView(socialNetwork, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialNetwork socialNetwork, final String message) {
		ModelAndView result;
		result = new ModelAndView("social-network/edit");
		result.addObject("socialNetwork", socialNetwork);
		result.addObject("userId", this.userService.findByPrincipal().getId());
		result.addObject("message", message);
		result.addObject("requestURI", "social-network/user/edit.do");
		return result;
	}

}
