
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Role;
import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.userAccount.id=?1")
	User findByUserAccountId(int userAccountId);

	@Query("select u from User u where u.nutritionist.id=?1")
	Collection<User> findUsersByNutritionistId(int nutritionistId);
	
	@Query("select u from User u join u.marks m where u.role=?1 and u.privacy=0 group by u order by max(m.squat) desc")
	List<User> findTopSquat(Role rol, Pageable page);
	
	@Query("select u from User u join u.marks m where u.role=?1 and u.privacy=0 group by u order by max(m.benchPress) desc")
	List<User> findTopBenchPress(Role rol, Pageable page);
	
	@Query("select u from User u join u.marks m where u.role=?1 and u.privacy=0 group by u order by max(m.deadlift) desc")
	List<User> findTopDeadlift(Role rol, Pageable page);
	
	@Query("select u from User u join u.marks m where u.role=?1 and u.privacy=0 group by u order by max(m.pullUp) desc")
	List<User> findTopPullUp(Role rol, Pageable page);
	
	@Query("select u from User u join u.marks m where u.role=?1 and u.privacy=0 group by u order by max(m.rowing) desc")
	List<User> findTopRowing(Role rol, Pageable page);
	
	//Dashboard
	@Query("select 1.0*count(s)/(select count(u) from User u) from Subscription s")
	Double getRatioUsersWithoutGym();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.diet is null")
	Double getRatioUsersWithoutDiet();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.privacy=0")
	Double getRatioUsersWithClosedPrivacy();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.privacy=1")
	Double getRatioUsersWithOpenPrivacy();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.privacy=2")
	Double getRatioUsersWithFriendsPrivacy();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.role=0")
	Double getRatioUsersWithRoleBodybuilder();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.role=1")
	Double getRatioUsersWithRolePowerlifter();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.role=2")
	Double getRatioUsersWithRoleCalisthenics();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.role=3")
	Double getRatioUsersWithRoleStrongman();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.role=4")
	Double getRatioUsersWithRoleCrossfitter();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.role=5")
	Double getRatioUsersWithRoleWeightlifter();
	
	@Query("select 1.0*count(u)/(select count(u) from User u) from User u where u.role=6")
	Double getRatioUsersWithRoleNone();
	
	
	
	

	
	
}
