
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.GymService;
import services.UserService;
import domain.Gym;
import domain.GymRating;
import domain.User;

@Controller
@RequestMapping("/gym")
public class GymController extends AbstractController {

	@Autowired
	private GymService		gymService;

	@Autowired
	private UserService		userService;

	@Autowired
	private ActorService	actorService;


	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		final Collection<Gym> gyms = this.gymService.findAll();

		result = new ModelAndView("gym/list");
		result.addObject("gyms", gyms);
		result.addObject("requestURI", "/gym/list.do");

		return result;
	}

	@RequestMapping("/list-top")
	public ModelAndView listTop() {
		ModelAndView result;

		Collection<Gym> gyms = new ArrayList<Gym>();
		gyms = this.gymService.findTop5();

		result = new ModelAndView("gym/list");
		result.addObject("gyms", gyms);
		result.addObject("requestURI", "/gym/list-top.do");

		return result;
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int gymId) {
		ModelAndView result;
		boolean alreadyRated = false;
		double averageRating = 0.0;
		User principal = null;
		final Collection<GymRating> grUser;

		boolean isSubscribed = false;
		boolean hasSubscription = false;

		Gym gym;
		gym = this.gymService.findOne(gymId);

		final Collection<GymRating> grGym = new ArrayList<GymRating>(gym.getGymRatings());

		Assert.notNull(gym);

		if (this.actorService.isUser()) {
			principal = this.userService.findByPrincipal();
			grUser = new ArrayList<GymRating>(principal.getGymRatings());
			for (final GymRating g : grUser)
				if (grGym.contains(g))
					alreadyRated = true;

			//Check if the user is subscribed to a gym and if he is subscribed to the gym displayed
			if (principal.getSubscription() != null) {
				hasSubscription = true;
				if (principal.getSubscription().getGym().equals(gym))
					isSubscribed = true;
			}
		}

		double allRatings = 0.0;
		for (final GymRating g : grGym) {
			allRatings += g.getRating();
			averageRating = allRatings / grGym.size();
		}

		result = new ModelAndView("gym/display");
		result.addObject("gym", gym);
		result.addObject("hasGym", true);
		result.addObject("hasGymUser", true);
		result.addObject("alreadyRated", alreadyRated);
		result.addObject("averageRating", averageRating);
		result.addObject("isSubscribed", isSubscribed);
		result.addObject("hasSubscription", hasSubscription);

		return result;
	}

}
