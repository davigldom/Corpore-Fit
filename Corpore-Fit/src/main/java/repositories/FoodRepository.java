
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DailyDiet;
import domain.Diet;
import domain.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

	@Query("select d from DailyDiet d join d.foods f where f.id=?1")
	DailyDiet findDailyDiet(int foodId);
	
	@Query("select d from Diet d join d.dailyDiets dd where dd in (select d from DailyDiet d join d.foods f where f.id=?1)")
	Diet findDiet(int foodId);
	
}
