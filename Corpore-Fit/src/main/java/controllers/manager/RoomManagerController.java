
package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.ActorService;
import services.GymService;
import services.ManagerService;
import services.RoomService;
import controllers.AbstractController;
import domain.Gym;
import domain.Room;

@Controller
@RequestMapping("/room/manager")
public class RoomManagerController extends AbstractController {

	@Autowired
	RoomService		roomService;

	@Autowired
	ActivityService	activityService;

	@Autowired
	GymService		gymService;

	@Autowired
	ManagerService	managerService;

	@Autowired
	ActorService	actorService;


	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final int gymId) {
		ModelAndView result;

		final Gym gym = this.gymService.findOne(gymId);

		boolean isCreator = false;
		if (this.actorService.isManager())
			if (this.managerService.findByPrincipal().equals(gym.getManager()))
				isCreator = true;

		final Collection<Room> rooms = this.roomService.findByGym(gymId);

		result = new ModelAndView("room/list");
		result.addObject("rooms", rooms);
		result.addObject("isCreator", isCreator);
		result.addObject("requestURI", "/room/manager/list.do");

		return result;
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;

		Room room;
		room = this.roomService.create();

		result = this.createEditModelAndView(room);

		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int roomId) {
		ModelAndView result;
		Room room;

		room = this.roomService.findOneToEdit(roomId);
		Assert.notNull(room);
		result = this.createEditModelAndView(room);

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Room room, final BindingResult binding) {
		ModelAndView result;

		room = this.roomService.reconstruct(room, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(room);
		else
			try {
				this.roomService.save(room);
				result = new ModelAndView("redirect:/room/manager/list.do?gymId=" + room.getManager().getGym().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(room, "room.commit.error");
			}
		return result;
	}

	//Delete
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Room room, final BindingResult binding) {
		ModelAndView result;

		room = this.roomService.reconstruct(room, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(room);
		else
			try {
				this.roomService.delete(room);
				result = new ModelAndView("redirect:/room/manager/list.do?gymId=" + room.getManager().getGym().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(room, "room.commit.error");
			}
		return result;
	}

	// Ancilliary methods
	protected ModelAndView createEditModelAndView(final Room room) {
		ModelAndView result;
		result = this.createEditModelAndView(room, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Room room, final String message) {
		ModelAndView result;
		result = new ModelAndView("room/edit");
		result.addObject("message", message);
		result.addObject("room", room);
		return result;
	}

}
