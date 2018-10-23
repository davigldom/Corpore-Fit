
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Room extends DomainEntity {

	private String	name;
	private int		capacity;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Min(1)
	public int getCapacity() {
		return this.capacity;
	}

	public void setCapacity(final int capacity) {
		this.capacity = capacity;
	}


	//Relationships

	private Manager	manager;


	@Valid
	@NotNull
	@ManyToOne
	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

}
