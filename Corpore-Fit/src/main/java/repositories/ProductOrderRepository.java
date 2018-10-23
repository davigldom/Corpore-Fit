
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ProductOrder;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

	@Query("select po from ProductOrder po where po.creditCard.id=?1")
	Collection<ProductOrder> findByCreditCard(int creditCard);

}
