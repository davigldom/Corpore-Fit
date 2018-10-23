
package repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Mark;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
		@Query("select m from User u join u.marks m where u.id=?1 order by m.creationDate desc")
		List<Mark> getLatestMark(int userId, Pageable pageable);
}
