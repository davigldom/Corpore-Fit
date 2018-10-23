
package repositories;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ShoppingCart;
import domain.User;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

	@Query("select u from User u join u.shoppingCart sc where sc.id=?1")
	User findOwner(int cartId);
	
	@Query("select sum(sc.price) from User u join u.shoppingCart sc where u.id=?1")
	Double getTotalPrice(int userId);
	
	@Query("select sc from User u join u.shoppingCart sc where u.id=?2 and sc.product.id=?1")
	ShoppingCart findByProduct(int productId, int userId);
	
	@Query("select sc from User u join u.shoppingCart sc where sc.product.id=?1")
	Collection<ShoppingCart> findByProduct(int productId);

}
