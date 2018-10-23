
package controllers.provider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertService;
import services.ProductService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Actor;
import domain.Advert;
import domain.Product;
import domain.Provider;

@Controller
@RequestMapping("/advert/provider")
public class AdvertProviderController extends AbstractController {

	@Autowired
	private ProviderService	providerService;

	@Autowired
	private AdvertService	advertService;

	@Autowired
	private ProductService	productService;


	public AdvertProviderController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Advert> adverts;

		final Provider principal = this.providerService.findByPrincipal();

		adverts = this.advertService.findAllByProvider(principal.getId());

		result = new ModelAndView("advert/listCreated");
		result.addObject("adverts", adverts);
		result.addObject("requestURI", "advert/provider/list.do");
		result.addObject("isListingCreated", true);
		result.addObject("principal", principal);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Advert advert;
		Collection<Product> availablesToAdd;

		final Provider principal = this.providerService.findByPrincipal();

		advert = this.advertService.create();
		advert.setProducts(new ArrayList<Product>());

		availablesToAdd = this.productService.findAllAvailableToAddByProvider(principal.getId(), advert);

		result = this.createEditModelAndView(advert);
		result.addObject("availablesToAdd", availablesToAdd);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int advertId) {
		ModelAndView result;
		Advert advert;

		advert = this.advertService.findOneToEdit(advertId);
		Assert.notNull(advert);

		Hibernate.initialize(advert.getProducts());

		result = this.createEditModelAndView(advert);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam(value = "productId", required = false) final Set<String> productsIds, Advert advert, final BindingResult binding) {
		ModelAndView result;

		advert = this.advertService.reconstruct(advert, binding);

		Hibernate.initialize(advert.getProducts());

		if (binding.hasErrors())
			result = this.createEditModelAndView(advert);
		else
			try {
				final Advert advertSaved = this.advertService.save(advert);
				if (productsIds != null)
					for (final String id : productsIds) {
						final Product product = this.productService.findOneToAdd(Integer.valueOf(id), advert);
						product.setAdvert(advertSaved);
						advertSaved.getProducts().add(product);
						this.advertService.save(advertSaved);
					}
				result = new ModelAndView("redirect:/advert/display.do?advertId=" + advertSaved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(advert, "advert.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView removeProduct(@RequestParam final int advertId, @RequestParam final int productId) {
		ModelAndView result;
		final Advert advert = this.advertService.findOne(advertId);
		final Product product = this.productService.findOne(productId);
		this.advertService.checkIsAdded(advert, product);

		try {
			advert.getProducts().remove(product);
			product.setAdvert(null);
			this.advertService.save(advert);
			result = new ModelAndView("redirect:/advert/display.do?advertId=" + advertId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView2(advert, "advert.remove.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int advertId) {
		ModelAndView result;

		final Advert advert = this.advertService.findOne(advertId);

		Hibernate.initialize(advert.getProducts());

		try {

			final List<Product> products = new ArrayList<Product>(advert.getProducts());

			advert.getProducts().clear();

			for (final Product p : products)
				p.setAdvert(null);

			this.advertService.delete(advert);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(advert, "advert.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Advert advert) {
		ModelAndView result;

		result = this.createEditModelAndView(advert, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Advert advert, final String message) {
		ModelAndView result = null;
		Collection<Product> availablesToAdd;

		final Provider principal = this.providerService.findByPrincipal();

		if (advert.getId() != 0)
			result = new ModelAndView("advert/edit");
		else if (advert.getId() == 0)
			result = new ModelAndView("advert/create");

		availablesToAdd = this.productService.findAllAvailableToAddByProvider(principal.getId(), advert);

		result.addObject("advert", advert);
		result.addObject("availablesToAdd", availablesToAdd);

		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Advert advert, final String message) {
		ModelAndView result;
		Actor principal = null;
		final Provider provider;
		String startFormatted = null;
		String endFormatted = null;

		final List<Product> products = new ArrayList<Product>(advert.getProducts());

		provider = advert.getProvider();

		Assert.notNull(advert);

		principal = this.providerService.findByPrincipal();

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		startFormatted = formatter.format(advert.getStart().getTime());
		endFormatted = formatter.format(advert.getEnd().getTime());

		result = new ModelAndView("advert/display");

		result.addObject("principal", principal);
		result.addObject("advert", advert);
		result.addObject("advertId", advert.getId());
		result.addObject("products", products);
		result.addObject("advertProvider", provider);
		result.addObject("startFormatted", startFormatted);
		result.addObject("endFormatted", endFormatted);
		result.addObject("displayAdvert", true);
		result.addObject("message", message);

		return result;
	}

}
