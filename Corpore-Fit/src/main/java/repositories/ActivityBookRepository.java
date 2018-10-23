
package repositories;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ActivityBook;
import domain.DayName;

@Repository
public interface ActivityBookRepository extends JpaRepository<ActivityBook, Integer> {

	@Query("select count(ab) from ActivityBook ab where ab.user.id=?1 and ab.activity.id=?2")
	int alreadyBooked(int userId, int activityId);

	@Query("select ab from ActivityBook ab where ab.user.id=?1 and ab.activity.id=?2")
	ActivityBook findByUserAndActivity(int userId, int activityId);

	@Query("select ab from ActivityBook ab where ab.user.id=?1")
	Collection<ActivityBook> findAllByUser(int userId);

	@Query("select ab from ActivityBook ab where ab.activity.id=?1")
	Collection<ActivityBook> findAllByActivity(int activityId);

	@Query("select count(ab) from ActivityBook ab where ab.user.id=?1 AND ab.activity.gym.id=?2 AND ab.activity.day=?3 AND ((ab.activity.start between ?4 and ?5) OR (ab.activity.end between ?4 and ?5))")
	int countByUserGymDayAndTime(int userId, int gymId, DayName day, Calendar start, Calendar end);

}
