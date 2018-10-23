
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ArticleRatingRepository;
import domain.Actor;
import domain.Article;
import domain.ArticleRating;
import domain.Editor;

@Service
@Transactional
public class ArticleRatingService {

	@Autowired
	private ArticleRatingRepository	articleRatingRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private EditorService			editorService;


	public ArticleRating create() {
		Assert.isTrue(this.actorService.isAuthenticated());

		final ArticleRating ar = new ArticleRating();

		return ar;
	}

	public ArticleRating findOne(final int articleRatingId) {
		ArticleRating result;
		result = this.articleRatingRepository.findOne(articleRatingId);
		Assert.notNull(result);
		return result;
	}

	public Collection<ArticleRating> findAll() {
		return this.articleRatingRepository.findAll();
	}

	public ArticleRating save(final ArticleRating ar) {
		Assert.isTrue(this.actorService.isAuthenticated());

		final ArticleRating saved = this.articleRatingRepository.save(ar);
		return saved;
	}

	public Actor findActorByArticleRating(final ArticleRating ar) {
		return this.articleRatingRepository.findActorByArticleRating(ar);
	}

	public Article findArticleByArticleRating(final ArticleRating ar) {
		return this.articleRatingRepository.findArticleByArticleRating(ar);
	}

	public void delete(final ArticleRating articleRating) {
		Assert.isTrue(this.actorService.isAdmin() || this.actorService.isEditor());

		final Article article = this.findArticleByArticleRating(articleRating);
		Assert.notNull(article);

		if (this.actorService.isEditor()) {
			final Editor editor = this.editorService.findByPrincipal();
			Assert.isTrue(article.getEditor().equals(editor));
		}

		this.findActorByArticleRating(articleRating).getArticleRatings().remove(articleRating);
		article.getArticleRatings().remove(articleRating);

		this.articleRatingRepository.delete(articleRating);

		Assert.isTrue(!this.articleRatingRepository.findAll().contains(articleRating));

	}
}
