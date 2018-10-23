
package controllers;

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
import domain.Actor;
import domain.Article;
import domain.ArticleRating;
import domain.Comment;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ArticleRatingService	articleRatingService;

	@Autowired
	private CommentService			commentService;


	public ArticleController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Article> articles;

		articles = this.articleService.findAllPublished();

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("isListingCreated", false);
		result.addObject("requestURI", "article/list.do");

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		Article article;
		String momentFormatted = null;
		Actor principal = null;
		final Collection<ArticleRating> arActor;
		final Collection<ArticleRating> arArticle;
		boolean alreadyRated = false;
		double averageRating = 0.0;

		article = this.articleService.findOne(articleId);

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

		final Collection<Comment> comments = this.commentService.findAllParentsByArticle(articleId);

		final Comment comment = this.commentService.create();

		result.addObject("principal", principal);
		result.addObject("momentFormated", momentFormatted);
		result.addObject("article", article);
		result.addObject("articleId", articleId);
		result.addObject("displayArticle", true);
		result.addObject("alreadyRated", alreadyRated);
		result.addObject("averageRating", averageRating);
		result.addObject("comments", comments);
		result.addObject("comment", comment);
		if (this.actorService.isAuthenticated())
			result.addObject("principal", principal);

		return result;
	}

	@RequestMapping(value = "/search-word", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam final String keyword) {
		ModelAndView result;

		final Collection<Article> articles = this.articleService.findByKeyword(keyword);

		result = new ModelAndView("article/search");
		result.addObject("articles", articles);
		result.addObject("requestURI", "article/search-word.do");
		result.addObject("keyword", keyword);
		result.addObject("isListingCreated", false);
		result.addObject("searchArticle", true);

		return result;
	}

	@RequestMapping(value = "/rate", method = RequestMethod.GET)
	public ModelAndView rate(@RequestParam final int articleId, final int rating) {
		ModelAndView result;
		final Article article = this.articleService.findOne(articleId);
		final Actor principal = this.actorService.findByPrincipal();

		final ArticleRating ar = this.articleRatingService.create();
		ar.setRating(rating);

		final Collection<ArticleRating> arActor = new ArrayList<ArticleRating>(principal.getArticleRatings());
		final Collection<ArticleRating> arArticle = new ArrayList<ArticleRating>(article.getArticleRatings());
		boolean alreadyRated = false;

		for (final ArticleRating a : arActor)
			if (arArticle.contains(a))
				alreadyRated = true;

		Assert.isTrue(!alreadyRated);

		final ArticleRating saved = this.articleRatingService.save(ar);

		this.articleService.saveRating(article, saved);

		this.actorService.saveRating(principal, saved);

		result = new ModelAndView("redirect:/article/display.do?articleId=" + articleId);

		return result;
	}

}
