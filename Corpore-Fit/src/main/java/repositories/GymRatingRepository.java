
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Gym;
import domain.GymRating;
import domain.User;

@Repository
public interface GymRatingRepository extends JpaRepository<GymRating, Integer> {

	@Query("select u from User u where ?1 member of u.gymRatings")
	User findUserByGymRating(GymRating gr);

	@Query("select g from Gym g where ?1 member of g.gymRatings")
	Gym findGymByGymRating(GymRating gr);

}
