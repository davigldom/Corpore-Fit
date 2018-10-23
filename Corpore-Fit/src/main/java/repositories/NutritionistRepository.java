
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DayName;
import domain.DaySchedule;
import domain.Nutritionist;
import domain.User;

@Repository
public interface NutritionistRepository extends JpaRepository<Nutritionist, Integer> {

	@Query("select n from Nutritionist n where n.userAccount.id=?1")
	Nutritionist findByNutritionistAccountId(int nutritionistAccountId);

	@Query("select n from Nutritionist n where n.validated = 0 and n.banned=0")
	Collection<Nutritionist> findNotValidated();

	@Query("select n from Nutritionist n where n.validated = 1 and n.banned=0")
	Collection<Nutritionist> findValidated();
	
	@Query("select s from Nutritionist n join n.schedule s where s.day=?2 and n.id=?1")
	Collection<DaySchedule> getDaySchedule(DayName day, int nutritionistId);
	

	@Query("select u from User u where u.nutritionist.id=?1")
	Collection<User> getAssigners(int nutritionistId);
}
