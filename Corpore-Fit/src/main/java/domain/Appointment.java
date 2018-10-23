
package domain;

import java.util.Calendar;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Appointment extends DomainEntity {

	private String		cancelReason;
	private boolean		cancelled;
	private String		comments;
	private Calendar	time;
	
	
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@NotNull
	@Future
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Calendar getTime() {
		return time;
	}
	public void setTime(Calendar time) {
		this.time = time;
	}
	
	//RELATIONSHIPS
	
	private Nutritionist nutritionist;
	private User user;

	@Valid
	@NotNull
	@ManyToOne
	public Nutritionist getNutritionist() {
		return nutritionist;
	}
	public void setNutritionist(Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}
	
	@Valid
	@NotNull
	@ManyToOne
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	

	
}
