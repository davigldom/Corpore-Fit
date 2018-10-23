
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Day;
import domain.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

	@Query("select d from Day d join d.exercises e where e.id=?1")
	Day findDay(int exerciseId);
	
}
