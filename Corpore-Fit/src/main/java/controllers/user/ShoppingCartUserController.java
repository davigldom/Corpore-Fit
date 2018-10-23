/*
 * ExerciseUserController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CreditCardService;
import services.OrderLineService;
import services.ProductOrderService;
import services.ProductService;
import services.ShoppingCartService;
import services.UserService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.OrderLine;
import domain.Product;
import domain.ProductOrder;
import domain.ShoppingCart;
import domain.User;

@Controller
@RequestMapping("/shopping-cart/user")
public class ShoppingCartUserController extends AbstractController {

	@Autowired
	ActorService actorService;

	@Autowired
	UserService userService;

	@Autowired
	ShoppingCartService cartService;

	@Autowired
	ProductService productService;

	@Autowired
	CreditCardService creditCardService;

	@Autowired
	OrderLineService orderLineService;

	@Autowired
	ProductOrderService orderService;

	// Constructors -----------------------------------------------------------

	public ShoppingCartUserController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;

		final Calendar now = Calendar.getInstance();
		now.setTime(new Date());

		result = new ModelAndView("product/shoppingCart");
		result.addObject("shoppingCarts", this.userService.findByPrincipal()
				.getShoppingCart());
		result.addObject("requestURI", "shopping-cart/user/display.do");
		result.addObject("shoppingCartPrice", this.cartService
				.getTotalPrice(this.userService.findByPrincipal().getId()));
		result.addObject("creditCards", this.userService.findByPrincipal()
				.getCreditCards());
		result.addObject("now", now);
		return result;
	}

	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public ModelAndView pay(@RequestParam int creditCardId) {
		ModelAndView result;
		try {
			ProductOrder order = this.orderService.create();
			Collection<OrderLine> lines = new ArrayList<OrderLine>();
			CreditCard creditCard = this.creditCardService
					.findOneToEdit(creditCardId);
			User principal = this.userService.findByPrincipal();
			Collection<ShoppingCart> carts = principal.getShoppingCart();

			Assert.isTrue(!carts.isEmpty());

			Iterator<ShoppingCart> iter = carts.iterator();
			while (iter.hasNext()) {
				ShoppingCart sc = iter.next();
				Product p = sc.getProduct();
				OrderLine ol = this.orderLineService.create();
				// Description
				ol.setDescription(p.getDescription());
				// Image
				ol.setImage(p.getImage());
				// Name
				ol.setName(p.getName());
				// Price
				ol.setPrice(sc.getPrice());
				// SKU
				ol.setSku(p.getSku());
				// Amount
				ol.setAmount(sc.getAmount());

				lines.add(ol);
				Assert.isTrue(this.productService.findOne(p.getId()).getUnits() >= sc
						.getAmount());
				this.productService.buy(p,sc);

				iter.remove();
				this.cartService.delete(sc);
			}

			order.setOrderLines(lines);
			order.setCreditCard(creditCard);
			order.setDate(Calendar.getInstance());
			this.orderService.save(order);
			result = new ModelAndView("redirect:/order/user/list.do");
		} catch (final Throwable oops) {

			final Calendar now = Calendar.getInstance();
			now.setTime(new Date());

			result = new ModelAndView("product/shoppingCart");
			result.addObject("shoppingCarts", this.userService
					.findByPrincipal().getShoppingCart());
			result.addObject("requestURI", "shopping-cart/user/display.do");
			result.addObject("shoppingCartPrice", this.cartService
					.getTotalPrice(this.userService.findByPrincipal().getId()));
			result.addObject("now", now);
			result.addObject("creditCards", this.userService.findByPrincipal()
					.getCreditCards());
			result.addObject("message", "product.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/addProduct", method = RequestMethod.GET)
	public ModelAndView addProduct(@RequestParam int productId,
			@RequestParam int amount) {
		ModelAndView result;
		try {
			Assert.isTrue(amount > 0);

			ShoppingCart cart = this.cartService.findByProduct(productId,
					this.userService.findByPrincipal().getId());

			if (cart != null)
				cart.setAmount(cart.getAmount() + amount);
			else {
				cart = this.cartService.create();
				cart.setAmount(amount);
				cart.setProduct(this.productService.findOne(productId));
			}
			
			if(cart.getAmount()>cart.getProduct().getUnits())
				cart.setAmount(cart.getProduct().getUnits());
			
			this.cartService.save(cart);
			result = new ModelAndView("redirect:/shopping-cart/user/display.do");
		} catch (final Throwable oops) {

			final Calendar now = Calendar.getInstance();
			now.setTime(new Date());

			result = new ModelAndView("product/shoppingCart");
			result.addObject("shoppingCarts", this.userService
					.findByPrincipal().getShoppingCart());
			result.addObject("requestURI", "shopping-cart/user/display.do");
			result.addObject("shoppingCartPrice", this.cartService
					.getTotalPrice(this.userService.findByPrincipal().getId()));
			result.addObject("now", now);
			result.addObject("creditCards", this.userService.findByPrincipal()
					.getCreditCards());
			result.addObject("message", "product.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/removeProduct", method = RequestMethod.GET)
	public ModelAndView removeProduct(@RequestParam int cartId) {
		ModelAndView result;
		try {
			ShoppingCart cart = this.cartService.findOne(cartId);
			this.userService.findByPrincipal().getShoppingCart().remove(cart);
			this.cartService.delete(cart);
			result = new ModelAndView("redirect:/shopping-cart/user/display.do");
		} catch (final Throwable oops) {

			final Calendar now = Calendar.getInstance();
			now.setTime(new Date());

			result = new ModelAndView("product/shoppingCart");
			result.addObject("shoppingCarts", this.userService
					.findByPrincipal().getShoppingCart());
			result.addObject("requestURI", "shopping-cart/user/display.do");
			result.addObject("shoppingCartPrice", this.cartService
					.getTotalPrice(this.userService.findByPrincipal().getId()));
			result.addObject("now", now);
			result.addObject("creditCards", this.userService.findByPrincipal()
					.getCreditCards());
			result.addObject("message", "product.commit.error");
		}
		return result;
	}

}
