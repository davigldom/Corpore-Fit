/*
 * ExerciseUserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.GymService;
import services.ManagerService;
import services.SocialNetworkService;
import controllers.AbstractController;
import domain.SNetwork;
import domain.SocialNetwork;

@Controller
@RequestMapping("/social-network/manager")
public class SocialNetworkManagerController extends AbstractController {

	@Autowired
	ActorService actorService;

	@Autowired
	ManagerService managerService;

	@Autowired
	GymService gymService;

	@Autowired
	SocialNetworkService socialNetworkService;

	// Constructors -----------------------------------------------------------

	public SocialNetworkManagerController() {
		super();
	}

	// Create User
	@RequestMapping(value = "/addFacebook", method = RequestMethod.GET)
	public ModelAndView addFacebook() {
		ModelAndView result;
		final SocialNetwork socialNetwork = this.socialNetworkService.create();
		socialNetwork.setsocialNetworkType(SNetwork.FACEBOOK);
		result = this.createEditModelAndView(socialNetwork);
		return result;
	}

	@RequestMapping(value = "/addTwitter", method = RequestMethod.GET)
	public ModelAndView addTwitter() {
		ModelAndView result;
		final SocialNetwork socialNetwork = this.socialNetworkService.create();
		socialNetwork.setsocialNetworkType(SNetwork.TWITTER);
		result = this.createEditModelAndView(socialNetwork);
		return result;
	}

	@RequestMapping(value = "/addInstagram", method = RequestMethod.GET)
	public ModelAndView addInstagram() {
		ModelAndView result;
		final SocialNetwork socialNetwork = this.socialNetworkService.create();
		socialNetwork.setsocialNetworkType(SNetwork.INSTAGRAM);
		result = this.createEditModelAndView(socialNetwork);
		return result;
	}

	@RequestMapping(value = "/addYoutube", method = RequestMethod.GET)
	public ModelAndView addYoutube() {
		ModelAndView result;
		final SocialNetwork socialNetwork = this.socialNetworkService.create();
		socialNetwork.setsocialNetworkType(SNetwork.YOUTUBE);
		result = this.createEditModelAndView(socialNetwork);
		return result;
	}

	// Edit User
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(SocialNetwork socialNetwork,
			final BindingResult binding) {
		ModelAndView result;
		socialNetwork = this.socialNetworkService.reconstruct(socialNetwork,
				binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(socialNetwork);
		else
			try {
				Assert.isTrue(this.managerService.findByPrincipal().getGym()
						.getSocialNetworks().size() < 4);
				Assert.isNull(this.socialNetworkService.findByTypeAndGym(
						this.managerService.findByPrincipal().getGym().getId(),
						socialNetwork.getSocialNetworkType()));
				SocialNetwork saved = this.socialNetworkService
						.save(socialNetwork);
				this.managerService.findByPrincipal().getGym()
						.getSocialNetworks().add(saved);
				this.gymService.save(this.managerService.findByPrincipal()
						.getGym());

				result = new ModelAndView(
						"redirect:/gym/manager/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(socialNetwork,
						"social.network.commit.error");
			}
		return result;
	}

	// Delete an user
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int socialNetworkId) {
		ModelAndView result;
		try {
			final SocialNetwork socialNetwork = this.socialNetworkService
					.findOne(socialNetworkId);
			Assert.isTrue(this.managerService.findByPrincipal().getGym()
					.getSocialNetworks().contains(socialNetwork));
			this.managerService.findByPrincipal().getGym().getSocialNetworks()
					.remove(socialNetwork);
			this.socialNetworkService.delete(socialNetwork);
			result = new ModelAndView("redirect:/gym/manager/display.do");
		} catch (final Throwable oops) {
			final SocialNetwork socialNetwork = this.socialNetworkService
					.findOne(socialNetworkId);
			result = this.createEditModelAndView(socialNetwork,
					"social.network.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(
			final SocialNetwork socialNetwork) {
		ModelAndView result;
		result = this.createEditModelAndView(socialNetwork, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(
			final SocialNetwork socialNetwork, final String message) {
		ModelAndView result;
		result = new ModelAndView("social-network/edit");
		result.addObject("socialNetwork", socialNetwork);
		result.addObject("gymId", this.managerService.findByPrincipal().getGym().getId());
		result.addObject("message", message);
		result.addObject("requestURI", "social-network/manager/edit.do");
		return result;
	}

}
