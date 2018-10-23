
package domain;

import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class ProductOrder extends DomainEntity {

	private Calendar	date;


	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Calendar getDate() {
		return this.date;
	}

	public void setDate(final Calendar date) {
		this.date = date;
	}


	// Relationships
	private User					user;
	private Collection<OrderLine>	orderLines;
	private CreditCard				creditCard;


	@Valid
	@NotNull
	@ManyToOne
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Valid
	@ManyToOne
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@Valid
	@NotEmpty
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<OrderLine> getOrderLines() {
		return this.orderLines;
	}

	public void setOrderLines(final Collection<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

}
