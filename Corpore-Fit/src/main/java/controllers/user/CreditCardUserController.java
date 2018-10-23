package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CreditCardService;
import services.ProductOrderService;
import services.SubscriptionService;
import services.UserService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.ProductOrder;
import domain.User;

@Controller
@RequestMapping("/credit-card/user")
public class CreditCardUserController extends AbstractController {

	// Services ---------------------------------------------------

	@Autowired
	private CreditCardService creditCardService;
	@Autowired
	private UserService userService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private ProductOrderService orderService;

	// Constructors -----------------------------------------------

	public CreditCardUserController() {
		super();
	}

	// Listing ----------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final User user = this.userService.findByPrincipal();

		result = new ModelAndView("credit-card/list");
		result.addObject("creditCards", user.getCreditCards());
		result.addObject("requestURI", "credit-card/user/list.do");

		return result;
	}

	// Creation ---------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final CreditCard creditCard = this.creditCardService.create();

		result = this.createEditModelAndView(creditCard);

		return result;
	}

	// Edition ---------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(CreditCard creditCard, final BindingResult binding) {
		ModelAndView result;
		creditCard = this.creditCardService.reconstruct(creditCard, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(creditCard);
		else
			try {
				this.creditCardService.save(creditCard);
				result = new ModelAndView("redirect:/credit-card/user/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(creditCard,
						"credit-card.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int creditCardId) {
		ModelAndView result;
		final CreditCard creditCard = this.creditCardService
				.findOneToEdit(creditCardId);
		try {
			if(this.userService.findByPrincipal().getSubscription()!=null
					&& this.userService.findByPrincipal().getSubscription().getCreditCard().equals(creditCard))
				this.subscriptionService.delete(this.userService.findByPrincipal().getSubscription());
			for (ProductOrder order : this.orderService.findByCreditCard(creditCardId)) {
				order.setCreditCard(null);
			}
			this.creditCardService.delete(creditCard);
			result = new ModelAndView("redirect:/credit-card/user/list.do");
		} catch (final Throwable oops) {
			if (this.userService.findByPrincipal().getCreditCards()
					.contains(creditCard))
				result = this.createEditModelAndView(creditCard,
						"social.network.commit.error");
			else
				result = this.createEditModelAndView(
						this.creditCardService.create(),
						"social.network.commit.error");

		}
		return result;
	}

	// Ancillary methods -------------------------------------------

	protected ModelAndView createEditModelAndView(final CreditCard creditCard) {
		ModelAndView result;

		result = this.createEditModelAndView(creditCard, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CreditCard creditCard,
			final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("credit-card/create");

		result.addObject("creditCard", creditCard);
		result.addObject("message", messageCode);

		return result;
	}

}
