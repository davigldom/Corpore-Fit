
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Access(AccessType.PROPERTY)
public class Routine extends DomainEntity {

	private String		name;

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private User user;
	private Collection<Day> days;

	@NotNull
	@Valid
	@OneToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@NotEmpty
	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<Day> getDays() {
		return days;
	}

	public void setDays(Collection<Day> days) {
		this.days = days;
	}
	
	
	
	
	
}
