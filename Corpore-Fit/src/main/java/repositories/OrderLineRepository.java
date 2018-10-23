
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.OrderLine;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {

	@Query("select po.orderLines from ProductOrder po where po.id=?1")
	Collection<OrderLine> findByOrder(int orderId);

}
