
package usecases;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AdvertService;
import services.ProductService;
import services.ProviderService;
import utilities.AbstractTest;
import domain.Advert;
import domain.Product;
import domain.Provider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProviderTest extends AbstractTest {

	// System under test

	@Autowired
	private ProviderService	providerService;

	@Autowired
	private ProductService	productService;

	@Autowired
	private AdvertService	advertService;

	DecimalFormat			df	= new DecimalFormat("#.##");	// To get only 2 decimals


	// ------------------------------------------------------ TESTS
	// ------------------------------------------------------------------

	// Edit provider's data
	// ***********************************************************************************************************
	// POSITIVE: Edit provider's data
	@Test
	public void editProviderPositive() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Provider provider = this.providerService.findOne(this.getEntityId("provider1"));
		provider.setName("New name");
		final Provider stored = this.providerService.save(provider);
		Assert.isTrue(stored.getName().equals("New name"));
		this.authenticate(null);
	}

	// NEGATIVE: Edit provider's data invalid email
	@Test(expected = IllegalArgumentException.class)
	public void editProviderNegativeInvalidEmail() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Provider provider = this.providerService.findOne(this.getEntityId("provider1"));
		provider.setEmail("Invalid email");
		final Provider stored = this.providerService.save(provider);
		Assert.isTrue(!stored.getEmail().equals("Invalid email"));
		this.authenticate(null);
	}

	// NEGATIVE: Edit provider's data as user
	@Test(expected = IllegalArgumentException.class)
	public void editProviderNegativeAsUser() {
		this.authenticate("user1");
		this.setUpAuth("user1");

		final Provider provider = this.providerService.findByPrincipal();
		provider.setName("New name");
		final Provider stored = this.providerService.save(provider);
		Assert.isTrue(stored.getName().equals("New name"));
		this.authenticate(null);
	}

	// Delete provider account
	// ***********************************************************************************************************
	// POSITIVE: Delete provider account
	@Test
	public void deleteProviderPositive() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Provider provider = this.providerService.findOne(this.getEntityId("provider1"));
		final List<Product> products = new ArrayList<Product>(this.productService.findAllByProvider(provider.getId()));
		final List<Advert> adverts = new ArrayList<Advert>(this.advertService.findAllByProvider(provider.getId()));

		for (final Product p : products)
			this.productService.delete(p);

		for (final Advert a : adverts)
			this.advertService.delete(a);

		this.providerService.delete(provider);

		this.authenticate(null);
	}

	// NEGATIVE: Delete provider account as user
	@Test(expected = IllegalArgumentException.class)
	public void deleteProviderNegativeAsUser() {
		this.authenticate("user1");
		this.setUpAuth("user1");

		final Provider provider = this.providerService.findByPrincipal();
		final List<Product> products = new ArrayList<Product>(this.productService.findAllByProvider(provider.getId()));
		final List<Advert> adverts = new ArrayList<Advert>(this.advertService.findAllByProvider(provider.getId()));

		for (final Product p : products)
			this.productService.delete(p);

		for (final Advert a : adverts)
			this.advertService.delete(a);

		this.providerService.delete(provider);

		this.authenticate(null);
	}

	// NEGATIVE: Delete provider account that doesn't exists
	@Test(expected = AssertionError.class)
	public void deleteProviderNegativeInvalidProvider() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Provider provider = this.providerService.findOne(this.getEntityId("provider2"));
		final List<Product> products = new ArrayList<Product>(this.productService.findAllByProvider(provider.getId()));
		final List<Advert> adverts = new ArrayList<Advert>(this.advertService.findAllByProvider(provider.getId()));

		for (final Product p : products)
			this.productService.delete(p);

		for (final Advert a : adverts)
			this.advertService.delete(a);

		this.providerService.delete(provider);

		this.authenticate(null);
	}

	// Delete product
	// ***********************************************************************************************************
	// POSITIVE: Delete product
	@Test
	public void deleteProductPositive() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Product product = this.productService.findOne(this.getEntityId("product1"));

		this.productService.delete(product);

		this.authenticate(null);
	}

	// NEGATIVE: Delete product that doesn't exists
	@Test(expected = AssertionError.class)
	public void deleteProductNegativeInvalidProduct() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Product product = this.productService.findOne(this.getEntityId("product10"));

		this.productService.delete(product);

		this.authenticate(null);
	}

	// NEGATIVE: Delete product as manager
	@Test(expected = IllegalArgumentException.class)
	public void deleteProductNegativeAsManager() {
		this.authenticate("manager1");
		this.setUpAuth("manager1");

		final Product product = this.productService.findOne(this.getEntityId("product1"));

		this.productService.delete(product);

		this.authenticate(null);
	}

	// Edit advert
	// ***********************************************************************************************************
	// POSITIVE: Edit advert
	@Test
	public void editAdvertPositive() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Advert advert = this.advertService.findOne(this.getEntityId("advert1"));
		advert.setDiscount(15);
		final Advert stored = this.advertService.save(advert);

		Assert.isTrue(stored.getDiscount() == 15);
		this.authenticate(null);
	}

	// NEGATIVE: Edit advert as admin
	@Test(expected = IllegalArgumentException.class)
	public void editAdvertNegativeAsAdmin() {
		this.authenticate("admin1");
		this.setUpAuth("admin1");

		final Advert advert = this.advertService.findOne(this.getEntityId("advert1"));
		advert.setDiscount(15);
		final Advert stored = this.advertService.save(advert);

		Assert.isTrue(stored.getDiscount() == 15);
		this.authenticate(null);
	}

	// NEGATIVE: Edit advert invalid end date
	@Test(expected = IllegalArgumentException.class)
	public void editAdvertNegativeInvalidEndDate() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Advert advert = this.advertService.findOne(this.getEntityId("advert1"));
		final Calendar endDate = Calendar.getInstance();
		endDate.set(2017, 9, 18);
		advert.setEnd(endDate);
		this.advertService.save(advert);

		this.authenticate(null);
	}

	// Delete advert
	// ***********************************************************************************************************
	// POSITIVE: Delete advert
	@Test
	public void deleteAdvertPositive() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Advert advert = this.advertService.findOne(this.getEntityId("advert1"));
		final List<Product> products = new ArrayList<Product>(advert.getProducts());
		advert.getProducts().clear();

		for (final Product p : products)
			p.setAdvert(null);

		this.advertService.delete(advert);

		this.authenticate(null);
	}

	// NEGATIVE: Delete advert without removing it from products
	@Test(expected = DataIntegrityViolationException.class)
	public void deleteAdvertNegativeNoRemovingProducts() {
		this.authenticate("provider1");
		this.setUpAuth("provider1");

		final Advert advert = this.advertService.findOne(this.getEntityId("advert1"));

		this.advertService.delete(advert);

		this.authenticate(null);
	}

	// POSITIVE: Delete advert as editor
	@Test(expected = IllegalArgumentException.class)
	public void deleteAdvertNegativeAsEditor() {
		this.authenticate("editor1");
		this.setUpAuth("editor1");

		final Advert advert = this.advertService.findOne(this.getEntityId("advert1"));
		final List<Product> products = new ArrayList<Product>(advert.getProducts());
		advert.getProducts().clear();

		for (final Product p : products)
			p.setAdvert(null);

		this.advertService.delete(advert);

		this.authenticate(null);
	}

	public void setupAuthProvider() {
		SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("GUEST", "USERNAME", AuthorityUtils.createAuthorityList("PROVIDER")));
	}

	// Create product
	// ***********************************************************************************************************
	@Test
	public void driverCreateProduct() {
		final String image = "https://cdn.lynda.com/course/160716/160716-635356607675140223-16x9.jpg";
		final Object testingData[][] = {
			// POSITIVE
			{
				"provider1", "Test name", "Test sku", "Test description", image, 10.0, 10, null
			},
			// NEGATIVE - The actor is not a provider
			{
				"user1", "Test name", "Test sku", "Test description", image, 10.0, 10, IllegalArgumentException.class
			},
			// NEGATIVE - The price is an invalid number
			{
				"provider1", "Test name", "Test sku", "Test description", image, -10.0, 10, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateProduct((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (double) testingData[i][5], (int) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void templateCreateProduct(final String username, final String name, final String sku, final String description, final String image, final double price, final int units, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Provider principal = this.providerService.findByPrincipal();

			final Product product = this.productService.create();
			product.setName(name);
			product.setSku(sku);
			product.setDescription(description);
			product.setImage(image);
			product.setPrice(price);
			product.setUnits(units);
			product.setProvider(principal);
			product.setAdvert(null);

			final Product stored = this.productService.save(product);
			this.productService.flush();

			Assert.notNull(this.productService.findOne(stored.getId()));
			Assert.isTrue(this.productService.findOne(stored.getId()).getProvider().equals(principal));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Edit product
	// ***********************************************************************************************************
	@Test
	public void driverEditProduct() {
		final String image = "https://cdn.lynda.com/course/160716/160716-635356607675140223-16x9.jpg";
		final Object testingData[][] = {
			// POSITIVE
			{
				"provider1", "product1", "Test name", "Test sku", "Test description", image, 10.0, 10, null
			},
			// NEGATIVE - The description is blank
			{
				"provider1", "product1", "Test name", "Test sku", "", image, 10.0, 10, ConstraintViolationException.class
			},
			// NEGATIVE - The units are an invalid number
			{
				"provider1", "product1", "Test name", "Test sku", "Test description", image, 10.0, -10, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditProduct((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (double) testingData[i][6],
				(int) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateEditProduct(final String username, final String entity, final String name, final String sku, final String description, final String image, final double price, final int units, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);

			final int productId = this.getEntityId(entity);
			final Product product = this.productService.findOne(productId);
			product.setName(name);
			product.setSku(sku);
			product.setDescription(description);
			product.setImage(image);
			product.setPrice(price);
			product.setUnits(units);

			final Product stored = this.productService.save(product);
			this.productService.flush();

			Assert.notNull(this.productService.findOne(stored.getId()));
			Assert.isTrue(this.productService.findOne(stored.getId()).getName().equals(name));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Create advert
	// ***********************************************************************************************************
	@Test
	public void driverCreateAdvert() {
		final Calendar start = Calendar.getInstance();
		start.set(2018, 10, 25, 18, 0);
		final Calendar end = Calendar.getInstance();
		end.set(2018, 10, 26, 18, 0);
		final Object testingData[][] = {
			// POSITIVE
			{
				"provider1", "Test name", start, end, 10, null
			},
			// NEGATIVE - The start date is after the end date
			{
				"provider1", "Test name", end, start, 10, IllegalArgumentException.class
			},
			// NEGATIVE - The discount is an invalid number
			{
				"provider1", "Test name", start, end, -10, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAdvert((String) testingData[i][0], (String) testingData[i][1], (Calendar) testingData[i][2], (Calendar) testingData[i][3], (int) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateCreateAdvert(final String username, final String name, final Calendar start, final Calendar end, final int discount, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Provider principal = this.providerService.findByPrincipal();

			final Advert advert = this.advertService.create();
			advert.setName(name);
			advert.setStart(start);
			advert.setEnd(end);
			advert.setDiscount(discount);
			advert.setProducts(new HashSet<Product>());
			advert.setProvider(principal);

			final Advert stored = this.advertService.save(advert);
			this.productService.flush();

			Assert.notNull(this.advertService.findOne(stored.getId()));
			Assert.isTrue(this.advertService.findOne(stored.getId()).getProvider().equals(principal));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
