
package usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AppointmentService;
import services.DailyDietService;
import services.DietService;
import services.FoodService;
import services.NutritionistService;
import utilities.AbstractTest;
import domain.Appointment;
import domain.DailyDiet;
import domain.DaySchedule;
import domain.Diet;
import domain.Food;
import domain.FoodTime;
import domain.Nutritionist;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class NutritionistTest extends AbstractTest {

	@Autowired
	private NutritionistService	nutritionistService;

	@Autowired
	private DietService			dietService;

	@Autowired
	private AppointmentService	appointmentService;

	@Autowired
	private DailyDietService	dayService;

	@Autowired
	private FoodService			foodService;


	@Before
	public void setupAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("GUEST", "USERNAME", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
	}

	// Create diet
	// POSITIVE
	@Test
	public void createDietPositiveTest() {
		this.authenticate("nutri1");
		final Diet diet = this.dietService.create();
		diet.setName("Example");
		this.dietService.save(diet);

		this.authenticate(null);
	}

	// NEGATIVE- Name in blank
	@Test(expected = IllegalArgumentException.class)
	public void createDietNegativeTest() {
		this.authenticate("nutri1");
		final Diet diet = this.dietService.create();
		diet.setName("");
		final Diet dietSaved = this.dietService.save(diet);
		Assert.isTrue(this.nutritionistService.findByPrincipal().getDiets().contains(dietSaved));

		this.authenticate(null);
	}

	// NEGATIVE- authenticated as provider
	@Test(expected = IllegalArgumentException.class)
	public void createDietNegativeTest2() {
		this.authenticate("provider1");
		final Diet diet = this.dietService.create();
		diet.setName("Example");
		final Diet dietSaved = this.dietService.save(diet);
		Assert.isTrue(this.nutritionistService.findByPrincipal().getDiets().contains(dietSaved));

		this.authenticate(null);
	}

	// Remove diet
	// POSITIVE
	@Test
	public void deleteDietPositiveTest() {
		this.authenticate("nutri1");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		final Diet diet = this.dietService.findOne(this.getEntityId("diet1"));
		this.dietService.delete(diet);
		Assert.isTrue(!nutritionist.getDiets().contains(diet));

		this.authenticate(null);
	}

	// NEGATIVE- Deleting another nutritionist's diet
	@Test(expected = IllegalArgumentException.class)
	public void deleteDietNegativeTest() {
		this.authenticate("nutri2");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		final Diet diet = this.dietService.findOne(this.getEntityId("diet1"));
		this.dietService.delete(diet);
		Assert.isTrue(!nutritionist.getDiets().contains(diet));

		this.authenticate(null);
	}

	// NEGATIVE- Deleting another nutritionist's diet as user
	@Test(expected = IllegalArgumentException.class)
	public void deleteDietNegativeTest2() {
		this.authenticate("user1");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		final Diet diet = this.dietService.findOne(this.getEntityId("diet1"));
		this.dietService.delete(diet);
		Assert.isTrue(!nutritionist.getDiets().contains(diet));

		this.authenticate(null);
	}

	// Edit nutritionist data
	// POSITIVE
	@Test
	public void editNutritionitsPositiveTest() {
		this.authenticate("nutri1");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		nutritionist.setName("Example");
		this.nutritionistService.save(nutritionist);
		Assert.isTrue(nutritionist.getName().equals("Example"));

		this.authenticate(null);
	}

	// NEGATIVE-Name in blank
	@Test(expected = IllegalArgumentException.class)
	public void editNutritionitsNegativeTest() {
		this.authenticate("nutri1");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		nutritionist.setName("");
		this.nutritionistService.save(nutritionist);
		Assert.isTrue(!nutritionist.getName().equals(""));

		this.authenticate(null);
	}

	// NEGATIVE-Change data of other nutritionist
	@Test(expected = AssertionError.class)
	public void editNutritionitsNegativeTest2() {
		this.authenticate("nutri1");
		final Nutritionist nutritionist = this.nutritionistService.findOne(this.getEntityId("nutri2"));
		nutritionist.setName("Example");
		this.nutritionistService.save(nutritionist);
		Assert.isTrue(!nutritionist.getName().equals("Example"));

		this.authenticate(null);
	}

	// Delete nutritionist
	// POSITIVE
	@Test
	public void deleteNutritionitsPositiveTest() {
		this.authenticate("nutri2");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		this.nutritionistService.delete(nutritionist);

		this.authenticate(null);
	}

	// Delete nutritionist
	// NEGATIVE- Try to delete another nutritionist account
	@Test(expected = AssertionError.class)
	public void deleteNutritionitsNegativeTest() {
		this.authenticate("nutri1");
		final Nutritionist nutritionist = this.nutritionistService.findOne(this.getEntityId("nutri2"));
		this.nutritionistService.delete(nutritionist);

		this.authenticate(null);
	}

	// Delete nutritionist
	// NEGATIVE- Try to delete nutritionist account authenticated as user
	@Test(expected = AssertionError.class)
	public void deleteNutritionitsNegativeTest2() {
		this.authenticate("user1");
		final Nutritionist nutritionist = this.nutritionistService.findOne(this.getEntityId("nutri1"));
		this.nutritionistService.delete(nutritionist);

		this.authenticate(null);
	}

	// Edit schedule
	// POSITIVE
	@Test
	public void editSchedulePositiveTest() {
		this.authenticate("nutri1");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		this.nutritionistService.addDaySchedule("MONDAY", "10:00", "11:00", "15:00", "19:00");
		final List<DaySchedule> schedule = nutritionist.getSchedule();
		final DaySchedule monday = schedule.get(0);
		Assert.isTrue(monday.getMorningStart().equals("10:00") && monday.getMorningEnd().equals("11:00") && monday.getAfternoonStart().equals("15:00") && monday.getAfternoonEnd().equals("19:00"));
		this.authenticate(null);
	}

	// NEGATIVE- Incorrect schedule
	@Test(expected = IllegalArgumentException.class)
	public void editScheduleNegativeTest() {
		this.authenticate("nutri1");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		this.nutritionistService.addDaySchedule("MONDAY", "11:00", "10:00", "15:00", "19:00");
		final List<DaySchedule> schedule = nutritionist.getSchedule();
		final DaySchedule monday = schedule.get(0);
		Assert.isTrue(!monday.getMorningStart().equals("11:00") && !monday.getMorningEnd().equals("10:00") && monday.getAfternoonStart().equals("15:00") && monday.getAfternoonEnd().equals("19:00"));
		this.authenticate(null);
	}

	// NEGATIVE- Editing other nutritionist's schedule as user
	@Test(expected = IllegalArgumentException.class)
	public void editScheduleNegativeTest2() {
		this.authenticate("user1");
		final Nutritionist nutritionist = this.nutritionistService.findByPrincipal();
		this.nutritionistService.addDaySchedule("MONDAY", "10:00", "11:00", "15:00", "19:00");
		final List<DaySchedule> schedule = nutritionist.getSchedule();
		final DaySchedule monday = schedule.get(0);
		Assert.isTrue(!monday.getMorningStart().equals("10:00") && !monday.getMorningEnd().equals("11:00") && !monday.getAfternoonStart().equals("15:00") && !monday.getAfternoonEnd().equals("19:00"));
		this.authenticate(null);
	}

	// POSITIVE - List appointments
	@Test
	public void listAppointmentPositiveTest() {
		this.authenticate("nutri1");
		final Collection<Appointment> apps = this.nutritionistService.findByPrincipal().getAppointments();
		final Appointment ap = (Appointment) apps.toArray()[0];
		Assert.isTrue(ap.getNutritionist().equals(this.nutritionistService.findByPrincipal()));
		Assert.isTrue(apps.size() == 1);
		this.authenticate(null);
	}

	// NEGATIVE - List other nutritionist's appointments
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void listAppointmentNegativeTestOtherAppointments() {
		this.authenticate("nutri1");
		final Collection<Appointment> apps = this.nutritionistService.findOne(this.getEntityId("nutritionist2")).getAppointments();
		final Appointment ap = (Appointment) apps.toArray()[0];
		Assert.isTrue(ap.getNutritionist().equals(this.nutritionistService.findByPrincipal()));
		Assert.isTrue(apps.size() == 1);
		this.authenticate(null);
	}

	// NEGATIVE - Incorrect number of appointments
	@Test(expected = IllegalArgumentException.class)
	public void listAppointmentNegativeTestIncorrectNumberAppointments() {
		this.authenticate("nutri1");
		final Collection<Appointment> apps = this.nutritionistService.findOne(this.getEntityId("nutritionist1")).getAppointments();
		final Appointment ap = (Appointment) apps.toArray()[0];
		Assert.isTrue(ap.getNutritionist().equals(this.nutritionistService.findByPrincipal()));
		apps.remove(ap);
		Assert.isTrue(apps.size() == 1);
		this.authenticate(null);
	}

	// POSITIVE - Reject appointment
	@Test
	public void rejectAppointmentPositiveTest() {
		this.authenticate("nutri1");
		final Collection<Appointment> apps = this.nutritionistService.findOne(this.getEntityId("nutritionist1")).getAppointments();
		final Appointment ap = (Appointment) apps.toArray()[0];
		ap.setCancelReason("Test");
		final Appointment apSaved = this.appointmentService.cancel(ap);
		Assert.isTrue(apSaved.isCancelled() && apSaved.getCancelReason().equals(ap.getCancelReason()));

		this.authenticate(null);
	}

	// NEGATIVE - Create appointment on sunday
	@Test(expected = IllegalArgumentException.class)
	public void rejectAppointmentNegativeTestAlreadyCancelled() {
		this.authenticate("nutri1");
		final Collection<Appointment> apps = this.nutritionistService.findOne(this.getEntityId("nutritionist1")).getAppointments();
		final Appointment ap = (Appointment) apps.toArray()[0];
		ap.setCancelled(true);
		ap.setCancelReason("Test");
		final Appointment apSaved = this.appointmentService.cancel(ap);
		Assert.isTrue(apSaved.isCancelled() && apSaved.getCancelReason().equals(ap.getCancelReason()));

		this.authenticate(null);
	}

	// NEGATIVE - Make appointment out of nutritionist's schedule
	@Test(expected = IllegalArgumentException.class)
	public void rejectAppointmentNegativeTestOtherNutritionistAppointment() {
		this.authenticate("nutri2");
		final Collection<Appointment> apps = this.nutritionistService.findOne(this.getEntityId("nutritionist1")).getAppointments();
		final Appointment ap = (Appointment) apps.toArray()[0];
		ap.setCancelReason("Test");
		final Appointment apSaved = this.appointmentService.cancel(ap);
		Assert.isTrue(apSaved.isCancelled() && apSaved.getCancelReason().equals(ap.getCancelReason()));

		this.authenticate(null);
	}

	// Display diet
	// POSITIVE
	@Test
	public void displayDietPositiveTest() {
		this.authenticate("nutri1");
		final Diet diet = this.dietService.findOne(this.getEntityId("diet1"));
		Assert.notNull(diet);
		this.authenticate(null);
	}

	// NEGATIVE - Displaying a non existent diet
	@Test(expected = AssertionError.class)
	public void displayDietNegativeTest() {
		this.authenticate("nutri1");
		final Diet diet = this.dietService.findOne(this.getEntityId("diet99"));
		Assert.notNull(diet);
		this.authenticate(null);
	}

	// Add meal to diet
	// ***********************************************************************************************************
	@Test
	public void driverAddMeal() {
		final String image = "https://static.vix.com/es/sites/default/files/styles/large/public/btg/curiosidades.batanga.com/files/Por-que-nuestro-cerebro-ama-la-comida-chatarra-00.jpg";

		final Object testingData[][] = {
			// POSITIVE
			{
				"nutri1", "diet1", "Test food", 250.0, 120, image, null
			},
			// NEGATIVE - The principal is not a nutritionist
			{
				"user1", "diet1", "Test food", 250.0, 120, image, IllegalArgumentException.class
			},
			// NEGATIVE - The calories are an invalid number
			{
				"nutri1", "diet1", "Test food", -250.0, 120, image, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddMeal((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (double) testingData[i][3], (int) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateAddMeal(final String username, final String entity, final String name, final double calories, final int amount, final String image, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);

			final int dietId = this.getEntityId(entity);
			final Diet diet = this.dietService.findOne(dietId);

			final List<DailyDiet> dailyDiets = new ArrayList<DailyDiet>(diet.getDailyDiets());
			final int dayId = dailyDiets.get(0).getId();
			final DailyDiet day = this.dayService.findOneToEdit(dayId);

			final Food food = this.foodService.create();
			food.setName(name);
			food.setCalories(calories);
			food.setTime(FoodTime.BREAKFAST);
			food.setAmount(amount);
			food.setImage(image);

			final Food stored = this.foodService.save(food);
			this.foodService.flush();

			day.getFoods().add(stored);
			this.dayService.save(day);

			Assert.notNull(this.foodService.findOne(stored.getId()));
			Assert.isTrue(day.getFoods().contains(stored));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Delete meal
	// ***********************************************************************************************************
	@Test
	public void driverDeleteMeal() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"nutri1", "food1M", null
			},
			// NEGATIVE - The principal is not a nutritionist
			{
				"admin", "food1M", IllegalArgumentException.class
			},
			// NEGATIVE - The principal is a nutritionist but is not the food's creator
			{
				"nutri2", "food1M", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteMeal((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDeleteMeal(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);

			final int foodId = this.getEntityId(entity);
			final Food food = this.foodService.findOne(foodId);

			this.foodService.delete(food);
			this.foodService.flush();

			Assert.isTrue(!new HashSet<Food>(this.foodService.findAll()).contains(food));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// List diets
	// ***********************************************************************************************************
	// POSITIVE: List diets
	@Test
	public void listDietsPositive() {
		this.authenticate("nutri1");
		final Collection<Diet> diets = this.nutritionistService.findByPrincipal().getDiets();
		Assert.isTrue(diets.size() == 1);
		this.authenticate(null);
	}

	// NEGATIVE: List diets of non-validated nutritionist
	@Test(expected = IllegalArgumentException.class)
	public void listDietsNegativeTestNonValidated() {
		this.authenticate("nutri1");
		final Collection<Diet> diets = this.nutritionistService.findOne(this.getEntityId("nutritionist2")).getDiets();
		Assert.isTrue(diets.size() == 1);
		this.authenticate(null);
	}

	// NEGATIVE: List diets without being authenticated
	@Test(expected = IllegalArgumentException.class)
	public void listDietsNegativeTestUnauthenticated() {
		final Collection<Diet> diets = this.nutritionistService.findByPrincipal().getDiets();
		Assert.isTrue(diets.size() == 1);
	}

}
