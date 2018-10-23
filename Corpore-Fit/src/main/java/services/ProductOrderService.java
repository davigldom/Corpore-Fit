
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProductOrderRepository;
import domain.OrderLine;
import domain.ProductOrder;
import domain.User;

@Service
@Transactional
public class ProductOrderService {

	@Autowired
	private ProductOrderRepository	productOrderRepository;

	@Autowired
	private UserService				userService;

	@Autowired
	private Validator				validator;


	public ProductOrder create() {

		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		final ProductOrder result = new ProductOrder();
		result.setUser(user);

		return result;
	}

	public ProductOrder save(final ProductOrder productOrder) {
		Assert.notNull(productOrder);
		Assert.isTrue(productOrder.getId() == 0);
		Assert.isTrue(productOrder.getUser().equals(this.userService.findByPrincipal()));
		Assert.isTrue(productOrder.getCreditCard() != null);

		final ProductOrder productOrderSaved = this.productOrderRepository.save(productOrder);
		//		if(productOrder.getId()==0)
		//			this.userService.findByPrincipal().getOrders().add(productOrderSaved);
		return productOrderSaved;
	}

	public ProductOrder reconstruct(final ProductOrder productOrder, final BindingResult binding) {

		productOrder.setId(0);
		productOrder.setVersion(0);
		productOrder.setOrderLines(new ArrayList<OrderLine>());
		productOrder.setUser(this.userService.findByPrincipal());

		this.validator.validate(productOrder, binding);

		return productOrder;

	}

	public ProductOrder findOne(final int productOrderId) {
		ProductOrder result;
		result = this.productOrderRepository.findOne(productOrderId);
		Assert.notNull(result);
		return result;
	}

	public Collection<ProductOrder> findByCreditCard(final int creditCard) {
		return this.productOrderRepository.findByCreditCard(creditCard);
	}

}
