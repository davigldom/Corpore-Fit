
package controllers.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdvertService;
import services.ProductService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Advert;
import domain.Exercise;
import domain.Product;
import domain.Provider;

@Controller
@RequestMapping("/provider/admin")
public class ProviderAdministratorController extends AbstractController {

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ProviderService	providerService;

	@Autowired
	private ProductService	productService;

	@Autowired
	private AdvertService	advertService;


	// Constructors -----------------------------------------------------------

	public ProviderAdministratorController() {
		super();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int actorId) {
		ModelAndView result;
		final Provider provider = this.providerService.findOne(actorId);
		final List<Product> products = new ArrayList<Product>(this.productService.findAllByProvider(provider.getId()));
		final List<Advert> adverts = new ArrayList<Advert>(this.advertService.findAllByProvider(provider.getId()));

		try {
			for (final Product p : products)
				this.productService.delete(p);

			for (final Advert a : adverts)
				this.advertService.delete(a);

			this.providerService.delete(provider);
			result = new ModelAndView("redirect:/actor/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(provider, "actor.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Provider provider) {
		ModelAndView result;

		result = this.createEditModelAndView(provider, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Provider provider, final String message) {
		ModelAndView result;
		final boolean isFriend = false;
		final boolean existsRequest = false;
		final boolean isValid = true;
		final Collection<Exercise> exercisesOfTheDay = null;
		final int providerId = provider.getId();

		provider = this.providerService.findOne(providerId);
		Assert.notNull(provider);

		if (provider.isBanned())
			Assert.isTrue(this.actorService.isAdmin());

		result = new ModelAndView("actor/display");
		result.addObject("actor", provider);
		result.addObject("isFriend", isFriend);
		result.addObject("existsRequest", existsRequest);
		result.addObject("isValid", isValid);
		result.addObject("exercisesOfTheDay", exercisesOfTheDay);
		result.addObject("authority", provider.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());

		result.addObject("message", message);

		return result;
	}

}
