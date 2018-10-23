
package controllers.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.EditorService;
import services.ManagerService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Editor;
import domain.Manager;
import domain.Provider;
import forms.RegisterActor;

@Controller
@RequestMapping("/admin")
public class RegisterAccountController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ProviderService			providerService;

	@Autowired
	private EditorService			editorService;


	// Constructors -----------------------------------------------------------

	public RegisterAccountController() {
		super();
	}

	// Create Actor(Manager)
	@RequestMapping(value = "/create-manager", method = RequestMethod.GET)
	public ModelAndView createManager() {
		ModelAndView result;
		RegisterActor registerActor;

		final Administrator principal = this.administratorService.findByPrincipal();

		Assert.notNull(principal);

		registerActor = new RegisterActor();
		result = this.createEditModelAndViewRegisterManager(registerActor, null);
		return result;
	}

	// Finish creating Actor(Manager)
	@RequestMapping(value = "/create-manager-ok", method = RequestMethod.POST, params = "save")
	public ModelAndView saveManager(@Valid final RegisterActor registerActor, final BindingResult binding) {
		ModelAndView result;
		final Manager manager;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegisterManager(registerActor, null);
		else
			try {
				manager = this.managerService.reconstructRegister(registerActor, binding);
				if (binding.hasErrors())
					result = this.createEditModelAndViewRegisterManager(registerActor, null);
				else {
					this.managerService.save(manager);
					result = new ModelAndView("redirect:/welcome/index.do");
				}
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegisterManager(registerActor, "actor.commit.error");
			}

		return result;
	}

	// Create Actor(Provider)
	@RequestMapping(value = "/create-provider", method = RequestMethod.GET)
	public ModelAndView createProvider() {
		ModelAndView result;
		RegisterActor registerActor;

		Assert.notNull(this.administratorService.findByPrincipal());

		registerActor = new RegisterActor();
		result = this.createEditModelAndViewRegisterProvider(registerActor, null);
		return result;
	}

	// Finish creating Actor(Provider)
	@RequestMapping(value = "/create-provider-ok", method = RequestMethod.POST, params = "save")
	public ModelAndView saveProvider(@Valid final RegisterActor registerActor, final BindingResult binding) {
		ModelAndView result;
		final Provider provider;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegisterProvider(registerActor, null);
		else
			try {
				provider = this.providerService.reconstructRegister(registerActor, binding);
				if (binding.hasErrors())
					result = this.createEditModelAndViewRegisterProvider(registerActor, null);
				else {
					this.providerService.save(provider);
					result = new ModelAndView("redirect:/welcome/index.do");
				}
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegisterProvider(registerActor, "actor.commit.error");
			}

		return result;
	}

	// Create Actor(Editor)
	@RequestMapping(value = "/create-editor", method = RequestMethod.GET)
	public ModelAndView createActor() {
		ModelAndView result;
		RegisterActor registerActor;

		Assert.notNull(this.administratorService.findByPrincipal());

		registerActor = new RegisterActor();
		result = this.createEditModelAndViewRegisterEditor(registerActor, null);
		return result;
	}

	// Finish creating Actor(Editor)
	@RequestMapping(value = "/create-editor-ok", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEditor(@Valid final RegisterActor registerActor, final BindingResult binding) {
		ModelAndView result;
		final Editor editor;

		if (binding.hasErrors())
			result = this.createEditModelAndViewRegisterEditor(registerActor, null);
		else
			try {
				editor = this.editorService.reconstructRegister(registerActor, binding);
				if (binding.hasErrors())
					result = this.createEditModelAndViewRegisterEditor(registerActor, null);
				else {
					this.editorService.save(editor);
					result = new ModelAndView("redirect:/welcome/index.do");
				}
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewRegisterEditor(registerActor, "actor.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndViewRegisterManager(final RegisterActor registerActor, final String message) {
		ModelAndView result;

		final String requestURI = "admin/create-manager-ok.do";

		result = new ModelAndView("actor/signup");
		result.addObject("registerActor", registerActor);
		result.addObject("registerActorForm", "registerActor");
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);

		return result;
	}

	protected ModelAndView createEditModelAndViewRegisterProvider(final RegisterActor registerActor, final String message) {
		ModelAndView result;

		final String requestURI = "admin/create-provider-ok.do";

		result = new ModelAndView("actor/signup");
		result.addObject("registerActor", registerActor);
		result.addObject("registerActorForm", "registerActor");
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);

		return result;
	}

	protected ModelAndView createEditModelAndViewRegisterEditor(final RegisterActor registerActor, final String message) {
		ModelAndView result;

		final String requestURI = "admin/create-editor-ok.do";

		result = new ModelAndView("actor/signup");
		result.addObject("registerActor", registerActor);
		result.addObject("registerActorForm", "registerActor");
		result.addObject("message", message);
		result.addObject("requestURI", requestURI);

		return result;
	}
}
