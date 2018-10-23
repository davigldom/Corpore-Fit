
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

	@Query("select count(fr) from FriendRequest fr where fr.sender.id=?1 and fr.receiver.id=?2")
	Integer numberOfRequests(int senderId, int receiverId);

	@Query("select count(fr) from FriendRequest fr where (fr.sender.id=?1 and fr.receiver.id=?2 and fr.accepted=1) or (fr.sender.id=?2 and fr.receiver.id=?1 and fr.accepted=1)")
	int areFriends(int user1Id, int user2Id);

	@Query("select fr from FriendRequest fr where (fr.sender.id=?1 and fr.receiver.id=?2) or (fr.sender.id=?2 and fr.receiver.id=?1) and fr.accepted=1")
	FriendRequest findByUsers(int user1Id, int user2Id);

	@Query("select fr from FriendRequest fr where fr.sender.id=?1 and fr.accepted=0 and fr.receiver.banned=0")
	Collection<FriendRequest> getSentRequests(int userId);

	@Query("select fr from FriendRequest fr where fr.receiver.id=?1 and fr.accepted=0 and fr.sender.banned=0")
	Collection<FriendRequest> getReceivedRequests(int userId);

	@Query("select fr from FriendRequest fr where (fr.sender.id=?1 or fr.receiver.id=?1) and fr.accepted=1 and fr.sender.banned=0 and fr.receiver.banned=0")
	Collection<FriendRequest> getAcceptedRequests(int userId);

}
