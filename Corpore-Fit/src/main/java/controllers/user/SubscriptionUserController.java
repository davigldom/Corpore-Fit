
package controllers.user;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.GymService;
import services.SubscriptionService;
import services.UserService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Gym;
import domain.Subscription;
import domain.User;

@Controller
@RequestMapping("/subscription/user")
public class SubscriptionUserController extends AbstractController {

	@Autowired
	SubscriptionService	subscriptionService;

	@Autowired
	GymService			gymService;

	@Autowired
	UserService			userService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView sendRequest(@RequestParam final int gymId) {
		ModelAndView result;

		final User user = this.userService.findByPrincipal();
		final Gym gym = this.gymService.findOne(gymId);

		Assert.isTrue(user.getSubscription() == null);

		final Subscription subscription = this.subscriptionService.create();
		subscription.setGym(gym);
		subscription.setUser(user);

		result = this.createEditModelAndView(subscription);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Subscription subscription, final BindingResult binding) {
		ModelAndView result;

		subscription = this.subscriptionService.reconstruct(subscription, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(subscription);
		else
			try {
				this.subscriptionService.save(subscription);
				result = new ModelAndView("redirect:/gym/display.do?gymId=" + subscription.getGym().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(subscription, "subscription.commit.error");
			}
		return result;
	}

	// Delete gym
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int gymId) {
		ModelAndView result;

		final Gym gym = this.gymService.findOne(gymId);
		final User principal = this.userService.findByPrincipal();
		final Subscription subscription = principal.getSubscription();

		try {
			Assert.isTrue(subscription.getGym().getId() == gymId);
			final Collection<Subscription> subscriptions = new HashSet<Subscription>(gym.getSubscriptions());

			subscriptions.remove(subscription);
			gym.setSubscriptions(subscriptions);

			this.subscriptionService.delete(subscription);

			result = new ModelAndView("redirect:/gym/display.do?gymId=" + subscription.getGym().getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/gym/display.do");
			result.addObject("message", "subscription.commit.error");
		}

		return result;
	}
	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Subscription subscription) {
		ModelAndView result;
		result = this.createEditModelAndView(subscription, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Subscription subscription, final String message) {
		ModelAndView result;

		final Collection<CreditCard> creditCards = this.userService.findByPrincipal().getCreditCards();
		final Gym gym = subscription.getGym();

		result = new ModelAndView("subscription/edit");
		result.addObject("message", message);
		result.addObject("subscription", subscription);
		result.addObject("creditCards", creditCards);
		result.addObject("gym", gym);
		return result;
	}

}
