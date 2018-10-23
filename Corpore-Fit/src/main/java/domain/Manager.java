
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor {

	private boolean	active;


	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}


	// Relationships

	private Gym	gym;


	@Valid
	@OneToOne(mappedBy = "manager")
	public Gym getGym() {
		return this.gym;
	}

	public void setGym(final Gym gym) {
		this.gym = gym;
	}

}
