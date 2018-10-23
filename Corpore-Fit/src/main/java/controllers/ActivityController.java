package controllers;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityBookService;
import services.ActivityService;
import services.ActorService;
import services.GymService;
import services.ManagerService;
import services.UserService;
import domain.Activity;
import domain.ActivityBook;
import domain.Gym;

@Controller
@RequestMapping("/activity")
public class ActivityController extends AbstractController {

	@Autowired
	ActivityService activityService;

	@Autowired
	ActivityBookService activityBookService;

	@Autowired
	ActorService actorService;

	@Autowired
	ManagerService managerService;

	@Autowired
	UserService userService;

	@Autowired
	GymService gymService;

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final int gymId) {
		ModelAndView result;

		final Gym gym = this.gymService.findOne(gymId);

		boolean isCreator = false;
		if (this.actorService.isManager())
			if (this.managerService.findByPrincipal().equals(gym.getManager()))
				isCreator = true;

		final Collection<Activity> mondayActivities = this.activityService
				.findByGymMonday(gymId);
		final Collection<Activity> tuesdayActivities = this.activityService
				.findByGymTuesday(gymId);
		final Collection<Activity> wednesdayActivities = this.activityService
				.findByGymWednesday(gymId);
		final Collection<Activity> thursdayActivities = this.activityService
				.findByGymThursday(gymId);
		final Collection<Activity> fridayActivities = this.activityService
				.findByGymFriday(gymId);
		final Collection<Activity> saturdayActivities = this.activityService
				.findByGymSaturday(gymId);
		final Collection<Activity> sundayActivities = this.activityService
				.findByGymSunday(gymId);

		result = new ModelAndView("activity/list");
		result.addObject("mondayActivities", mondayActivities);
		result.addObject("tuesdayActivities", tuesdayActivities);
		result.addObject("wednesdayActivities", wednesdayActivities);
		result.addObject("thursdayActivities", thursdayActivities);
		result.addObject("fridayActivities", fridayActivities);
		result.addObject("saturdayActivities", saturdayActivities);
		result.addObject("sundayActivities", sundayActivities);
		result.addObject("isCreator", isCreator);
		result.addObject("requestURI", "/activity/list.do");
		result.addObject("backURI", "/gym/display.do?gymId=" + gym.getId());

		return result;
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int activityId) {
		ModelAndView result;

		Activity activity;
		activity = this.activityService.findOne(activityId);

		boolean isCreator = false;
		if (this.actorService.isManager())
			if (this.managerService.findByPrincipal().equals(
					activity.getGym().getManager()))
				isCreator = true;

		boolean isSubscribed = false;
		boolean alreadyBooked = false;
		boolean activityFull = false;
		final Calendar cal = Calendar.getInstance();
		final int week = cal.get(Calendar.WEEK_OF_YEAR);
		if (this.actorService.isUser())
			if (this.userService.findByPrincipal().getSubscription() != null
					&& this.userService.findByPrincipal().getSubscription()
							.getGym().equals(activity.getGym())) {

				final Collection<ActivityBook> books = this.activityBookService
						.findAllByUser(this.userService.findByPrincipal()
								.getId());
				for (final ActivityBook ab : books) {
					ab.getCreationDate();
					int bookWeek = ab.getCreationDate().get(Calendar.WEEK_OF_YEAR);
					if (week != bookWeek)
						this.activityBookService.delete(ab);
				}

				isSubscribed = true;
				if (this.activityBookService.alreadyBooked(activity.getId()))
					alreadyBooked = true;
				else
					activityFull = this.activityBookService
							.activityFull(activity.getId());

			}

		Assert.notNull(activity);
		result = new ModelAndView("activity/display");
		result.addObject("activity", activity);
		result.addObject("isCreator", isCreator);
		result.addObject("isSubscribed", isSubscribed);
		result.addObject("activityFull", activityFull);
		result.addObject("alreadyBooked", alreadyBooked);

		return result;
	}
}
