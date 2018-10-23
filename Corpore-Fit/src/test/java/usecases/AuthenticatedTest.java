
package usecases;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.ArticleRatingService;
import services.ArticleService;
import services.CommentService;
import services.UserService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Article;
import domain.ArticleRating;
import domain.Comment;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AuthenticatedTest extends AbstractTest {

	// System under test

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private ArticleRatingService	articleRatingService;

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CommentService			commentService;

	DecimalFormat					df	= new DecimalFormat("#.##");	// To get only 2 decimals


	// ------------------------------------------------------ TESTS
	// ------------------------------------------------------------------

	// Rate article
	// ***********************************************************************************************************
	// POSITIVE: Rate article
	@Test
	public void rateArticlePositive() {
		this.authenticate("user2");

		final Article article = this.articleService.findOne(this.getEntityId("article1"));
		final ArticleRating ar = this.articleRatingService.create();
		ar.setRating(2);
		this.articleRatingService.save(ar);
		this.articleService.saveRating(article, ar);

		this.authenticate(null);
	}

	// NEGATIVE: Rate article already rated
	@Test(expected = IllegalArgumentException.class)
	public void rateArticleNegativeAlreadyRated() {
		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();
		final Article article = this.articleService.findOne(this.getEntityId("article1"));
		final ArticleRating ar = this.articleRatingService.create();
		ar.setRating(2);

		final Collection<ArticleRating> arActor = new ArrayList<ArticleRating>(principal.getArticleRatings());
		final Collection<ArticleRating> arArticle = new ArrayList<ArticleRating>(article.getArticleRatings());
		boolean alreadyRated = false;

		for (final ArticleRating a : arActor)
			if (arArticle.contains(a))
				alreadyRated = true;

		Assert.isTrue(alreadyRated);

		this.articleRatingService.save(ar);
		this.articleService.saveRating(article, ar);

		this.authenticate(null);
	}

	// NEGATIVE: Rate article without being authenticated
	@Test(expected = IllegalArgumentException.class)
	public void rateArticleNegativeNotAuthenticated() {
		SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("GUEST", "USERNAME", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));

		final Article article = this.articleService.findOne(this.getEntityId("article1"));
		final ArticleRating ar = this.articleRatingService.create();
		ar.setRating(2);
		this.articleRatingService.save(ar);
		this.articleService.saveRating(article, ar);

	}

	// Comment article
	// ***********************************************************************************************************
	@Test
	public void driverCommentArticle() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", "article1", "Comment to test", null
			},
			// NEGATIVE - Non authenticated
			{
				null, "article1", "Comment to test", IllegalArgumentException.class
			},
			// NEGATIVE - The article is not published and you are not the editor
			{
				"user1", "article3", "Comment to test", AssertionError.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCommentArticle((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateCommentArticle(final String username, final String entity, final String text, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Actor principal = this.actorService.findByPrincipal();

			final Calendar now = Calendar.getInstance();
			now.setTime(new Date());

			final int articleId = this.getEntityId(entity);
			final Article article = this.articleService.findOne(articleId);

			final Comment comment = this.commentService.create();
			comment.setText(text);
			comment.setMoment(now);
			comment.setOwner(principal);
			comment.setParent(null);
			comment.setReplies(new HashSet<Comment>());

			final Comment stored = this.commentService.save(comment, article);
			this.commentService.flush();

			Assert.notNull(this.commentService.findOne(stored.getId()));
			Assert.isTrue(article.getComments().contains(stored));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
