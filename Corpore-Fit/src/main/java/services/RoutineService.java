package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RoutineRepository;
import domain.Day;
import domain.DayName;
import domain.Routine;
import domain.User;

@Service
@Transactional
public class RoutineService {

	@Autowired
	private RoutineRepository routineRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private DayService dayService;

	public Routine create() {
		final Routine result;

		result = new Routine();


		return result;
	}

	public Routine findOne(final int routineId) {
		Routine result;
		result = this.routineRepository.findOne(routineId);
		Assert.notNull(result);
		return result;
	}

	public Routine findOneToEdit(final int routineId) {
		Routine result;
		result = this.findOne(routineId);
		Assert.isTrue(this.userService.findByPrincipal().equals(
				result.getUser()));

		return result;
	}

	public Collection<Routine> findAll() {
		return this.routineRepository.findAll();
	}

	public Routine save(final Routine routine) {
		Routine result;

		Assert.notNull(routine);
		final User principal = this.userService.findByPrincipal();
		Assert.isTrue(principal.equals(routine.getUser()));
		Assert.isTrue(routine.getDays().size()==7);
		
		result = this.routineRepository.save(routine);
		return result;
	}

	public void delete(final Routine routine) {
		Assert.isTrue(this.userService.findByPrincipal().equals(
				routine.getUser()));

		this.routineRepository.delete(routine);

		Assert.isTrue(!this.routineRepository.findAll().contains(routine));

	}

	// Reconstruct

	@Autowired
	private Validator validator;

	public Routine reconstruct(final Routine routine,
			final BindingResult binding) {
		Routine routineStored;

		if (routine.getId() != 0) {
			routineStored = this.routineRepository.findOne(routine.getId());

			routine.setId(routineStored.getId());
			routine.setVersion(routineStored.getVersion());
			routine.setUser(routineStored.getUser());
			routine.setDays(routineStored.getDays());
		}else{
			routine.setDays(new ArrayList<Day>());
			
			Day monday = this.dayService.create();
			monday.setDay(DayName.MONDAY);
			routine.getDays().add(monday);
			
			Day tuesday = this.dayService.create();
			tuesday.setDay(DayName.TUESDAY);
			routine.getDays().add(tuesday);
			
			Day wednesday = this.dayService.create();
			wednesday.setDay(DayName.WEDNESDAY);
			routine.getDays().add(wednesday);
			
			Day thursday = this.dayService.create();
			thursday.setDay(DayName.THURSDAY);
			routine.getDays().add(thursday);
			
			Day friday = this.dayService.create();
			friday.setDay(DayName.FRIDAY);
			routine.getDays().add(friday);
			
			Day saturday = this.dayService.create();
			saturday.setDay(DayName.SATURDAY);
			routine.getDays().add(saturday);
			
			Day sunday = this.dayService.create();
			sunday.setDay(DayName.SUNDAY);
			routine.getDays().add(sunday);
			
			
			routine.setUser(this.userService.findByPrincipal());
		}

		this.validator.validate(routine, binding);
		return routine;

	}

}
