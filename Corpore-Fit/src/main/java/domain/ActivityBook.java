
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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class ActivityBook extends DomainEntity {

	private Calendar	creationDate;


	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Calendar getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(final Calendar creationDate) {
		this.creationDate = creationDate;
	}


	//Relationships
	private User		user;
	private Activity	activity;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(final Activity activity) {
		this.activity = activity;
	}

}
