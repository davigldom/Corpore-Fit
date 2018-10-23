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
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.OrderLineService;
import services.ProductOrderService;
import services.UserService;
import controllers.AbstractController;
import domain.OrderLine;
import domain.ProductOrder;

@Controller
@RequestMapping("/order/user")
public class OrderUserController extends AbstractController {

	@Autowired
	UserService			userService;

	@Autowired
	ProductOrderService	orderService;

	@Autowired
	OrderLineService	orderLineService;


	// Constructors -----------------------------------------------------------

	public OrderUserController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final List<OrderLine> lines = new ArrayList<OrderLine>();

		for (final ProductOrder o : this.userService.findByPrincipal().getOrders())
			for (final OrderLine ol : o.getOrderLines())
				lines.add(ol);
		result = new ModelAndView("order/list");
		result.addObject("orderLines", lines);
		result.addObject("requestURI", "order/user/list.do");
		return result;
	}

	@RequestMapping(value = "/list-products", method = RequestMethod.GET)
	public ModelAndView listProducts(@RequestParam final int orderId) {
		ModelAndView result;

		final Collection<OrderLine> lines = this.orderLineService.findByOrder(orderId);

		result = new ModelAndView("order/list");
		result.addObject("orderLines", lines);
		result.addObject("requestURI", "order/user/list.do");
		return result;
	}
	@RequestMapping(value = "/list-orders", method = RequestMethod.GET)
	public ModelAndView listOrders() {
		ModelAndView result;
		Collection<ProductOrder> orders = new ArrayList<ProductOrder>();

		orders = this.userService.findByPrincipal().getOrders();

		result = new ModelAndView("order/list-orders");
		result.addObject("orders", orders);
		result.addObject("requestURI", "order/user/list-orders.do");
		return result;
	}

}
