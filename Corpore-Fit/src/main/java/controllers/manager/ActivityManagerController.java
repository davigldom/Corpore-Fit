
package controllers.manager;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityBookService;
import services.ActivityService;
import services.GymService;
import services.ManagerService;
import services.RoomService;
import controllers.AbstractController;
import domain.Activity;
import domain.DayName;
import domain.Gym;
import domain.Room;

@Controller
@RequestMapping("/activity/manager")
public class ActivityManagerController extends AbstractController {

	@Autowired
	ActivityService	activityService;

	@Autowired
	GymService		gymService;

	@Autowired
	RoomService		roomService;

	@Autowired
	ManagerService	managerService;

	@Autowired
	ActivityBookService	activitybookService;


	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;

		Activity activity;
		activity = this.activityService.create();

		result = this.createEditModelAndView(activity);

		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int activityId) {
		ModelAndView result;
		Activity activity;

		activity = this.activityService.findOneToEdit(activityId);
		Assert.notNull(activity);
		result = this.createEditModelAndView(activity);

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Activity activity, final BindingResult binding) {
		ModelAndView result;

		activity = this.activityService.reconstruct(activity, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(activity);
		else
			try {

				final Gym gym = activity.getGym();
				final DayName day = activity.getDay();
				final Room room = activity.getRoom();
				final Calendar start = activity.getStart();
				final Calendar end = activity.getEnd();

				final Activity existing = this.activityService.countByGymDayRoomAndTime(gym.getId(), day, room.getId(), start, end);

				if(existing!=null) 
					Assert.isTrue(activity.getId()==existing.getId());

				this.activityService.save(activity);
				result = new ModelAndView("redirect:/activity/list.do?gymId=" + activity.getGym().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(activity, "activity.commit.error");
			}
		return result;
	}
	//Delete
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Activity activity, final BindingResult binding) {
		ModelAndView result;

		activity = this.activityService.reconstruct(activity, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(activity);
		else
			try {
				this.activityService.delete(activity);
				result = new ModelAndView("redirect:/activity/list.do?gymId=" + activity.getGym().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(activity, "activity.commit.error");
			}
		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Activity activity) {
		ModelAndView result;
		result = this.createEditModelAndView(activity, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Activity activity, final String message) {
		ModelAndView result;

		final Collection<Room> rooms = this.roomService.findByGym(activity.getGym().getId());

		result = new ModelAndView("activity/edit");
		result.addObject("message", message);
		result.addObject("activity", activity);
		result.addObject("rooms", rooms);
		return result;
	}
}
