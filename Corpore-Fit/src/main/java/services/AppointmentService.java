
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AppointmentRepository;
import domain.Appointment;
import domain.DaySchedule;
import domain.User;

@Service
@Transactional
public class AppointmentService {

	@Autowired
	private AppointmentRepository	appointmentRepository;

	@Autowired
	private UserService				userService;

	@Autowired
	private NutritionistService		nutritionistService;

	@Autowired
	private ActorService			actorService;


	public Appointment create() {
		final Appointment result;

		result = new Appointment();

		return result;
	}

	public Appointment findOne(final int appointmentId) {
		Appointment result;
		result = this.appointmentRepository.findOne(appointmentId);
		Assert.notNull(result);
		return result;
	}

	// public Appointment findOneToEdit(final int appointmentId) {
	// Appointment result;
	// result = this.findOne(appointmentId);
	// Assert.isTrue(this.managerService.findByPrincipal().equals(
	// result.getGym().getManager()));
	//
	// return result;
	// }

	public Collection<Appointment> findAll() {
		return this.appointmentRepository.findAll();
	}

	public Appointment make(final Appointment appointment) {
		Appointment result;

		Assert.notNull(appointment);
		Assert.isTrue(appointment.getId() == 0);
		final User principal = this.userService.findByPrincipal();
		Assert.isTrue(appointment.getUser().equals(principal));
		Assert.isTrue(principal.getNutritionist().equals(appointment.getNutritionist()));
		Assert.isNull(this.findByTime(appointment.getTime()));
		Assert.isTrue(appointment.getTime().get(Calendar.DAY_OF_WEEK)!=1);
		Assert.isTrue(appointment.getTime().get(Calendar.MINUTE)==0 || appointment.getTime().get(Calendar.MINUTE)==30 );

		for (final DaySchedule schedule : appointment.getNutritionist().getSchedule())
			if (schedule.getDay().ordinal() + 1 == appointment.getTime().get(Calendar.DAY_OF_WEEK))
				Assert.isTrue(this.isInSchedule(schedule, appointment.getTime()));

		result = this.appointmentRepository.save(appointment);
		principal.getAppointments().add(result);
		appointment.getNutritionist().getAppointments().add(result);
		return result;
	}

	public Appointment cancel(final Appointment appointment) {

		Assert.isTrue(this.nutritionistService.findByPrincipal().equals(appointment.getNutritionist()));
		Assert.isTrue(!this.findOne(appointment.getId()).isCancelled());
		appointment.setCancelled(true);
		final Appointment result = this.appointmentRepository.save(appointment);
		return result;

	}

	public void delete(final Appointment appointment) {
		User user;

		if (this.actorService.isUser()) {
			user = this.userService.findByPrincipal();
			Assert.isTrue(user.equals(appointment.getUser()));
		} else {
			Assert.isTrue(this.actorService.isAdmin());
			user = this.userService.findOne(appointment.getUser().getId());
		}

		user.getNutritionist().getAppointments().remove(appointment);
		user.getAppointments().remove(appointment);
		this.appointmentRepository.delete(appointment);

	}

	public Appointment findByTime(final Calendar date) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(date.getTime());
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(new Date(date.getTimeInMillis() + 29 * 60 * 1000));

		Appointment result = this.appointmentRepository.findByTime(startDate, endDate);

		if (result == null) {
			startDate = Calendar.getInstance();
			startDate.setTime(new Date(date.getTimeInMillis() - 29 * 60 * 1000));
			endDate = Calendar.getInstance();
			endDate.setTime(date.getTime());

			result = this.appointmentRepository.findByTime(startDate, endDate);
		}

		return result;
	}
	
	public Collection<Calendar> findAllBetweenTimes(Calendar date1, Calendar date2, int nutritionistId){
		return this.appointmentRepository.findAllBetweenTimes(date1, date2, nutritionistId);
	}

	private boolean isInSchedule(final DaySchedule schedule, final Calendar date) {
		boolean res = true;

		final LocalTime morningStart = LocalTime.parse(schedule.getMorningStart());
		final LocalTime morningEnd = LocalTime.parse(schedule.getMorningEnd());
		final LocalTime afternoonStart = LocalTime.parse(schedule.getAfternoonStart());
		final LocalTime afternoonEnd = LocalTime.parse(schedule.getAfternoonEnd());

		final LocalTime dateTime = LocalTime.parse(date.get(Calendar.HOUR_OF_DAY) + ":" + (date.get(Calendar.MINUTE)));

		if (dateTime.isBefore(morningStart) || (dateTime.isAfter(morningEnd) && dateTime.isBefore(afternoonStart)) || dateTime.isAfter(afternoonEnd))
			res = false;
		return res;
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Appointment reconstruct(final Appointment appointment, final BindingResult binding) {
		Appointment appointmentStored;

		if (appointment.getId() != 0) {
			appointmentStored = this.appointmentRepository.findOne(appointment.getId());

			appointment.setId(appointmentStored.getId());
			appointment.setVersion(appointmentStored.getVersion());

			// appointment.setTitle(appointmentStored.getTitle());
			// appointment.setDescription(appointmentStored.getDescription());
			// appointment.setPhoto(appointmentStored.getPhoto());
			// appointment.setTrainer(appointmentStored.getTrainer());
			// appointment.setStart(appointmentStored.getStart());
			// appointment.setEnd(appointmentStored.getEnd());

			// appointment.setRoom(appointmentStored.getRoom());
			appointment.setNutritionist(appointmentStored.getNutritionist());
			appointment.setUser(appointmentStored.getUser());
		} else {
			appointment.setUser(this.userService.findByPrincipal());
			appointment.setNutritionist(this.userService.findByPrincipal().getNutritionist());
		}
		this.validator.validate(appointment, binding);
		return appointment;

	}

	public void flush() {
		this.appointmentRepository.flush();
	}

}
