
package services;

import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MeasureRepository;
import domain.Measure;

@Service
@Transactional
public class MeasureService {

	@Autowired
	private MeasureRepository	measureRepository;

	@Autowired
	private UserService			userService;


	public Measure create() {
		final Measure result;
		result = new Measure();
		result.setCreationDate(Calendar.getInstance());
		return result;
	}

	public Measure findOne(final int measureId) {
		Measure result;
		Assert.isTrue(measureId != 0);
		result = this.measureRepository.findOne(measureId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Measure> findAll() {
		return this.measureRepository.findAll();
	}

	public Measure save(final Measure measure) {
		Measure result;
		Assert.notNull(measure);

		result = this.measureRepository.save(measure);
		result.setCreationDate(Calendar.getInstance());
		this.userService.findByPrincipal().getMeasures().add(result);

		return result;
	}

	public void delete(final Measure measure) {
		Assert.isTrue(measure.getId() != 0);
		this.measureRepository.delete(measure);
		Assert.isTrue(!this.measureRepository.findAll().contains(measure));
	}

	public void flush() {
		this.measureRepository.flush();
	}


	@Autowired
	private Validator	validator;


	public Measure reconstruct(final Measure measure, final BindingResult binding) {

		this.validator.validate(measure, binding);

		return measure;

	}

}
