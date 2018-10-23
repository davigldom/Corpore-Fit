
package controllers.provider;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertService;
import services.ProductService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Advert;
import domain.Product;
import domain.Provider;

@Controller
@RequestMapping("/actor/provider")
public class ActorProviderController extends AbstractController {

	@Autowired
	private ProviderService	providerService;

	@Autowired
	private ProductService	productService;

	@Autowired
	private AdvertService	advertService;


	//Edit Provider
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") Provider provider, final BindingResult binding) {
		ModelAndView result;
		provider = this.providerService.reconstruct(provider, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(provider);
		else
			try {
				this.providerService.save(provider);
				result = new ModelAndView("redirect:/actor/display-principal.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(provider, "actor.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int actorId) {
		ModelAndView result;
		final Provider provider = this.providerService.findOne(actorId);
		Assert.isTrue(provider.equals(this.providerService.findByPrincipal()));
		final List<Product> products = new ArrayList<Product>(this.productService.findAllByProvider(provider.getId()));
		final List<Advert> adverts = new ArrayList<Advert>(this.advertService.findAllByProvider(provider.getId()));
		Hibernate.initialize(provider.getUserAccount().getAuthorities());

		try {
			for (final Product p : products)
				this.productService.delete(p);

			for (final Advert a : adverts)
				this.advertService.delete(a);

			this.providerService.delete(provider);
			result = new ModelAndView("redirect:../../j_spring_security_logout");
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

	protected ModelAndView createEditModelAndView(final Provider provider, final String message) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject(provider.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase(), provider);
		result.addObject("authority", provider.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());
		result.addObject("message", message);
		result.addObject("actor", provider);

		return result;
	}

}
