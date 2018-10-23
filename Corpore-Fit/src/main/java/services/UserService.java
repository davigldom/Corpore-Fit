
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Appointment;
import domain.ArticleRating;
import domain.CreditCard;
import domain.FriendRequest;
import domain.GymRating;
import domain.Mark;
import domain.Measure;
import domain.Nutritionist;
import domain.ProductOrder;
import domain.Role;
import domain.ShoppingCart;
import domain.SocialNetwork;
import domain.User;
import forms.RegisterUser;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository		userRepository;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private NutritionistService	nutritionistService;


	// @Autowired
	// private Validator validator;

	public User create() {

		final UserAccount userAccount = this.userAccountService.create();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.USER);
		userAccount.addAuthority(authority);

		final User result;
		result = new User();
		result.setUserAccount(userAccount);
		return result;
	}

	public User findOne(final int userId) {
		User result;
		Assert.isTrue(userId != 0);
		result = this.userRepository.findOne(userId);
		Assert.notNull(result);
		return result;
	}

	public User save(final User user) {
		User result;
		Assert.notNull(user);
		Assert.notNull(user.getUserAccount().getUsername());
		Assert.notNull(user.getUserAccount().getPassword());
		if (user.getId() == 0) {
			String passwordHashed = null;
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			passwordHashed = encoder.encodePassword(user.getUserAccount().getPassword(), null);
			user.getUserAccount().setPassword(passwordHashed);

		} else
			Assert.isTrue(this.findByPrincipal().equals(user));

		result = this.userRepository.save(user);
		return result;
	}

	public User saveRating(final User user, final GymRating gr) {
		final User principal = this.findByPrincipal();
		Assert.isTrue(user.equals(principal));

		principal.getGymRatings().add(gr);
		final User saved = this.userRepository.save(user);

		return saved;

	}

	public User assignNutritionist(final int nutritionistId) {
		final User user = this.findByPrincipal();
		final Nutritionist nutritionist = this.nutritionistService.findOne(nutritionistId);
		Assert.isTrue(nutritionist.isValidated());
		Assert.isTrue(!nutritionist.isBanned());
		user.setNutritionist(nutritionist);
		final User result = this.userRepository.save(user);
		return result;
	}

	public void delete(final User user) {
		Assert.isTrue(user.getId() != 0);
		Assert.isTrue(this.actorService.findByPrincipal().equals(user));
		this.userRepository.delete(user);
		Assert.isTrue(!this.userRepository.findAll().contains(user));
	}

	public User findByPrincipal() {
		User result;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public User findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		User result;
		result = this.userRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<User> findUsersByNutritionistId(final int nutritionistId) {
		return this.userRepository.findUsersByNutritionistId(nutritionistId);
	}

	public Collection<User> findTopSquat(final Role rol) {
		return this.userRepository.findTopSquat(rol, new PageRequest(0, 10));
	}

	public Collection<User> findTopBenchPress(final Role rol) {
		return this.userRepository.findTopBenchPress(rol, new PageRequest(0, 10));
	}

	public Collection<User> findTopDeadlift(final Role rol) {
		return this.userRepository.findTopDeadlift(rol, new PageRequest(0, 10));
	}

	public Collection<User> findTopPullUp(final Role rol) {
		return this.userRepository.findTopPullUp(rol, new PageRequest(0, 10));
	}

	public Collection<User> findTopRowing(final Role rol) {
		return this.userRepository.findTopRowing(rol, new PageRequest(0, 10));
	}
	
	
	public Double getRatioUsersWithoutDiet(){
		return this.userRepository.getRatioUsersWithoutDiet();
	}
	
	public Double getRatioUsersWithoutGym(){
		return this.userRepository.getRatioUsersWithoutGym();
	}
	
	public Double getRatioUsersWithClosedPrivacy(){
		return this.userRepository.getRatioUsersWithClosedPrivacy();
	}
	
	public Double getRatioUsersWithOpenPrivacy(){
		return this.userRepository.getRatioUsersWithOpenPrivacy();
	}
	
	public Double getRatioUsersWithFriendsPrivacy(){
		return this.userRepository.getRatioUsersWithFriendsPrivacy();
	}
	
	public Double getRatioUsersWithRoleBodybuilder(){
		return this.userRepository.getRatioUsersWithRoleBodybuilder();
	}
	
	public Double getRatioUsersWithRolePowerlifter(){
		return this.userRepository.getRatioUsersWithRolePowerlifter();
	}
	
	public Double getRatioUsersWithRoleCalisthenics(){
		return this.userRepository.getRatioUsersWithRoleCalisthenics();
	}
	
	public Double getRatioUsersWithRoleStrongman(){
		return this.userRepository.getRatioUsersWithRoleStrongman();
	}
	
	public Double getRatioUsersWithRoleCrossfitter(){
		return this.userRepository.getRatioUsersWithRoleCrossfitter();
	}
	
	public Double getRatioUsersWithRoleWeightlifter(){
		return this.userRepository.getRatioUsersWithRoleWeightlifter();
	}
	
	public Double getRatioUsersWithRoleNone(){
		return this.userRepository.getRatioUsersWithRoleNone();
	}

	
	// Reconstruct

	@Autowired
	private Validator	validator;


	public User reconstructRegister(final RegisterUser user, final BindingResult binding) {
		final User result;
		Assert.isTrue(user.isAcceptedTerms());
		Assert.isTrue(user.getPassword().equals(user.getRepeatedPassword()));
		result = this.create();

		result.setEmail(user.getEmail());
		result.setName(user.getName());
		result.setPhone(user.getPhone());
		result.setAddress(user.getaddress());
		result.setSurname(user.getSurname());
		result.setBirthdate(user.getBirthdate());
		result.setRole(user.getRole());
		result.setPrivacy(user.getPrivacy());
		result.setPhoto(user.getPhoto());
		result.setBanned(false);

		result.setArticleRatings(new ArrayList<ArticleRating>());
		result.setMarks(new ArrayList<Mark>());
		result.setMeasures(new ArrayList<Measure>());
		result.setReceivedRequests(new ArrayList<FriendRequest>());
		result.setSentRequests(new ArrayList<FriendRequest>());
		result.setSocialNetworks(new ArrayList<SocialNetwork>());
		result.setCreditCards(new ArrayList<CreditCard>());
		result.setGymRatings(new HashSet<GymRating>());
		result.setAppointments(new ArrayList<Appointment>());
		result.setOrders(new ArrayList<ProductOrder>());
		result.setShoppingCart(new ArrayList<ShoppingCart>());

		result.getUserAccount().setUsername(user.getUsername());
		result.getUserAccount().setPassword(user.getPassword());

		this.validator.validate(result, binding);

		return result;
	}

	public User reconstruct(final User user, final BindingResult binding) {
		User userStored;

		if (user.getId() != 0) {
			userStored = this.userRepository.findOne(user.getId());

			user.setId(userStored.getId());
			user.setUserAccount(userStored.getUserAccount());
			user.setVersion(userStored.getVersion());

			user.setArticleRatings(userStored.getArticleRatings());
			user.setBanned(userStored.isBanned());
			user.setMarks(userStored.getMarks());
			user.setMeasures(userStored.getMeasures());
			user.setReceivedRequests(userStored.getReceivedRequests());
			user.setSentRequests(userStored.getSentRequests());
			user.setRoutine(userStored.getRoutine());
			user.setGymRatings(userStored.getGymRatings());
			user.setSocialNetworks(userStored.getSocialNetworks());
			user.setCreditCards(userStored.getCreditCards());
			user.setAppointments(userStored.getAppointments());
			user.setShoppingCart(userStored.getShoppingCart());
			user.setOrders(userStored.getOrders());
			user.setDiet(userStored.getDiet());
			user.setNutritionist(userStored.getNutritionist());
			user.setShoppingCart(userStored.getShoppingCart());
		}
		this.validator.validate(user, binding);
		return user;

	}

	public void flush() {
		this.userRepository.flush();
	}

}
