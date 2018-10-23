
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NutritionistService;
import controllers.AbstractController;
import domain.Nutritionist;

@Controller
@RequestMapping("/nutritionist/admin")
public class NutritionistAdministratorController extends AbstractController {

	@Autowired
	private NutritionistService	nutritionistService;


	// Constructors -----------------------------------------------------------

	public NutritionistAdministratorController() {
		super();
	}

	// List not validated
	@RequestMapping(value = "/list-not-validated", method = RequestMethod.GET)
	public ModelAndView listNotValidatedNutritionists() {
		final ModelAndView result;

		final Collection<Nutritionist> notValidated = this.nutritionistService.findNotValidated();

		result = new ModelAndView("nutritionist/list");
		result.addObject("nutritionists", notValidated);
		result.addObject("requestURI", "/nutritionist/admin/list-not-validated.do");

		return result;
	}

	// List validated
	@RequestMapping(value = "/list-validated", method = RequestMethod.GET)
	public ModelAndView listValidatedNutritionists() {
		final ModelAndView result;

		final Collection<Nutritionist> validated = this.nutritionistService.findValidated();

		result = new ModelAndView("nutritionist/list");
		result.addObject("nutritionists", validated);
		result.addObject("requestURI", "/nutritionist/admin/list-not-validated.do");

		return result;
	}

	// Validate nutritionist
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	public ModelAndView validateNutritionist(@RequestParam final int nutritionistId) {
		final ModelAndView result;
		final Nutritionist nutritionist = this.nutritionistService.validate(nutritionistId);

		Assert.isTrue(nutritionist.isValidated());

		result = new ModelAndView("nutritionist/list");
		result.addObject("nutritionists", this.nutritionistService.findNotValidated());
		result.addObject("requestURI", "/nutritionist/admin/list-validated.do");

		return result;
	}
}
