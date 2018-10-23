
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class FriendRequest extends DomainEntity {

	private boolean	accepted;


	public boolean isAccepted() {
		return this.accepted;
	}

	public void setAccepted(final boolean accepted) {
		this.accepted = accepted;
	}


	// RELATIONSHIPS

	private User	sender;
	private User	receiver;


	@NotNull
	@Valid
	@ManyToOne
	public User getSender() {
		return this.sender;
	}

	public void setSender(final User sender) {
		this.sender = sender;
	}

	@NotNull
	@Valid
	@ManyToOne
	public User getReceiver() {
		return this.receiver;
	}

	public void setReceiver(final User receiver) {
		this.receiver = receiver;
	}

}
