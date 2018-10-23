
package controllers.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ArticleRatingService;
import services.ArticleService;
import services.CommentService;
import controllers.AbstractController;
import domain.Actor;
import domain.Article;
import domain.ArticleRating;
import domain.Comment;

@Controller
@RequestMapping("/article/admin")
public class ArticleAdministratorController extends AbstractController {

	@Autowired
	private CommentService			commentService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private ArticleRatingService	articleRatingService;

	@Autowired
	private ActorService			actorService;


	// Constructors -----------------------------------------------------------

	public ArticleAdministratorController() {
		super();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int articleId) {
		ModelAndView result;

		final Article article = this.articleService.findOne(articleId);

		try {

			final Collection<ArticleRating> articleRatings = new ArrayList<ArticleRating>(article.getArticleRatings());
			final Collection<Comment> comments = new ArrayList<Comment>(this.commentService.findAllParentsByArticle(articleId));

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

			result = new ModelAndView("redirect:/article/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(article, "article.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Article article) {
		ModelAndView result;

		result = this.createEditModelAndView(article, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Article article, final String message) {
		final ModelAndView result;
		String momentFormatted = null;
		Actor principal = null;
		final Collection<ArticleRating> arActor;
		final Collection<ArticleRating> arArticle;
		boolean alreadyRated = false;
		double averageRating = 0.0;

		article = this.articleService.findOne(article.getId());

		if (this.actorService.isAuthenticated()) {
			principal = this.actorService.findByPrincipal();
			arActor = new ArrayList<ArticleRating>(principal.getArticleRatings());
			arArticle = new ArrayList<ArticleRating>(article.getArticleRatings());
			for (final ArticleRating a : arActor)
				if (arArticle.contains(a))
					alreadyRated = true;

			if (alreadyRated) {
				double allRatings = 0.0;
				for (final ArticleRating a : arArticle) {
					allRatings += a.getRating();
					averageRating = allRatings / arArticle.size();
				}
			}
		}

		if (article.getPublicationDate() == null)
			Assert.isTrue(article.getEditor().equals(principal));

		Assert.notNull(article);
		result = new ModelAndView("article/display");

		if (article.getPublicationDate() != null) {
			final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			momentFormatted = formatter.format(article.getPublicationDate().getTime());
		}

		final Collection<Comment> comments = this.commentService.findAllParentsByArticle(article.getId());

		final Comment comment = this.commentService.create();

		result.addObject("principal", principal);
		result.addObject("momentFormated", momentFormatted);
		result.addObject("article", article);
		result.addObject("articleId", article.getId());
		result.addObject("displayArticle", true);
		result.addObject("alreadyRated", alreadyRated);
		result.addObject("averageRating", averageRating);
		result.addObject("comments", comments);
		result.addObject("comment", comment);
		if (this.actorService.isAuthenticated())
			result.addObject("principal", principal);

		result.addObject("message", message);

		return result;
	}

}
