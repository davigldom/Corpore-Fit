
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advert;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Integer> {

	@Query("select a from Advert a where a.provider.id=?1")
	Collection<Advert> findAllByProvider(int provierId);

}
