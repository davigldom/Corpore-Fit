
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Routine;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Integer> {

}
