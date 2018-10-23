
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Activity;
import domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

	@Query("select r from Room r where r.manager.gym.id=?1")
	Collection<Room> findByGym(int gymId);
	
	@Query("select r from Room r where r.manager.id=?1")
	Collection<Room> findByManager(int managerId);

	@Query("select a from Activity a where a.room.id=?1")
	Collection<Activity> findActivitiesByRoom(int roomId);

}
