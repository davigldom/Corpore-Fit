
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select c from Comment c where c.owner.id=?1")
	Collection<Comment> findAllByActor(int actorId);

	@Query("select c from Comment c, Article a where c member of a.comments and a.id=?1 and c.parent=null order by c.moment desc")
	Collection<Comment> findAllParentsByArticle(int actorId);

}
