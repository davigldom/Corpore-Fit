
package domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Nutritionist extends Actor {

	private String				curriculum;
	private String				officeAddress;
	private boolean				validated;
	private List<DaySchedule>	schedule;


	@NotBlank
	@URL
	public String getCurriculum() {
		return this.curriculum;
	}

	public void setCurriculum(final String curriculum) {
		this.curriculum = curriculum;
	}

	@NotBlank
	public String getOfficeAddress() {
		return this.officeAddress;
	}

	public void setOfficeAddress(final String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public boolean isValidated() {
		return this.validated;
	}

	public void setValidated(final boolean validated) {
		this.validated = validated;
	}

	@NotEmpty
	@ElementCollection(targetClass = DaySchedule.class)
	public List<DaySchedule> getSchedule() {
		return this.schedule;
	}

	public void setSchedule(final List<DaySchedule> schedule) {
		this.schedule = schedule;
	}


	// Relationships
	private Collection<Diet>	diets;
	private Collection<Appointment> appointments;

	@NotNull
	@Valid
	@OneToMany(mappedBy = "nutritionist",cascade = CascadeType.REMOVE)
	public Collection<Diet> getDiets() {
		return this.diets;
	}

	public void setDiets(final Collection<Diet> diets) {
		this.diets = diets;
	}
	
	@NotNull
	@Valid
	@OneToMany(mappedBy = "nutritionist", cascade = CascadeType.REMOVE)
	public Collection<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(Collection<Appointment> appointments) {
		this.appointments = appointments;
	}
}
