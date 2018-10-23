
package forms;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

public class RegisterNutritionist {

	private String		username;
	private String		password;
	private String		repeatedPassword;
	private String		name;
	private String		surname;
	private String		phone;
	private String		email;
	private String		curriculum;
	private String		officeAddress;
	private boolean		acceptedTerms;
	private String		address;
	private Calendar	birthdate;
	private String		photo;


	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@Size(min = 5, max = 32)
	public String getRepeatedPassword() {
		return this.repeatedPassword;
	}

	public void setRepeatedPassword(final String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}

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

	@NotNull
	public boolean isAcceptedTerms() {
		return this.acceptedTerms;
	}

	public void setAcceptedTerms(final boolean acceptedTerms) {
		this.acceptedTerms = acceptedTerms;
	}

	@URL
	@NotBlank
	public String getCurriculum() {
		return this.curriculum;
	}

	public void setCurriculum(final String curriculum) {
		this.curriculum = curriculum;
	}

	@NotBlank
	public String getOfficeAddress() {
		return this.officeAddress;
	}

	public void setOfficeAddress(final String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
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

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

}
