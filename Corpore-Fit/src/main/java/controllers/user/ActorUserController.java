
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityBookService;
import services.ActorService;
import services.FriendRequestService;
import services.GymRatingService;
import services.NutritionistService;
import services.SubscriptionService;
import services.UserService;
import controllers.AbstractController;
import domain.ActivityBook;
import domain.Actor;
import domain.FriendRequest;
import domain.GymRating;
import domain.Nutritionist;
import domain.User;

@Controller
@RequestMapping("/actor")
public class ActorUserController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserService				userService;

	@Autowired
	private NutritionistService		nutritionistService;

	@Autowired
	private FriendRequestService	friendRequestService;

	@Autowired
	private GymRatingService		gymRatingService;

	@Autowired
	private SubscriptionService		subscriptionService;

	@Autowired
	private ActivityBookService		activityBookService;


	@RequestMapping(value = "/user/list-friends", method = RequestMethod.GET)
	public ModelAndView listFriends() {
		ModelAndView result;
		final User principal = this.userService.findByPrincipal();
		final Collection<User> friends = new ArrayList<User>();

		// For every request, if the sender is the logged user, then the friend
		// is the receiver.
		// If not, then the friend is the sender, and the logged user is the receiver.

		for (final FriendRequest fr : this.friendRequestService.getAcceptedRequests())
			if (fr.getSender().equals(principal))
				friends.add(fr.getReceiver());
			else
				friends.add(fr.getSender());

		result = new ModelAndView("actor/list");
		result.addObject("actors", friends);
		result.addObject("isFriendList", true);
		result.addObject("requestURI", "actor/user/list-friends.do");
		return result;
	}

	@RequestMapping(value = "/user/seeStatistics", method = RequestMethod.GET)
	public ModelAndView seeStatistics() {
		ModelAndView result;
		final User principal = this.userService.findByPrincipal();

		result = new ModelAndView("actor/statistics");
		result.addObject("actor", principal);
		return result;
	}

	//List validated nutritionist
	@RequestMapping(value = "/user/list-nutritionist", method = RequestMethod.GET)
	public ModelAndView listNutritionists() {
		Integer nutritionistId = null;
		ModelAndView result;
		final User principal = this.userService.findByPrincipal();
		Collection<Nutritionist> nutritionists = new ArrayList<Nutritionist>();
		nutritionists = this.nutritionistService.findValidated();
		if (principal.getNutritionist() != null)
			nutritionistId = principal.getNutritionist().getId();

		result = new ModelAndView("actor/list");
		result.addObject("actors", nutritionists);
		result.addObject("nutritionistId", nutritionistId);
		result.addObject("requestURI", "actor/user/list-nutritionist.do");
		return result;
	}

	//Set nutritionist as choosen
	@RequestMapping(value = "/user/choose-nutritionist", method = RequestMethod.GET)
	public ModelAndView chooseNutritionist(@RequestParam final int nutritionistId) {
		ModelAndView result;
		final User principal = this.userService.findByPrincipal();
		final Nutritionist nutritionist = this.nutritionistService.findOne(nutritionistId);
		Assert.isTrue(nutritionist.isValidated());
		Assert.isTrue(!nutritionist.isBanned());
		principal.setNutritionist(nutritionist);
		this.userService.save(principal);
		result = new ModelAndView("redirect:/actor/user/list-nutritionist.do");
		return result;
	}

	//Unset nutritionist as choosen
	@RequestMapping(value = "/user/unchoose-nutritionist", method = RequestMethod.GET)
	public ModelAndView unchooseNutritionist(@RequestParam final int nutritionistId) {
		ModelAndView result;
		final User principal = this.userService.findByPrincipal();
		final Nutritionist nutritionist = this.nutritionistService.findOne(nutritionistId);
		Assert.isTrue(nutritionist.equals(principal.getNutritionist().getId()));
		principal.setNutritionist(null);
		principal.setDiet(null);
		this.userService.save(principal);
		result = new ModelAndView("redirect:/actor/user/list-nutritionist.do");
		return result;
	}

	//Display nutritionist choosen
	@RequestMapping(value = "/user/my-nutritionist", method = RequestMethod.GET)
	public ModelAndView myNutritionist() {
		ModelAndView result;
		final User principal = this.userService.findByPrincipal();
		final Nutritionist nutritionist = principal.getNutritionist();
		if (nutritionist == null)
			result = new ModelAndView("redirect:/actor/user/list-nutritionist.do");
		else
			result = new ModelAndView("redirect:/actor/display.do?actorId=" + nutritionist.getId());
		return result;
	}

	// Edit User
	@RequestMapping(value = "/user/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") User user, final BindingResult binding) {
		ModelAndView result;
		user = this.userService.reconstruct(user, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(user);
		else
			try {
				this.userService.save(user);
				result = new ModelAndView("redirect:/actor/display-principal.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(user, "actor.commit.error");
			}
		return result;
	}

	// Delete an user
	@RequestMapping(value = "/user/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final int actorId) {
		ModelAndView result;
		final User user = this.userService.findOne(actorId);
		Assert.isTrue(user.equals(this.actorService.findByPrincipal()));
		try {
			//			for (Appointment ap : user.getAppointments()) {
			//				this.appointmentService.delete(ap);
			//			}
			for (final ActivityBook ab : this.activityBookService.findAllByUser(user.getId()))
				this.activityBookService.delete(ab);
			this.subscriptionService.delete(user.getSubscription());
			if (user.getGymRatings().isEmpty())
				this.userService.delete(user);
			else {
				final Collection<GymRating> gymRatings = new HashSet<GymRating>(user.getGymRatings());

				for (final GymRating gr : gymRatings)
					this.gymRatingService.delete(gr);

				this.userService.delete(user);
			}

			result = new ModelAndView("redirect:../../j_spring_security_logout");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(user, "actor.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;
		result = this.createEditModelAndView(actor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String message) {
		ModelAndView result;
		result = new ModelAndView("actor/edit");
		result.addObject(actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase(), actor);
		result.addObject("authority", actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());
		result.addObject("message", message);
		result.addObject("actor", actor);
		return result;
	}
}
