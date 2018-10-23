
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SNetwork;
import domain.SocialNetwork;

@Repository
public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Integer> {

	@Query("select sn from User u join u.socialNetworks sn where u.id=?1 and sn.socialNetworkType=?2")
	SocialNetwork findByTypeAndUser(int userId, SNetwork snType);

	@Query("select sn from Gym g join g.socialNetworks sn where g.id=?1 and sn.socialNetworkType=?2")
	SocialNetwork findByTypeAndGym(int gymId, SNetwork snType);

}
