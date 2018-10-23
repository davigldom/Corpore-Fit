
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
import services.AdvertService;
import domain.Actor;
import domain.Advert;
import domain.Product;
import domain.Provider;

@Controller
@RequestMapping("/advert")
public class AdvertController extends AbstractController {

	@Autowired
	private AdvertService	advertService;

	@Autowired
	private ActorService	actorService;


	public AdvertController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Advert> adverts;

		adverts = this.advertService.findAll();

		result = new ModelAndView("advert/list");
		result.addObject("adverts", adverts);
		result.addObject("isListingCreated", false);
		result.addObject("requestURI", "advert/list.do");

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int advertId) {
		ModelAndView result;
		Advert advert;
		Actor principal = null;
		final Provider provider;
		String startFormatted = null;
		String endFormatted = null;
		final Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		advert = this.advertService.findOne(advertId);

		final List<Product> products = new ArrayList<Product>(advert.getProducts());

		provider = advert.getProvider();

		if (this.actorService.isAuthenticated())
			principal = this.actorService.findByPrincipal();

		Assert.notNull(advert);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		startFormatted = formatter.format(advert.getStart().getTime());
		endFormatted = formatter.format(advert.getEnd().getTime());

		result = new ModelAndView("advert/display");

		result.addObject("principal", principal);
		result.addObject("advert", advert);
		result.addObject("advertId", advertId);
		result.addObject("products", products);
		result.addObject("advertProvider", provider);
		result.addObject("startFormatted", startFormatted);
		result.addObject("endFormatted", endFormatted);
		result.addObject("displayAdvert", true);
		result.addObject("now", now);

		return result;
	}

}
