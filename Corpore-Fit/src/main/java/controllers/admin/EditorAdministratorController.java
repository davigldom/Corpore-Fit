
package controllers.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import services.EditorService;
import controllers.AbstractController;
import domain.Article;
import domain.ArticleRating;
import domain.Editor;
import domain.Exercise;

@Controller
@RequestMapping("/editor/admin")
public class EditorAdministratorController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private EditorService			editorService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private ArticleRatingService	articleRatingService;


	// Constructors -----------------------------------------------------------

	public EditorAdministratorController() {
		super();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int actorId) {
		ModelAndView result;
		final Editor editor = this.editorService.findOne(actorId);
		final List<Article> articles = new ArrayList<Article>(this.articleService.findAllByEditor(editor.getId()));
		List<ArticleRating> articleRatings = null;

		try {
			for (final Article a : articles) {
				articleRatings = new ArrayList<ArticleRating>(a.getArticleRatings());
				for (final ArticleRating ar : articleRatings)
					this.articleRatingService.delete(ar);
				this.articleService.delete(a);
			}
			this.editorService.delete(editor);
			result = new ModelAndView("redirect:/actor/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(editor, "actor.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Editor editor) {
		ModelAndView result;

		result = this.createEditModelAndView(editor, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Editor editor, final String message) {
		ModelAndView result;
		final boolean isFriend = false;
		final boolean existsRequest = false;
		final boolean isValid = true;
		final Collection<Exercise> exercisesOfTheDay = null;
		final int editorId = editor.getId();

		editor = this.editorService.findOne(editorId);
		Assert.notNull(editor);

		if (editor.isBanned())
			Assert.isTrue(this.actorService.isAdmin());

		result = new ModelAndView("actor/display");
		result.addObject("actor", editor);
		result.addObject("isFriend", isFriend);
		result.addObject("existsRequest", existsRequest);
		result.addObject("isValid", isValid);
		result.addObject("exercisesOfTheDay", exercisesOfTheDay);
		result.addObject("authority", editor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());

		result.addObject("message", message);

		return result;
	}

}
