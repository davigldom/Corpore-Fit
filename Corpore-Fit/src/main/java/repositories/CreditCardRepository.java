
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CreditCard;
import domain.User;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

	@Query("select distinct u from User u join u.creditCards cc where cc.id = ?1")
	User findOwner(int creditCardId);

	@Query("select cc.number from CreditCard cc")
	Collection<String> findAllNumbers();
}
