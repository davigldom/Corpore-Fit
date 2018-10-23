
package controllers.manager;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ManagerService;
import services.RoomService;
import controllers.AbstractController;
import domain.Actor;
import domain.Manager;
import domain.Room;

@Controller
@RequestMapping("/actor")
public class ActorManagerController extends AbstractController {

	@Autowired
	ActorService	actorService;

	@Autowired
	ManagerService	managerService;

	@Autowired
	RoomService		roomService;


	//Edit User
	@RequestMapping(value = "/manager/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") Manager manager, final BindingResult binding) {
		ModelAndView result;
		manager = this.managerService.reconstruct(manager, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(manager);
		else
			try {
				Assert.isTrue(this.managerService.findByPrincipal().equals(manager));
				this.managerService.save(manager);
				result = new ModelAndView("redirect:/actor/display-principal.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(manager, "actor.commit.error");
			}
		return result;
	}

	// Delete a manager
	@RequestMapping(value = "/manager/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final int actorId) {
		ModelAndView result;
		final Manager manager = this.managerService.findOne(actorId);

		for (final Room room : this.roomService.findByManager(manager.getId()))
			this.roomService.delete(room);
		if (manager.getGym() != null)
			result = this.createEditModelAndViewDisplay(actorId, "manager.delete.has.gym.error");
		else {
			this.managerService.delete(manager);
			result = new ModelAndView("redirect:../../j_spring_security_logout");
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

	protected ModelAndView createEditModelAndViewDisplay(final int actorId, final String message) {
		ModelAndView result;

		final Actor actor = this.actorService.findOne(actorId);

		result = new ModelAndView("actor/display");
		result.addObject("authority", actor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());
		result.addObject("actor", actor);
		result.addObject("message", message);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		final String dateFormated = formatter.format(actor.getBirthdate().getTime());

		result.addObject("dateFormated", dateFormated);

		return result;
	}
}
