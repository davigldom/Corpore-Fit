
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DailyDiet;
import domain.DayName;
import domain.Nutritionist;

@Repository
public interface DailyDietRepository extends JpaRepository<DailyDiet, Integer> {

	@Query("select d.nutritionist from Diet d join d.dailyDiets dd where dd.id=?1")
	Nutritionist findCreator(int dayId);

	@Query("select dd from Diet d join d.dailyDiets dd where d.id=?1 and dd.day=?2")
	DailyDiet findByNameAndNutritionist(int dietId, DayName dayType);
	
}
