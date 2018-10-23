
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ArticleRepository;
import domain.Article;
import domain.ArticleRating;
import domain.Comment;
import domain.Editor;

@Service
@Transactional
public class ArticleService {

	@Autowired
	private ArticleRepository	articleRepository;

	@Autowired
	private EditorService		editorService;

	@Autowired
	private ActorService		actorService;


	public Article create() {
		final Article result;

		result = new Article();

		return result;
	}

	public Article findOne(final int articleId) {
		Article result;
		Assert.isTrue(articleId != 0);
		result = this.articleRepository.findOne(articleId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Article> findAllByEditor(final int editorId) {
		Assert.isTrue(editorId != 0);
		final Collection<Article> result = this.articleRepository.findAllByEditor(editorId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Article> findAllPublished() {
		final Collection<Article> result;
		result = this.articleRepository.findAllPublished();
		Assert.notNull(result);
		return result;
	}

	public Collection<Article> findAll() {
		return this.articleRepository.findAll();
	}

	public Article findByComment(final Comment comment) {
		return this.articleRepository.findByComment(comment);
	}

	public Article save(final Article article) {
		Article result;
		final Editor principal = this.editorService.findByPrincipal();
		Assert.notNull(article);

		Assert.isTrue(article.getEditor().equals(principal));

		result = this.articleRepository.save(article);
		return result;
	}

	public Article saveRating(final Article article, final ArticleRating ar) {
		Assert.notNull(article);
		Assert.isTrue(this.actorService.isAuthenticated());

		article.getArticleRatings().add(ar);
		final Article saved = this.articleRepository.save(article);

		return saved;
	}

	public void publish(final int articleId) {
		Assert.notNull(articleId);
		final Calendar moment = Calendar.getInstance();

		final Editor principal = this.editorService.findByPrincipal();
		Assert.notNull(principal);

		final Article article = this.findOne(articleId);
		Assert.notNull(article);
		Assert.isTrue(article.getEditor().equals(principal));
		Assert.isTrue(article.getPublicationDate() == null);
		Assert.isTrue(!article.isDraft());

		article.setPublicationDate(moment);

		this.editorService.save(principal);
	}

	public void delete(final Article article) {
		Assert.isTrue(this.actorService.isAdmin() || this.actorService.isEditor());

		if (this.actorService.isEditor())
			Assert.isTrue(this.editorService.findByPrincipal().equals(article.getEditor()));
		else
			Assert.isTrue(!article.isDraft());

		this.articleRepository.delete(article);

		Assert.isTrue(!this.articleRepository.findAll().contains(article));

	}

	public Article findOneToEdit(final int articleId) {
		Article result;
		final Editor principal = this.editorService.findByPrincipal();

		Assert.isTrue(articleId != 0);
		result = this.articleRepository.findOne(articleId);
		Assert.notNull(result);
		Assert.isTrue(result.getEditor().equals(principal));
		Assert.isTrue(result.isDraft());
		Assert.isNull(result.getPublicationDate());

		return result;
	}

	public void flush() {
		this.articleRepository.flush();
	}

	public Collection<Article> findByKeyword(String keyword) {
		if (keyword == null)
			keyword = "%";
		Collection<Article> result = null;
		result = this.articleRepository.findByKeyword(keyword);
		return result;
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Article reconstruct(final Article article, final BindingResult binding) {
		final Article articleStored;

		if (article.getId() == 0) {
			final Editor principal = this.editorService.findByPrincipal();
			article.setEditor(principal);
			article.setPublicationDate(null);
			article.setDraft(true);
			article.setArticleRatings(new ArrayList<ArticleRating>());
			article.setComments(new ArrayList<Comment>());
		} else {
			articleStored = this.articleRepository.findOne(article.getId());

			article.setEditor(articleStored.getEditor());
			article.setId(articleStored.getId());
			article.setVersion(articleStored.getVersion());
			article.setPublicationDate(articleStored.getPublicationDate());
			article.setArticleRatings(articleStored.getArticleRatings());
			article.setComments(articleStored.getComments());
		}
		this.validator.validate(article, binding);

		return article;
	}
}
