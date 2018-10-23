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

import repositories.DayRepository;
import domain.Day;
import domain.DayName;
import domain.Exercise;
import domain.User;

@Service
@Transactional
public class DayService {

	@Autowired
	private DayRepository dayRepository;

	@Autowired
	private UserService userService;

	public Day create() {
		final Day result;

		result = new Day();
		result.setExercises(new ArrayList<Exercise>());

		return result;
	}

	public User findCreator(int dayId) {
		return this.dayRepository.findCreator(dayId);
	}

	public Day findOne(final int dayId) {
		Day result;
		result = this.dayRepository.findOne(dayId);
		Assert.notNull(result);
		return result;
	}

	public Day findOneToEdit(final int dayId) {
		Day result;
		result = this.findOne(dayId);
		Assert.isTrue(this.userService.findByPrincipal().equals(
				this.findCreator(result.getId())));

		return result;
	}

	public Collection<Day> findAll() {
		return this.dayRepository.findAll();
	}

	public Day save(final Day day) {
		Day result;

		Assert.notNull(day);
		final User principal = this.userService.findByPrincipal();
		if (day.getId() != 0)
			Assert.isTrue(principal.equals(this.findCreator(day.getId())));

		result = this.dayRepository.save(day);
		return result;
	}

	public void delete(final Day day) {
		Assert.isTrue(this.userService.findByPrincipal().equals(
				this.findCreator(day.getId())));

		this.dayRepository.delete(day);

	}

	public Day findCurrentDay(int userId) {
		Day result = null;
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);

		switch (day) {

		case 1:
			// Current day is Monday
			result = this.dayRepository.findByNameAndUser(userId,
					DayName.SUNDAY);
			break;

		case 2:
			result = this.dayRepository.findByNameAndUser(userId,
					DayName.MONDAY);
			break;

		case 3:
			result = this.dayRepository.findByNameAndUser(userId,
					(DayName.TUESDAY));
			break;

		case 4:
			result = this.dayRepository.findByNameAndUser(userId,
					DayName.WEDNESDAY);
			break;

		case 5:
			result = this.dayRepository.findByNameAndUser(userId,
					(DayName.THURSDAY));
			break;

		case 6:
			result = this.dayRepository.findByNameAndUser(userId,
					(DayName.FRIDAY));
			break;

		case 7:
			result = this.dayRepository.findByNameAndUser(userId,
					(DayName.SATURDAY));
			break;
		}

		return result;
	}

	// Reconstruct

	@Autowired
	private Validator validator;

	public Day reconstruct(final Day day, final BindingResult binding) {
		Day dayStored;

		if (day.getId() != 0) {
			dayStored = this.dayRepository.findOne(day.getId());

			day.setId(dayStored.getId());
			day.setVersion(dayStored.getVersion());
			day.setExercises(dayStored.getExercises());
		} else {
			day.setExercises(new ArrayList<Exercise>());
		}

		this.validator.validate(day, binding);
		return day;

	}

}
