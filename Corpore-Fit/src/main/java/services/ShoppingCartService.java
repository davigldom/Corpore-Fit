package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ShoppingCartRepository;
import domain.Advert;
import domain.Product;
import domain.ShoppingCart;
import domain.User;

@Service
@Transactional
public class ShoppingCartService {

	// Managed
	// repository------------------------------------------------------------
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	// Supporting
	// services-----------------------------------------------------------

	@Autowired
	private UserService userService;

	// CRUD methods ---------------------------------------------------
	public ShoppingCart create() {
		ShoppingCart result;
		result = new ShoppingCart();
		result.setPrice(0.0);
		return result;
	}

	public Collection<ShoppingCart> findAll() {
		Collection<ShoppingCart> result;

		result = this.shoppingCartRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public ShoppingCart findOne(final int shoppingCartId) {
		ShoppingCart result;

		result = this.shoppingCartRepository.findOne(shoppingCartId);
		Assert.notNull(result);

		return result;
	}

	public ShoppingCart findOneToEdit(final int shoppingCartId) {
		ShoppingCart result;

		result = this.shoppingCartRepository.findOne(shoppingCartId);
		Assert.notNull(result);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart()
				.contains(result));

		return result;
	}

	public ShoppingCart findByProduct(final int productId, int userId) {
		return this.shoppingCartRepository.findByProduct(productId, userId);
	}
	
	public Collection<ShoppingCart> findByProduct(final int productId) {
		return this.shoppingCartRepository.findByProduct(productId);
	}

	public Double getTotalPrice(int userId) {
		return this.shoppingCartRepository.getTotalPrice(userId);
	}

	public ShoppingCart save(final ShoppingCart shoppingCart) {
		Assert.notNull(shoppingCart);
		if (shoppingCart.getId() != 0)
			Assert.isTrue(this.userService.findByPrincipal().getShoppingCart()
					.contains(shoppingCart));

		ShoppingCart result;
		Advert ad = shoppingCart.getProduct().getAdvert();
		if (ad != null && ad.getStart().before(Calendar.getInstance())
				&& ad.getEnd().after(Calendar.getInstance())) {
			shoppingCart
					.setPrice((shoppingCart.getProduct().getPrice() - (shoppingCart
							.getProduct().getPrice() * (ad.getDiscount()/100.0)))
							* shoppingCart.getAmount());
		} else
			shoppingCart.setPrice(shoppingCart.getProduct().getPrice()
					* shoppingCart.getAmount());

		result = this.shoppingCartRepository.save(shoppingCart);

		if (shoppingCart.getId() == 0)
			this.userService.findByPrincipal().getShoppingCart().add(result);

		return result;
	}

	public void removeProductGeneral(ShoppingCart sc, Product product) {

		this.shoppingCartRepository.delete(sc);
	}

	public void delete(final ShoppingCart shoppingCart) {
//		Assert.notNull(shoppingCart);
//		this.userService.findByPrincipal().getShoppingCart()
//				.remove(shoppingCart);
		this.shoppingCartRepository.delete(shoppingCart);
	}

	// Other business methods

	public User findOwner(final int shoppingCartId) {
		final User shoppingCarts = this.shoppingCartRepository
				.findOwner(shoppingCartId);

		return shoppingCarts;
	}

	@Autowired
	private Validator validator;

	public ShoppingCart reconstruct(final ShoppingCart shoppingCart,
			final BindingResult binding) {

		if (shoppingCart.getId() != 0) {
			ShoppingCart scStored = this.findOne(shoppingCart.getId());
			shoppingCart.setProduct(scStored.getProduct());
			shoppingCart.setPrice(shoppingCart.getPrice());
		} else {
			shoppingCart.setAmount(0);
		}

		this.validator.validate(shoppingCart, binding);

		return shoppingCart;

	}

}
