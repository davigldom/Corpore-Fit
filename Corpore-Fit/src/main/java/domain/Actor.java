
package domain;

import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Actor extends DomainEntity {

	private String		name;
	private String		surname;
	private String		phone;
	private String		email;
	private String		address;
	private String		photo;
	private Calendar	birthdate;
	private boolean		banned;


	@NotBlank
	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}
	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@Pattern(regexp = "(^\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$)?")
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}

	public String getAddress() {
		return this.address;
	}
	public void setAddress(final String address) {
		this.address = address;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Calendar getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(final Calendar birthdate) {
		this.birthdate = birthdate;
	}

	public boolean isBanned() {
		return this.banned;
	}

	public void setBanned(final boolean banned) {
		this.banned = banned;
	}


	//Relationships

	private UserAccount					userAccount;
	private Collection<ArticleRating>	articleRatings;


	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}
	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@NotNull
	@Valid
	@OneToMany
	public Collection<ArticleRating> getArticleRatings() {
		return this.articleRatings;
	}

	public void setArticleRatings(final Collection<ArticleRating> articleRatings) {
		this.articleRatings = articleRatings;
	}
}
