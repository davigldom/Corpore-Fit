package controllers.user;

import java.util.Calendar;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityBookService;
import services.ActivityService;
import services.UserService;
import controllers.AbstractController;
import domain.Activity;
import domain.ActivityBook;
import domain.DayName;
import domain.Gym;
import domain.User;

@Controller
@RequestMapping("/activity-book/user")
public class ActivityBookUserController extends AbstractController {

	@Autowired
	private ActivityBookService activityBookService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UserService userService;

	// Create Ativity Book
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int activityId) {

		final User user = this.userService.findByPrincipal();
		final Activity activity = this.activityService.findOne(activityId);

		final ActivityBook activityBook = this.activityBookService.create();
		activityBook.setActivity(activity);
		activityBook.setUser(user);

		return this.save(activityBook);
	}

	// Save Activity Book
	@RequestMapping(value = "/book", method = RequestMethod.POST, params = "book")
	public ModelAndView save(@Valid final ActivityBook activityBook) {
		ModelAndView result;

		try {
			// Check if the room where the activity takes place is full
			Assert.isTrue(!this.activityBookService.isFull(activityBook));

			// Check if you have a book for another activity the same day at the
			// same time
			final User principal = this.userService.findByPrincipal();
			final Activity activity = activityBook.getActivity();
			final Gym gym = activity.getGym();
			final DayName day = activity.getDay();
			final Calendar start = activity.getStart();
			final Calendar end = activity.getEnd();
			final int books = this.activityBookService
					.countByUserGymDayAndTime(principal.getId(), gym.getId(),
							day, start, end);
			Assert.isTrue(books == 0);

			this.activityBookService.save(activityBook);
			result = new ModelAndView(
					"redirect:/activity/display.do?activityId="
							+ activityBook.getActivity().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(activityBook,
					"activity.book.commit.error");
		}
		return result;
	}

	// Save Activity Book
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView delete(@Valid final int activityId) {
		ModelAndView result;

		final User principal = this.userService.findByPrincipal();
		final ActivityBook activityBook = this.activityBookService
				.findByUserAndActivity(principal.getId(), activityId);

		try {

			Assert.isTrue(activityBook.getUser().equals(principal));

			this.activityBookService.delete(activityBook);
			result = new ModelAndView(
					"redirect:/activity/display.do?activityId="
							+ activityBook.getActivity().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(activityBook,
					"activity.book.commit.error");
		}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(
			final ActivityBook activityBook) {
		ModelAndView result;
		result = this.createEditModelAndView(activityBook, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(
			final ActivityBook activityBook, final String message) {
		ModelAndView result;

		final Activity activity = activityBook.getActivity();

		boolean isCreator = false;

		boolean isSubscribed = false;
		boolean alreadyBooked = false;
		boolean activityFull = false;
		final Calendar cal = Calendar.getInstance();
		final int week = cal.get(Calendar.WEEK_OF_YEAR);
		if (this.userService.findByPrincipal().getSubscription() != null
				&& this.userService.findByPrincipal().getSubscription()
						.getGym().equals(activity.getGym())) {

			final Collection<ActivityBook> books = this.activityBookService
					.findAllByUser(this.userService.findByPrincipal().getId());
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
				activityFull = this.activityBookService.activityFull(activity
						.getId());

		}

		Assert.notNull(activity);
		result = new ModelAndView("activity/display");
		result.addObject("activity", activity);
		result.addObject("isCreator", isCreator);
		result.addObject("isSubscribed", isSubscribed);
		result.addObject("activityFull", activityFull);
		result.addObject("alreadyBooked", alreadyBooked);
		result.addObject("message", message);
		result.addObject("activityBook", activityBook);

		return result;
	}

}
