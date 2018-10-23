
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class GymRating extends DomainEntity {

	private int	rating;


	@Range(min = 1, max = 5)
	public int getRating() {
		return this.rating;
	}

	public void setRating(final int rating) {
		this.rating = rating;
	}

}
