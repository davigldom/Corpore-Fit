
package usecases;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ArticleRatingService;
import services.ArticleService;
import services.CommentService;
import services.EditorService;
import utilities.AbstractTest;
import domain.Article;
import domain.ArticleRating;
import domain.Comment;
import domain.Editor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EditorTest extends AbstractTest {

	// System under test

	@Autowired
	private EditorService			editorService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private ArticleRatingService	articleRatingService;

	@Autowired
	private CommentService			commentService;

	DecimalFormat					df	= new DecimalFormat("#.##");	// To get only 2 decimals


	// ------------------------------------------------------ TESTS
	// ------------------------------------------------------------------

	// Edit editor's data
	// ***********************************************************************************************************
	// POSITIVE: Edit editor's data
	@Test
	public void editEditorPositive() {
		this.authenticate("editor1");

		final Editor editor = this.editorService.findOne(this.getEntityId("editor1"));
		editor.setName("New name");
		final Editor stored = this.editorService.save(editor);
		Assert.isTrue(stored.getName().equals("New name"));
		this.authenticate(null);
	}

	// NEGATIVE: Edit edit's data invalid email
	@Test(expected = ConstraintViolationException.class)
	public void editEditorNegativeInvalidEmail() {
		this.authenticate("editor1");

		final Editor editor = this.editorService.findOne(this.getEntityId("editor1"));
		editor.setEmail("Invalid email");
		final Editor stored = this.editorService.save(editor);
		Assert.isTrue(!stored.getEmail().equals("Invalid email"));
		this.authenticate(null);
	}

	// NEGATIVE: Edit editor's data as user
	@Test(expected = IllegalArgumentException.class)
	public void editEditorNegativeAsUser() {
		this.authenticate("user1");

		final Editor editor = this.editorService.findByPrincipal();
		editor.setName("New name");
		final Editor stored = this.editorService.save(editor);
		Assert.isTrue(stored.getName().equals("New name"));
		this.authenticate(null);
	}

	// Delete editor account
	// ***********************************************************************************************************
	// POSITIVE: Delete editor account
	@Test
	public void deleteEditorPositive() {
		this.authenticate("editor1");
		this.setUpAuth("editor1");

		final Editor editor = this.editorService.findOne(this.getEntityId("editor1"));
		final List<Article> articles = new ArrayList<Article>(this.articleService.findAllByEditor(editor.getId()));
		List<ArticleRating> articleRatings = null;

		for (final Article a : articles) {
			articleRatings = new ArrayList<ArticleRating>(a.getArticleRatings());
			for (final ArticleRating ar : articleRatings)
				this.articleRatingService.delete(ar);
			this.articleService.delete(a);
		}

		this.editorService.delete(editor);

		this.authenticate(null);
	}

	// NEGATIVE: Delete editor account as user
	@Test(expected = IllegalArgumentException.class)
	public void deleteEditorNegativeAsUser() {
		this.authenticate("user1");

		final Editor editor = this.editorService.findByPrincipal();
		this.editorService.delete(editor);

		this.authenticate(null);
	}

	// NEGATIVE: Delete editor account that doesn't exists
	@Test(expected = AssertionError.class)
	public void deleteEditorNegativeInvalidEditor() {
		this.authenticate("editor1");

		final Editor editor = this.editorService.findOne(this.getEntityId("editor2"));
		this.editorService.delete(editor);

		this.authenticate(null);
	}

	// Delete article
	// ***********************************************************************************************************
	// POSITIVE: Delete article -> AHORA MISMO NO FUNCIONA PORQUE NO HAY ARTICULOS CREADOS
	@Test
	public void deleteArticlePositive() {
		this.authenticate("editor1");
		this.setUpAuth("editor1");

		final Article article = this.articleService.findOne(this.getEntityId("article1"));

		final Collection<ArticleRating> articleRatings = new ArrayList<ArticleRating>(article.getArticleRatings());
		final Collection<Comment> comments = new ArrayList<Comment>(this.commentService.findAllParentsByArticle(article.getId()));

		for (final ArticleRating ar : articleRatings)
			this.articleRatingService.delete(ar);

		for (final Comment c : comments) {
			if (!c.getReplies().isEmpty()) {
				final java.util.Iterator<Comment> iter = c.getReplies().iterator();
				while (iter.hasNext()) {
					final Comment reply = iter.next();
					iter.remove();
					this.commentService.delete(reply);
				}
			}
			this.commentService.delete(c);
		}

		this.articleService.delete(article);

		this.authenticate(null);
	}

	// NEGATIVE: Delete article that doesn't exist
	@Test(expected = AssertionError.class)
	public void deleteArticleNegativeInvalidArticle() {
		this.authenticate("editor1");

		final Article article = this.articleService.findOne(this.getEntityId("article10"));

		final Collection<ArticleRating> articleRatings = new ArrayList<ArticleRating>(article.getArticleRatings());
		final Collection<Comment> comments = new ArrayList<Comment>(this.commentService.findAllParentsByArticle(article.getId()));

		for (final ArticleRating ar : articleRatings)
			this.articleRatingService.delete(ar);

		for (final Comment c : comments) {
			if (!c.getReplies().isEmpty()) {
				final java.util.Iterator<Comment> iter = c.getReplies().iterator();
				while (iter.hasNext()) {
					final Comment reply = iter.next();
					iter.remove();
					this.commentService.delete(reply);
				}
			}
			this.commentService.delete(c);
		}

		this.articleService.delete(article);

		this.authenticate(null);
	}

	// NEGATIVE: Delete article as user -> AHORA MISMO NO FUNCIONA PORQUE NO HAY ARTICULOS CREADOS
	@Test(expected = IllegalArgumentException.class)
	public void deleteArticleNegativeAsUser() {
		this.authenticate("user1");
		this.setUpAuth("user1");

		final Article article = this.articleService.findOne(this.getEntityId("article1"));

		final Collection<ArticleRating> articleRatings = new ArrayList<ArticleRating>(article.getArticleRatings());
		final Collection<Comment> comments = new ArrayList<Comment>(this.commentService.findAllParentsByArticle(article.getId()));

		for (final ArticleRating ar : articleRatings)
			this.articleRatingService.delete(ar);

		for (final Comment c : comments) {
			if (!c.getReplies().isEmpty()) {
				final java.util.Iterator<Comment> iter = c.getReplies().iterator();
				while (iter.hasNext()) {
					final Comment reply = iter.next();
					iter.remove();
					this.commentService.delete(reply);
				}
			}
			this.commentService.delete(c);
		}

		this.articleService.delete(article);

		this.authenticate(null);
	}

	// Write article
	// ***********************************************************************************************************
	@Test
	public void driverWriteArticle() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"editor1", "Test title", "Test text", null
			},
			// NEGATIVE - The actor is not an editor
			{
				"manager1", "Test title", "Test text", IllegalArgumentException.class
			},
			// NEGATIVE - The title is blank
			{
				"editor1", "", "Test body", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateWriteArticle((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateWriteArticle(final String username, final String title, final String text, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Editor principal = this.editorService.findByPrincipal();

			final Article article = this.articleService.create();
			article.setTitle(title);
			article.setText(text);
			article.setDraft(true);
			article.setPublicationDate(null);
			article.setEditor(principal);
			article.setArticleRatings(new HashSet<ArticleRating>());
			article.setComments(new HashSet<Comment>());

			final Article stored = this.articleService.save(article);
			this.articleService.flush();

			Assert.notNull(this.articleService.findOne(stored.getId()));
			Assert.isTrue(this.articleService.findOne(stored.getId()).getEditor().equals(principal));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Edit article
	// ***********************************************************************************************************
	@Test
	public void driverEditArticle() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"editor1", "article2", "Test title", "Test text", null
			},
			// NEGATIVE - The article is not in draft mode
			{
				"editor1", "article1", "Test title", "Test text", IllegalArgumentException.class
			},
			// NEGATIVE - The title is blank
			{
				"editor1", "article2", "", "Test text", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditArticle((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateEditArticle(final String username, final String entity, final String title, final String text, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);

			final List<Article> articles = new ArrayList<Article>(this.articleService.findAll());

			// First, obtain the selected article to be tested
			final int articleStoredId = super.getEntityId(entity);
			final Article articleStored = this.articleService.findOne(articleStoredId);
			// Then, obtain it from the list of all articles (simulating
			// that the user select it in the view of list articles)
			final int articleId = articles.indexOf(articleStored);
			final int articleInListId = articles.get(articleId).getId();
			// Finally, obtain it by findOneToEdit in order to check that is not
			// in final mode or deleted
			final Article article = this.articleService.findOneToEdit(articleInListId);

			article.setTitle(title);
			article.setText(text);
			article.setPublicationDate(null);

			final Article stored = this.articleService.save(article);
			this.articleService.flush();

			Assert.notNull(this.articleService.findOne(stored.getId()));
			Assert.isTrue(this.articleService.findOne(stored.getId()).getTitle().equals(title));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
