
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Gym extends DomainEntity {

	private String	name;
	private String	description;
	private String	photo;
	private String	address;
	private String	schedule;
	private double	fees;
	private String	services;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotBlank
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@NotBlank
	public String getSchedule() {
		return this.schedule;
	}

	public void setSchedule(final String schedule) {
		this.schedule = schedule;
	}

	@Min(1)
	public double getFees() {
		return this.fees;
	}

	public void setFees(final double fees) {
		this.fees = fees;
	}

	@NotBlank
	public String getServices() {
		return this.services;
	}

	public void setServices(final String services) {
		this.services = services;
	}


	// Relationships

	private Collection<Activity>		activities;
	private Manager						manager;
	private Collection<SocialNetwork>	socialNetworks;
	private Collection<Subscription>	subscriptions;
	private Collection<GymRating>		gymRatings;


	@NotNull
	@Valid
	@OneToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "gym", cascade = CascadeType.REMOVE)
	public Collection<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(final Collection<Activity> activities) {
		this.activities = activities;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.REMOVE)
	public Collection<SocialNetwork> getSocialNetworks() {
		return this.socialNetworks;
	}

	public void setSocialNetworks(final Collection<SocialNetwork> socialNetworks) {
		this.socialNetworks = socialNetworks;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "gym", cascade = CascadeType.REMOVE)
	public Collection<Subscription> getSubscriptions() {
		return this.subscriptions;
	}

	public void setSubscriptions(final Collection<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.REMOVE)
	public Collection<GymRating> getGymRatings() {
		return this.gymRatings;
	}

	public void setGymRatings(final Collection<GymRating> gymRatings) {
		this.gymRatings = gymRatings;
	}
}
