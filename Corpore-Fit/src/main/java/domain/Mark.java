
package domain;

import java.util.Calendar;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Mark extends DomainEntity {

	private Calendar	creationDate;
	private double		benchPress;
	private double		squat;
	private double		deadlift;
	private double		pullUp;
	private double		rowing;


	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Calendar getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(final Calendar creationDate) {
		this.creationDate = creationDate;
	}

	@Min(0)
	public double getBenchPress() {
		return this.benchPress;
	}

	public void setBenchPress(final double benchPress) {
		this.benchPress = benchPress;
	}

	@Min(0)
	public double getSquat() {
		return this.squat;
	}

	public void setSquat(final double squat) {
		this.squat = squat;
	}

	@Min(0)
	public double getDeadlift() {
		return this.deadlift;
	}

	public void setDeadlift(final double deadlift) {
		this.deadlift = deadlift;
	}

	@Min(0)
	public double getPullUp() {
		return this.pullUp;
	}

	public void setPullUp(final double pullUp) {
		this.pullUp = pullUp;
	}

	@Min(0)
	public double getRowing() {
		return this.rowing;
	}

	public void setRowing(final double rowing) {
		this.rowing = rowing;
	}

}
