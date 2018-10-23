
package controllers.editor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleRatingService;
import services.ArticleService;
import services.EditorService;
import controllers.AbstractController;
import domain.Article;
import domain.ArticleRating;
import domain.Editor;

@Controller
@RequestMapping("/actor/editor")
public class ActorEditorController extends AbstractController {

	@Autowired
	private EditorService			editorService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private ArticleRatingService	articleRatingService;


	//Edit Editor
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actor") Editor editor, final BindingResult binding) {
		ModelAndView result;
		editor = this.editorService.reconstruct(editor, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(editor);
		else
			try {
				this.editorService.save(editor);
				result = new ModelAndView("redirect:/actor/display-principal.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(editor, "actor.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int actorId) {
		ModelAndView result;
		final Editor editor = this.editorService.findOne(actorId);
		Assert.isTrue(this.editorService.findByPrincipal().equals(editor));

		final List<Article> articles = new ArrayList<Article>(this.articleService.findAllByEditor(editor.getId()));
		List<ArticleRating> articleRatings = null;
		Hibernate.initialize(editor.getUserAccount().getAuthorities());

		try {
			for (final Article a : articles) {
				articleRatings = new ArrayList<ArticleRating>(a.getArticleRatings());
				for (final ArticleRating ar : articleRatings)
					this.articleRatingService.delete(ar);
				this.articleService.delete(a);
			}
			this.editorService.delete(editor);
			result = new ModelAndView("redirect:../../j_spring_security_logout");
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

	protected ModelAndView createEditModelAndView(final Editor editor, final String message) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject(editor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase(), editor);
		result.addObject("authority", editor.getUserAccount().getAuthorities().toArray()[0].toString().toLowerCase());
		result.addObject("message", message);
		result.addObject("actor", editor);

		return result;
	}
}
