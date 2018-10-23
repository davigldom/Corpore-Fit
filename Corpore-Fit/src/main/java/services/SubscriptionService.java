
package services;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SubscriptionRepository;
import domain.Gym;
import domain.Subscription;
import domain.User;

@Service
@Transactional
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository	subscriptionRepository;

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;


	public Subscription findOne(final int subscriptionId) {
		Subscription result;
		Assert.isTrue(subscriptionId != 0);
		result = this.subscriptionRepository.findOne(subscriptionId);
		Assert.notNull(result);
		return result;
	}

	public Subscription create() {
		final Subscription result;

		result = new Subscription();

		return result;
	}

	public Subscription save(final Subscription subscription) {
		//		Assert.notNull(subscription);
		//		Assert.isTrue(subscription.getId() == 0);

		final LocalDate now = LocalDate.now();
		if (subscription.getCreditCard().getExpirationYear() == now.getYear())
			Assert.isTrue(subscription.getCreditCard().getExpirationMonth() > now.getMonthOfYear());
		else if (subscription.getCreditCard().getExpirationYear() < now.getYear())
			Assert.isTrue(false);

		Assert.isTrue(this.userService.findByPrincipal().getCreditCards().contains(subscription.getCreditCard()));

		Subscription result;
		result = this.subscriptionRepository.save(subscription);

		final User principal = this.userService.findByPrincipal();
		principal.setSubscription(result);

		//		final Gym gym = this.gymService.findOne(subscription.getGym().getId());
		//		gym.getSubscriptions().add(subscription);
		subscription.getGym().getSubscriptions().add(result);

		return result;
	}

	public void delete(final Subscription subscription) {
		User user;
		final Gym gym = subscription.getGym();

		if (this.actorService.isUser()) {
			user = this.userService.findByPrincipal();
			Assert.isTrue(user.equals(subscription.getUser()));
		} else {
			Assert.isTrue(this.actorService.isAdmin());
			user = this.userService.findOne(subscription.getUser().getId());
		}

		//Assert.isTrue(subscription.getGym().equals(this.userService.findByPrincipal().getSubscription().getGym()));

		user.setSubscription(null);
		gym.getSubscriptions().remove(subscription);
		//this.userService.save(user);

		this.subscriptionRepository.delete(subscription);

		Assert.isTrue(!this.subscriptionRepository.findAll().contains(subscription));

	}


	@Autowired
	private Validator	validator;


	public Subscription reconstruct(final Subscription subscription, final BindingResult binding) {

		final User principal = this.userService.findByPrincipal();
		subscription.setUser(principal);

		final Gym gym = subscription.getGym();
		Assert.notNull(gym);

		subscription.setUser(principal);
		subscription.setGym(gym);
		subscription.setId(0);

		this.validator.validate(subscription, binding);

		return subscription;

	}
}
