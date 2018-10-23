
package usecases;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.AppointmentService;
import services.CreditCardService;
import services.DayService;
import services.DietService;
import services.ExerciseService;
import services.FriendRequestService;
import services.GymRatingService;
import services.GymService;
import services.MarkService;
import services.MeasureService;
import services.NutritionistService;
import services.OrderLineService;
import services.ProductOrderService;
import services.ProductService;
import services.RoutineService;
import services.ShoppingCartService;
import services.SocialNetworkService;
import services.UserService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Appointment;
import domain.CreditCard;
import domain.Day;
import domain.DayName;
import domain.Diet;
import domain.Exercise;
import domain.FriendRequest;
import domain.Gym;
import domain.GymRating;
import domain.Mark;
import domain.Measure;
import domain.Nutritionist;
import domain.OrderLine;
import domain.Product;
import domain.ProductOrder;
import domain.Routine;
import domain.SNetwork;
import domain.ShoppingCart;
import domain.SocialNetwork;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserTest extends AbstractTest {

	@Autowired
	private NutritionistService		nutritionistService;

	@Autowired
	private UserService				userService;

	@Autowired
	private DietService				dietService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private GymService				gymService;

	@Autowired
	private GymRatingService		gymRatingService;

	@Autowired
	private SocialNetworkService	socialNetworkService;

	@Autowired
	private MarkService				markService;

	@Autowired
	private MeasureService			measureService;

	@Autowired
	private FriendRequestService	friendRequestService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private RoutineService			routineService;

	@Autowired
	private ExerciseService			exerciseService;

	@Autowired
	private AppointmentService		appointmentService;

	@Autowired
	private ShoppingCartService		cartService;

	@Autowired
	private ProductOrderService		orderService;

	@Autowired
	private DayService				dayService;

	@Autowired
	private ProductService			productService;

	@Autowired
	private OrderLineService		orderLineService;


	@Before
	public void setupAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("GUEST", "USERNAME", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
	}

	// Edit user data
	// POSITIVE
	@Test
	public void editUserPositiveTest() {
		this.authenticate("user1");
		final User user = this.userService.findByPrincipal();
		user.setName("Example");
		final User result = this.userService.save(user);
		Assert.isTrue(result.getName().equals("Example"));

		this.authenticate(null);
	}

	// NEGATIVE Cannot change name to blank ("")
	@Test(expected = ConstraintViolationException.class)
	public void editUserNegativeTest() {
		this.authenticate("user1");
		final User user = this.userService.findByPrincipal();
		user.setName("");
		final User result = this.userService.save(user);
		Assert.isTrue(result.getName().equals(""));

		this.authenticate(null);
	}

	// NEGATIVE Cannot change name to another user
	@Test(expected = IllegalArgumentException.class)
	public void editUserNegativeTest2() {
		this.authenticate("user1");
		final User user = this.userService.findOne(this.getEntityId("user2"));
		user.setName("Example");
		final User result = this.userService.save(user);
		Assert.isTrue(result.getName().equals("Example"));

		this.authenticate(null);
	}

	// Delete user
	// POSITIVE
	@Test
	public void deleteUserPositiveTest() {
		this.authenticate("user15");
		final User user = this.userService.findByPrincipal();
		this.userService.delete(user);

		this.authenticate(null);
	}

	// NEGATIVE- Deleting other user being authenticated as user1
	@Test(expected = IllegalArgumentException.class)
	public void deleteUserNegativeTest() {
		this.authenticate("user1");
		final User user = this.userService.findOne(this.getEntityId("user10"));
		this.userService.delete(user);

		this.authenticate(null);
	}

	// NEGATIVE- Deleting other user being authenticated as provider
	@Test(expected = IllegalArgumentException.class)
	public void deleteUserNegativeTest2() {
		this.authenticate("provider1");
		final User user = this.userService.findOne(this.getEntityId("user10"));
		this.userService.delete(user);

		this.authenticate(null);
	}

	// Add mark
	// ***********************************************************************************************************
	@Test
	public void driverAddMark() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", 10.0, 10.0, 10.0, 10.0, 10.0, null
			},
			// NEGATIVE - The principal is not an user
			{
				"manager1", 10.0, 10.0, 10.0, 10.0, 10.0, IllegalArgumentException.class
			},
			// NEGATIVE - Any mark is a negative number
			{
				"user1", -10.0, 10.0, 10.0, 10.0, 10.0, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddMark((String) testingData[i][0], (Double) testingData[i][1], (Double) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4], (Double) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateAddMark(final String username, final Double benchPress, final Double squat, final Double deadlift, final Double pullUp, final Double rowing, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User principal = this.userService.findByPrincipal();

			final Calendar now = Calendar.getInstance();
			now.setTime(new Date());

			final Mark mark = this.markService.create();
			mark.setCreationDate(now);
			mark.setBenchPress(benchPress);
			mark.setSquat(squat);
			mark.setDeadlift(deadlift);
			mark.setPullUp(pullUp);
			mark.setRowing(rowing);

			final Mark stored = this.markService.save(mark);
			this.markService.flush();

			Assert.notNull(this.markService.findOne(stored.getId()));
			Assert.isTrue(principal.getMarks().contains(stored));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Add measure
	// ***********************************************************************************************************
	@Test
	public void driverAddMeasure() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, null
			},
			// NEGATIVE - The principal is not an user
			{
				"manager1", 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, IllegalArgumentException.class
			},
			// NEGATIVE - Any measure is a negative number
			{
				"user1", -10.0, 10.0, 10.0, 10.0, 10.0, 10.0, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddMeasure((String) testingData[i][0], (Double) testingData[i][1], (Double) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4], (Double) testingData[i][5], (Double) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void templateAddMeasure(final String username, final Double weight, final Double chest, final Double thigh, final Double waist, final Double biceps, final Double calf, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User principal = this.userService.findByPrincipal();

			final Calendar now = Calendar.getInstance();
			now.setTime(new Date());

			final Measure measure = this.measureService.create();
			measure.setCreationDate(now);
			measure.setWeight(weight);
			measure.setChest(chest);
			measure.setThigh(thigh);
			measure.setWaist(waist);
			measure.setBiceps(biceps);
			measure.setCalf(calf);

			final Measure stored = this.measureService.save(measure);
			this.markService.flush();

			Assert.notNull(this.measureService.findOne(stored.getId()));
			Assert.isTrue(principal.getMeasures().contains(stored));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Delete mark
	// ***********************************************************************************************************
	@Test
	public void driverDeleteMark() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", "mark1", null
			},
			// NEGATIVE - The principal is not an user
			{
				"manager1", "mark2", IllegalArgumentException.class
			},
			// NEGATIVE - The principal is an user but is not the mark's
			// creator
			{
				"user2", "mark3", DataIntegrityViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMark((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteMark(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User principal = this.userService.findByPrincipal();

			final int markId = this.getEntityId(entity);
			final Mark mark = this.markService.findOne(markId);

			principal.getMarks().remove(mark);
			this.markService.delete(mark);
			this.markService.flush();

			Assert.isTrue(!new HashSet<Mark>(this.markService.findAll()).contains(mark));
			Assert.isTrue(!principal.getMarks().contains(mark));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Delete measure
	// ***********************************************************************************************************
	@Test
	public void driverDeleteMeasure() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", "measure1", null
			},
			// NEGATIVE - The principal is not an user
			{
				"manager1", "measure2", IllegalArgumentException.class
			},
			// NEGATIVE - The principal is an user but is not the measure's
			// creator
			{
				"user2", "measure3", DataIntegrityViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMeasure((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteMeasure(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User principal = this.userService.findByPrincipal();

			final int measureId = this.getEntityId(entity);
			final Measure measure = this.measureService.findOne(measureId);

			principal.getMeasures().remove(measure);
			this.measureService.delete(measure);
			this.measureService.flush();

			Assert.isTrue(!new HashSet<Measure>(this.measureService.findAll()).contains(measure));
			Assert.isTrue(!principal.getMeasures().contains(measure));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// POSITIVE - Create routine
	@Test
	public void createRoutinePositiveTest() {
		this.authenticate("user3");
		final Routine routine = this.routineService.create();

		routine.setDays(new ArrayList<Day>());

		final Day monday = this.dayService.create();
		monday.setDay(DayName.MONDAY);
		routine.getDays().add(monday);

		final Day tuesday = this.dayService.create();
		tuesday.setDay(DayName.TUESDAY);
		routine.getDays().add(tuesday);

		final Day wednesday = this.dayService.create();
		wednesday.setDay(DayName.WEDNESDAY);
		routine.getDays().add(wednesday);

		final Day thursday = this.dayService.create();
		thursday.setDay(DayName.THURSDAY);
		routine.getDays().add(thursday);

		final Day friday = this.dayService.create();
		friday.setDay(DayName.FRIDAY);
		routine.getDays().add(friday);

		final Day saturday = this.dayService.create();
		saturday.setDay(DayName.SATURDAY);
		routine.getDays().add(saturday);

		final Day sunday = this.dayService.create();
		sunday.setDay(DayName.SUNDAY);
		routine.getDays().add(sunday);

		routine.setUser(this.userService.findByPrincipal());

		final Routine saved = this.routineService.save(routine);
		Assert.notNull(saved);

		this.authenticate(null);
	}

	// NEGATIVE - Create routine when the user already has one
	@Test(expected = IllegalArgumentException.class)
	public void createRoutineNegativeTestAlreadyExists() {
		this.authenticate("user1");
		Assert.isNull(this.userService.findByPrincipal().getRoutine());
		final Routine routine = this.routineService.create();

		routine.setDays(new ArrayList<Day>());

		final Day monday = this.dayService.create();
		monday.setDay(DayName.MONDAY);
		routine.getDays().add(monday);

		final Day tuesday = this.dayService.create();
		tuesday.setDay(DayName.TUESDAY);
		routine.getDays().add(tuesday);

		final Day wednesday = this.dayService.create();
		wednesday.setDay(DayName.WEDNESDAY);
		routine.getDays().add(wednesday);

		final Day thursday = this.dayService.create();
		thursday.setDay(DayName.THURSDAY);
		routine.getDays().add(thursday);

		final Day friday = this.dayService.create();
		friday.setDay(DayName.FRIDAY);
		routine.getDays().add(friday);

		final Day saturday = this.dayService.create();
		saturday.setDay(DayName.SATURDAY);
		routine.getDays().add(saturday);

		final Day sunday = this.dayService.create();
		sunday.setDay(DayName.SUNDAY);
		routine.getDays().add(sunday);

		routine.setUser(this.userService.findByPrincipal());

		final Routine saved = this.routineService.save(routine);
		Assert.notNull(saved);

		this.authenticate(null);
	}

	// NEGATIVE - Create routine with less than 7 days
	@Test(expected = IllegalArgumentException.class)
	public void createRoutineNegativeTestLessThan7Days() {
		this.authenticate("user1");
		Assert.notNull(this.userService.findByPrincipal());
		final Routine routine = this.routineService.create();

		routine.setDays(new ArrayList<Day>());

		final Day monday = this.dayService.create();
		monday.setDay(DayName.MONDAY);
		routine.getDays().add(monday);

		final Day tuesday = this.dayService.create();
		tuesday.setDay(DayName.TUESDAY);
		routine.getDays().add(tuesday);

		final Day wednesday = this.dayService.create();
		wednesday.setDay(DayName.WEDNESDAY);
		routine.getDays().add(wednesday);

		final Day thursday = this.dayService.create();
		thursday.setDay(DayName.THURSDAY);
		routine.getDays().add(thursday);

		final Day friday = this.dayService.create();
		friday.setDay(DayName.FRIDAY);
		routine.getDays().add(friday);

		final Day saturday = this.dayService.create();
		saturday.setDay(DayName.SATURDAY);
		routine.getDays().add(saturday);

		routine.setUser(this.userService.findByPrincipal());

		final Routine saved = this.routineService.save(routine);
		Assert.notNull(saved);

		this.authenticate(null);
	}

	// POSITIVE - Add exercise to routine
	@Test
	public void addExercisePositiveTest() {
		this.authenticate("user1");
		final Exercise exercise = this.exerciseService.create();
		exercise.setTitle("Test");
		exercise.setDescription("Test");
		final Exercise saved = this.exerciseService.save(exercise);
		this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises().add(saved);
		Assert.isTrue(this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises().contains(saved));

		this.authenticate(null);
	}

	// NEGATIVE - Add exercise to non-existent user
	@Test(expected = IllegalArgumentException.class)
	public void addExerciseNegativeTestNonExistentUser() {
		this.authenticate("user100");
		final Exercise exercise = this.exerciseService.create();
		exercise.setTitle("Test");
		exercise.setDescription("Test");
		final Exercise saved = this.exerciseService.save(exercise);
		this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises().add(saved);
		Assert.isTrue(this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises().contains(saved));

		this.authenticate(null);
	}

	// NEGATIVE - Add exercise to other user's routine
	@Test(expected = IllegalArgumentException.class)
	public void addExerciseNegativeTestOtherUserExercise() {
		this.authenticate("user1");
		Exercise exercise = this.exerciseService.create();
		exercise.setTitle("Test");
		exercise.setDescription("Test");
		final Exercise saved = this.exerciseService.save(exercise);
		this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises().add(saved);
		Assert.isTrue(this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises().contains(saved));

		this.authenticate(null);

		this.authenticate("user2");
		exercise = this.exerciseService.findOne(saved.getId());
		exercise.setTitle("Bad test");
		this.exerciseService.save(exercise);

		this.authenticate(null);
	}

	// POSITIVE - Remove exercise from routine
	@Test
	public void removeExercisePositiveTest() {
		this.authenticate("user1");
		final Collection<Exercise> exercises = this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises();
		final Exercise exercise = (Exercise) exercises.toArray()[0];
		this.exerciseService.delete(exercise);
		Assert.isTrue(!this.exerciseService.findAll().contains(exercise));

		this.authenticate(null);
	}

	// NEGATIVE - Remove exercise from non-existent user
	@Test(expected = IllegalArgumentException.class)
	public void removeExerciseNegativeTestNonExistentUser() {
		this.authenticate("user100");
		final Collection<Exercise> exercises = this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises();
		final Exercise exercise = (Exercise) exercises.toArray()[0];
		this.exerciseService.delete(exercise);
		Assert.isTrue(!this.exerciseService.findAll().contains(exercise));
		this.authenticate(null);
	}

	// NEGATIVE - Remove exercise from other user's routine
	@Test(expected = IllegalArgumentException.class)
	public void removeExerciseNegativeTestOtherUserExercise() {
		this.authenticate("user2");
		final Collection<Exercise> exercises = this.dayService.findCurrentDay(this.getEntityId("user1")).getExercises();
		final Exercise exercise = (Exercise) exercises.toArray()[0];
		this.exerciseService.delete(exercise);
		Assert.isTrue(!this.exerciseService.findAll().contains(exercise));
		this.authenticate(null);
	}

	// POSITIVE - Remove routine
	@Test
	public void removeRoutinePositiveTest() {
		this.authenticate("user1");
		final Routine routine = this.userService.findByPrincipal().getRoutine();
		this.routineService.delete(routine);
		Assert.isTrue(!this.exerciseService.findAll().contains(routine));

		this.authenticate(null);
	}

	// NEGATIVE - Remove routine from non-existent user
	@Test(expected = IllegalArgumentException.class)
	public void removeRoutineNegativeTestNonExistentUser() {
		this.authenticate("user100");
		final Routine routine = this.userService.findByPrincipal().getRoutine();
		this.routineService.delete(routine);
		Assert.isTrue(!this.exerciseService.findAll().contains(routine));
		this.authenticate(null);
	}

	// NEGATIVE - Remove other user's routine
	@Test(expected = IllegalArgumentException.class)
	public void removeRoutineNegativeTestOtherUserRoutine() {
		this.authenticate("user1");
		final Routine routine = this.userService.findByPrincipal().getRoutine();
		this.authenticate(null);

		this.authenticate("user2");
		this.routineService.delete(routine);
		Assert.isTrue(!this.exerciseService.findAll().contains(routine));
		this.authenticate(null);
	}

	// POSITIVE - Assign a nutritionist
	@Test
	public void assignNutritionistPositiveTest() {
		this.authenticate("user1");
		final User result = this.userService.assignNutritionist(this.getEntityId("nutritionist1"));
		final Nutritionist nutritionist = this.nutritionistService.findOne(this.getEntityId("nutritionist1"));

		Assert.isTrue(nutritionist.equals(result.getNutritionist()));

		this.authenticate(null);
	}

	// NEGATIVE - Assign non existent nutritionist
	@Test(expected = AssertionError.class)
	public void assignNutritionistNegativeTest() {
		this.authenticate("user1");
		final User result = this.userService.assignNutritionist(this.getEntityId("nutritionist99"));
		final Nutritionist nutritionist = this.nutritionistService.findOne(this.getEntityId("nutritionist99"));

		Assert.isTrue(nutritionist.equals(result.getNutritionist()));

		this.authenticate(null);
	}

	// NEGATIVE - Assign non validated nutritionist
	@Test(expected = IllegalArgumentException.class)
	public void assignNutritionistNegativeTest2() {
		this.authenticate("user1");
		final User result = this.userService.assignNutritionist(this.getEntityId("nutritionist2"));
		final Nutritionist nutritionist = this.nutritionistService.findOne(this.getEntityId("nutritionist2"));

		Assert.isTrue(nutritionist.equals(result.getNutritionist()));

		this.authenticate(null);
	}

	// POSITIVE - Make appointment with nutritionist
	@Test
	public void makeAppointmentPositiveTest() {
		this.authenticate("user1");
		final Appointment ap = this.appointmentService.create();

		ap.setCancelled(false);
		ap.setNutritionist(this.userService.findByPrincipal().getNutritionist());
		final Calendar time = Calendar.getInstance();
		// 28/06/2019 11:00
		time.setTimeInMillis(1561705200000L);
		ap.setTime(time);
		ap.setUser(this.userService.findByPrincipal());

		final Appointment apSaved = this.appointmentService.make(ap);
		Assert.isTrue(this.userService.findByPrincipal().getAppointments().contains(apSaved));
		Assert.isTrue(this.userService.findByPrincipal().getNutritionist().getAppointments().contains(apSaved));

		this.authenticate(null);
	}

	// NEGATIVE - Create appointment on sunday
	@Test(expected = IllegalArgumentException.class)
	public void createAppointmentNegativeTestSunday() {
		this.authenticate("user1");
		final Appointment ap = this.appointmentService.create();

		ap.setCancelled(false);
		ap.setNutritionist(this.userService.findByPrincipal().getNutritionist());
		final Calendar time = Calendar.getInstance();
		// 30/06/2019 15:30
		time.setTimeInMillis(1561901400000L);
		ap.setTime(time);
		ap.setUser(this.userService.findByPrincipal());

		final Appointment apSaved = this.appointmentService.make(ap);
		Assert.isTrue(this.userService.findByPrincipal().getAppointments().contains(apSaved));
		Assert.isTrue(this.userService.findByPrincipal().getNutritionist().getAppointments().contains(apSaved));

		this.authenticate(null);
	}

	// NEGATIVE - Make appointment out of nutritionist's schedule
	@Test(expected = IllegalArgumentException.class)
	public void createAppointmentNegativeTestSchedule() {
		this.authenticate("user1");
		final Appointment ap = this.appointmentService.create();

		ap.setCancelled(false);
		ap.setNutritionist(this.userService.findByPrincipal().getNutritionist());
		final Calendar time = Calendar.getInstance();
		// 28/06/2019 00:00
		time.setTimeInMillis(1561672800000L);
		ap.setTime(time);
		ap.setUser(this.userService.findByPrincipal());

		final Appointment apSaved = this.appointmentService.make(ap);
		Assert.isTrue(this.userService.findByPrincipal().getAppointments().contains(apSaved));
		Assert.isTrue(this.userService.findByPrincipal().getNutritionist().getAppointments().contains(apSaved));

		this.authenticate(null);
	}

	// List diets
	// ***********************************************************************************************************
	// POSITIVE: List diets
	@Test
	public void listDietsPositive() {
		this.authenticate("user1");
		final Collection<Diet> diets = this.userService.findByPrincipal().getNutritionist().getDiets();
		Assert.isTrue(diets.size() == 1);
		this.authenticate(null);
	}

	// NEGATIVE: List diets of non-validated nutritionist
	@Test(expected = IllegalArgumentException.class)
	public void listDietsNegativeTestNonValidated() {
		this.authenticate("user1");
		final Collection<Diet> diets = this.nutritionistService.findOne(this.getEntityId("nutritionist2")).getDiets();
		Assert.isTrue(diets.size() == 1);
		this.authenticate(null);
	}

	// NEGATIVE: List diets without being authenticated
	@Test(expected = IllegalArgumentException.class)
	public void listDietsNegativeTestUnauthenticated() {
		final Collection<Diet> diets = this.userService.findByPrincipal().getNutritionist().getDiets();
		Assert.isTrue(diets.size() == 1);
	}

	// Follow a diet
	// POSITIVE
	@Test
	public void followDietPositiveTest() {
		this.authenticate("user1");
		final Diet diet = this.dietService.findOne(this.getEntityId("diet1"));
		User result = this.userService.assignNutritionist(this.getEntityId("nutritionist1"));
		Assert.isTrue(result.getNutritionist().equals(diet.getNutritionist()));
		result.setDiet(diet);
		result = this.userService.save(result);

		this.authenticate(null);
	}

	// NEGATIVE- You cannot follow a diet if you have not assigned the
	// nutritionist before
	@Test(expected = NullPointerException.class)
	public void followDietNegativeTest() {
		this.authenticate("user2");
		final Diet diet = this.dietService.findOne(this.getEntityId("diet1"));
		User result = this.userService.findByPrincipal();
		Assert.isTrue(result.getNutritionist().equals(diet.getNutritionist()));
		result.setDiet(diet);
		result = this.userService.save(result);

		this.authenticate(null);
	}

	// NEGATIVE- Follow a non existent diet
	@Test(expected = AssertionError.class)
	public void followDietNegativeTest2() {
		this.authenticate("user1");
		final Diet diet = this.dietService.findOne(this.getEntityId("diet99"));
		User result = this.userService.findByPrincipal();
		Assert.isTrue(result.getNutritionist().equals(diet.getNutritionist()));
		result.setDiet(diet);
		result = this.userService.save(result);

		this.authenticate(null);
	}

	// Add products to shopping cart
	// POSITIVE
	@Test
	public void addProductsPositiveTest() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		cart.setProduct(product);
		cart.setAmount(10);

		final ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		this.authenticate(null);
	}

	// NEGATIVE- Adding a product to other user's cart
	// nutritionist before
	@Test(expected = IllegalArgumentException.class)
	public void addProductsNegativeTestOtherUserCart() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		cart.setProduct(product);
		cart.setAmount(10);

		final ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		this.authenticate(null);

		this.authenticate("user2");
		cartSaved.setAmount(5);
		this.cartService.save(cartSaved);
		this.authenticate(null);

	}

	// NEGATIVE- Add negative ammount
	@Test(expected = ConstraintViolationException.class)
	public void addProductsNegativeTestAmmount() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		cart.setProduct(product);
		cart.setAmount(-10);

		final ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		this.authenticate(null);
	}

	// Remove products from shopping cart
	// POSITIVE
	@Test
	public void removeProductsPositiveTest() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		cart.setProduct(product);
		cart.setAmount(10);

		final ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		this.userService.findByPrincipal().getShoppingCart().remove(cartSaved);
		this.cartService.delete(cartSaved);
		Assert.isTrue(!this.cartService.findAll().contains(cartSaved));

		this.authenticate(null);
	}

	// NEGATIVE- Removeing a product from other user's cart
	// nutritionist before
	@Test(expected = IllegalArgumentException.class)
	public void removeProductsNegativeTestOtherUserCart() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		cart.setProduct(product);
		cart.setAmount(10);

		ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		this.authenticate(null);

		this.authenticate("user2");
		cartSaved = this.cartService.findOneToEdit(cartSaved.getId());
		this.cartService.delete(cartSaved);
		Assert.isTrue(!this.cartService.findAll().contains(cartSaved));
		this.authenticate(null);

	}

	// NEGATIVE- Remove product without removing from user
	@Test(expected = DataIntegrityViolationException.class)
	public void removeProductsNegativeTestAmmount() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		cart.setProduct(product);
		cart.setAmount(10);

		final ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		//		this.userService.findByPrincipal().getShoppingCart().remove(cartSaved);
		this.cartService.delete(cartSaved);
		Assert.isTrue(!this.cartService.findAll().contains(cartSaved));
		this.authenticate(null);
	}

	// Pay shopping cart
	// POSITIVE
	@Test
	public void payCartPositiveTest() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		final int unitsForTest = product.getUnits();
		cart.setProduct(product);
		cart.setAmount(10);

		final ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		final ProductOrder order = this.orderService.create();
		final Collection<OrderLine> lines = new ArrayList<OrderLine>();
		final User principal = this.userService.findByPrincipal();
		final CreditCard creditCard = (CreditCard) principal.getCreditCards().toArray()[0];
		final Collection<ShoppingCart> carts = principal.getShoppingCart();

		Assert.isTrue(!carts.isEmpty());

		final Iterator<ShoppingCart> iter = carts.iterator();
		while (iter.hasNext()) {
			final ShoppingCart sc = iter.next();
			final Product p = sc.getProduct();
			final OrderLine ol = this.orderLineService.create();
			// Description
			ol.setDescription(p.getDescription());
			// Image
			ol.setImage(p.getImage());
			// Name
			ol.setName(p.getName());
			// Price
			ol.setPrice(sc.getPrice());
			// SKU
			ol.setSku(p.getSku());
			// Amount
			ol.setAmount(sc.getAmount());

			lines.add(ol);
			Assert.isTrue(this.productService.findOne(p.getId()).getUnits() >= sc.getAmount());
			this.productService.buy(p, sc);

			iter.remove();
			this.cartService.delete(sc);
		}

		order.setOrderLines(lines);
		order.setCreditCard(creditCard);
		order.setDate(Calendar.getInstance());
		this.orderService.save(order);

		Assert.isTrue(this.productService.findOne(product.getId()).getUnits() == unitsForTest - cart.getAmount());

		this.authenticate(null);
	}

	// NEGATIVE- Pay shopping cart no units available
	// nutritionist before
	@Test(expected = IllegalArgumentException.class)
	public void payCartNegativeTestNoUnitsAvailable() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		cart.setProduct(product);
		cart.setAmount(1000);

		final ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		final ProductOrder order = this.orderService.create();
		final Collection<OrderLine> lines = new ArrayList<OrderLine>();
		final User principal = this.userService.findByPrincipal();
		final CreditCard creditCard = (CreditCard) principal.getCreditCards().toArray()[0];
		final Collection<ShoppingCart> carts = principal.getShoppingCart();

		Assert.isTrue(!carts.isEmpty());

		final Iterator<ShoppingCart> iter = carts.iterator();
		while (iter.hasNext()) {
			final ShoppingCart sc = iter.next();
			final Product p = sc.getProduct();
			final OrderLine ol = this.orderLineService.create();
			// Description
			ol.setDescription(p.getDescription());
			// Image
			ol.setImage(p.getImage());
			// Name
			ol.setName(p.getName());
			// Price
			ol.setPrice(sc.getPrice());
			// SKU
			ol.setSku(p.getSku());
			// Amount
			ol.setAmount(sc.getAmount());

			lines.add(ol);
			Assert.isTrue(this.productService.findOne(p.getId()).getUnits() >= sc.getAmount());
			this.productService.buy(p, sc);

			iter.remove();
			this.cartService.delete(sc);
		}

		order.setOrderLines(lines);
		order.setCreditCard(creditCard);
		order.setDate(Calendar.getInstance());
		this.orderService.save(order);

		Assert.isTrue(this.productService.findOne(product.getId()).getUnits() == product.getUnits() - cart.getAmount());

		this.authenticate(null);
	}

	// NEGATIVE- Pay other user's cart
	@Test(expected = IllegalArgumentException.class)
	public void payCartNegativeTestOtherUser() {
		this.authenticate("user1");
		final ShoppingCart cart = this.cartService.create();
		final Product product = (Product) this.productService.findAll().toArray()[0];
		cart.setProduct(product);
		cart.setAmount(10);

		final ShoppingCart cartSaved = this.cartService.save(cart);
		Assert.isTrue(this.userService.findByPrincipal().getShoppingCart().contains(cartSaved));
		Assert.isTrue(cartSaved.getPrice() == product.getPrice() * 10);

		this.authenticate(null);
		this.authenticate("user2");

		final ProductOrder order = this.orderService.create();
		final Collection<OrderLine> lines = new ArrayList<OrderLine>();
		final User principal = this.userService.findOne(this.getEntityId("user1"));
		final CreditCard creditCard = (CreditCard) principal.getCreditCards().toArray()[0];
		final Collection<ShoppingCart> carts = principal.getShoppingCart();

		Assert.isTrue(!carts.isEmpty());

		final Iterator<ShoppingCart> iter = carts.iterator();
		while (iter.hasNext()) {
			final ShoppingCart sc = iter.next();
			final Product p = sc.getProduct();
			final OrderLine ol = this.orderLineService.create();
			// Description
			ol.setDescription(p.getDescription());
			// Image
			ol.setImage(p.getImage());
			// Name
			ol.setName(p.getName());
			// Price
			ol.setPrice(sc.getPrice());
			// SKU
			ol.setSku(p.getSku());
			// Amount
			ol.setAmount(sc.getAmount());

			lines.add(ol);
			Assert.isTrue(this.productService.findOne(p.getId()).getUnits() >= sc.getAmount());
			this.productService.buy(p, sc);

			iter.remove();
			this.cartService.delete(sc);
		}

		order.setOrderLines(lines);
		order.setCreditCard(creditCard);
		order.setDate(Calendar.getInstance());
		this.orderService.save(order);

		Assert.isTrue(this.productService.findOne(product.getId()).getUnits() == product.getUnits() - cart.getAmount());

		this.authenticate(null);
	}

	// ADD CREDIT CARD
	// *****************************************************************************************************************
	@Test
	public void driverAddCreditCard() {
		final Object testingData[][] = {
			// 1. POSITIVE
			{
				"user1", "Card 1", "4768 9370 9472 3704", 5, 2019, 703, null
			},
			// 2. NEGATIVE - The principal is not an user
			{
				"manager1", "Card 1", "4768 9370 9472 3704", 5, 2019, 703, IllegalArgumentException.class
			},
			// 3. NEGATIVE - Not authenticated
			{
				null, "Card 1", "4768 9370 9472 3704", 5, 2019, 703, IllegalArgumentException.class
			},
			// 4. NEGATIVE - Expired card
			{
				"user1", "Card 1", "4768 9370 9472 3704", 5, 2017, 703, IllegalArgumentException.class
			},
			// 5. NEGATIVE - Invalid credit card number
			{
				"user1", "Card 1", "1111 1111 1111 1111", 5, 2019, 703, ConstraintViolationException.class
			},
			// 6. NEGATIVE - Name is blank
			{
				"user1", "", "4768 9370 9472 3704", 5, 2019, 703, ConstraintViolationException.class
			},
			// 7. NEGATIVE - Invalid CVV
			{
				"user1", "Card 1", "4768 9370 9472 3704", 5, 2019, 7703, ConstraintViolationException.class
			},
			// 8. NEGATIVE - Invalid month
			{
				"user1", "Card 1", "4768 9370 9472 3704", 13, 2019, 703, ConstraintViolationException.class
			},
			// 9. NEGATIVE - Invalid year
			{
				"user1", "Card 1", "4768 9370 9472 3704", 5, 3020, 703, ConstraintViolationException.class
			},
			// 10. NEGATIVE - The number already exists
			{
				"user1", "Card 1", "4716 8677 4269 5249", 5, 2019, 703, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddCreditCard((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (int) testingData[i][4], (int) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateAddCreditCard(final String username, final String name, final String number, final int month, final int year, final int cvv, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			User principal = null;
			List<CreditCard> cards = null;
			this.authenticate(username);

			final CreditCard card = this.creditCardService.create();
			card.setName(name);
			card.setNumber(number);
			card.setExpirationMonth(month);
			card.setExpirationYear(year);
			card.setCVV(cvv);

			final CreditCard stored = this.creditCardService.save(card);
			this.creditCardService.flush();

			if (username != null || this.actorService.isUser()) {
				principal = this.userService.findByPrincipal();
				cards = new ArrayList<CreditCard>(principal.getCreditCards());
			}

			Assert.isTrue(cards.contains(stored));
			Assert.notNull(this.creditCardService.findOne(stored.getId()));
			Assert.isTrue(this.creditCardService.findOne(stored.getId()).getNumber().equals(number));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// ADD SOCIAL NETWORK USER
	// *****************************************************************************************************************
	@Test
	public void driverAddSocialNetworkUser() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", null
			},
			// NEGATIVE - User does not exist
			{
				"user123", IllegalArgumentException.class
			},
			// NEGATIVE - Manager does not have social networks
			{
				"manager5", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddSocialNetworkUser((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateAddSocialNetworkUser(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			// final int userId = this.getEntityId(username);
			this.authenticate(username);
			// this.setUpAuth(username);

			final User user = this.userService.findByPrincipal();

			// final User user = this.userService.findOne(userId);

			final Collection<SocialNetwork> socialNetworks = new ArrayList<SocialNetwork>();

			final SocialNetwork youtube = this.socialNetworkService.create();
			youtube.setsocialNetworkType(SNetwork.YOUTUBE);
			youtube.setUrl("https://www.youtube.com/user/caseyneistat");
			// socialNetwors.add(youtube);
			// this.socialNetworkService.save(youtube);
			final SocialNetwork stored = this.socialNetworkService.save(youtube);
			socialNetworks.add(stored);

			user.setSocialNetworks(socialNetworks);
			// this.userService.save(user);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// REMOVE SOCIAL NETWORK USER
	// *****************************************************************************************************************
	@Test
	public void driverRemoveSocialNetworkUser() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", null
			},
			// NEGATIVE - Manager does not exist
			{
				"user123", IllegalArgumentException.class
			},
			// NEGATIVE - Manager does not have social networks
			{
				"manager1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateRemoveSocialNetworkUser((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateRemoveSocialNetworkUser(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);

			final User user = this.userService.findByPrincipal();
			final Collection<SocialNetwork> socialNetworks = user.getSocialNetworks();

			final SocialNetwork sn = (SocialNetwork) socialNetworks.toArray()[0];
			socialNetworks.remove(sn);
			this.socialNetworkService.delete(sn);

			user.setSocialNetworks(socialNetworks);
			this.userService.save(user);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Remove credit card
	// POSITIVE
	@Test
	public void removeCreditCardPositiveTest() {
		this.authenticate("user1");
		final CreditCard card = this.creditCardService.findOne(this.getEntityId("creditCard1"));
		this.creditCardService.delete(card);

		this.authenticate(null);
	}

	// NEGATIVE-Removing a credit card that does not belong to user1
	@Test(expected = IllegalArgumentException.class)
	public void removeCreditCardNegativeTest() {
		this.authenticate("user1");
		final CreditCard card = this.creditCardService.findOne(this.getEntityId("creditCard3"));
		this.creditCardService.delete(card);

		this.authenticate(null);
	}

	// NEGATIVE-Removing a credit card authenticated as provider
	@Test(expected = IllegalArgumentException.class)
	public void removeCreditCardNegativeTest2() {
		this.authenticate("provider1");
		final CreditCard card = this.creditCardService.findOne(this.getEntityId("creditCard1"));
		this.creditCardService.delete(card);

		this.authenticate(null);
	}

	// Rate gym
	// ***********************************************************************************************************
	// POSITIVE: Rate gym
	@Test
	public void rateGymPositive() {
		this.authenticate("user1");
		this.setUpAuth("user1");

		final Gym gym = this.userService.findOne(this.getEntityId("user1")).getSubscription().getGym();
		final GymRating gymRating = this.gymRatingService.create();
		gymRating.setRating(3);
		this.gymRatingService.save(gymRating);

		this.gymService.saveRating(gym, gymRating);

		this.authenticate(null);
	}

	// NEGATIVE: Rate gym not subscribed to
	@Test(expected = IllegalArgumentException.class)
	public void rateGymNegativeNotSubscribedTo() {
		this.authenticate("user1");
		this.setUpAuth("user1");

		final Gym gym = this.gymService.findOne(this.getEntityId("gym2"));
		final GymRating gymRating = this.gymRatingService.create();
		gymRating.setRating(3);
		Assert.isTrue(this.userService.findByPrincipal().getSubscription().getGym().equals(gym));
		this.gymRatingService.save(gymRating);
		this.gymService.saveRating(gym, gymRating);

		this.authenticate(null);
	}

	// NEGATIVE: Rate gym already rated
	@Test(expected = IllegalArgumentException.class)
	public void rateGymNegativeAlreadyRated() {
		this.authenticate("user1");
		this.setUpAuth("user1");

		final User principal = this.userService.findByPrincipal();
		final Gym gym = principal.getSubscription().getGym();
		final GymRating gymRating = this.gymRatingService.create();
		gymRating.setRating(3);
		this.gymRatingService.save(gymRating);
		this.gymService.saveRating(gym, gymRating);

		final GymRating gymRating2 = this.gymRatingService.create();
		gymRating2.setRating(4);

		final Collection<GymRating> grUser = new ArrayList<GymRating>(principal.getGymRatings());
		final Collection<GymRating> grGym = new ArrayList<GymRating>(gym.getGymRatings());
		boolean alreadyRated = false;

		for (final GymRating g : grUser)
			if (grGym.contains(g))
				alreadyRated = true;

		Assert.isTrue(alreadyRated);
		this.gymRatingService.save(gymRating2);
		this.gymService.saveRating(gym, gymRating2);

		this.authenticate(null);
	}

	// Display friend list
	// ***********************************************************************************************************
	@Test
	public void driverDisplayFriendList() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", null
			},
			// NEGATIVE - Login as not user
			{
				"manager1", IllegalArgumentException.class
			},
			// NEGATIVE - Non authenticated
			{
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDisplayFriendList((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateDisplayFriendList(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			Actor principal = null;
			this.authenticate(username);
			if (username != null)
				principal = this.actorService.findByPrincipal();

			final Collection<User> friends = new ArrayList<User>();

			for (final FriendRequest fr : this.friendRequestService.getAcceptedRequests())
				if (fr.getSender().equals(principal))
					friends.add(fr.getReceiver());
				else
					friends.add(fr.getSender());

			Assert.notEmpty(friends);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// List received friend requests
	// ***********************************************************************************************************
	@Test
	public void driverListReceivedFriendRequests() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", null
			},
			// NEGATIVE - Login as not user
			{
				"manager1", IllegalArgumentException.class
			},
			// NEGATIVE - Non authenticated
			{
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListReceivedFriendRequests((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateListReceivedFriendRequests(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);

			final Collection<FriendRequest> requests = this.friendRequestService.getReceivedRequests();

			Assert.notNull(requests);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// List sent friend requests
	// ***********************************************************************************************************
	@Test
	public void driverListSentFriendRequests() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", null
			},
			// NEGATIVE - Login as not user
			{
				"manager1", IllegalArgumentException.class
			},
			// NEGATIVE - Non authenticated
			{
				null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListSentFriendRequests((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void templateListSentFriendRequests(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);

			final Collection<FriendRequest> requests = this.friendRequestService.getSentRequests();

			Assert.notNull(requests);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Send request
	// ***********************************************************************************************************
	@Test
	public void driverSendRequest() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", "user10", null
			},
			// NEGATIVE - The users are friends
			{
				"user1", "user2", IllegalArgumentException.class
			},
			// NEGATIVE - The receiver sent you a request previously
			{
				"user1", "user4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSendRequest((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateSendRequest(final String username, final String receiverEntity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User sender = this.userService.findByPrincipal();
			final int receiverId = this.getEntityId(receiverEntity);
			final User receiver = this.userService.findOne(receiverId);

			final FriendRequest request = this.friendRequestService.create();
			request.setAccepted(false);
			request.setSender(sender);
			request.setReceiver(receiver);

			final FriendRequest stored = this.friendRequestService.save(request);
			this.friendRequestService.flush();

			Assert.notNull(this.friendRequestService.findOne(stored.getId()));
			Assert.isTrue(sender.getSentRequests().contains(stored));
			Assert.isTrue(receiver.getReceivedRequests().contains(stored));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Delete request
	// ***********************************************************************************************************
	@Test
	public void driverDeleteRequest() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", "request6", null
			},
			// NEGATIVE - The user is not the request's sender
			{
				"user4", "request6", IllegalArgumentException.class
			},
			// NEGATIVE - The request is accepted
			{
				"user1", "request1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteRequest((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteRequest(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User sender = this.userService.findByPrincipal();

			final int requestId = this.getEntityId(entity);
			final FriendRequest request = this.friendRequestService.findOne(requestId);

			final User receiver = request.getReceiver();

			this.friendRequestService.delete(request);
			this.friendRequestService.flush();

			Assert.isTrue(!new HashSet<FriendRequest>(this.friendRequestService.findAll()).contains(request));
			Assert.isTrue(!sender.getSentRequests().contains(request));
			Assert.isTrue(!receiver.getReceivedRequests().contains(request));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Accept request
	// ***********************************************************************************************************
	@Test
	public void driverAcceptRequest() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user4", "request6", null
			},
			// NEGATIVE - The user is not the request's receiver
			{
				"user1", "request6", IllegalArgumentException.class
			},
			// NEGATIVE - Non authenticated
			{
				null, "request6", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAcceptRequest((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateAcceptRequest(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User principal = this.userService.findByPrincipal();

			final int requestId = this.getEntityId(entity);
			final FriendRequest request = this.friendRequestService.findOne(requestId);

			final User sender = request.getSender();

			final FriendRequest accepted = this.friendRequestService.accept(request);
			this.friendRequestService.flush();

			Assert.notNull(this.friendRequestService.findOne(accepted.getId()));
			Assert.isTrue(accepted.isAccepted());
			Assert.isTrue(principal.getReceivedRequests().contains(accepted));
			Assert.isTrue(sender.getSentRequests().contains(accepted));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Delete friend
	// ***********************************************************************************************************
	@Test
	public void driverDeleteFriend() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"user1", "request1", null
			},
			// NEGATIVE - The user is not the request's sender or the
			// request's receiver
			{
				"user3", "request1", IllegalArgumentException.class
			},
			// NEGATIVE - The request is not accepted, so they are not
			// friends
			{
				"user1", "request6", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteFriend((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteFriend(final String username, final String requestEntity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final User principal = this.userService.findByPrincipal();
			User friend = null;

			final int requestId = this.getEntityId(requestEntity);
			final FriendRequest request = this.friendRequestService.findOne(requestId);

			if (request.getSender().equals(principal))
				friend = request.getReceiver();
			if (request.getReceiver().equals(principal))
				friend = request.getSender();

			this.friendRequestService.removeFriend(request);
			this.friendRequestService.flush();

			Assert.isTrue(!this.friendRequestService.areFriends(principal, friend));
			Assert.isTrue(!(principal.getSentRequests().contains(request)) || !(principal.getReceivedRequests().contains(request)));
			Assert.isTrue(!(friend.getReceivedRequests().contains(request)) || !(friend.getSentRequests().contains(request)));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
