
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.GymRatingRepository;
import domain.Gym;
import domain.GymRating;
import domain.User;

@Service
@Transactional
public class GymRatingService {

	@Autowired
	private GymRatingRepository	gymRatingRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserService			userService;


	public GymRating create() {
		Assert.isTrue(this.actorService.isUser());

		final GymRating gr = new GymRating();

		return gr;
	}

	public GymRating findOne(final int gymRatingId) {
		GymRating result;
		result = this.gymRatingRepository.findOne(gymRatingId);
		Assert.notNull(result);
		return result;
	}

	public Collection<GymRating> findAll() {
		return this.gymRatingRepository.findAll();
	}

	public GymRating save(final GymRating gr) {
		Assert.isTrue(this.actorService.isUser());

		final GymRating saved = this.gymRatingRepository.save(gr);
		return saved;
	}

	public User findUserByGymRating(final GymRating gr) {
		return this.gymRatingRepository.findUserByGymRating(gr);
	}

	public Gym findGymByGymRating(final GymRating gr) {
		return this.gymRatingRepository.findGymByGymRating(gr);
	}

	public void delete(final GymRating gymRating) {
		Assert.isTrue(this.actorService.isUser() || this.actorService.isAdmin());

		final Gym gym = this.findGymByGymRating(gymRating);
		final User user = this.findUserByGymRating(gymRating);
		Assert.notNull(gym);

		if (this.actorService.isUser()) {
			final User principal = this.userService.findByPrincipal();
			Assert.isTrue(user.equals(principal));
		}

		user.getGymRatings().remove(gymRating);
		gym.getGymRatings().remove(gymRating);

		this.gymRatingRepository.delete(gymRating);

		Assert.isTrue(!this.gymRatingRepository.findAll().contains(gymRating));

	}

	public void flush() {
		this.gymRatingRepository.flush();
	}
}
