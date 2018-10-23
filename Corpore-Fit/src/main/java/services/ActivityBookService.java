
package services;

import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ActivityBookRepository;
import domain.Activity;
import domain.ActivityBook;
import domain.DayName;
import domain.User;

@Service
@Transactional
public class ActivityBookService {

	@Autowired
	private ActivityBookRepository	activityBookRepository;

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ActivityService			activityService;


	//Create
	public ActivityBook create() {
		final ActivityBook result;

		result = new ActivityBook();

		final Calendar cal = Calendar.getInstance();
		result.setCreationDate(cal);

		return result;
	}

	public ActivityBook findOne(final int activityBookId) {
		ActivityBook result;
		result = this.activityBookRepository.findOne(activityBookId);
		Assert.notNull(result);
		return result;
	}

	public Collection<ActivityBook> findAll() {
		return this.activityBookRepository.findAll();
	}

	//Save
	public ActivityBook save(final ActivityBook activityBook) {
		ActivityBook result;

		Assert.notNull(activityBook);
		final User principal = this.userService.findByPrincipal();
		Assert.isTrue(principal.getSubscription().getGym().equals(activityBook.getActivity().getGym()));

		final Activity activity = this.activityService.findOne(activityBook.getActivity().getId());
		Assert.notNull(activity);
		Assert.isTrue(activity.getGym().equals(principal.getSubscription().getGym()));

		result = this.activityBookRepository.save(activityBook);
		return result;
	}

	//Delete
	public void delete(final ActivityBook activityBook) {
		if (this.actorService.isUser())
			Assert.isTrue(this.userService.findByPrincipal().equals(activityBook.getUser()));

		if (this.actorService.isManager())
			Assert.isTrue(this.managerService.findByPrincipal().equals(activityBook.getActivity().getGym().getManager()));

		this.activityBookRepository.delete(activityBook);

		Assert.isTrue(!this.activityBookRepository.findAll().contains(activityBook));

	}

	//isFull?
	public boolean isFull(final ActivityBook activityBook) {

		boolean result = false;

		final Activity activity = activityBook.getActivity();
		final int books = this.activityService.countBooksForActivity(activity.getId());

		if (books >= activity.getRoom().getCapacity())
			result = true;

		return result;
	}

	//activityFull?
	public boolean activityFull(final int activityId) {

		boolean result = false;

		final Activity activity = this.activityService.findOne(activityId);

		final int books = this.activityService.countBooksForActivity(activityId);

		if (books >= activity.getRoom().getCapacity())
			result = true;

		return result;
	}

	//isBooked?
	public boolean alreadyBooked(final int activityId) {

		boolean result = false;

		final User principal = this.userService.findByPrincipal();

		final int book = this.activityBookRepository.alreadyBooked(principal.getId(), activityId);

		if (book != 0)
			result = true;

		return result;
	}

	public ActivityBook findByUserAndActivity(final int userId, final int activityId) {
		ActivityBook result;
		result = this.activityBookRepository.findByUserAndActivity(userId, activityId);
		return result;
	}

	public Collection<ActivityBook> findAllByUser(final int userId) {
		Collection<ActivityBook> result;
		result = this.activityBookRepository.findAllByUser(userId);
		return result;
	}

	public Collection<ActivityBook> findAllByActivity(final int activityId) {
		Collection<ActivityBook> result;
		result = this.activityBookRepository.findAllByActivity(activityId);
		return result;
	}

	public int countByUserGymDayAndTime(final int userId, final int gymId, final DayName day, final Calendar start, final Calendar end) {
		int result;

		result = this.activityBookRepository.countByUserGymDayAndTime(userId, gymId, day, start, end);
		return result;
	}


	//Reconstruct
	@Autowired
	private Validator	validator;


	public ActivityBook reconstruct(final ActivityBook activityBook, final BindingResult binding) {

		final User principal = this.userService.findByPrincipal();
		activityBook.setUser(principal);

		final Activity activity = activityBook.getActivity();
		Assert.notNull(activity);

		activityBook.setUser(principal);
		activityBook.setActivity(activity);

		this.validator.validate(activityBook, binding);

		return activityBook;

	}

}
