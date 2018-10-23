package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ExerciseRepository;
import domain.Day;
import domain.Exercise;
import domain.User;

@Service
@Transactional
public class ExerciseService {

	@Autowired
	private ExerciseRepository exerciseRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private DayService dayService;

	public Exercise create() {
		final Exercise result;

		result = new Exercise();

		return result;
	}

	public Exercise findOne(final int exerciseId) {
		Exercise result;
		result = this.exerciseRepository.findOne(exerciseId);
		Assert.notNull(result);
		return result;
	}

	public Exercise findOneToEdit(final int exerciseId) {
		Exercise result;
		result = this.findOne(exerciseId);
		Assert.isTrue(this.userService.findByPrincipal().equals(
				this.dayService.findCreator(this.findDay(result.getId())
						.getId())));

		return result;
	}

	public Collection<Exercise> findAll() {
		return this.exerciseRepository.findAll();
	}

	public Exercise save(final Exercise exercise) {
		Exercise result;

		Assert.notNull(exercise);
		final User principal = this.userService.findByPrincipal();
		if (exercise.getId() != 0)
			Assert.isTrue(principal.equals(this.dayService.findCreator(this
					.findDay(exercise.getId()).getId())));

		result = this.exerciseRepository.save(exercise);
		return result;
	}

	public void delete(final Exercise exercise) {

		Assert.isTrue(this.userService.findByPrincipal().equals(
				this.dayService.findCreator(this.findDay(exercise.getId())
						.getId())));
		this.findDay(exercise.getId()).getExercises().remove(exercise);

		this.exerciseRepository.delete(exercise);

	}

	public Day findDay(int exerciseId) {
		return this.exerciseRepository.findDay(exerciseId);
	}

	// Reconstruct

	@Autowired
	private Validator validator;

	public Exercise reconstruct(final Exercise exercise,
			final BindingResult binding) {

		this.validator.validate(exercise, binding);
		return exercise;

	}

}
