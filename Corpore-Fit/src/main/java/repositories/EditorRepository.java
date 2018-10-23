
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Editor;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Integer> {

	@Query("select e from Editor e where e.userAccount.id=?1")
	Editor findByUserAccountId(int userAccountId);

}
