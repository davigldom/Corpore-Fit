
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class ShoppingCart extends DomainEntity {

	private double	price;
	private int		amount;


	@Min(0)
	public double getPrice() {
		return this.price;
	}

	public void setPrice(final double price) {
		this.price = price;
	}

	@Min(0)
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(final int amount) {
		this.amount = amount;
	}


	private Product	product;


	@NotNull
	@Valid
	@OneToOne
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(final Product product) {
		this.product = product;
	}

}
