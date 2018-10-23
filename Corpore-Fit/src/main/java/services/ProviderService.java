
package services;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProviderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.ArticleRating;
import domain.Provider;
import forms.RegisterActor;

@Service
@Transactional
public class ProviderService {

	@Autowired
	private ProviderRepository		providerRepository;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	adminService;


	public Provider create() {

		final UserAccount userAccount = this.userAccountService.create();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.PROVIDER);
		userAccount.addAuthority(authority);

		final Provider result;
		result = new Provider();
		result.setUserAccount(userAccount);
		return result;
	}

	public Provider findOne(final int providerId) {
		Provider result;
		Assert.isTrue(providerId != 0);
		result = this.providerRepository.findOne(providerId);
		Assert.notNull(result);
		return result;
	}

	public Provider save(final Provider provider) {
		Provider result;
		Assert.notNull(provider);
		Assert.notNull(provider.getUserAccount().getUsername());
		Assert.notNull(provider.getUserAccount().getPassword());
		if (provider.getId() == 0) {
			Assert.notNull(this.adminService.findByPrincipal());
			String passwordHashed = null;
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			passwordHashed = encoder.encodePassword(provider.getUserAccount().getPassword(), null);
			provider.getUserAccount().setPassword(passwordHashed);
		}

		result = this.providerRepository.save(provider);
		return result;
	}

	public void delete(final Provider provider) {
		Assert.isTrue(provider.getId() != 0);

		if (this.actorService.isProvider())
			Assert.isTrue(this.findByPrincipal().equals(provider));
		else
			Assert.isTrue(this.actorService.isAdmin());

		this.providerRepository.delete(provider);
		Assert.isTrue(!this.providerRepository.findAll().contains(provider));
	}

	public Provider findByPrincipal() {
		Provider result;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Provider findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Provider result;
		result = this.providerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}


	//Reconstruct 

	@Autowired
	private Validator	validator;


	public Provider reconstructRegister(final RegisterActor provider, final BindingResult binding) {
		Provider result;
		Assert.isTrue(provider.getPassword().equals(provider.getRepeatedPassword()));
		result = this.create();

		result.setName(provider.getName());
		result.setSurname(provider.getSurname());
		result.setPhone(provider.getPhone());
		result.setEmail(provider.getEmail());
		result.setAddress(provider.getaddress());
		result.setPhoto(provider.getPhoto());
		result.setBirthdate(provider.getBirthdate());

		result.setArticleRatings(new ArrayList<ArticleRating>());

		result.getUserAccount().setUsername(provider.getUsername());
		result.getUserAccount().setPassword(provider.getPassword());

		this.validator.validate(result, binding);

		return result;
	}

	public Provider reconstruct(final Provider provider, final BindingResult binding) {
		Provider providerStored;

		if (provider.getId() != 0) {
			providerStored = this.providerRepository.findOne(provider.getId());

			provider.setId(providerStored.getId());
			provider.setUserAccount(providerStored.getUserAccount());
			provider.setVersion(providerStored.getVersion());

			provider.setArticleRatings(providerStored.getArticleRatings());
			provider.setBanned(providerStored.isBanned());
		}
		this.validator.validate(provider, binding);
		return provider;

	}

	public void flush() {
		this.providerRepository.flush();
	}

}
