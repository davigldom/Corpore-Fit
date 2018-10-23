package services;

import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ActivityRepository;
import domain.Activity;
import domain.ActivityBook;
import domain.DayName;
import domain.Gym;
import domain.Manager;

@Service
@Transactional
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ActivityBookService activityBookService;

	@Autowired
	private GymService gymService;

	@Autowired
	private ActorService actorService;

	public Activity create() {
		final Activity result;

		result = new Activity();

		result.setGym(this.managerService.findByPrincipal().getGym());

		return result;
	}

	public Activity findOne(final int activityId) {
		Activity result;
		result = this.activityRepository.findOne(activityId);
		Assert.notNull(result);
		return result;
	}

	public Activity findOneToEdit(final int activityId) {
		Activity result;
		result = this.findOne(activityId);
		Assert.isTrue(this.managerService.findByPrincipal().equals(
				result.getGym().getManager()));

		return result;
	}

	public Collection<Activity> findAll() {
		return this.activityRepository.findAll();
	}

	public Collection<Activity> findByGym(final int gymId) {
		Collection<Activity> result;

		result = this.activityRepository.findByGym(gymId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Activity> findByGymMonday(final int gymId) {
		Collection<Activity> result;

		result = this.activityRepository.findByGymMonday(gymId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Activity> findByGymTuesday(final int gymId) {
		Collection<Activity> result;

		result = this.activityRepository.findByGymTuesday(gymId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Activity> findByGymWednesday(final int gymId) {
		Collection<Activity> result;

		result = this.activityRepository.findByGymWednesday(gymId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Activity> findByGymThursday(final int gymId) {
		Collection<Activity> result;

		result = this.activityRepository.findByGymThursday(gymId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Activity> findByGymFriday(final int gymId) {
		Collection<Activity> result;

		result = this.activityRepository.findByGymFriday(gymId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Activity> findByGymSaturday(final int gymId) {
		Collection<Activity> result;

		result = this.activityRepository.findByGymSaturday(gymId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Activity> findByGymSunday(final int gymId) {
		Collection<Activity> result;

		result = this.activityRepository.findByGymSunday(gymId);
		Assert.notNull(result);
		return result;
	}

	public int countBooksForActivity(final int activityId) {
		int result = 0;

		result = this.activityRepository.countBooksForActivity(activityId);
		return result;
	}

	public Activity countByGymDayRoomAndTime(final int gymId,
			final DayName day, final int roomId, final Calendar start,
			final Calendar end) {
		Activity result;

		result = this.activityRepository.countByGymDayRoomAndTime(gymId, day,
				roomId, start, end);
		return result;
	}

	public Activity save(final Activity activity) {
		Activity result;

		Assert.notNull(activity);
		final Manager principal = this.managerService.findByPrincipal();
		Assert.isTrue(principal.getGym().equals(activity.getGym()));

		Assert.isTrue(activity.getEnd().after(activity.getStart()));
		Assert.isTrue(activity.getRoom().getManager().equals(principal));

		final Gym gym = principal.getGym();
		gym.getActivities().add(activity);
		this.gymService.save(gym);

		result = this.activityRepository.save(activity);
		return result;
	}

	public void delete(final Activity activity) {

		if (this.actorService.isManager())
			Assert.isTrue(this.managerService.findByPrincipal().equals(
					activity.getGym().getManager()));
		else
			Assert.isTrue(this.actorService.isAdmin());

		if (this.actorService.isAdmin()) {
			final Collection<ActivityBook> books = this.activityBookService
					.findAllByActivity(activity.getId());
			if (!books.isEmpty())
				for (final ActivityBook ab : books)
					this.activityBookService.delete(ab);
		}
		Assert.isTrue(
				this.activityBookService.findAllByActivity(activity.getId())
						.isEmpty(),
				"There are users who have booked this activity.");

		final Gym gym = activity.getGym();
		gym.getActivities().remove(activity);
		this.activityRepository.delete(activity);

		Assert.isTrue(!this.activityRepository.findAll().contains(activity));

	}

	// Reconstruct

	@Autowired
	private Validator validator;

	public Activity reconstruct(final Activity activity,
			final BindingResult binding) {
		Activity activityStored;

		if (activity.getId() != 0) {
			activityStored = this.activityRepository.findOne(activity.getId());

			activity.setId(activityStored.getId());
			activity.setVersion(activityStored.getVersion());

			// activity.setTitle(activityStored.getTitle());
			// activity.setDescription(activityStored.getDescription());
			// activity.setPhoto(activityStored.getPhoto());
			// activity.setTrainer(activityStored.getTrainer());
			// activity.setStart(activityStored.getStart());
			// activity.setEnd(activityStored.getEnd());

			// activity.setRoom(activityStored.getRoom());
			activity.setGym(activityStored.getGym());
		} else
			activity.setGym(this.managerService.findByPrincipal().getGym());

		this.validator.validate(activity, binding);
		return activity;

	}

	public void flush() {
		this.activityRepository.flush();
	}

}
