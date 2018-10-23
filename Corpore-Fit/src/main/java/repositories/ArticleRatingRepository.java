
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Article;
import domain.ArticleRating;

@Repository
public interface ArticleRatingRepository extends JpaRepository<ArticleRating, Integer> {

	@Query("select a from Actor a where ?1 member of a.articleRatings")
	Actor findActorByArticleRating(ArticleRating ar);

	@Query("select a from Article a where ?1 member of a.articleRatings")
	Article findArticleByArticleRating(ArticleRating ar);

}
