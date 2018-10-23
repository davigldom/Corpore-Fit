package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.DietRepository;
import domain.DailyDiet;
import domain.DayName;
import domain.Diet;
import domain.Nutritionist;
import domain.User;

@Service
@Transactional
public class DietService {

	@Autowired
	private DietRepository dietRepository;

	@Autowired
	private NutritionistService nutritionistService;

	@Autowired
	private DailyDietService dayService;

	@Autowired
	private Validator validator;

	public Diet create() {

		final Nutritionist nutritionist = this.nutritionistService
				.findByPrincipal();
		Assert.notNull(nutritionist);
		final Diet result = new Diet();
		result.setNutritionist(nutritionist);

		return result;
	}

	public Diet save(final Diet diet) {
		Assert.notNull(diet);

		// DailyDiet m=dailyDietService.save(monday,dietSaved);
		// DailyDiet t=dailyDietService.save(tuesday,dietSaved);
		// DailyDiet w=dailyDietService.save(wednesday,dietSaved);
		// DailyDiet th=dailyDietService.save(thursday,dietSaved);
		// DailyDiet f=dailyDietService.save(friday,dietSaved);
		// DailyDiet sat=dailyDietService.save(saturday,dietSaved);
		// DailyDiet s=dailyDietService.save(sunday,dietSaved);

		// DailyDiet monday= new DailyDiet();
		// DailyDiet tuesday= new DailyDiet();
		// DailyDiet wednesday= new DailyDiet();
		// DailyDiet thursday= new DailyDiet();
		// DailyDiet friday= new DailyDiet();
		// DailyDiet saturday= new DailyDiet();
		// DailyDiet sunday= new DailyDiet();
		// monday.setDay(DayName.MONDAY);
		// tuesday.setDay(DayName.TUESDAY);
		// wednesday.setDay(DayName.WEDNESDAY);
		// thursday.setDay(DayName.THURSDAY);
		// friday.setDay(DayName.FRIDAY);
		// saturday.setDay(DayName.SATURDAY);
		// sunday.setDay(DayName.SUNDAY);
		// Collection<DailyDiet> days= new ArrayList<DailyDiet>();
		//
		// days.add(monday);
		// days.add(tuesday);
		// days.add(wednesday);
		// days.add(thursday);
		// days.add(friday);
		// days.add(saturday);
		// days.add(sunday);
		//
		// diet.setDailyDiet(days);
		Assert.isTrue(this.nutritionistService.findByPrincipal().isValidated(),"Nutritionist not validated");
		Assert.isTrue(this.nutritionistService.findByPrincipal().equals(diet.getNutritionist()));

		final Diet dietSaved = this.dietRepository.save(diet);
		return dietSaved;
	}

	public Diet reconstruct(final Diet diet, final BindingResult binding) {
		Diet dietStored;

		if (diet.getId() != 0) {
			dietStored = this.dietRepository.findOne(diet.getId());

			diet.setId(dietStored.getId());
			diet.setVersion(dietStored.getVersion());
			diet.setNutritionist(dietStored.getNutritionist());
			diet.setDailyDiets(dietStored.getDailyDiets());
		} else {
			diet.setDailyDiets(new ArrayList<DailyDiet>());

			DailyDiet monday = this.dayService.create();
			monday.setDay(DayName.MONDAY);
			diet.getDailyDiets().add(monday);

			DailyDiet tuesday = this.dayService.create();
			tuesday.setDay(DayName.TUESDAY);
			diet.getDailyDiets().add(tuesday);

			DailyDiet wednesday = this.dayService.create();
			wednesday.setDay(DayName.WEDNESDAY);
			diet.getDailyDiets().add(wednesday);

			DailyDiet thursday = this.dayService.create();
			thursday.setDay(DayName.THURSDAY);
			diet.getDailyDiets().add(thursday);

			DailyDiet friday = this.dayService.create();
			friday.setDay(DayName.FRIDAY);
			diet.getDailyDiets().add(friday);

			DailyDiet saturday = this.dayService.create();
			saturday.setDay(DayName.SATURDAY);
			diet.getDailyDiets().add(saturday);

			DailyDiet sunday = this.dayService.create();
			sunday.setDay(DayName.SUNDAY);
			diet.getDailyDiets().add(sunday);

			diet.setNutritionist(this.nutritionistService.findByPrincipal());
		}
		
		this.validator.validate(diet, binding);

		return diet;

	}

	public Diet findOne(final int dietId) {
		Diet result;
		result = this.dietRepository.findOne(dietId);
		Assert.notNull(result);
		return result;
	}

	public Diet findOneToEdit(final int dietId) {
		Diet result;
		result = this.dietRepository.findOne(dietId);
		Assert.notNull(result);
		Assert.isTrue(result.getNutritionist().equals(
				this.nutritionistService.findByPrincipal()));

		return result;
	}

	public void delete(Diet diet) {
		Assert.isTrue(diet.getNutritionist().equals(
				this.nutritionistService.findByPrincipal()));
		this.nutritionistService.findByPrincipal().getDiets().remove(diet);
		this.dietRepository.delete(diet);
	}

	
	public Collection<User> getFollowers(int dietId){
		return this.dietRepository.getFollowers(dietId);
	}
	
}
