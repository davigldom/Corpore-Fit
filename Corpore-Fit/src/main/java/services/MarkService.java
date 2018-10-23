
package services;

import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MarkRepository;
import domain.Mark;
import domain.User;

@Service
@Transactional
public class MarkService {

	@Autowired
	private MarkRepository	markRepository;

	@Autowired
	private UserService		userService;


	public Mark create() {
		final Mark result;
		result = new Mark();
		result.setCreationDate(Calendar.getInstance());
		return result;
	}

	public Mark findOne(final int markId) {
		Mark result;
		Assert.isTrue(markId != 0);
		result = this.markRepository.findOne(markId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Mark> findAll() {
		return this.markRepository.findAll();
	}

	public Mark save(final Mark mark) {
		Mark result;
		Assert.notNull(mark);

		result = this.markRepository.save(mark);
		result.setCreationDate(Calendar.getInstance());
		this.userService.findByPrincipal().getMarks().add(result);

		return result;
	}

	public void delete(final Mark mark) {
		Assert.isTrue(mark.getId() != 0);

		this.markRepository.delete(mark);
		Assert.isTrue(!this.markRepository.findAll().contains(mark));
	}

	public Mark getLatestMark(User user) {
		return this.markRepository.getLatestMark(user.getId(), new PageRequest(0, 1)).get(0);
	}

	
	public void flush() {
		this.markRepository.flush();
	}


	@Autowired
	private Validator	validator;


	public Mark reconstruct(final Mark mark, final BindingResult binding) {

		mark.setId(0);
		mark.setVersion(0);

		this.validator.validate(mark, binding);

		return mark;

	}
}
