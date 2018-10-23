
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RoomRepository;
import domain.Activity;
import domain.Manager;
import domain.Room;

@Service
@Transactional
public class RoomService {

	@Autowired
	private RoomRepository	roomRepository;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private ActivityService	activityService;


	public Room create() {
		final Room result;

		result = new Room();

		Manager manager;
		manager = this.managerService.findByPrincipal();

		result.setManager(manager);

		return result;
	}

	public Room findOne(final int roomId) {
		Room result;
		result = this.roomRepository.findOne(roomId);
		Assert.notNull(result);
		return result;
	}

	public Room findOneToEdit(final int roomId) {
		Room result;
		result = this.findOne(roomId);
		Assert.isTrue(this.managerService.findByPrincipal().equals(result.getManager()));

		return result;
	}

	public Collection<Room> findAll() {
		return this.roomRepository.findAll();
	}

	public Collection<Room> findByGym(final int gymId) {
		Collection<Room> result;

		result = this.roomRepository.findByGym(gymId);
		Assert.notNull(result);
		return result;
	}
	
	public Collection<Room> findByManager(final int managerId) {
		Collection<Room> result;

		result = this.roomRepository.findByManager(managerId);
		Assert.notNull(result);
		return result;
	}

	public Room save(final Room room) {
		Room result;

		Assert.notNull(room);
		final Manager principal = this.managerService.findByPrincipal();
		Assert.isTrue(principal.equals(room.getManager()));

		result = this.roomRepository.save(room);
		return result;
	}

	public void delete(final Room room) {
		Assert.isTrue(this.managerService.findByPrincipal().equals(room.getManager()));

		final Collection<Activity> activities = this.roomRepository.findActivitiesByRoom(room.getId());

		for (final Activity a : activities) {
			a.setRoom(null);
			this.activityService.delete(a);
		}

		this.roomRepository.delete(room);

		Assert.isTrue(!this.roomRepository.findAll().contains(room));

	}


	//Reconstruct

	@Autowired
	private Validator	validator;


	public Room reconstruct(final Room room, final BindingResult binding) {
		Room roomStored;

		if (room.getId() != 0) {
			roomStored = this.roomRepository.findOne(room.getId());

			room.setId(roomStored.getId());
			room.setVersion(roomStored.getVersion());

			//			room.setName(roomStored.getName());
			//			room.setCapacity(roomStored.getCapacity());

			room.setManager(roomStored.getManager());
		} else
			room.setManager(this.managerService.findByPrincipal());

		this.validator.validate(room, binding);
		return room;

	}

}
