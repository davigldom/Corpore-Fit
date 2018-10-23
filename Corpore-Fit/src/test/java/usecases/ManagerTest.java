
package usecases;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

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

import services.ActivityService;
import services.GymService;
import services.ManagerService;
import services.RoomService;
import services.SocialNetworkService;
import utilities.AbstractTest;
import domain.Activity;
import domain.DayName;
import domain.Gym;
import domain.GymRating;
import domain.Manager;
import domain.Room;
import domain.SNetwork;
import domain.SocialNetwork;
import domain.Subscription;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerTest extends AbstractTest {

	@Autowired
	private GymService				gymService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private SocialNetworkService	socialNetworkService;

	@Autowired
	private ActivityService			activityService;

	@Autowired
	private RoomService				roomService;


	@Before
	public void setupAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("GUEST", "USERNAME", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
	}

	// ADD SOCIAL NETWORK GYM *****************************************************************************************************************
	@Test
	public void driverAddSocialNetworkGym() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager1", "gym1", "https://www.youtube.com/user/caseyneistat", null
			},
			// NEGATIVE - Manager does not exist
			{
				"manager123", "gym1", "https://www.youtube.com/user/caseyneistat", IllegalArgumentException.class
			},
			// NEGATIVE - Manager does not own the gym
			{
				"manager5", "gym1", "https://www.youtube.com/user/caseyneistat", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddSocialNetworkGym((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void templateAddSocialNetworkGym(final String username, final String entity, final String url, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final int gymId = this.getEntityId(entity);
			this.authenticate(username);

			final Gym gym = this.gymService.findOne(gymId);
			final Collection<SocialNetwork> socialNetworks = new ArrayList<SocialNetwork>();

			final SocialNetwork youtube = this.socialNetworkService.create();
			youtube.setsocialNetworkType(SNetwork.YOUTUBE);
			youtube.setUrl(url);
			socialNetworks.add(youtube);
			this.socialNetworkService.save(youtube);

			gym.setSocialNetworks(socialNetworks);
			this.gymService.save(gym);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// REMOVE SOCIAL NETWORK GYM *****************************************************************************************************************
	@Test
	public void driverRemoveSocialNetworkGym() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager1", "gym1", null
			},
			// NEGATIVE - Manager does not exist
			{
				"manager123", "gym1", IllegalArgumentException.class
			},
			// NEGATIVE - Null gym
			{
				"manager1", null, AssertionError.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateRemoveSocialNetworkGym((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateRemoveSocialNetworkGym(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final int gymId = this.getEntityId(entity);
			this.authenticate(username);

			final Gym gym = this.gymService.findOne(gymId);
			final Collection<SocialNetwork> socialNetworks = gym.getSocialNetworks();

			final SocialNetwork sn = (SocialNetwork) socialNetworks.toArray()[0];
			socialNetworks.remove(sn);
			this.socialNetworkService.delete(sn);

			gym.setSocialNetworks(socialNetworks);
			this.gymService.save(gym);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// ADD GYM *****************************************************************************************************************
	@Test
	public void driverAddGym() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager7", null
			},
			// NEGATIVE - Manager does not exist
			{
				"manager123", IllegalArgumentException.class
			},
			// NEGATIVE - User cannot own gym
			{
				"user1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddGym((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	protected void templateAddGym(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Manager manager = this.managerService.findByPrincipal();

			final Gym gym = this.gymService.create();

			gym.setName("Test name");
			gym.setDescription("Test description");
			gym.setPhoto("https://www.t-nation.com/system/publishing/articles/10005529/original/6-Reasons-You-Should-Never-Open-a-Gym.png?1509471214");
			gym.setAddress("Test address");
			gym.setSchedule("Test schedule");
			gym.setFees(10.);
			gym.setServices("Test services");

			gym.setManager(manager);
			gym.setSocialNetworks(new ArrayList<SocialNetwork>());
			gym.setActivities(new ArrayList<Activity>());
			gym.setSubscriptions(new ArrayList<Subscription>());
			gym.setGymRatings(new ArrayList<GymRating>());

			this.gymService.save(gym);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// EDIT GYM *****************************************************************************************************************
	@Test
	public void driverEditGym() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager1", null
			},
			// NEGATIVE - Manager does not exist
			{
				"manager123", IllegalArgumentException.class
			},
			// NEGATIVE - Manager does not have gym
			{
				"manager7", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditGym((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	protected void templateEditGym(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Manager manager = this.managerService.findByPrincipal();

			final Gym gym = manager.getGym();

			gym.setName("Test name");
			gym.setFees(10.);
			gym.setServices("Test services");

			this.gymService.save(gym);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// ADD ACTIVITY *****************************************************************************************************************
	@Test
	public void driverAddActivity() {
		final Calendar start = Calendar.getInstance();
		start.set(2020, 12, 21, 18, 0);
		final Calendar end = Calendar.getInstance();
		end.set(2020, 12, 21, 19, 0);
		final String photo = "https://www.t-nation.com/system/publishing/articles/10005529/original/6-Reasons-You-Should-Never-Open-a-Gym.png";

		final Object testingData[][] = {
			// POSITIVE
			{
				"manager1", "auditorioVivagym", "Test activity", "This is an activity to test", photo, "Test trainer", start, end, null
			},
			// NEGATIVE - The start date is after the end date
			{
				"manager1", "auditorioVivagym", "Test activity", "This is an activity to test", photo, "Test trainer", end, start, IllegalArgumentException.class
			},
			// NEGATIVE - The manager is not the room's owner
			{
				"manager1", "auditorioVivagym2", "Test activity", "This is an activity to test", photo, "Test trainer", start, end, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddActivity((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Calendar) testingData[i][6],
				(Calendar) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	protected void templateAddActivity(final String username, final String entity, final String title, final String description, final String photo, final String trainer, final Calendar start, final Calendar end, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			final Manager principal = this.managerService.findByPrincipal();

			final Gym gym = principal.getGym();

			final int roomId = this.getEntityId(entity);
			final Room room = this.roomService.findOne(roomId);

			final Activity activity = this.activityService.create();
			activity.setTitle(title);
			activity.setDescription(description);
			activity.setPhoto(photo);
			activity.setTrainer(trainer);
			activity.setStart(start);
			activity.setEnd(end);
			activity.setDay(DayName.SUNDAY);
			activity.setRoom(room);
			activity.setGym(gym);

			final Activity stored = this.activityService.save(activity);
			this.activityService.flush();

			Assert.notNull(this.activityService.findOne(stored.getId()));
			Assert.isTrue(gym.getActivities().contains(stored));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// REMOVE ACTIVITY *****************************************************************************************************************
	@Test
	public void driverRemoveActivity() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager4", "yogaVivagym2Wednesday", null
			},
			// NEGATIVE - Manager does not exist
			{
				"manager1", "notActivity", AssertionError.class
			},
			// NEGATIVE - User cannot own gym
			{
				"user1", "yogaVivagym2Wednesday", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateRemoveActivity((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateRemoveActivity(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final int activityId = this.getEntityId(entity);
			this.authenticate(username);
			this.setUpAuth(username);

			final Activity activity = this.activityService.findOne(activityId);
			this.activityService.delete(activity);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// EDIT ACTIVITY *****************************************************************************************************************
	@Test
	public void driverEditActivity() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager4", "yogaVivagym2Wednesday", null
			},
			// NEGATIVE - Manager does not exist
			{
				"manager1", "notActivity", AssertionError.class
			},
			// NEGATIVE - User cannot edit activity
			{
				"user1", "yogaVivagym2Wednesday", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditActivity((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateEditActivity(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final int activityId = this.getEntityId(entity);
			this.authenticate(username);
			this.setUpAuth(username);

			final Activity activity = this.activityService.findOne(activityId);
			activity.setTrainer("Pedro");

			this.activityService.save(activity);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// ADD ROOM *****************************************************************************************************************
	@Test
	public void driverAddRoom() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager2", null
			},
			// NEGATIVE - Manager does not exist
			{
				"manager123", IllegalArgumentException.class
			},
			// NEGATIVE - User cannot own gym
			{
				"user1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddRoom((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	protected void templateAddRoom(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Manager manager = this.managerService.findByPrincipal();

			final Room room = this.roomService.create();
			room.setName("Test room");
			room.setCapacity(20);
			room.setManager(manager);
			this.roomService.save(room);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// REMOVE ROOM *****************************************************************************************************************
	@Test
	public void driverRemoveRoom() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager1", "salaAzul", null
			},
			// NEGATIVE - The room does not exist
			{
				"manager1", "noExiste", AssertionError.class
			},
			// NEGATIVE - Manager does not own room
			{
				"manager2", "salaAzul", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateRemoveRoom((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateRemoveRoom(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final int roomId = this.getEntityId(entity);
			this.authenticate(username);

			final Room room = this.roomService.findOne(roomId);
			this.roomService.delete(room);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	// EDIT ROOM *****************************************************************************************************************
	@Test
	public void driverEditRoom() {
		final Object testingData[][] = {
			// POSITIVE
			{
				"manager1", "salaAzul", null
			},
			// NEGATIVE - The room does not exist
			{
				"manager1", "noExiste", AssertionError.class
			},
			// NEGATIVE - Manager does not own room
			{
				"manager2", "salaAzul", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditRoom((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void templateEditRoom(final String username, final String entity, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			final int roomId = this.getEntityId(entity);
			this.authenticate(username);

			final Room room = this.roomService.findOne(roomId);
			room.setCapacity(123);
			this.roomService.save(room);

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Edit manager data
	//POSITIVE
	@Test
	public void editManagerPositiveTest() {
		this.authenticate("manager1");
		final Manager manager = this.managerService.findOne(this.getEntityId("manager1"));
		manager.setName("Example");
		final Manager result = this.managerService.save(manager);
		Assert.isTrue(result.getName().equals("Example"));

		this.authenticate(null);
	}

	//NEGATIVE- Name in blank
	@Test(expected = IllegalArgumentException.class)
	public void editManagerNegativeTest() {
		this.authenticate("manager1");
		final Manager manager = this.managerService.findOne(this.getEntityId("manager1"));
		manager.setName("");
		final Manager result = this.managerService.save(manager);
		Assert.isTrue(!result.getName().equals(""));

		this.authenticate(null);
	}

	//NEGATIVE- Editing other manager data
	@Test(expected = IllegalArgumentException.class)
	public void editManagerNegativeTest2() {
		this.authenticate("manager2");
		final Manager manager = this.managerService.findOne(this.getEntityId("manager1"));
		manager.setName("Example");
		final Manager result = this.managerService.save(manager);
		Assert.isTrue(!result.getName().equals("Example"));

		this.authenticate(null);
	}

	//Delete manager 
	//POSITIVE
	@Test
	public void deleteManagerPositiveTest() {
		this.authenticate("manager7");
		this.setUpAuth("manager7");
		final Manager manager = this.managerService.findOne(this.getEntityId("manager7"));
		this.managerService.delete(manager);
		this.authenticate(null);
	}

	//NEGATIVE- Deleting other manager account
	@Test(expected = IllegalArgumentException.class)
	public void deleteManagerNegativeTest() {
		this.authenticate("manager2");
		this.setUpAuth("manager2");
		final Manager manager = this.managerService.findOne(this.getEntityId("manager1"));
		this.managerService.delete(manager);
		this.authenticate(null);
	}

	//NEGATIVE- Deleting other manager account as user
	@Test(expected = IllegalArgumentException.class)
	public void deleteManagerNegativeTest2() {
		this.authenticate("user1");
		this.setUpAuth("user1");
		final Manager manager = this.managerService.findOne(this.getEntityId("manager1"));
		this.managerService.delete(manager);
		this.authenticate(null);
	}

}
