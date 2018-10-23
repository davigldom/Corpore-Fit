package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NutritionistRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Appointment;
import domain.ArticleRating;
import domain.DayName;
import domain.DaySchedule;
import domain.Diet;
import domain.Nutritionist;
import domain.User;
import forms.RegisterNutritionist;

@Service
@Transactional
public class NutritionistService {

	@Autowired
	private NutritionistRepository nutritionistRepository;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private Validator validator;

	@Autowired
	private AdministratorService adminService;

	public Nutritionist create() {

		final UserAccount userAccount = this.userAccountService.create();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.NUTRITIONIST);
		userAccount.addAuthority(authority);

		final Nutritionist result;
		result = new Nutritionist();
		result.setUserAccount(userAccount);
		final List<DaySchedule> schedule = new ArrayList<DaySchedule>();
		final DaySchedule monday = new DaySchedule();
		monday.setDay(DayName.MONDAY);
		final DaySchedule tuesday = new DaySchedule();
		tuesday.setDay(DayName.TUESDAY);
		final DaySchedule wednesday = new DaySchedule();
		wednesday.setDay(DayName.WEDNESDAY);
		final DaySchedule thursday = new DaySchedule();
		thursday.setDay(DayName.THURSDAY);
		final DaySchedule friday = new DaySchedule();
		friday.setDay(DayName.FRIDAY);
		final DaySchedule saturday = new DaySchedule();
		saturday.setDay(DayName.SATURDAY);
		schedule.add(monday);
		schedule.add(tuesday);
		schedule.add(wednesday);
		schedule.add(thursday);
		schedule.add(friday);
		schedule.add(saturday);

		final List<ArticleRating> ratings = new ArrayList<ArticleRating>();
		result.setValidated(false);
		result.setBanned(false);
		result.setSchedule(schedule);
		result.setArticleRatings(ratings);
		return result;
	}

	public Nutritionist save(final Nutritionist nutritionist) {
		Nutritionist result;
		Assert.notNull(nutritionist);
		Assert.notNull(nutritionist.getUserAccount().getUsername());
		Assert.notNull(nutritionist.getUserAccount().getPassword());
		if (nutritionist.getId() == 0) {
			String passwordHashed = null;
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			passwordHashed = encoder.encodePassword(nutritionist
					.getUserAccount().getPassword(), null);
			nutritionist.getUserAccount().setPassword(passwordHashed);
		}

		result = this.nutritionistRepository.save(nutritionist);
		return result;
	}

	public void delete(final Nutritionist nutritionist) {
		Assert.isTrue(nutritionist.getId() != 0);
		Assert.isTrue(this.findByPrincipal().equals(nutritionist));
		this.nutritionistRepository.delete(nutritionist);
		Assert.isTrue(!this.nutritionistRepository.findAll().contains(
				nutritionist));
	}

	public Nutritionist addDaySchedule(final String day,
			final String morningStart, final String morningEnd,
			final String afternoonStart, final String afternoonEnd) {
		final Nutritionist nutritionist = this.findByPrincipal();

		Assert.isTrue(morningStart.matches("\\d{2}:00|\\d{2}:30"));
		Assert.isTrue(morningEnd.matches("\\d{2}:00|\\d{2}:30"));
		Assert.isTrue(afternoonStart.matches("\\d{2}:00|\\d{2}:30"));
		Assert.isTrue(afternoonEnd.matches("\\d{2}:00|\\d{2}:30"));

		final LocalTime mStart = LocalTime.parse(morningStart);
		final LocalTime mEnd = LocalTime.parse(morningEnd);
		final LocalTime aStart = LocalTime.parse(afternoonStart);
		final LocalTime aEnd = LocalTime.parse(afternoonEnd);

		Assert.notNull(morningStart);
		Assert.notNull(morningEnd);
		Assert.notNull(afternoonStart);
		Assert.notNull(afternoonEnd);
		Assert.isTrue(mStart.isBefore(mEnd) && aStart.isBefore(aEnd));
		Assert.isTrue(mStart.isBefore(aStart) && mStart.isBefore(aEnd)
				&& mEnd.isBefore(aStart) && mEnd.isBefore(aEnd));

		final DaySchedule daySchedule = new DaySchedule();
		daySchedule.setMorningStart(morningStart);
		daySchedule.setMorningEnd(morningEnd);
		daySchedule.setAfternoonStart(afternoonStart);
		daySchedule.setAfternoonEnd(afternoonEnd);

		final List<DaySchedule> schedule = nutritionist.getSchedule();
		switch (day) {
		case "MONDAY":
			daySchedule.setDay(DayName.MONDAY);
			schedule.set(0, daySchedule);
			break;
		case "TUESDAY":
			daySchedule.setDay(DayName.TUESDAY);
			schedule.set(1, daySchedule);
			break;

		case "WEDNESDAY":
			daySchedule.setDay(DayName.WEDNESDAY);
			schedule.set(2, daySchedule);
			break;

		case "THURSDAY":
			daySchedule.setDay(DayName.THURSDAY);
			schedule.set(3, daySchedule);
			break;
		case "FRIDAY":
			daySchedule.setDay(DayName.FRIDAY);
			schedule.set(4, daySchedule);
			break;
		case "SATURDAY":
			daySchedule.setDay(DayName.SATURDAY);
			schedule.set(5, daySchedule);
			break;
		default:
			break;
		}

		nutritionist.setSchedule(schedule);
		this.nutritionistRepository.save(nutritionist);
		return nutritionist;
	}

	public Nutritionist reconstructRegister(
			final RegisterNutritionist nutritionist, final BindingResult binding) {
		final Nutritionist result;
		Assert.isTrue(nutritionist.isAcceptedTerms());
		Assert.isTrue(nutritionist.getPassword().equals(
				nutritionist.getRepeatedPassword()));
		result = this.create();

		result.setEmail(nutritionist.getEmail());
		result.setName(nutritionist.getName());
		result.setPhone(nutritionist.getPhone());
		result.setCurriculum(nutritionist.getCurriculum());
		result.setSurname(nutritionist.getSurname());
		result.setOfficeAddress(nutritionist.getOfficeAddress());
		result.setAddress(nutritionist.getAddress());
		result.setBirthdate((nutritionist.getBirthdate()));
		result.setPhoto(nutritionist.getPhoto());
		result.getUserAccount().setUsername(nutritionist.getUsername());
		result.getUserAccount().setPassword(nutritionist.getPassword());
		result.setDiets(new ArrayList<Diet>());
		result.setAppointments(new ArrayList<Appointment>());

		this.validator.validate(result, binding);

		return result;
	}

	public Nutritionist reconstruct(final Nutritionist nutritionist,
			final BindingResult binding) {
		Nutritionist nutritionistStored;

		if (nutritionist.getId() != 0) {
			nutritionistStored = this.nutritionistRepository
					.findOne(nutritionist.getId());

			nutritionist.setId(nutritionistStored.getId());
			nutritionist.setUserAccount(nutritionistStored.getUserAccount());
			nutritionist.setVersion(nutritionistStored.getVersion());

			nutritionist.setArticleRatings(nutritionistStored
					.getArticleRatings());
			nutritionist.setSchedule(nutritionistStored.getSchedule());
			nutritionist.setBanned(nutritionistStored.isBanned());
			nutritionist.setValidated(nutritionistStored.isValidated());
			nutritionist.setDiets(nutritionistStored.getDiets());
			nutritionist.setAppointments(nutritionistStored.getAppointments());
		}
		this.validator.validate(nutritionist, binding);
		return nutritionist;

	}

	public Nutritionist findOne(final int nutritionistId) {
		Nutritionist result;
		result = this.nutritionistRepository.findOne(nutritionistId);
		Assert.notNull(result);
		return result;
	}

	public Nutritionist findOneToEdit(final int nutritionistId) {
		Nutritionist result;
		result = this.findOne(nutritionistId);
		Assert.isTrue(this.findByPrincipal().equals(result));

		return result;
	}

	public Nutritionist findByPrincipal() {
		Nutritionist result;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByNutritionistAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Nutritionist findByNutritionistAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Nutritionist result;
		result = this.nutritionistRepository
				.findByNutritionistAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Nutritionist findByNutritionistAccountId(final int nutritionistId) {
		Nutritionist result;
		result = this.nutritionistRepository
				.findByNutritionistAccountId(nutritionistId);
		return result;
	}

	public void flush() {
		this.nutritionistRepository.flush();
	}

	public Collection<Nutritionist> findValidated() {
		return this.nutritionistRepository.findValidated();
	}

	public Collection<Nutritionist> findNotValidated() {
		Assert.notNull(this.adminService.findByPrincipal());
		return this.nutritionistRepository.findNotValidated();
	}

	public Nutritionist validate(final int nutritionistId) {
		Assert.notNull(this.adminService.findByPrincipal());
		Assert.isTrue(nutritionistId != 0);

		Nutritionist result;
		result = this.findOne(nutritionistId);

		Assert.isTrue(!result.isValidated());

		final List<DaySchedule> days = result.getSchedule();
		for (final DaySchedule ds : days) {
			Assert.notNull(ds.getMorningStart());
			Assert.notNull(ds.getMorningEnd());
			Assert.notNull(ds.getAfternoonStart());
			Assert.notNull(ds.getAfternoonEnd());
		}

		result.setValidated(true);
		final Nutritionist validated = this.save(result);
		return validated;
	}

	public Collection<User> getAssigners(int nutritionistId) {
		return this.nutritionistRepository.getAssigners(nutritionistId);
	}

}
