
package usecases;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActivityService;
import services.ActorService;
import services.AdministratorService;
import services.ArticleRatingService;
import services.ArticleService;
import services.CommentService;
import services.EditorService;
import services.GymRatingService;
import services.GymService;
import services.ManagerService;
import services.NutritionistService;
import services.ProductService;
import services.ProviderService;
import services.SubscriptionService;
import utilities.AbstractTest;
import domain.Activity;
import domain.Actor;
import domain.Administrator;
import domain.Article;
import domain.ArticleRating;
import domain.Comment;
import domain.DayName;
import domain.DaySchedule;
import domain.Editor;
import domain.Gym;
import domain.GymRating;
import domain.Manager;
import domain.Nutritionist;
import domain.Product;
import domain.Provider;
import domain.Subscription;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminTest extends AbstractTest {

	// System under test

	@Autowired
	private AdministratorService	adminService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ProviderService			providerService;

	@Autowired
	private EditorService			editorService;

	@Autowired
	private NutritionistService		nutriService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private GymService				gymService;

	@Autowired
	private GymRatingService		gymRatingService;

	@Autowired
	private ActivityService			activityService;

	@Autowired
	private SubscriptionService		subscriptionService;

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private ArticleRatingService	articleRatingService;

	@Autowired
	private CommentService			commentService;

	@Autowired
	private ProductService			productService;

	DecimalFormat					df	= new DecimalFormat("#.##");	// To get only 2 decimals


	// ------------------------------------------------------ TESTS
	// ------------------------------------------------------------------

	// Edit admin's data
	// ***********************************************************************************************************
	// POSITIVE: Edit admin's data
	@Test
	public void editAdminPositive() {
		this.authenticate("admin");
		final Administrator admin = this.adminService.findByPrincipal();
		admin.setName("New name");
		final Administrator stored = this.adminService.save(admin);
		Assert.isTrue(stored.getName().equals("New name"));
		this.authenticate(null);
	}

	// NEGATIVE: Edit admin's data invalid email
	@Test(expected = ConstraintViolationException.class)
	public void editAdminNegativeInvalidEmail() {
		this.authenticate("admin");
		final Administrator admin = this.adminService.findByPrincipal();
		admin.setEmail("Invalid email");
		final Administrator stored = this.adminService.save(admin);
		Assert.isTrue(!stored.getEmail().equals("Invalid email"));
		this.authenticate(null);
	}

	// NEGATIVE: Edit admin's data as user
	@Test(expected = IllegalArgumentException.class)
	public void editAdminNegativeUser() {
		this.authenticate("user1");
		final Administrator admin = this.adminService.findByPrincipal();
		admin.setEmail("Invalid email");
		this.adminService.save(admin);
		this.authenticate(null);
	}

	// Register manager
	// ***********************************************************************************************************
	// POSITIVE: Register manager
	@Test
	public void registerManagerPositive() {
		this.authenticate("admin");
		final Manager manager = this.managerService.create();
		manager.getUserAccount().setUsername("manager");
		manager.getUserAccount().setPassword("password");
		manager.setName("name");
		manager.setSurname("surname");
		manager.setEmail("valid@email.com");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		manager.setBirthdate(birthdate);
		manager.setArticleRatings(new HashSet<ArticleRating>());

		final Manager stored = this.managerService.save(manager);
		Assert.notNull(this.managerService.findOne(stored.getId()));
		this.authenticate(null);
	}

	// NEGATIVE: Register manager invalid email
	@Test(expected = ConstraintViolationException.class)
	public void registerManagerNegativeInvalidEmail() {
		this.authenticate("admin");
		final Manager manager = this.managerService.create();
		manager.getUserAccount().setUsername("manager");
		manager.getUserAccount().setPassword("password");
		manager.setName("name");
		manager.setSurname("surname");
		manager.setEmail("invalid email");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		manager.setBirthdate(birthdate);
		manager.setArticleRatings(new HashSet<ArticleRating>());

		this.managerService.save(manager);
		this.managerService.flush();
		this.authenticate(null);
	}

	// NEGATIVE: Register manager invalid username
	@Test(expected = DataIntegrityViolationException.class)
	public void registerManagerNegativeInvalidUsername() {
		this.authenticate("admin");
		final Manager manager = this.managerService.create();
		manager.getUserAccount().setUsername("manager1");
		manager.getUserAccount().setPassword("password");
		manager.setName("name");
		manager.setSurname("surname");
		manager.setEmail("valid@email.com");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		manager.setBirthdate(birthdate);
		manager.setArticleRatings(new HashSet<ArticleRating>());

		this.managerService.save(manager);
		this.managerService.flush();
		this.authenticate(null);
	}

	// Register provider
	// ***********************************************************************************************************
	// POSITIVE: Register provider
	@Test
	public void registerProviderPositive() {
		this.authenticate("admin");
		final Provider provider = this.providerService.create();
		provider.getUserAccount().setUsername("provider");
		provider.getUserAccount().setPassword("password");
		provider.setName("name");
		provider.setSurname("surname");
		provider.setEmail("valid@email.com");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		provider.setBirthdate(birthdate);
		provider.setArticleRatings(new HashSet<ArticleRating>());

		final Provider stored = this.providerService.save(provider);
		Assert.notNull(this.providerService.findOne(stored.getId()));
		this.authenticate(null);
	}

	// NEGATIVE: Register provider invalid email
	@Test(expected = ConstraintViolationException.class)
	public void registerProviderNegativeInvalidEmail() {
		this.authenticate("admin");
		final Provider provider = this.providerService.create();
		provider.getUserAccount().setUsername("provider");
		provider.getUserAccount().setPassword("password");
		provider.setName("name");
		provider.setSurname("surname");
		provider.setEmail("invalid email");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		provider.setBirthdate(birthdate);
		provider.setArticleRatings(new HashSet<ArticleRating>());

		this.providerService.save(provider);
		this.providerService.flush();
		this.authenticate(null);
	}

	// NEGATIVE: Register provider invalid username
	@Test(expected = DataIntegrityViolationException.class)
	public void registerProviderNegativeInvalidUsername() {
		this.authenticate("admin");
		final Provider provider = this.providerService.create();
		provider.getUserAccount().setUsername("provider1");
		provider.getUserAccount().setPassword("password");
		provider.setName("name");
		provider.setSurname("surname");
		provider.setEmail("invalid email");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		provider.setBirthdate(birthdate);
		provider.setArticleRatings(new HashSet<ArticleRating>());

		this.providerService.save(provider);
		this.providerService.flush();
		this.authenticate(null);
	}

	// Register editor
	// ***********************************************************************************************************
	// POSITIVE: Register editor
	@Test
	public void registerEditorPositive() {
		this.authenticate("admin");
		final Editor editor = this.editorService.create();
		editor.getUserAccount().setUsername("editor");
		editor.getUserAccount().setPassword("password");
		editor.setName("name");
		editor.setSurname("surname");
		editor.setEmail("valid@email.com");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		editor.setBirthdate(birthdate);
		editor.setArticleRatings(new HashSet<ArticleRating>());

		final Editor stored = this.editorService.save(editor);
		Assert.notNull(this.editorService.findOne(stored.getId()));
		this.authenticate(null);
	}

	// NEGATIVE: Register editor invalid email
	@Test(expected = ConstraintViolationException.class)
	public void registerEditorNegativeInvalidEmail() {
		this.authenticate("admin");
		final Editor editor = this.editorService.create();
		editor.getUserAccount().setUsername("editor");
		editor.getUserAccount().setPassword("password");
		editor.setName("name");
		editor.setSurname("surname");
		editor.setEmail("invalid email");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		editor.setBirthdate(birthdate);
		editor.setArticleRatings(new HashSet<ArticleRating>());

		this.editorService.save(editor);
		this.editorService.flush();
		this.authenticate(null);
	}

	// NEGATIVE: Register editor invalid username
	@Test(expected = DataIntegrityViolationException.class)
	public void registerEditorNegativeInvalidUsername() {
		this.authenticate("admin");
		final Editor editor = this.editorService.create();
		editor.getUserAccount().setUsername("editor1");
		editor.getUserAccount().setPassword("password");
		editor.setName("name");
		editor.setSurname("surname");
		editor.setEmail("valid@email.com");
		final Calendar birthdate = Calendar.getInstance();
		birthdate.set(1996, 06, 07);
		editor.setBirthdate(birthdate);
		editor.setArticleRatings(new HashSet<ArticleRating>());

		this.editorService.save(editor);
		this.editorService.flush();
		this.authenticate(null);
	}

	// Validate nutritionists
	// ***********************************************************************************************************
	// POSITIVE: Validate nutritionists
	@Test
	public void validateNutritionistPositive() {
		this.authenticate("admin");
		final Nutritionist nutri = this.nutriService.findOne(this.getEntityId("nutritionist2"));
		final List<DaySchedule> schedule = new ArrayList<>();
		final DaySchedule monday = new DaySchedule();
		monday.setDay(DayName.MONDAY);
		monday.setMorningStart("09:00");
		monday.setMorningEnd("10:00");
		monday.setAfternoonStart("17:00");
		monday.setAfternoonEnd("19:00");
		final DaySchedule tuesday = new DaySchedule();
		tuesday.setDay(DayName.TUESDAY);
		tuesday.setMorningStart("09:00");
		tuesday.setMorningEnd("10:00");
		tuesday.setAfternoonStart("17:00");
		tuesday.setAfternoonEnd("19:00");
		final DaySchedule wednesday = new DaySchedule();
		wednesday.setDay(DayName.WEDNESDAY);
		wednesday.setMorningStart("09:00");
		wednesday.setMorningEnd("10:00");
		wednesday.setAfternoonStart("17:00");
		wednesday.setAfternoonEnd("19:00");
		final DaySchedule thursday = new DaySchedule();
		thursday.setDay(DayName.THURSDAY);
		thursday.setMorningStart("09:00");
		thursday.setMorningEnd("10:00");
		thursday.setAfternoonStart("17:00");
		thursday.setAfternoonEnd("19:00");
		final DaySchedule friday = new DaySchedule();
		friday.setDay(DayName.FRIDAY);
		friday.setMorningStart("09:00");
		friday.setMorningEnd("10:00");
		friday.setAfternoonStart("17:00");
		friday.setAfternoonEnd("19:00");
		final DaySchedule saturday = new DaySchedule();
		saturday.setDay(DayName.SATURDAY);
		saturday.setMorningStart("09:00");
		saturday.setMorningEnd("10:00");
		saturday.setAfternoonStart("17:00");
		saturday.setAfternoonEnd("19:00");

		schedule.add(monday);
		schedule.add(tuesday);
		schedule.add(wednesday);
		schedule.add(thursday);
		schedule.add(friday);
		schedule.add(saturday);

		nutri.setSchedule(schedule);

		final Nutritionist stored = this.nutriService.save(nutri);

		final Nutritionist validated = this.nutriService.validate(stored.getId());
		Assert.isTrue(validated.isValidated());
		this.authenticate(null);
	}

	// NEGATIVE: Validate nutritionists no schedule
	@Test(expected = IllegalArgumentException.class)
	public void validateNutritionistNegativeNoSchedule() {
		this.authenticate("admin");
		final Nutritionist nutri = this.nutriService.findOne(this.getEntityId("nutritionist2"));

		final Nutritionist validated = this.nutriService.validate(nutri.getId());
		Assert.isTrue(validated.isValidated());
		this.authenticate(null);
	}

	// NEGATIVE: Validate nutritionists as nutritionist
	@Test(expected = IllegalArgumentException.class)
	public void validateNutritionistNegativeAsNutritionist() {
		this.authenticate("nutritionist1");
		final Nutritionist nutri = this.nutriService.findOne(this.getEntityId("nutritionist2"));

		final Nutritionist validated = this.nutriService.validate(nutri.getId());
		Assert.isTrue(validated.isValidated());
		this.authenticate(null);
	}

	// Ban actors
	// ***********************************************************************************************************
	// POSITIVE: Ban actors
	@Test
	public void banActorPositive() {
		this.authenticate("admin");
		final Actor actor = this.actorService.findOne(this.getEntityId("user18"));
		final Actor banned = this.actorService.banActor(actor.getId());
		Assert.isTrue(banned.isBanned());
		this.authenticate(null);
	}

	// NEGATIVE: Ban actor already banned
	@Test(expected = IllegalArgumentException.class)
	public void banActorNegativeAlreadyBanned() {
		this.authenticate("admin");
		final Actor actor = this.actorService.findOne(this.getEntityId("user19"));
		final Actor banned = this.actorService.banActor(actor.getId());
		Assert.isTrue(banned.isBanned());
		this.authenticate(null);
	}

	// NEGATIVE: Ban actor as manager
	@Test(expected = IllegalArgumentException.class)
	public void banActorNegativeAsManager() {
		this.authenticate("manager1");
		final Actor actor = this.actorService.findOne(this.getEntityId("user18"));
		final Actor banned = this.actorService.banActor(actor.getId());
		Assert.isTrue(banned.isBanned());
		this.authenticate(null);
	}

	// Delete gym
	// ***********************************************************************************************************
	// POSITIVE: Delete gym
	@Test
	public void deleteGymPositive() {
		this.authenticate("admin");
		this.setUpAuth("admin");

		final Gym gym = this.gymService.findOne(this.getEntityId("gym1"));

		final Collection<GymRating> gymRatings = new HashSet<GymRating>(gym.getGymRatings());

		for (final GymRating gr : gymRatings)
			this.gymRatingService.delete(gr);

		final Collection<Activity> gymActivities = new ArrayList<Activity>(gym.getActivities());

		for (final Activity a : gymActivities)
			this.activityService.delete(a);

		final Collection<Subscription> subscriptions = new ArrayList<Subscription>(gym.getSubscriptions());

		for (final Subscription s : subscriptions)
			this.subscriptionService.delete(s);

		this.gymService.delete(gym);

		this.authenticate(null);
	}

	// NEGATIVE: Delete gym that doesn't exist
	@Test(expected = AssertionError.class)
	public void deleteGymNegativeInvalidGym() {
		this.authenticate("admin");

		final Gym gym = this.gymService.findOne(this.getEntityId("gym10"));

		final Collection<GymRating> gymRatings = new HashSet<GymRating>(gym.getGymRatings());

		for (final GymRating gr : gymRatings)
			this.gymRatingService.delete(gr);

		final Collection<Activity> gymActivities = new ArrayList<Activity>(gym.getActivities());

		for (final Activity a : gymActivities)
			this.activityService.delete(a);

		final Collection<Subscription> subscriptions = new ArrayList<Subscription>(gym.getSubscriptions());

		for (final Subscription s : subscriptions)
			this.subscriptionService.delete(s);

		this.gymService.delete(gym);

		this.authenticate(null);
	}

	// NEGATIVE: Delete gym as user
	@Test(expected = IllegalArgumentException.class)
	public void deleteGymNegativeAsUser() {
		this.authenticate("user1");
		this.setUpAuth("user1");

		final Gym gym = this.gymService.findOne(this.getEntityId("gym1"));

		final Collection<GymRating> gymRatings = new HashSet<GymRating>(gym.getGymRatings());

		for (final GymRating gr : gymRatings)
			this.gymRatingService.delete(gr);

		final Collection<Activity> gymActivities = new ArrayList<Activity>(gym.getActivities());

		for (final Activity a : gymActivities)
			this.activityService.delete(a);

		final Collection<Subscription> subscriptions = new ArrayList<Subscription>(gym.getSubscriptions());

		for (final Subscription s : subscriptions)
			this.subscriptionService.delete(s);

		this.gymService.delete(gym);

		this.authenticate(null);
	}

	// Delete article
	// ***********************************************************************************************************
	// POSITIVE: Delete article
	@Test
	public void deleteArticlePositive() {
		this.authenticate("admin");
		this.setUpAuth("admin");

		final Article article = this.articleService.findOne(this.getEntityId("article1"));

		final Collection<ArticleRating> articleRatings = new ArrayList<ArticleRating>(article.getArticleRatings());
		final Collection<Comment> comments = new ArrayList<Comment>(this.commentService.findAllParentsByArticle(article.getId()));

		for (final ArticleRating ar : articleRatings)
			this.articleRatingService.delete(ar);

		for (final Comment c : comments) {
			if (!c.getReplies().isEmpty()) {
				final java.util.Iterator<Comment> iter = c.getReplies().iterator();
				while (iter.hasNext()) {
					final Comment reply = iter.next();
					iter.remove();
					this.commentService.delete(reply);
				}
			}
			this.commentService.delete(c);
		}

		this.articleService.delete(article);

		this.authenticate(null);
	}

	// NEGATIVE: Delete article that doesn't exist
	@Test(expected = AssertionError.class)
	public void deleteArticleNegativeInvalidArticle() {
		this.authenticate("admin");

		final Article article = this.articleService.findOne(this.getEntityId("article10"));

		final Collection<ArticleRating> articleRatings = new ArrayList<ArticleRating>(article.getArticleRatings());
		final Collection<Comment> comments = new ArrayList<Comment>(this.commentService.findAllParentsByArticle(article.getId()));

		for (final ArticleRating ar : articleRatings)
			this.articleRatingService.delete(ar);

		for (final Comment c : comments) {
			if (!c.getReplies().isEmpty()) {
				final java.util.Iterator<Comment> iter = c.getReplies().iterator();
				while (iter.hasNext()) {
					final Comment reply = iter.next();
					iter.remove();
					this.commentService.delete(reply);
				}
			}
			this.commentService.delete(c);
		}

		this.articleService.delete(article);

		this.authenticate(null);
	}

	// NEGATIVE: Delete article as user
	@Test(expected = IllegalArgumentException.class)
	public void deleteArticleNegativeAsUser() {
		this.authenticate("user1");
		this.setUpAuth("user1");

		final Article article = this.articleService.findOne(this.getEntityId("article1"));

		final Collection<ArticleRating> articleRatings = new ArrayList<ArticleRating>(article.getArticleRatings());
		final Collection<Comment> comments = new ArrayList<Comment>(this.commentService.findAllParentsByArticle(article.getId()));

		for (final ArticleRating ar : articleRatings)
			this.articleRatingService.delete(ar);

		for (final Comment c : comments) {
			if (!c.getReplies().isEmpty()) {
				final java.util.Iterator<Comment> iter = c.getReplies().iterator();
				while (iter.hasNext()) {
					final Comment reply = iter.next();
					iter.remove();
					this.commentService.delete(reply);
				}
			}
			this.commentService.delete(c);
		}

		this.articleService.delete(article);

		this.articleService.delete(article);

		this.authenticate(null);
	}

	// Delete comment
	// ***********************************************************************************************************
	// POSITIVE: Delete comment
	@Test
	public void deleteCommentPositive() {
		this.authenticate("admin");
		this.setUpAuth("admin");

		final Comment comment = this.commentService.findOne(this.getEntityId("comment1"));
		final java.util.Iterator<Comment> iter = comment.getReplies().iterator();

		while (iter.hasNext()) {
			final Comment reply = iter.next();
			iter.remove();
			this.commentService.delete(reply);
		}

		this.commentService.delete(comment);

		this.authenticate(null);
	}

	// NEGATIVE: Delete comment without removing its replies
	@Test(expected = DataIntegrityViolationException.class)
	public void deleteCommentNegativeNoRemovingReplies() {
		this.authenticate("admin");
		this.setUpAuth("admin");

		final Comment comment = this.commentService.findOne(this.getEntityId("comment1"));

		this.commentService.delete(comment);

		this.authenticate(null);
	}

	// NEGATIVE: Delete comment as manager
	@Test(expected = IllegalArgumentException.class)
	public void deleteCommentNegativeAsManager() {
		this.authenticate("manager1");
		this.setUpAuth("manager1");

		final Comment comment = this.commentService.findOne(this.getEntityId("comment1"));
		final java.util.Iterator<Comment> iter = comment.getReplies().iterator();

		while (iter.hasNext()) {
			final Comment reply = iter.next();
			iter.remove();
			this.commentService.delete(reply);
		}

		this.commentService.delete(comment);

		this.authenticate(null);
	}

	// Delete product
	// ***********************************************************************************************************
	// POSITIVE: Delete product
	@Test
	public void deleteProductPositive() {
		this.authenticate("admin");
		this.setUpAuth("admin");

		final Product product = this.productService.findOne(this.getEntityId("product1"));

		this.productService.delete(product);

		this.authenticate(null);
	}

	// NEGATIVE: Delete product that doesn't exists
	@Test(expected = AssertionError.class)
	public void deleteProductNegativeInvalidProduct() {
		this.authenticate("admin");

		final Product product = this.productService.findOne(this.getEntityId("product10"));

		this.productService.delete(product);

		this.authenticate(null);
	}

	// NEGATIVE: Delete product as manager
	@Test(expected = IllegalArgumentException.class)
	public void deleteProductNegativeAsManager() {
		this.authenticate("manager1");
		this.setUpAuth("manager1");

		final Product product = this.productService.findOne(this.getEntityId("product1"));

		this.productService.delete(product);

		this.authenticate(null);
	}
}
