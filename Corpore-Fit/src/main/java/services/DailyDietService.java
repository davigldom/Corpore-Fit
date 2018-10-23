package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.DailyDietRepository;
import domain.DailyDiet;
import domain.DayName;
import domain.Food;
import domain.Nutritionist;

@Service
@Transactional
public class DailyDietService {

	@Autowired
	private DailyDietRepository dayRepository;

	@Autowired
	private NutritionistService nutritionistService;

	public DailyDiet create() {
		final DailyDiet result;

		result = new DailyDiet();
		result.setFoods(new ArrayList<Food>());

		return result;
	}

	public Nutritionist findCreator(int dayId) {
		return this.dayRepository.findCreator(dayId);
	}

	public DailyDiet findOne(final int dayId) {
		DailyDiet result;
		result = this.dayRepository.findOne(dayId);
		Assert.notNull(result);
		return result;
	}

	public DailyDiet findOneToEdit(final int dayId) {
		DailyDiet result;
		result = this.findOne(dayId);
		Assert.isTrue(this.nutritionistService.findByPrincipal().equals(
				this.findCreator(result.getId())));

		return result;
	}

	public Collection<DailyDiet> findAll() {
		return this.dayRepository.findAll();
	}

	public DailyDiet save(final DailyDiet day) {
		DailyDiet result;

		Assert.notNull(day);
		final Nutritionist principal = this.nutritionistService.findByPrincipal();
		if (day.getId() != 0)
			Assert.isTrue(principal.equals(this.findCreator(day.getId())));

		result = this.dayRepository.save(day);
		return result;
	}

	public void delete(final DailyDiet day) {
		Assert.isTrue(this.nutritionistService.findByPrincipal().equals(
				this.findCreator(day.getId())));

		this.dayRepository.delete(day);

	}
	
	public DailyDiet findCurrentDailyDiet(int dietId) {
		DailyDiet result = null;
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);

		switch (day) {

		case 1:
			// Current day is Monday
			result = this.dayRepository.findByNameAndNutritionist(dietId,
					DayName.SUNDAY);
			break;

		case 2:
			result = this.dayRepository.findByNameAndNutritionist(dietId,
					DayName.MONDAY);
			break;

		case 3:
			result = this.dayRepository.findByNameAndNutritionist(dietId,
					(DayName.TUESDAY));
			break;

		case 4:
			result = this.dayRepository.findByNameAndNutritionist(dietId,
					DayName.WEDNESDAY);
			break;

		case 5:
			result = this.dayRepository.findByNameAndNutritionist(dietId,
					(DayName.THURSDAY));
			break;

		case 6:
			result = this.dayRepository.findByNameAndNutritionist(dietId,
					(DayName.FRIDAY));
			break;

		case 7:
			result = this.dayRepository.findByNameAndNutritionist(dietId,
					(DayName.SATURDAY));
			break;
		}

		return result;
	}

	// Reconstruct

	@Autowired
	private Validator validator;

	public DailyDiet reconstruct(final DailyDiet day, final BindingResult binding) {
		DailyDiet dayStored;

		if (day.getId() != 0) {
			dayStored = this.dayRepository.findOne(day.getId());

			day.setId(dayStored.getId());
			day.setVersion(dayStored.getVersion());
			day.setFoods(dayStored.getFoods());
		} else {
			day.setFoods(new ArrayList<Food>());
		}

		this.validator.validate(day, binding);
		return day;

	}

}
