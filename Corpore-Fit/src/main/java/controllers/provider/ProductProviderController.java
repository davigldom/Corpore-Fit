
package controllers.provider;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProductService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Product;
import domain.Provider;

@Controller
@RequestMapping("/product/provider")
public class ProductProviderController extends AbstractController {

	@Autowired
	private ProviderService	providerService;

	@Autowired
	private ProductService	productService;


	public ProductProviderController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Product> products;
		final Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		final Provider principal = this.providerService.findByPrincipal();

		products = this.productService.findAllByProvider(principal.getId());

		result = new ModelAndView("product/listCreated");
		result.addObject("products", products);
		result.addObject("requestURI", "product/provider/list.do");
		result.addObject("isListingCreated", true);
		result.addObject("principal", principal);
		result.addObject("now", now);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Product product;
		product = this.productService.create();
		result = this.createEditModelAndView(product);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int productId) {
		ModelAndView result;
		Product product;

		product = this.productService.findOneToEdit(productId);
		Assert.notNull(product);
		result = this.createEditModelAndView(product);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Product product, final BindingResult binding) {
		ModelAndView result;

		product = this.productService.reconstruct(product, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(product);
		else
			try {
				final Product productSaved = this.productService.save(product);
				result = new ModelAndView("redirect:/product/display.do?productId=" + productSaved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(product, "product.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int productId) {
		ModelAndView result;

		final Product product = this.productService.findOne(productId);

		try {
			this.productService.delete(product);
			result = this.list();
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(product, "product.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Product product) {
		ModelAndView result;

		result = this.createEditModelAndView(product, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Product product, final String message) {
		ModelAndView result = null;

		if (product.getId() != 0)
			result = new ModelAndView("product/edit");
		else if (product.getId() == 0)
			result = new ModelAndView("product/create");

		result.addObject("product", product);

		result.addObject("message", message);

		return result;
	}

}
