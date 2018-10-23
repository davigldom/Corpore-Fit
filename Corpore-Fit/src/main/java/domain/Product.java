
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Product extends DomainEntity {

	private String	name;
	private String	sku;
	private String	description;
	private String	image;
	private double	price;
	private int		units;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@Column(unique = true)
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
	public int getUnits() {
		return this.units;
	}

	public void setUnits(final int units) {
		this.units = units;
	}


	// Relationships

	private Provider	provider;
	private Advert		advert;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(final Provider provider) {
		this.provider = provider;
	}

	@Valid
	@ManyToOne
	public Advert getAdvert() {
		return this.advert;
	}

	public void setAdvert(final Advert advert) {
		this.advert = advert;
	}

}
