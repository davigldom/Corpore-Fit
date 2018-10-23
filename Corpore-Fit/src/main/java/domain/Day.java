
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Day extends DomainEntity {

	private DayName		day;

	@NotNull
	public DayName getDay() {
		return day;
	}

	public void setDay(DayName day) {
		this.day = day;
	}

	private Collection<Exercise> exercises;

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.REMOVE)
	public Collection<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(Collection<Exercise> exercises) {
		this.exercises = exercises;
	}
	
	
	
	
}
