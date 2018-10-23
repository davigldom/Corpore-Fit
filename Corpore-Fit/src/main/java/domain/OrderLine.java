
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class OrderLine extends DomainEntity {

	private String	name;
	private String	sku;
	private String	description;
	private String	image;
	private double 	amount;
	private double	price;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getSku() {
		return this.sku;
	}

	public void setSku(final String sku) {
		this.sku = sku;
	}

	@NotBlank
	@Column(length = 1000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	@URL
	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@Min(0)
	public double getPrice() {
		return this.price;
	}

	public void setPrice(final double price) {
		this.price = price;
	}
	
	@Min(0)
	public double getAmount() {
		return this.amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

}
