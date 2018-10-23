
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Actor;
import domain.Article;
import domain.Comment;
import domain.Editor;

@Service
@Transactional
public class CommentService {

	@Autowired
	private CommentRepository	commentRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ArticleService		articleService;


	public Comment create() {
		final Comment result;

		result = new Comment();

		return result;
	}

	public Comment findOne(final int commentId) {
		Comment result;
		Assert.isTrue(commentId != 0);
		result = this.commentRepository.findOne(commentId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Comment> findAll() {
		return this.commentRepository.findAll();
	}

	public Collection<Comment> findAllByActor(final int actorId) {
		Assert.isTrue(actorId != 0);
		final Collection<Comment> result = this.commentRepository.findAllByActor(actorId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Comment> findAllByArticle(final int articleId) {
		Assert.isTrue(articleId != 0);
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);
		final Collection<Comment> result = article.getComments();
		Assert.notNull(result);

		return result;
	}

	public Collection<Comment> findAllParentsByArticle(final int articleId) {
		Assert.isTrue(articleId != 0);
		final Collection<Comment> result = this.commentRepository.findAllParentsByArticle(articleId);
		Assert.notNull(result);

		return result;
	}

	public Comment findOneToEdit(final int commentId) {
		Comment result;
		final Actor principal = this.actorService.findByPrincipal();

		Assert.isTrue(commentId != 0);
		result = this.commentRepository.findOne(commentId);
		Assert.notNull(result);
		Assert.isTrue(result.getOwner().equals(principal));

		return result;
	}

	public Comment save(final Comment comment, final Article article) {
		Comment result;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(comment);

		Assert.isTrue(comment.getOwner().equals(principal));

		if (article.getPublicationDate() == null)
			Assert.isTrue(comment.getOwner().equals(article.getEditor()));

		// If a comment is a reply of another, it cannot have replies
		if (comment.getParent() != null)
			Assert.isTrue(comment.getReplies().isEmpty());
		if (!comment.getReplies().isEmpty())
			Assert.isNull(comment.getParent());

		result = this.commentRepository.save(comment);

		if (result.getParent() != null)
			result.getParent().getReplies().add(result);

		article.getComments().add(result);

		return result;
	}

	public void delete(final Comment comment) {
		final Actor principal = this.actorService.findByPrincipal();
		final Article article = this.articleService.findByComment(comment);
		final Editor editor = article.getEditor();
		Assert.notNull(editor);

		Assert.isTrue(this.actorService.isAdmin() || editor.equals(principal) || comment.getOwner().equals(principal));

		article.getComments().remove(comment);

		this.commentRepository.delete(comment);

		Assert.isTrue(!this.commentRepository.findAll().contains(comment));

	}

	public void deleteReply(final Comment comment) {
		Assert.isTrue(this.actorService.isAuthenticated());

		final Article article = this.articleService.findByComment(comment);

		article.getComments().remove(comment);

		this.commentRepository.delete(comment);

		Assert.isTrue(!this.commentRepository.findAll().contains(comment));

	}

	public void flush() {
		this.commentRepository.flush();
	}


	// Reconstruct

	@Autowired
	private Validator	validator;


	public Comment reconstruct(final Comment comment, final Comment parentComment, final BindingResult binding) {
		final Comment commentStored;

		if (comment.getId() == 0) {
			final Actor principal = this.actorService.findByPrincipal();
			final Calendar moment = Calendar.getInstance();
			moment.setTime(new Date());

			comment.setOwner(principal);
			comment.setMoment(moment);
			comment.setReplies(new ArrayList<Comment>());
			if (parentComment != null)
				comment.setParent(parentComment);
		} else {
			commentStored = this.commentRepository.findOne(comment.getId());

			comment.setOwner(commentStored.getOwner());
			comment.setId(commentStored.getId());
			comment.setVersion(commentStored.getVersion());
			comment.setMoment(commentStored.getMoment());
			comment.setReplies(commentStored.getReplies());
			comment.setParent(commentStored.getParent());
		}
		this.validator.validate(comment, binding);

		return comment;
	}

}
