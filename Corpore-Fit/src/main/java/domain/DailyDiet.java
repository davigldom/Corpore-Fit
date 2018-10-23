
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class DailyDiet extends DomainEntity {

	private DayName	day;


	@NotNull
	public DayName getDay() {
		return this.day;
	}

	public void setDay(final DayName day) {
		this.day = day;
	}


	//Relationships

	private Collection<Food>	foods;


	@Valid
	@NotNull
	@OneToMany
	public Collection<Food> getFoods() {
		return this.foods;
	}

	public void setFoods(final Collection<Food> foods) {
		this.foods = foods;
	}

}
