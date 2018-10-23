
package repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Gym;

@Repository
public interface GymRepository extends JpaRepository<Gym, Integer> {

	@Query("select g from Gym g order by g.subscriptions.size desc")
	List<Gym> findTop5(Pageable page);
	

}
