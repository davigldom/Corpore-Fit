
package services;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ManagerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.ArticleRating;
import domain.Manager;
import forms.RegisterActor;

@Service
@Transactional
public class ManagerService {

	@Autowired
	private ManagerRepository		managerRepository;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	adminService;


	public Manager create() {

		final UserAccount userAccount = this.userAccountService.create();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MANAGER);
		userAccount.addAuthority(authority);

		final Manager result;
		result = new Manager();
		result.setUserAccount(userAccount);
		return result;
	}

	public Manager findOne(final int managerId) {
		Manager result;
		Assert.isTrue(managerId != 0);
		result = this.managerRepository.findOne(managerId);
		Assert.notNull(result);
		return result;
	}

	public Manager findByPrincipal() {
		Manager result;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Manager findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Manager result;
		result = this.managerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Manager save(final Manager manager) {
		Manager result;
		Assert.notNull(manager);
		Assert.notNull(manager.getUserAccount().getUsername());
		Assert.notNull(manager.getUserAccount().getPassword());
		if (manager.getId() == 0) {
			Assert.notNull(this.adminService.findByPrincipal());
			String passwordHashed = null;
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			passwordHashed = encoder.encodePassword(manager.getUserAccount().getPassword(), null);
			manager.getUserAccount().setPassword(passwordHashed);
		}

		result = this.managerRepository.save(manager);
		return result;
	}

	public void delete(final Manager manager) {
		Assert.isTrue(manager.getId() != 0);

		if (this.actorService.isManager())
			Assert.isTrue(this.findByPrincipal().equals(manager));
		else
			Assert.isTrue(this.actorService.isAdmin());

		this.managerRepository.delete(manager);
		Assert.isTrue(!this.managerRepository.findAll().contains(manager));
	}


	//Reconstruct

	@Autowired
	private Validator	validator;


	public Manager reconstruct(final Manager manager, final BindingResult binding) {
		Manager managerStored;

		if (manager.getId() != 0) {
			managerStored = this.managerRepository.findOne(manager.getId());

			manager.setId(managerStored.getId());
			manager.setUserAccount(managerStored.getUserAccount());
			manager.setVersion(managerStored.getVersion());

			manager.setGym(managerStored.getGym());

			manager.setArticleRatings(managerStored.getArticleRatings());
			manager.setBanned(managerStored.isBanned());
		}
		this.validator.validate(manager, binding);
		return manager;

	}

	public Manager reconstructRegister(final RegisterActor manager, final BindingResult binding) {
		Manager result;
		Assert.isTrue(manager.getPassword().equals(manager.getRepeatedPassword()));
		result = this.create();

		result.setName(manager.getName());
		result.setSurname(manager.getSurname());
		result.setPhone(manager.getPhone());
		result.setEmail(manager.getEmail());
		result.setAddress(manager.getaddress());
		result.setPhoto(manager.getPhoto());
		result.setBirthdate(manager.getBirthdate());

		result.setArticleRatings(new ArrayList<ArticleRating>());

		result.getUserAccount().setUsername(manager.getUsername());
		result.getUserAccount().setPassword(manager.getPassword());

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.managerRepository.flush();
	}

}
