
package domain;

import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Article extends DomainEntity {

	private String		title;
	private String		text;
	private boolean		draft;
	private Calendar	publicationDate;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@Column(length = 1000000000)
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public boolean isDraft() {
		return this.draft;
	}

	public void setDraft(final boolean draft) {
		this.draft = draft;
	}

	public Calendar getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(final Calendar publicationDate) {
		this.publicationDate = publicationDate;
	}


	//Relationships

	private Editor						editor;
	private Collection<ArticleRating>	articleRatings;
	private Collection<Comment>			comments;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Editor getEditor() {
		return this.editor;
	}

	public void setEditor(final Editor editor) {
		this.editor = editor;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	public Collection<ArticleRating> getArticleRatings() {
		return this.articleRatings;
	}

	public void setArticleRatings(final Collection<ArticleRating> articleRatings) {
		this.articleRatings = articleRatings;
	}

	@NotNull
	@Valid
	@OneToMany
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}
}
