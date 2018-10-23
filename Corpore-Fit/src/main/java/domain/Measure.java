
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
public class Measure extends DomainEntity {

	private Calendar	creationDate;
	private double		weight;
	private double		chest;
	private double		thigh;
	private double		waist;
	private double		biceps;
	private double		calf;


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
	public double getWeight() {
		return this.weight;
	}

	public void setWeight(final double weight) {
		this.weight = weight;
	}

	@Min(0)
	public double getChest() {
		return this.chest;
	}

	public void setChest(final double chest) {
		this.chest = chest;
	}

	@Min(0)
	public double getThigh() {
		return this.thigh;
	}

	public void setThigh(final double thigh) {
		this.thigh = thigh;
	}

	@Min(0)
	public double getWaist() {
		return this.waist;
	}

	public void setWaist(final double waist) {
		this.waist = waist;
	}

	@Min(0)
	public double getBiceps() {
		return this.biceps;
	}

	public void setBiceps(final double biceps) {
		this.biceps = biceps;
	}

	@Min(0)
	public double getCalf() {
		return this.calf;
	}

	public void setCalf(final double calf) {
		this.calf = calf;
	}

}
