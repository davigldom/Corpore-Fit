
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Food extends DomainEntity {

	private String		name;
	private double		calories;
	private FoodTime	time;
	private int			amount;
	private String		image;


	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@Min(0)
	public double getCalories() {
		return this.calories;
	}
	public void setCalories(final double calories) {
		this.calories = calories;
	}
	@NotNull
	public FoodTime getTime() {
		return this.time;
	}
	public void setTime(final FoodTime time) {
		this.time = time;
	}

	@Min(0)
	public int getAmount() {
		return this.amount;
	}
	public void setAmount(final int amount) {
		this.amount = amount;
	}

	@NotBlank
	@URL
	public String getImage() {
		return this.image;
	}
	public void setImage(final String image) {
		this.image = image;
	}

}
