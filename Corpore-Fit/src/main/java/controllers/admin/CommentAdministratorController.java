
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
import services.ArticleService;
import services.CommentService;
import controllers.AbstractController;
import domain.Actor;
import domain.Article;
import domain.ArticleRating;
import domain.Comment;

@Controller
@RequestMapping("/comment/admin")
public class CommentAdministratorController extends AbstractController {

	@Autowired
	private CommentService	commentService;

	@Autowired
	private ArticleService	articleService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public CommentAdministratorController() {
		super();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int commentId) {
		ModelAndView result;
		final Comment comment = this.commentService.findOne(commentId);
		final int articleId = this.articleService.findByComment(comment).getId();
		final java.util.Iterator<Comment> iter = comment.getReplies().iterator();

		try {
			while (iter.hasNext()) {
				final Comment reply = iter.next();
				iter.remove();
				this.commentService.delete(reply);
			}
			this.commentService.delete(comment);
			result = new ModelAndView("redirect:/article/display.do?articleId=" + articleId + "#aComments");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(comment, articleId, "article.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comment comment, final int articleId) {
		ModelAndView result;

		result = this.createEditModelAndView(comment, articleId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comment comment, final int articleId, final String message) {
		final ModelAndView result;
		String momentFormatted = null;
		Actor principal = null;
		Collection<ArticleRating> arActor;
		Collection<ArticleRating> arArticle;
		boolean alreadyRated = false;
		double averageRating = 0.0;

		final Article article = this.articleService.findOne(articleId);

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

		result.addObject("principal", principal);
		result.addObject("momentFormated", momentFormatted);
		result.addObject("article", article);
		result.addObject("articleId", article.getId());
		result.addObject("displayArticle", true);
		result.addObject("alreadyRated", alreadyRated);
		result.addObject("averageRating", averageRating);
		result.addObject("comments", comments);
		result.addObject("comment", comment);
		result.addObject("message", message);

		return result;
	}
}
