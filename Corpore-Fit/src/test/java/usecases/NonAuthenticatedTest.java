package usecases;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

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
import services.ArticleService;
import services.GymService;
import services.NutritionistService;
import services.UserService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Appointment;
import domain.Article;
import domain.ArticleRating;
import domain.CreditCard;
import domain.DayName;
import domain.DaySchedule;
import domain.Diet;
import domain.FriendRequest;
import domain.GymRating;
import domain.Mark;
import domain.Measure;
import domain.Nutritionist;
import domain.Privacy;
import domain.ProductOrder;
import domain.Role;
import domain.Routine;
import domain.ShoppingCart;
import domain.SocialNetwork;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class NonAuthenticatedTest extends AbstractTest {

	@Autowired
	private GymService gymService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private UserService userService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private NutritionistService nutritionistService;

	@Before
	public void setupAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(
				new AnonymousAuthenticationToken("GUEST", "USERNAME",
						AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
	}

	// Register as an user
	// ***********************************************************************************************************
	@Test
	public void driverRegisterUser() {
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		final Calendar futureBirthdate = Calendar.getInstance();
		futureBirthdate.set(2020, 06, 07);

		final Object testingData[][] = {
				// POSITIVE
				{ "usertest", "usertest", "User", "Test", "valid@email.com",
						birthdate, null },
				// NEGATIVE - Invalid email
				{ "usertest", "usertest", "User", "Test", "invalid.email.com",
						birthdate, DataIntegrityViolationException.class },
				// NEGATIVE - Future birthdate
				{ "usertest", "usertest", "User", "Test", "invalid.email.com",
						futureBirthdate, DataIntegrityViolationException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterUser((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4],
					(Calendar) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	protected void templateRegisterUser(final String username,
			final String pass, final String name, final String surname,
			final String email, final Calendar birthdate,
			final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(null);
			final User user = this.userService.create();
			user.getUserAccount().setUsername(username);
			user.getUserAccount().setPassword(pass);
			user.setName(name);
			user.setSurname(surname);
			user.setEmail(email);
			user.setBirthdate(birthdate);
			user.setRole(Role.CALISTHENICS);
			user.setPrivacy(Privacy.FRIENDS);
			user.setArticleRatings(new HashSet<ArticleRating>());
			user.setMarks(new HashSet<Mark>());
			user.setMeasures(new HashSet<Measure>());
			user.setSentRequests(new HashSet<FriendRequest>());
			user.setReceivedRequests(new HashSet<FriendRequest>());
			user.setRoutine(null);
			user.setDiet(null);
			user.setNutritionist(null);
			user.setSocialNetworks(new HashSet<SocialNetwork>());
			user.setCreditCards(new HashSet<CreditCard>());
			user.setSubscription(null);
			user.setGymRatings(new HashSet<GymRating>());
			user.setAppointments(new HashSet<Appointment>());
			user.setShoppingCart(new HashSet<ShoppingCart>());
			user.setOrders(new HashSet<ProductOrder>());

			final User stored = this.userService.save(user);
			this.userService.flush();
			Assert.notNull(this.userService.findOne(stored.getId()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Register as a nutritionist
	// ***********************************************************************************************************
	@Test
	public void driverRegisterNutritionist() {
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		final Calendar futureBirthdate = Calendar.getInstance();
		futureBirthdate.set(2020, 06, 07);

		final Object testingData[][] = {
				// POSITIVE
				{ "nutritionisttest", "nutritionisttest", "Nutritionist",
						"Test", "valid@email.com", birthdate, "Calle Betis",
						"https://www.google.com", null },
				// NEGATIVE - Invalid email
				{ "nutritionisttest", "nutritionisttest", "Nutritionist",
						"Test", "invalid.email.com", birthdate, "Calle Betis",
						"https://www.google.com",
						DataIntegrityViolationException.class },
				// NEGATIVE - Future birthdate
				{ "nutritionisttest", "nutritionisttest", "Nutritionist",
						"Test", "invalid.email.com", futureBirthdate,
						"Calle Betis", "https://www.google.com",
						DataIntegrityViolationException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterNutritionist((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4],
					(Calendar) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}

	protected void templateRegisterNutritionist(final String nutritionistname,
			final String pass, final String name, final String surname,
			final String email, final Calendar birthdate, final String address,
			final String curriculum, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(null);
			final Nutritionist nutritionist = this.nutritionistService.create();
			nutritionist.getUserAccount().setUsername(nutritionistname);
			nutritionist.getUserAccount().setPassword(pass);
			nutritionist.setName(name);
			nutritionist.setSurname(surname);
			nutritionist.setEmail(email);
			nutritionist.setBirthdate(birthdate);
			nutritionist.setOfficeAddress(address);
			nutritionist.setCurriculum(curriculum);
			nutritionist.setArticleRatings(new HashSet<ArticleRating>());
			nutritionist.setAppointments(new ArrayList<Appointment>());
			nutritionist.setDiets(new ArrayList<Diet>());
			nutritionist.setValidated(false);
			nutritionist.setSchedule(new ArrayList<DaySchedule>());
			DaySchedule ds = new DaySchedule();
			ds.setAfternoonEnd("20:30");
			ds.setAfternoonStart("16:00");
			ds.setDay(DayName.MONDAY);
			ds.setMorningEnd("11:00");
			ds.setMorningStart("08:30");

			nutritionist.getSchedule().add(ds);
			final Nutritionist stored = this.nutritionistService
					.save(nutritionist);
			this.nutritionistService.flush();
			Assert.notNull(this.nutritionistService.findOne(stored.getId()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// List actors
	// ***********************************************************************************************************
	// POSITIVE: List actors
	@Test
	public void listActorsPositive() {
		Collection<Actor> actors = this.actorService.findAllNotBanned();
		Assert.isTrue(actors.size() == 31);
	}

	// NEGATIVE: Try listing banned actors
	@Test(expected = IllegalArgumentException.class)
	public void listActorsNegativeBannedUser() {
		Collection<Actor> actors = this.actorService.findAllBanned();
		Assert.isTrue(actors.size() == 1);
	}

	// NEGATIVE: Try listing all actors (including banned actors)
	@Test(expected = IllegalArgumentException.class)
	public void listActorsNegativeAll() {
		Collection<Actor> actors = this.actorService.findAll();
		Assert.isTrue(actors.size() == 31);
	}

	// Display actor
	// ***********************************************************************************************************
	// POSITIVE: Display actor
	@Test
	public void displayActorPositive() {
		Collection<Actor> actors = this.actorService.findAllNotBanned();
		Assert.isTrue(actors.size() == 31);

		Actor actor = (Actor) actors.toArray()[0];
		Assert.notNull(actor);
	}

	// NEGATIVE: Try displaying non-existing actor
	@Test(expected = AssertionError.class)
	public void displayActorNegativeNonExistent() {
		Actor actor = this.actorService.findOne(getEntityId("user100"));
		Assert.notNull(actor);
	}

	// NEGATIVE: Try displaying banned actor
	@Test(expected = IllegalArgumentException.class)
	public void displayActorNegativeBanned() {
		Actor actor = this.userService.findOne(getEntityId("user19"));
		Assert.isTrue(!actor.isBanned());
		Assert.notNull(actor);
	}

	// Display user's measures
	// ***********************************************************************************************************
	// POSITIVE: Display user's measures
	@Test
	public void displayMeasuresPositive() {
		User user = this.userService.findOne(getEntityId("user1"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));

		Collection<Measure> measures = user.getMeasures();
		Assert.isTrue(!measures.isEmpty());
	}

	// NEGATIVE: Try displaying non-existing user's measures
	@Test(expected = AssertionError.class)
	public void displayMeasuresNegativeNonExistent() {
		User user = this.userService.findOne(getEntityId("user100"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));

		Collection<Measure> measures = user.getMeasures();
		Assert.isTrue(!measures.isEmpty());
	}

	// NEGATIVE: Try displaying private user's measures
	@Test(expected = IllegalArgumentException.class)
	public void displayMeasuresNegativePrivateUSer() {
		User user = this.userService.findOne(getEntityId("user2"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));

		Collection<Measure> measures = user.getMeasures();
		Assert.isTrue(!measures.isEmpty());
	}

	// Display user's marks
	// ***********************************************************************************************************
	// POSITIVE: Display user's marks
	@Test
	public void displayMarksPositive() {
		User user = this.userService.findOne(getEntityId("user1"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));

		Collection<Mark> marks = user.getMarks();
		Assert.isTrue(!marks.isEmpty());
	}

	// NEGATIVE: Try displaying non-existing user's marks
	@Test(expected = AssertionError.class)
	public void displayMarksNegativeNonExistent() {
		User user = this.userService.findOne(getEntityId("user100"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));

		Collection<Mark> marks = user.getMarks();
		Assert.isTrue(!marks.isEmpty());
	}

	// NEGATIVE: Try displaying private user's marks
	@Test(expected = IllegalArgumentException.class)
	public void displayMarksNegativePrivateUSer() {
		User user = this.userService.findOne(getEntityId("user2"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));

		Collection<Mark> marks = user.getMarks();
		Assert.isTrue(!marks.isEmpty());
	}

	// Display user's routine
	// ***********************************************************************************************************
	// POSITIVE: Display user's routine
	@Test
	public void displayRoutinePositive() {
		User user = this.userService.findOne(getEntityId("user1"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));
		Routine routine = user.getRoutine();

		Assert.notNull(routine);
	}

	// NEGATIVE: Try displaying non-existing user's routine
	@Test(expected = AssertionError.class)
	public void displayRoutineNegativeNonExistent() {
		User user = this.userService.findOne(getEntityId("user100"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));
		Routine routine = user.getRoutine();

		Assert.notNull(routine);
	}

	// NEGATIVE: Try displaying private user's routine
	@Test(expected = IllegalArgumentException.class)
	public void displayRoutineNegativePrivate() {
		User user = this.userService.findOne(getEntityId("user2"));
		Assert.notNull(user);
		Assert.isTrue(user.getPrivacy().equals(Privacy.OPEN));
		Routine routine = user.getRoutine();

		Assert.notNull(routine);
	}

	// LIST GYMS
	// *****************************************************************************************************************
	@Test
	public void driverListGyms() {
		final Object testingData[][] = {
				// POSITIVE - Unauthenticated shouldn't fail the test
				{ null, null },
				// NEGATIVE - User does not exist
				{ "user215", IllegalArgumentException.class },
				// NEGATIVE - Manager does not exist
				{ "manager123", IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateListGyms((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
	}

	protected void templateListGyms(final String username,
			final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			this.gymService.findAll();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// DISPLAY GYM
	// *****************************************************************************************************************
	@Test
	public void driverDisplayGym() {
		final Object testingData[][] = {
				// POSITIVE - Unauthenticated shouldn't fail the test
				{ null, "gym1", null },
				// NEGATIVE - User does not exist
				{ "user215", "gym1", IllegalArgumentException.class },
				// NEGATIVE - Null gym
				{ null, null, AssertionError.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateDisplayGym((String) testingData[i][0],
					(String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDisplayGym(final String username,
			final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final int gymId = this.getEntityId(entity);
			this.authenticate(username);
			this.gymService.findOne(gymId);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// List articles
	// ***********************************************************************************************************
	// POSITIVE: List articles
	@Test
	public void listArticlesPositive() {
		Collection<Article> articles = this.articleService.findAllPublished();
		Assert.isTrue(articles.size() == 1);
	}

	// NEGATIVE: Try listing incorrect number of articles
	@Test(expected = IllegalArgumentException.class)
	public void listArticlesNegativeIncorrectNumber() {
		Collection<Article> articles = this.articleService.findAllPublished();
		articles.clear();
		Assert.isTrue(articles.size() == 1);
	}

	// NEGATIVE: Try listing all articles (including non-published articles)
	@Test(expected = IllegalArgumentException.class)
	public void listArticlesNegativeAll() {
		Collection<Article> articles = this.articleService.findAll();
		Assert.isTrue(articles.size() == 1);
	}

	// Display article
	// ***********************************************************************************************************
	@Test
	public void driverDisplayArticle() {
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		final Calendar futureBirthdate = Calendar.getInstance();
		futureBirthdate.set(2020, 06, 07);

		final Object testingData[][] = {
				// POSITIVE
				{ null, "article1", null },
				// NEGATIVE - The article is not published and you are not the
				// editor
				{ "manager1", "article2", IllegalArgumentException.class },
				// NEGATIVE - The article don't exist
				{ "editor1", "article155", IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateDisplayArticle((String) testingData[i][0],
					(String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateDisplayArticle(final String username,
			final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			User principal = null;
			if (username != null) {
				this.authenticate(username);
				principal = this.userService.findByPrincipal();
			}

			final int articleId = this.getEntityId(entity);
			final Article article = this.articleService.findOne(articleId);

			if (article.getPublicationDate() == null)
				Assert.isTrue(article.getEditor().equals(principal));

			if (username != null)
				this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// Display nutritionist's schedule
	// ***********************************************************************************************************
	// POSITIVE: Display nutritionist's schedule
	@Test
	public void displaySchedulePositive() {
		Nutritionist nutritionist = this.nutritionistService
				.findOne(getEntityId("nutritionist1"));
		Assert.notNull(nutritionist);
		Assert.isTrue(nutritionist.isValidated());
		Collection<DaySchedule> schedule = nutritionist.getSchedule();
		Assert.isTrue(schedule.size() == 6);

		Assert.notNull(schedule);
	}

	// NEGATIVE: Try displaying non-existing nutritionist's schedule
	@Test(expected = AssertionError.class)
	public void displayScheduleNegativeNonExistent() {
		Nutritionist nutritionist = this.nutritionistService
				.findOne(getEntityId("nutritionist100"));
		Assert.notNull(nutritionist);
		Assert.isTrue(nutritionist.isValidated());
		Collection<DaySchedule> schedule = nutritionist.getSchedule();

		Assert.notNull(schedule);
	}

	// NEGATIVE: Try displaying non-validated nutritionist's schedule
	@Test(expected = IllegalArgumentException.class)
	public void displayScheduleNegativePrivate() {
		Nutritionist nutritionist = this.nutritionistService
				.findOne(getEntityId("nutritionist2"));
		Assert.notNull(nutritionist);
		Assert.isTrue(nutritionist.isValidated());
		Collection<DaySchedule> schedule = nutritionist.getSchedule();

		Assert.notNull(schedule);
	}

	// SEARCH ACTORS BY NAME
	// *****************************************************************************************************************
	@Test
	public void driverSearchActorsByName() {
		final Object testingData[][] = {
				// POSITIVE
				{ null, "luis", null },
				// NEGATIVE - User does not exist
				{ "user215", "luis", IllegalArgumentException.class },
				// NEGATIVE - Manager does not exist
				{ "manager123", null, IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateSearchActorsByName((String) testingData[i][0],
					(String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateSearchActorsByName(final String username,
			final String keyword, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			this.actorService.findAllByKeyword(keyword);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// List top 10 users
	// ***********************************************************************************************************
	// POSITIVE: List top 10 users
	@Test
	public void listTopPositive() {
		Collection<User> users = this.userService.findTopBenchPress(Role.CROSSFITTER);
		Assert.isTrue(users.size() == 9);
	}

	// NEGATIVE: Try listing incorrect number of users
	@Test(expected = IllegalArgumentException.class)
	public void listTopNegativeIncorrectNumber() {
		List<User> users = (List<User>) this.userService.findTopBenchPress(Role.CROSSFITTER);
		users.remove(0);
		Assert.isTrue(users.size() == 9);
	}

	// NEGATIVE: Try listing top with wrong order
	@Test(expected = IllegalArgumentException.class)
	public void listTopNegativeIncorrectOrder() {
		List<User> users = (List<User>) this.userService.findTopBenchPress(Role.CROSSFITTER);
		User user = users.get(0);
		users.remove(0);
		users.add(user);
		Assert.isTrue(users.get(0).equals(user));
	}

	// TOP 5 GYMS
	// *****************************************************************************************************************
	@Test
	public void driverTop5Gyms() {
		final Object testingData[][] = {
				// POSITIVE - Unauthenticated shouldn't fail the test
				{ null, null },
				// NEGATIVE - User does not exist
				{ "user215", IllegalArgumentException.class },
				// NEGATIVE - Manager does not exist
				{ "manager123", IllegalArgumentException.class } };
		for (int i = 0; i < testingData.length; i++)
			this.templateTop5Gyms((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
	}

	protected void templateTop5Gyms(final String username,
			final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			this.gymService.findTop5();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
