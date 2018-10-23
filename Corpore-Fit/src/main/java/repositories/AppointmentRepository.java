
package repositories;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	@Query("select a from Appointment a where a.cancelled=0 and a.time between ?1 and ?2")
	Appointment findByTime(Calendar time1, Calendar time2);

	@Query("select a.time.time from Appointment a where a.cancelled=0 and a.nutritionist.id=?3 and a.time between ?1 and ?2")
	Collection<Calendar> findAllBetweenTimes(Calendar time1, Calendar time2, int nutritionistId);

}
