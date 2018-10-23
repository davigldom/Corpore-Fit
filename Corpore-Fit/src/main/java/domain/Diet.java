
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Diet extends DomainEntity {

	private String	name;
	private String	comments;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}


	// Relationships

	private Nutritionist			nutritionist;
	private Collection<DailyDiet>	dailyDiets;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Nutritionist getNutritionist() {
		return this.nutritionist;
	}

	public void setNutritionist(final Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@Valid
	//	@NotNull
	public Collection<DailyDiet> getDailyDiets() {
		return this.dailyDiets;
	}

	public void setDailyDiets(final Collection<DailyDiet> dailyDiets) {
		this.dailyDiets = dailyDiets;
	}

}
