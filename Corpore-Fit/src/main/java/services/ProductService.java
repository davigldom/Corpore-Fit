
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProductRepository;
import domain.Advert;
import domain.Product;
import domain.Provider;
import domain.ShoppingCart;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository	productRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ProviderService		providerService;

	@Autowired
	private AdvertService		advertService;
	
	@Autowired
	private UserService		userService;
	
	@Autowired
	private ShoppingCartService		cartService;


	public Product create() {
		final Product result;

		result = new Product();

		return result;
	}

	public Product findOne(final int productId) {
		Product result;
		Assert.isTrue(productId != 0);
		result = this.productRepository.findOne(productId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Product> findAllByProvider(final int providerId) {
		Assert.isTrue(providerId != 0);
		final Collection<Product> result = this.productRepository.findAllByProvider(providerId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Product> findAll() {
		return this.productRepository.findAll();
	}

	public Product save(final Product product) {
		Product result;
		final Provider principal = this.providerService.findByPrincipal();
		Assert.notNull(product);

		Assert.isTrue(product.getProvider().equals(principal));

		result = this.productRepository.save(product);
		return result;
	}

	public Product buy(final Product product, ShoppingCart shoppingCart) {
		Product result;

		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart()
				.contains(shoppingCart));

		product.setUnits(product.getUnits() - shoppingCart.getAmount());
		result = this.productRepository.save(product);
		return result;
	}

	public void delete(final Product product) {
		Assert.isTrue(this.actorService.isAdmin() || this.actorService.isProvider());

		if (this.actorService.isProvider())
			Assert.isTrue(this.providerService.findByPrincipal().equals(product.getProvider()));
		
		for (ShoppingCart sc : this.cartService.findByProduct(product.getId())) {

			this.cartService.findOwner(sc.getId()).getShoppingCart().remove(sc);
			this.cartService.delete(sc);
		}

		this.productRepository.delete(product);

		Assert.isTrue(!this.productRepository.findAll().contains(product));

	}

	public Product findOneToEdit(final int productId) {
		Product result;
		final Provider principal = this.providerService.findByPrincipal();

		Assert.isTrue(productId != 0);
		result = this.productRepository.findOne(productId);
		Assert.notNull(result);
		Assert.isTrue(result.getProvider().equals(principal));

		return result;
	}

	public Product findOneToAdd(final int productId, final Advert advert) {
		Product result;
		final Provider principal = this.providerService.findByPrincipal();

		Assert.isTrue(productId != 0);
		result = this.productRepository.findOne(productId);
		Assert.notNull(result);
		Assert.isTrue(result.getProvider().equals(principal));
		Assert.isTrue(result.getAdvert() == null);
		Assert.isTrue(!advert.getProducts().contains(result));
		Assert.isTrue(advert.getProvider().equals(principal));

		return result;
	}

	public Collection<Product> findAdded(final int advertId) {
		final Advert advert = this.advertService.findOne(advertId);
		final Collection<Product> result;

		result = new HashSet<Product>(advert.getProducts());

		return result;
	}

	public Collection<Product> findAllAvailableToAddByProvider(final int providerId, final Advert advert) {
		Assert.notNull(this.providerService.findOne(providerId));
		Assert.notNull(advert);
		final Collection<Product> available = this.productRepository.findAllAvailableToAddByProvider(providerId);
		Assert.notNull(available);
		available.removeAll(advert.getProducts());

		return available;
	}

	public Collection<Product> findAllWithAdvert() {
		final Collection<Product> products = this.productRepository.findAllWithAdvert();
		Assert.notNull(products);

		return products;
	}

	public void flush() {
		this.productRepository.flush();
	}

	public Collection<Product> findByKeyword(String keyword) {
		if (keyword == null)
			keyword = "%";
		Collection<Product> result = null;
		result = this.productRepository.findByKeyword(keyword);
		return result;
	}

	public Collection<Product> findAllAvailable() {
		return this.productRepository.findAllAvailable();
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Product reconstruct(final Product product, final BindingResult binding) {
		final Product productStored;

		if (product.getId() == 0) {
			final Provider principal = this.providerService.findByPrincipal();
			product.setProvider(principal);
			product.setAdvert(null);
		} else {
			productStored = this.productRepository.findOne(product.getId());

			product.setProvider(productStored.getProvider());
			product.setId(productStored.getId());
			product.setVersion(productStored.getVersion());
			product.setAdvert(productStored.getAdvert());
		}
		this.validator.validate(product, binding);

		return product;
	}

}
