
package domain;

import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Advert extends DomainEntity {

	private String		name;
	private Calendar	start;
	private Calendar	end;
	private int			discount;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	@Future
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Calendar getStart() {
		return this.start;
	}

	public void setStart(final Calendar start) {
		this.start = start;
	}

	@NotNull
	@Future
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Calendar getEnd() {
		return this.end;
	}

	public void setEnd(final Calendar end) {
		this.end = end;
	}

	@Range(min = 1, max = 100)
	public int getDiscount() {
		return this.discount;
	}

	public void setDiscount(final int discount) {
		this.discount = discount;
	}


	// Relationships

	private Collection<Product>	products;
	private Provider			provider;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "advert")
	public Collection<Product> getProducts() {
		return this.products;
	}

	public void setProducts(final Collection<Product> products) {
		this.products = products;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(final Provider provider) {
		this.provider = provider;
	}

}
