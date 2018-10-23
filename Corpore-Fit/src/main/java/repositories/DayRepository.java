
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Day;
import domain.DayName;
import domain.User;

@Repository
public interface DayRepository extends JpaRepository<Day, Integer> {

	@Query("select r.user from Routine r join r.days d where d.id=?1")
	User findCreator(int dayId);
	
	@Query("select d from Routine r join r.days d where r.user.id=?1 and d.day=?2")
	Day findByNameAndUser(int userId, DayName dayType);
	
}
