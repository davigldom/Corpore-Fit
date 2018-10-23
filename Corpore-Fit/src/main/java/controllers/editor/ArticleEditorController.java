
package controllers.editor;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleRatingService;
import services.ArticleService;
import services.CommentService;
import services.EditorService;
import controllers.AbstractController;
import domain.Article;
import domain.ArticleRating;
import domain.Comment;
import domain.Editor;

@Controller
@RequestMapping("/article/editor")
public class ArticleEditorController extends AbstractController {

	@Autowired
	private EditorService			editorService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private ArticleRatingService	articleRatingService;

	@Autowired
	private CommentService			commentService;


	public ArticleEditorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Article> articles;

		final Editor principal = this.editorService.findByPrincipal();

		articles = this.articleService.findAllByEditor(principal.getId());

		result = new ModelAndView("article/listCreated");
		result.addObject("articles", articles);
		result.addObject("requestURI", "article/editor/list.do");
		result.addObject("isListingCreated", true);
		result.addObject("principal", principal);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Article article;
		article = this.articleService.create();
		result = this.createEditModelAndView(article);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int articleId) {
		ModelAndView result;
		Article article;

		article = this.articleService.findOneToEdit(articleId);
		Assert.notNull(article);
		result = this.createEditModelAndView(article);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Article article, final BindingResult binding) {
		ModelAndView result;
		if (article.getId() != 0) {
			final Article storedArticle = this.articleService.findOne(article.getId());
			Assert.isTrue(storedArticle.isDraft());
			Assert.isNull(storedArticle.getPublicationDate());
		}

		article = this.articleService.reconstruct(article, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(article);
		else
			try {
				final Article articleSaved = this.articleService.save(article);
				result = new ModelAndView("redirect:/article/display.do?articleId=" + articleSaved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(article, "article.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int articleId) {
		ModelAndView result;

		final Article article = this.articleService.findOne(articleId);

		Assert.isTrue(article.getEditor().equals(this.editorService.findByPrincipal()));

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

			result = this.list();
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(article, "article.commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ModelAndView publish(@RequestParam final int articleId) {
		ModelAndView result;

		try {
			this.articleService.publish(articleId);
			result = new ModelAndView("redirect:/article/list.do");
			result.addObject("message", "article.publish.ok");
		} catch (final Throwable oops) {
			result = this.list();
			result.addObject("message", "article.publish.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Article article) {
		ModelAndView result;

		result = this.createEditModelAndView(article, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Article article, final String message) {
		ModelAndView result = null;

		if (article.getId() != 0)
			result = new ModelAndView("article/edit");
		else if (article.getId() == 0)
			result = new ModelAndView("article/create");

		result.addObject("article", article);

		result.addObject("message", message);

		return result;
	}

}
