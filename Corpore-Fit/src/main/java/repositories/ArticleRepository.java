
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.Comment;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query("select a from Article a where a.editor.id=?1")
	Collection<Article> findAllByEditor(int editorId);

	@Query("select a from Article a where a.publicationDate != null")
	Collection<Article> findAllPublished();

	@Query("select a from Article a " + "where (a.title like CONCAT('%',?1,'%')" + "or a.text like CONCAT('%',?1,'%'))")
	Collection<Article> findByKeyword(String keyword);

	@Query("select a from Article a where ?1 member of a.comments")
	Article findByComment(Comment comment);
}
