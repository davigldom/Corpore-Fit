package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private Role role;
	private Privacy privacy;

	@NotNull
	public Role getRole() {
		return this.role;
	}

	public void setRole(final Role role) {
		this.role = role;
	}

	@NotNull
	public Privacy getPrivacy() {
		return this.privacy;
	}

	public void setPrivacy(final Privacy privacy) {
		this.privacy = privacy;
	}

	// Relationships

	private Collection<Mark> marks;
	private Collection<Measure> measures;
	private Collection<FriendRequest> sentRequests;
	private Collection<FriendRequest> receivedRequests;
	private Routine routine;
	private Diet diet;
	private Nutritionist nutritionist;
	private Collection<SocialNetwork> socialNetworks;
	private Collection<CreditCard> creditCards;
	private Subscription subscription;
	private Collection<GymRating> gymRatings;
	private Collection<Appointment> appointments;
	private Collection<ShoppingCart> shoppingCart;
	private Collection<ProductOrder> orders;

	@NotNull
	@Valid
	@OneToMany(mappedBy = "user", cascade=CascadeType.REMOVE)
	public Collection<ProductOrder> getOrders() {
		return orders;
	}

	public void setOrders(Collection<ProductOrder> orders) {
		this.orders = orders;
	}

	@ManyToOne(optional = true)
	public Diet getDiet() {
		return this.diet;
	}

	public void setDiet(final Diet diet) {
		this.diet = diet;
	}

	@ManyToOne(optional = true)
	public Nutritionist getNutritionist() {
		return this.nutritionist;
	}

	public void setNutritionist(final Nutritionist nutritionist) {
		this.nutritionist = nutritionist;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.REMOVE)
	public Collection<Mark> getMarks() {
		return this.marks;
	}

	public void setMarks(final Collection<Mark> marks) {
		this.marks = marks;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.REMOVE)
	public Collection<Measure> getMeasures() {
		return this.measures;
	}

	public void setMeasures(final Collection<Measure> measures) {
		this.measures = measures;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
	public Collection<FriendRequest> getSentRequests() {
		return this.sentRequests;
	}

	public void setSentRequests(final Collection<FriendRequest> sentRequests) {
		this.sentRequests = sentRequests;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
	public Collection<FriendRequest> getReceivedRequests() {
		return this.receivedRequests;
	}

	public void setReceivedRequests(
			final Collection<FriendRequest> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}

	@Valid
	@OneToOne(mappedBy = "user", optional = true, cascade = CascadeType.REMOVE)
	public Routine getRoutine() {
		return this.routine;
	}

	public void setRoutine(final Routine routine) {
		this.routine = routine;
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
	@OneToMany(cascade = CascadeType.REMOVE)
	public Collection<CreditCard> getCreditCards() {
		return this.creditCards;
	}

	public void setCreditCards(final Collection<CreditCard> creditCards) {
		this.creditCards = creditCards;
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

	@Valid
	@OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
	public Subscription getSubscription() {
		return this.subscription;
	}

	public void setSubscription(final Subscription subscription) {
		this.subscription = subscription;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	public Collection<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(Collection<Appointment> appointments) {
		this.appointments = appointments;
	}


	@NotNull
	@OneToMany(cascade = CascadeType.REMOVE)
	@Valid
	public Collection<ShoppingCart> getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(Collection<ShoppingCart> shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

}
