
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Diet;
import domain.User;

@Repository
public interface DietRepository extends JpaRepository<Diet, Integer> {

	@Query("select u from User u where u.diet.id=?1")
	Collection<User> getFollowers(int dietId);
	
}
