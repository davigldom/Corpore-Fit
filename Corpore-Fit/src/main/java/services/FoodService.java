
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FoodRepository;
import domain.DailyDiet;
import domain.Diet;
import domain.Food;
import domain.Nutritionist;

@Service
@Transactional
public class FoodService {

	@Autowired
	private FoodRepository		foodRepository;

	@Autowired
	private NutritionistService	nutritionistService;

	@Autowired
	private DailyDietService	dayService;


	public Food create() {
		final Food result;

		result = new Food();

		return result;
	}

	public Food findOne(final int foodId) {
		Food result;
		result = this.foodRepository.findOne(foodId);
		Assert.notNull(result);
		return result;
	}

	public Food findOneToEdit(final int foodId) {
		Food result;
		result = this.findOne(foodId);
		Assert.isTrue(this.nutritionistService.findByPrincipal().equals(this.dayService.findCreator(this.findDailyDiet(result.getId()).getId())));

		return result;
	}

	public Collection<Food> findAll() {
		return this.foodRepository.findAll();
	}

	public Food save(final Food food) {
		Food result;

		Assert.notNull(food);
		final Nutritionist principal = this.nutritionistService.findByPrincipal();
		if (food.getId() != 0)
			Assert.isTrue(principal.equals(this.dayService.findCreator(this.findDailyDiet(food.getId()).getId())));

		result = this.foodRepository.save(food);
		return result;
	}

	public void delete(final Food food) {

		Assert.isTrue(this.nutritionistService.findByPrincipal().equals(this.dayService.findCreator(this.findDailyDiet(food.getId()).getId())));
		this.findDailyDiet(food.getId()).getFoods().remove(food);

		this.foodRepository.delete(food);

	}

	public DailyDiet findDailyDiet(final int foodId) {
		return this.foodRepository.findDailyDiet(foodId);
	}

	public Diet findDiet(final int foodId) {
		return this.foodRepository.findDiet(foodId);
	}

	public void flush() {
		this.foodRepository.flush();
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Food reconstruct(final Food food, final BindingResult binding) {

		this.validator.validate(food, binding);
		return food;

	}

}
