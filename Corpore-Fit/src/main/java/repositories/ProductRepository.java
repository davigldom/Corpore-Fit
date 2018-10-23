
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("select p from Product p where p.provider.id=?1")
	Collection<Product> findAllByProvider(int providerId);

	@Query("select p from Product p where p.provider.id=?1 and p.advert is null")
	Collection<Product> findAllAvailableToAddByProvider(int providerId);

	@Query("select p from Product p where p.advert is not null")
	Collection<Product> findAllWithAdvert();

	@Query("select p from Product p " + "where (p.name like CONCAT('%',?1,'%')" + "or p.description like CONCAT('%',?1,'%')) and p.provider.banned=0")
	Collection<Product> findByKeyword(String keyword);

	@Query("select p from Product p where p.provider.banned=0")
	Collection<Product> findAllAvailable();

}
