
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.DailyDietService;
import services.DayService;
import services.FriendRequestService;
import services.NutritionistService;
import services.UserService;
import domain.Actor;
import domain.DaySchedule;
import domain.Exercise;
import domain.Food;
import domain.Nutritionist;
import domain.Role;
import domain.User;
import forms.RegisterNutritionist;
import forms.RegisterUser;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserService				userService;

	@Autowired
	private FriendRequestService	friendRequestService;

	@Autowired
	private NutritionistService		nutritionistService;

	@Autowired
	private DayService				dayService;

	@Autowired
	private DailyDietService		dailyDietService;


	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		final Collection<Actor> actors = this.actorService.findAllNotBanned();

		result = new ModelAndView("actor/list");
		result.addObject("actors", actors);
		result.addObject("requestURI", "/actor/list.do");

		return result;
	}

	@RequestMapping("/seeTop")
	public ModelAndView seeTop(final String roleSent, final String mark) {
		ModelAndView result;
		Collection<User> users = new ArrayList<User>();
		if (mark != null)
			switch (mark) {
			case "benchPress":
				users = this.userService.findTopBenchPress(Role.valueOf(roleSent));
				break;

			case "pullUp":
				users = this.userService.findTopPullUp(Role.valueOf(roleSent));
				break;

			case "deadlift":
				users = this.userService.findTopDeadlift(Role.valueOf(roleSent));
				break;

			case "squat":
				users = this.userService.findTopSquat(Role.valueOf(roleSent));
				break;

			case "rowing":
				users = this.userService.findTopRowing(Role.valueOf(roleSent));
				break;

			default:
				break;
			}

		result = new ModelAndView("actor/top");
		result.addObject("users", users);
		result.addObject("requestURI", "/users/seeTop.do");

		return result;
	}

	// Create User
	@RequestMapping(value = "/create-user", method = RequestMethod.GET)
	public ModelAndView createUser() {
		ModelAndView result;
		RegisterUser registerUser;

		registerUser = new RegisterUser();
		result = this.createEditModelAndViewRegisterUser(registerUser, null);
		return result;
	}

	@RequestMapping(value = "/create-user-ok", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RegisterUser registerUser, final BindingResult binding) {
		ModelAndView result;
		final User user;

		if (binding.hasErrors()) {
			registerUser.setAcceptedTerms(false);
			result = this.createEditModelAndViewRegisterUser(registerUser, null);
		} else
			try {
				user = this.userService.reconstructRegister(registerUser, binding);
				if (binding.hasErrors())
					result = this.createEditModelAndViewRegisterUser(registerUser, null);
				else {
					this.userService.save(user);
					result = new ModelAndView("redirect:/welcome/index.do");
				}
			} catch (final Throwable oops) {
				registerUser.setAcceptedTerms(false);
				result = this.createEditModelAndViewRegisterUser(registerUser, "actor.commit.error");
			}

		return result;
	}

	// Create Nutritionist
	@RequestMapping(value = "/create-nutritionist", method = RequestMethod.GET)
	public ModelAndView createNutritionist() {
		ModelAndView result;
		RegisterNutritionist registerNutritionist;

		registerNutritionist = new RegisterNutritionist();
		result = this.createEditModelAndViewRegisterNutritionist(registerNutritionist, null);
		return result;
	}

	@RequestMapping(value = "/create-nutritionist-ok", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RegisterNutritionist registerNutritionist, final BindingResult binding) {
		ModelAndView result;
		final Nutritionist nutritionist;

		if (binding.hasErrors()) {
			registerNutritionist.setAcceptedTerms(false);
			result = this.createEditModelAndViewRegisterNutritionist(registerNutritionist, null);
		} else
			try {
				nutritionist = this.nutritionistService.reconstructRegister(registerNutritionist, binding);
				if (binding.hasErrors())
					result = this.createEditModelAndViewRegisterNutritionist(registerNutritionist, null);
				else {
					this.nutritionistService.save(nutritionist);
					result = new ModelAndView("redirect:/welcome/index.do");
				}
			} catch (final Throwable oops) {
				registerNutritionist.setAcceptedTerms(false);
				result = this.createEditModelAndViewRegisterNutritionist(registerNutritionist, "actor.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int actorId) {
		ModelAndView result;
		final Actor actor = this.actorService.findOneToEdit(actorId);

		result = this.createEditModelAndView2(actor, null);
		result.addObject("actor", actor);

		return result;
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;
		boolean isFriend = false;
		boolean existsRequest = false;
		boolean isValid = true;
		Collection<Exercise> exercisesOfTheDay = null;
		Collection<Food> dietOfTheDay = null;

		actor = this.actorService.findOne(actorId);
		Assert.notNull(actor);

		if (actor.isBanned())
			Assert.isTrue(this.actorService.isAdmin());

		if (this.actorService.isUser() && actor.getUserAccount().getAuthorities().toArray()[0].toString().equals("USER")) {
			if (this.userService.findByPrincipal().equals(actor) && this.userService.findOne(actorId).getDiet() != null)
				dietOfTheDay = this.dailyDietService.findCurrentDailyDiet(this.userService.findByPrincipal().getDiet().getId()).getFoods();
			if (this.friendRequestService.areFriends(this.userService.findByPrincipal(), this.userService.findOne(actorId)))
				isFriend = true;

			// If there is a request already sent or received.
			if (this.friendRequestService.numberOfRequests(this.userService.findByPrincipal().getId(), actorId) == 1 || this.friendRequestService.numberOfRequests(actorId, this.userService.findByPrincipal().getId()) == 1)
				existsRequest = true;
		}

		if (actor.getUserAccount().getAuthorities().toArray()[0].toString().equals("USER") && this.userService.findOne(actorId).getRoutine() != null)
			exercisesOfTheDay = this.dayService.findCurrentDay(actor.getId()).getExercises();

		if (actor.getUserAccount().getAuthorities().toArray()[0].toString().equals("NUTRITIONIST")) {
			final List<DaySchedule> days = ((Nutritionist) actor).getSchedule();

			for (final DaySchedule ds : days)
				if (ds.getMorningStart() == null || ds.getMorningEnd() == null || ds.getAfternoonStart() == null || ds.getAfternoonEnd() == null)
					isValid = false;
		}

		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);
		result.addObject("isFriend", isFriend);
		result.addObject("existsRequest", existsRequest);
		result.addObject("isValid", isValid);
		result.addObject("exercisesOfTheDay", exercisesOfTheDay);
		result.addObject("dietOfTheDay", dietOfTheDay);
		result.addObject("authority", actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());

		return result;
	}

	// Display own data
	@RequestMapping(value = "/display-principal", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Actor actor;
		Collection<Exercise> exercisesOfTheDay = null;
		Collection<Food> dietOfTheDay = null;

		actor = this.actorService.findOne(this.actorService.findByPrincipal().getId());
		if (actor.getUserAccount().getAuthorities().toArray()[0].toString().equals("USER") && this.userService.findByPrincipal().getRoutine() != null)
			exercisesOfTheDay = this.dayService.findCurrentDay(actor.getId()).getExercises();

		if (actor.getUserAccount().getAuthorities().toArray()[0].toString().equals("USER") && this.userService.findByPrincipal().getDiet() != null)
			dietOfTheDay = this.dailyDietService.findCurrentDailyDiet(this.userService.findByPrincipal().getDiet().getId()).getFoods();
		Assert.notNull(actor);
		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);
		result.addObject("exercisesOfTheDay", exercisesOfTheDay);
		result.addObject("dietOfTheDay", dietOfTheDay);
		result.addObject("authority", actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());

		return result;
	}

	// Search actor
	@RequestMapping(value = "/search-word", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam final String keyword) {
		ModelAndView result;

		final Collection<Actor> actors = this.actorService.findAllByKeyword(keyword);

		result = new ModelAndView("actor/search");
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/search-word.do");
		result.addObject("keyword", keyword);
		result.addObject("searchActor", true);

		return result;
	}

	//	@RequestMapping(value = "/nutritionist/schedule", method = RequestMethod.GET)
	//	public ModelAndView editSchedule(@RequestParam final int nutritionistId) {
	//		ModelAndView result;
	//		final Nutritionist nutritionist = this.nutritionistService.findOne(nutritionistId);
	//		Hibernate.initialize(nutritionist.getSchedule());
	//		final List<DaySchedule> schedule = nutritionist.getSchedule();
	//		Assert.notNull(schedule);
	//
	//		if (!this.actorService.isAdmin())
	//			if (!nutritionist.isValidated())
	//				Assert.isTrue(this.nutritionistService.findByPrincipal().equals(nutritionist));
	//
	//		result = new ModelAndView("nutritionist/schedule");
	//
	//		if (this.actorService.isUser() && this.userService.findByPrincipal().getNutritionist() != null && this.userService.findByPrincipal().getNutritionist().equals(nutritionist))
	//			result.addObject("canMakeAppointment", true);
	//
	//		result.addObject("schedule", schedule);
	//		result.addObject("username", nutritionist.getUserAccount().getUsername());
	//		return result;
	//	}

	// Ancilliary methods
	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;
		result = this.createEditModelAndView(actor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String message) {
		ModelAndView result;
		result = new ModelAndView("actor/signup");
		result.addObject(actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase(), actor);
		result.addObject("authority", actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());
		result.addObject("message", message);
		return result;
	}

	protected ModelAndView createEditModelAndView2(final Actor actor, final String message) {
		ModelAndView result;
		result = new ModelAndView("actor/edit");
		result.addObject(actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase(), actor);
		result.addObject("authority", actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());
		result.addObject("message", message);
		return result;
	}

	protected ModelAndView createEditModelAndViewRegisterUser(final RegisterUser registerUser, final String message) {
		ModelAndView result;

		final String requestURI = "actor/create-user-ok.do";

		result = new ModelAndView("actor/signupuser");
		result.addObject("registerActorForm", "registerUser");
		result.addObject("registerUser", registerUser);
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);
		result.addObject("isUser", true);

		return result;
	}

	protected ModelAndView createEditModelAndViewRegisterNutritionist(final RegisterNutritionist registerNutritionist, final String message) {
		ModelAndView result;

		final String requestURI = "actor/create-nutritionist-ok.do";

		result = new ModelAndView("actor/signup");
		result.addObject("registerActorForm", "registerNutritionist");
		result.addObject("registerNutritionist", registerNutritionist);
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);

		return result;
	}

}
