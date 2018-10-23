
package controllers.admin;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProductService;
import controllers.AbstractController;
import domain.Product;

@Controller
@RequestMapping("/product/admin")
public class ProductAdministratorController extends AbstractController {

	@Autowired
	private ProductService	productService;


	// Constructors -----------------------------------------------------------

	public ProductAdministratorController() {
		super();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int productId) {
		ModelAndView result;

		final Product product = this.productService.findOne(productId);

		try {
			this.productService.delete(product);
			result = new ModelAndView("redirect:/product/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView("product.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final String message) {
		ModelAndView result;
		Collection<Product> products;
		final Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		products = this.productService.findAllAvailable();

		result = new ModelAndView("product/list");
		result.addObject("products", products);
		result.addObject("isListingCreated", false);
		result.addObject("requestURI", "product/list.do");
		result.addObject("now", now);

		result.addObject("message", message);

		return result;
	}

}
