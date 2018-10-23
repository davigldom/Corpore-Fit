
package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ProductService;
import domain.Actor;
import domain.Advert;
import domain.Product;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractController {

	@Autowired
	private ProductService	productService;

	@Autowired
	private ActorService	actorService;


	public ProductController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
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

		return result;
	}

	@RequestMapping(value = "/listWithAdverts", method = RequestMethod.GET)
	public ModelAndView listWithAdverts() {
		ModelAndView result;
		List<Product> products;
		final Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		products = new ArrayList<Product>(this.productService.findAllWithAdvert());

		if (!products.isEmpty())
			for (int i = 0; i < products.size(); i++) {
				final Advert advert = products.get(i).getAdvert();

				if (!(advert.getStart().before(now) && advert.getEnd().after(now)))
					products.remove(products.get(i));
			}

		result = new ModelAndView("product/listWithAdverts");
		result.addObject("products", products);
		result.addObject("isListingCreated", false);
		result.addObject("requestURI", "product/listWithAdverts.do");
		result.addObject("now", now);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int productId) {
		ModelAndView result;
		Product product;
		Actor principal = null;
		boolean isDiscounted = false;

		product = this.productService.findOne(productId);

		Assert.isTrue(!product.getProvider().isBanned());

		if (this.actorService.isAuthenticated())
			principal = this.actorService.findByPrincipal();

		Assert.notNull(product);

		result = new ModelAndView("product/display");

		result.addObject("principal", principal);
		result.addObject("product", product);
		result.addObject("productId", productId);
		result.addObject("displayProduct", true);

		if (product.getAdvert() != null) {
			final Advert advert = product.getAdvert();
			final Calendar now = Calendar.getInstance();
			now.setTime(new Date());

			if (advert.getStart().before(now) && advert.getEnd().after(now)) {
				final int discount = advert.getDiscount();
				final double priceDiscounted = ((100 - discount) * 0.01) * product.getPrice();
				result.addObject("discount", discount);
				result.addObject("priceDiscounted", priceDiscounted);
				isDiscounted = true;
				final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				final String endFormatted = formatter.format(advert.getEnd().getTime());
				result.addObject("endFormatted", endFormatted);
			}
		}

		result.addObject("isDiscounted", isDiscounted);

		return result;
	}

	@RequestMapping(value = "/search-word", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam final String keyword) {
		ModelAndView result;

		final Collection<Product> products = this.productService.findByKeyword(keyword);

		result = new ModelAndView("product/search");
		result.addObject("products", products);
		result.addObject("requestURI", "product/search-word.do");
		result.addObject("keyword", keyword);
		result.addObject("isListingCreated", false);
		result.addObject("searchProduct", true);

		return result;
	}

}
