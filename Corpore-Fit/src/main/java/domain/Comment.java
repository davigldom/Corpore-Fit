
package domain;

import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	private String		text;
	private Calendar	moment;


	@NotBlank
	@Column(length = 1000)
	public String getText() {
		return this.text;
	}
	public void setText(final String text) {
		this.text = text;
	}

	@NotNull
	public Calendar getMoment() {
		return this.moment;
	}
	public void setMoment(final Calendar moment) {
		this.moment = moment;
	}


	//Relationships

	private Actor				owner;
	private Comment				parent;
	private Collection<Comment>	replies;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Actor getOwner() {
		return this.owner;
	}

	public void setOwner(final Actor actor) {
		this.owner = actor;
	}

	@Valid
	@ManyToOne
	public Comment getParent() {
		return this.parent;
	}

	public void setParent(final Comment comment) {
		this.parent = comment;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "parent")
	public Collection<Comment> getReplies() {
		return this.replies;
	}

	public void setReplies(final Collection<Comment> comments) {
		this.replies = comments;
	}
}
