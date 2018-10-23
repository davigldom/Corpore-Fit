
package controllers.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.GymRatingService;
import services.GymService;
import services.SubscriptionService;
import controllers.AbstractController;
import domain.Activity;
import domain.Gym;
import domain.GymRating;
import domain.Subscription;

@Controller
@RequestMapping("/gym/admin")
public class GymAdministratorController extends AbstractController {

	@Autowired
	private GymService			gymService;

	@Autowired
	private ActivityService		activityService;

	@Autowired
	private GymRatingService	gymRatingService;

	@Autowired
	private SubscriptionService	subscriptionService;


	// Constructors -----------------------------------------------------------

	public GymAdministratorController() {
		super();
	}

	// Delete gym
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteGym(@RequestParam final int gymId) {
		ModelAndView result;

		final Gym gym = this.gymService.findOne(gymId);

		try {
			final Collection<GymRating> gymRatings = new HashSet<GymRating>(gym.getGymRatings());

			for (final GymRating gr : gymRatings)
				this.gymRatingService.delete(gr);

			final Collection<Activity> gymActivities = new ArrayList<Activity>(gym.getActivities());

			for (final Activity a : gymActivities)
				this.activityService.delete(a);

			final Collection<Subscription> subscriptions = new ArrayList<Subscription>(gym.getSubscriptions());

			for (final Subscription s : subscriptions)
				this.subscriptionService.delete(s);

			this.gymService.delete(gym);

			result = new ModelAndView("redirect:/gym/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/gym/list.do");
			result.addObject("message", "gym.commit.error");
		}

		return result;
	}
}
