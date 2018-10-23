
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SocialNetwork extends DomainEntity {

	SNetwork	socialNetworkType;
	String		url;


	@NotNull
	public SNetwork getSocialNetworkType() {
		return this.socialNetworkType;
	}

	public void setsocialNetworkType(final SNetwork socialNetworkType) {
		this.socialNetworkType = socialNetworkType;
	}

	@NotBlank
	@URL
	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	//Relationships

}
