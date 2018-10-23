
package controllers.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.AppointmentService;
import services.UserService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Appointment;
import domain.User;

@Controller
@RequestMapping("/actor")
public class ActorAdministratorController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private UserService				userService;

	@Autowired
	private AppointmentService		appointmentService;
	
	


	// Constructors -----------------------------------------------------------

	public ActorAdministratorController() {
		super();
	}

	// Edit Administrator
	@RequestMapping(value = "/admin/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") Administrator administrator, final BindingResult binding) {
		ModelAndView result;
		administrator = this.administratorService.reconstruct(administrator, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(administrator);
		else
			try {
				this.administratorService.save(administrator);
				result = new ModelAndView("redirect:/actor/display-principal.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(administrator, "actor.commit.error");
			}
		return result;
	}

	// Ban actor
	@RequestMapping(value = "/admin/ban", method = RequestMethod.GET)
	public ModelAndView banActor(@RequestParam final int actorId) {
		ModelAndView result;
		final Actor actor = this.actorService.findOne(actorId);
		final String authority = actor.getUserAccount().getAuthorities().toArray()[0].toString();

		if (authority.equals("NUTRITIONIST")) {
			final Collection<User> users = this.userService.findUsersByNutritionistId(actorId);
			for (final User u : users) {
				final Collection<Appointment> appointments = new ArrayList<Appointment>(u.getAppointments());
				for (final Appointment a : appointments)
					this.appointmentService.delete(a);

				u.setNutritionist(null);
				u.setDiet(null);
			}
		}

		this.actorService.banActor(actorId);
		result = new ModelAndView("redirect:/actor/display.do?actorId=" + actorId);

		return result;
	}
	// UnBan actor
	@RequestMapping(value = "/admin/unban", method = RequestMethod.GET)
	public ModelAndView unbanActor(@RequestParam final int actorId) {
		ModelAndView result;

		try {
			this.actorService.unbanActor(actorId);
			result = new ModelAndView("redirect:/actor/display.do?actorId=" + actorId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndViewDisplay(actorId, "actor.commit.error");
		}

		return result;
	}

	//List banned actors
	@RequestMapping("/admin/list-banned")
	public ModelAndView list() {
		ModelAndView result;

		final Collection<Actor> actors = this.actorService.findAllBanned();

		result = new ModelAndView("actor/list");
		result.addObject("actors", actors);
		result.addObject("requestURI", "/actor/admin/list-banned.do");

		return result;
	}
	
	//Dashboard
	@RequestMapping("/admin/dashboard")
	public ModelAndView dashboard() {
		ModelAndView result;

		result = new ModelAndView("actor/dashboard");
		result.addObject("ratioWithoutGym", userService.getRatioUsersWithoutGym());
		result.addObject("ratioWithoutDiet", userService.getRatioUsersWithoutDiet());
		result.addObject("ratioWithClosedPrivacy", userService.getRatioUsersWithClosedPrivacy());
		result.addObject("ratioWithOpenPrivacy", userService.getRatioUsersWithOpenPrivacy());
		result.addObject("ratioWithFriendsPrivacy", userService.getRatioUsersWithFriendsPrivacy());
		result.addObject("ratioBodybuilder", userService.getRatioUsersWithRoleBodybuilder());
		result.addObject("ratioCrossfitter", userService.getRatioUsersWithRoleCrossfitter());
		result.addObject("ratioCalisthenics", userService.getRatioUsersWithRoleCalisthenics());
		result.addObject("ratioPowerlifter", userService.getRatioUsersWithRolePowerlifter());
		result.addObject("ratioBodybuilder", userService.getRatioUsersWithRoleStrongman());
		result.addObject("ratioWeightlifter", userService.getRatioUsersWithRoleWeightlifter());
		result.addObject("ratioRoleNone", userService.getRatioUsersWithRoleNone());
		
		
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
