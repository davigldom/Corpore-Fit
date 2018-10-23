
package repositories;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Activity;
import domain.DayName;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

	@Query("select a from Activity a where a.gym.id=?1")
	Collection<Activity> findByGym(int gymId);

	@Query("select count(ab) from ActivityBook ab where ab.activity.id=?1")
	int countBooksForActivity(int activityId);

	@Query("select a from Activity a where a.gym.id=?1 AND a.day=?2 AND a.room.id=?3 AND ((a.start between ?4 and ?5) OR (a.end between ?4 and ?5) OR (a.start<=?4 and a.end>=?5))")
	Activity countByGymDayRoomAndTime(int gymId, DayName day, int roomId, Calendar start, Calendar end);

	//	@Query("select count(a) from Activity a where a.gym.id=?1 AND a.day=?2 AND ((a.start between ?3 and ?4) OR (a.end between ?3 and ?4))")
	//	int countByGymDayAndTime(int gymId, DayName day, Calendar start, Calendar end);

	//select count(a) from Activity a where a.gym.id=1154 AND a.day=2 AND a.room.id=1157 AND ((a.start between '1970-01-01 17:30:00' and '1970-01-01 18:30:00') OR (a.end between '1970-01-01 17:30:00' and '1970-01-01 18:30:00'));

	//Not useful
	@Query("select a from Activity a where a.gym.id=?1 and a.day=1 order by start")
	Collection<Activity> findByGymMonday(int gymId);

	@Query("select a from Activity a where a.gym.id=?1 and a.day=2 order by start")
	Collection<Activity> findByGymTuesday(int gymId);

	@Query("select a from Activity a where a.gym.id=?1 and a.day=3 order by start")
	Collection<Activity> findByGymWednesday(int gymId);

	@Query("select a from Activity a where a.gym.id=?1 and a.day=4 order by start")
	Collection<Activity> findByGymThursday(int gymId);

	@Query("select a from Activity a where a.gym.id=?1 and a.day=5 order by start")
	Collection<Activity> findByGymFriday(int gymId);

	@Query("select a from Activity a where a.gym.id=?1 and a.day=6 order by start")
	Collection<Activity> findByGymSaturday(int gymId);

	@Query("select a from Activity a where a.gym.id=?1 and a.day=0 order by start")
	Collection<Activity> findByGymSunday(int gymId);

}
