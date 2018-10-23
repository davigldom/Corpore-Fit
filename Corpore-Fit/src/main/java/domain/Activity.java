
package domain;

import java.util.Calendar;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Activity extends DomainEntity {

	private String		title;
	private String		description;
	private String		photo;
	private String		trainer;
	private Calendar	start;
	private Calendar	end;
	private DayName		day;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	public String getTrainer() {
		return this.trainer;
	}

	public void setTrainer(final String trainer) {
		this.trainer = trainer;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm")
	public Calendar getStart() {
		return this.start;
	}

	public void setStart(final Calendar start) {
		this.start = start;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm")
	public Calendar getEnd() {
		return this.end;
	}

	public void setEnd(final Calendar end) {
		this.end = end;
	}

	@NotNull
	public DayName getDay() {
		return this.day;
	}

	public void setDay(final DayName day) {
		this.day = day;
	}


	// Relationships

	private Room	room;
	private Gym		gym;


	@Valid
	@NotNull
	@ManyToOne
	public Gym getGym() {
		return this.gym;
	}

	public void setGym(final Gym gym) {
		this.gym = gym;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Room getRoom() {
		return this.room;
	}

	public void setRoom(final Room room) {
		this.room = room;
	}

}
