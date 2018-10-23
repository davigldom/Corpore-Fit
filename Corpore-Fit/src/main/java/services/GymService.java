
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.GymRepository;
import domain.Activity;
import domain.Gym;
import domain.GymRating;
import domain.Manager;
import domain.SocialNetwork;
import domain.Subscription;

@Service
@Transactional
public class GymService {

	@Autowired
	private GymRepository			gymRepository;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private AdministratorService	adminService;

	@Autowired
	private ActorService			actorService;


	public Gym create() {
		final Gym result;

		result = new Gym();

		Manager manager;
		manager = this.managerService.findByPrincipal();

		final Collection<Activity> activities = new ArrayList<Activity>();

		result.setManager(manager);
		result.setActivities(activities);

		return result;
	}

	public Gym findOne(final int gymId) {
		Gym result;
		Assert.isTrue(gymId != 0);
		result = this.gymRepository.findOne(gymId);
		Assert.notNull(result);
		return result;
	}

	public Gym findOneToEdit(final int gymId) {
		Gym result;
		final Manager principal = this.managerService.findByPrincipal();

		Assert.isTrue(gymId != 0);
		result = this.gymRepository.findOne(gymId);
		Assert.notNull(result);
		Assert.isTrue(principal.getGym().equals(result));

		return result;
	}

	public Collection<Gym> findAll() {
		return this.gymRepository.findAll();
	}

	public Collection<Gym> findTop5() {
		return this.gymRepository.findTop5(new PageRequest(0, 5));
	}

	public Gym save(final Gym gym) {
		Gym result;

		final Manager principal = this.managerService.findByPrincipal();
		Assert.notNull(principal);
		if (gym.getId() != 0)
			Assert.isTrue(principal.getGym().equals(gym));

		Assert.notNull(gym);

		result = this.gymRepository.save(gym);

		if (gym.getId() == 0)
			principal.setGym(result);

		return result;
	}

	public Gym saveRating(final Gym gym, final GymRating gr) {
		Assert.notNull(gym);
		Assert.isTrue(this.actorService.isUser());

		gym.getGymRatings().add(gr);
		final Gym saved = this.gymRepository.save(gym);

		return saved;
	}

	public void delete(final Gym gym) {
		Assert.notNull(this.adminService.findByPrincipal());

		gym.getManager().setGym(null);
		gym.getManager().setActive(false);

		this.gymRepository.delete(gym);

		Assert.isTrue(!this.gymRepository.findAll().contains(gym));

	}

	public void flush() {
		this.gymRepository.flush();
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Gym reconstruct(final Gym gym, final BindingResult binding) {
		final Gym gymStored;

		if (gym.getId() == 0) {
			final Manager manager = this.managerService.findByPrincipal();
			final Collection<Activity> activities = new ArrayList<Activity>();
			final Collection<SocialNetwork> socialNetworks = new ArrayList<SocialNetwork>();
			final Collection<Subscription> subscriptions = new ArrayList<Subscription>();

			gym.setManager(manager);
			gym.setActivities(activities);
			gym.setGymRatings(new HashSet<GymRating>());
			gym.setSocialNetworks(socialNetworks);
			gym.setSubscriptions(subscriptions);
		} else {
			gymStored = this.gymRepository.findOne(gym.getId());

			gym.setActivities(gymStored.getActivities());
			gym.setManager(gymStored.getManager());
			gym.setGymRatings(gymStored.getGymRatings());
			gym.setSocialNetworks(gymStored.getSocialNetworks());
			gym.setSubscriptions(gymStored.getSubscriptions());
		}

		this.validator.validate(gym, binding);

		return gym;
	}

}
