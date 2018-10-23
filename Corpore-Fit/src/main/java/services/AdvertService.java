
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdvertRepository;
import domain.Advert;
import domain.Product;
import domain.Provider;

@Service
@Transactional
public class AdvertService {

	@Autowired
	private AdvertRepository	advertRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ProviderService		providerService;


	public Advert create() {
		final Advert result;

		result = new Advert();

		return result;
	}

	public Advert findOne(final int advertId) {
		Advert result;
		Assert.isTrue(advertId != 0);
		result = this.advertRepository.findOne(advertId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Advert> findAllByProvider(final int providerId) {
		Assert.isTrue(providerId != 0);
		final Collection<Advert> result = this.advertRepository.findAllByProvider(providerId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Advert> findAll() {
		return this.advertRepository.findAll();
	}

	public Advert save(final Advert advert) {
		Advert result;
		final Provider principal = this.providerService.findByPrincipal();

		result = this.advertRepository.save(advert);
		Assert.isTrue(advert.getStart().before(advert.getEnd()));
		Assert.isTrue(advert.getProvider().equals(principal));

		return result;
	}

	public void delete(final Advert advert) {
		Provider principal = null;

		if (this.actorService.isProvider())
			principal = this.providerService.findByPrincipal();
		else
			Assert.isTrue(this.actorService.isAdmin());

		Assert.isTrue(advert.getProvider().equals(principal) || this.actorService.isAdmin());

		this.advertRepository.delete(advert);

		Assert.isTrue(!this.advertRepository.findAll().contains(advert));

	}
	public Advert findOneToEdit(final int advertId) {
		Advert result;
		final Provider principal = this.providerService.findByPrincipal();

		Assert.isTrue(advertId != 0);
		result = this.advertRepository.findOne(advertId);
		Assert.notNull(result);
		Assert.isTrue(result.getProvider().equals(principal));

		return result;
	}

	public void checkIsAdded(final Advert advert, final Product product) {
		final Provider principal = this.providerService.findByPrincipal();
		Assert.isTrue(advert.getId() != 0 && product.getId() != 0);
		Assert.notNull(advert);
		Assert.notNull(product);
		Assert.isTrue(product.getProvider().equals(principal));
		Assert.isTrue(advert.getProvider().equals(principal));
		Assert.isTrue(advert.getProducts().contains(product) && product.getAdvert().equals(advert));
	}

	public void flush() {
		this.advertRepository.flush();
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Advert reconstruct(final Advert advert, final BindingResult binding) {
		final Advert advertStored;
		final Provider principal = this.providerService.findByPrincipal();

		if (advert.getId() == 0) {
			advert.setProducts(new ArrayList<Product>());
			advert.setProvider(principal);
		} else {
			advertStored = this.advertRepository.findOne(advert.getId());

			advert.setId(advertStored.getId());
			advert.setVersion(advertStored.getVersion());
			advert.setProducts(advertStored.getProducts());
			advert.setProvider(advertStored.getProvider());
		}
		this.validator.validate(advert, binding);

		return advert;
	}

}
