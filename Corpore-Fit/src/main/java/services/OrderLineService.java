
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.OrderLineRepository;
import domain.OrderLine;
import domain.ProductOrder;
import domain.User;

@Service
@Transactional
public class OrderLineService {

	@Autowired
	private OrderLineRepository	orderLineRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private ProductOrderService	productOrderService;

	@Autowired
	private Validator			validator;


	public OrderLine create() {

		final OrderLine result = new OrderLine();

		return result;
	}

	public OrderLine save(final OrderLine orderLine) {
		Assert.notNull(orderLine);

		final OrderLine orderLineSaved = this.orderLineRepository.save(orderLine);
		return orderLineSaved;
	}

	public OrderLine reconstruct(final OrderLine orderLine, final BindingResult binding) {

		orderLine.setId(0);
		orderLine.setVersion(0);

		this.validator.validate(orderLine, binding);

		return orderLine;

	}

	public OrderLine findOne(final int orderLineId) {
		OrderLine result;
		result = this.orderLineRepository.findOne(orderLineId);
		Assert.notNull(result);
		return result;
	}

	public Collection<OrderLine> findByOrder(final int orderId) {
		final User user = this.userService.findByPrincipal();
		final ProductOrder order = this.productOrderService.findOne(orderId);
		Assert.isTrue(user.equals(order.getUser()));

		return this.orderLineRepository.findByOrder(orderId);
	}
}
