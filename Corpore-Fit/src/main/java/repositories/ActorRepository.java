
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id=?1")
	Actor findByUserAccountId(int userAccountId);

	@Query("select a.userAccount.username from Actor a where a.banned=1")
	Collection<String> findBannedUsernames();

	@Query("select a from Actor a " + "where a.banned=false and (a.name like CONCAT('%',?1,'%')" + "or a.surname like CONCAT('%',?1,'%')" + "or a.userAccount.username like CONCAT('%',?1,'%'))")
	Collection<Actor> findAllByKeyword(String keyword);

	@Query("select a from Actor a where a.banned=1")
	Collection<Actor> findAllBanned();

	@Query("select a from Actor a where a.banned=0")
	Collection<Actor> findAllNotBanned();
}
