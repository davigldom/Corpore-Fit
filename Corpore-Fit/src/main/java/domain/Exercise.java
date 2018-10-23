
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Exercise extends DomainEntity {

	private String	title;
	private String	description;
	private String	photo;
	private String	video;


	@NotBlank
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@Column(length=1000)
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}
	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@URL
	public String getVideo() {
		return this.video;
	}
	public void setVideo(final String video) {
		this.video = video;
	}

}
