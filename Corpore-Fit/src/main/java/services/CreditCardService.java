
package services;

import java.util.Collection;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CreditCardRepository;
import domain.CreditCard;
import domain.User;

@Service
@Transactional
public class CreditCardService {

	// Managed
	// repository------------------------------------------------------------
	@Autowired
	private CreditCardRepository	creditCardRepository;

	// Supporting
	// services-----------------------------------------------------------

	@Autowired
	private UserService				userService;


	// CRUD methods ---------------------------------------------------
	public CreditCard create() {
		CreditCard result;
		result = new CreditCard();
		return result;
	}

	public Collection<CreditCard> findAll() {
		Collection<CreditCard> result;

		result = this.creditCardRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public CreditCard findOne(final int creditCardId) {
		CreditCard result;

		result = this.creditCardRepository.findOne(creditCardId);
		Assert.notNull(result);

		return result;
	}

	public CreditCard findOneToEdit(final int creditCardId) {
		CreditCard result;

		result = this.creditCardRepository.findOne(creditCardId);
		Assert.notNull(result);
		Assert.isTrue(this.userService.findByPrincipal().getCreditCards().contains(result));

		return result;
	}

	public CreditCard save(final CreditCard creditCard) {
		Assert.notNull(creditCard);
		Assert.isTrue(creditCard.getId() == 0);

		Assert.isTrue(!this.creditCardRepository.findAllNumbers().contains(creditCard.getNumber()));

		final LocalDate now = LocalDate.now();
		if (creditCard.getExpirationYear() == now.getYear())
			Assert.isTrue(creditCard.getExpirationMonth() > now.getMonthOfYear());
		else if (creditCard.getExpirationYear() < now.getYear())
			Assert.isTrue(false);

		CreditCard result;
		result = this.creditCardRepository.save(creditCard);

		this.userService.findByPrincipal().getCreditCards().add(result);
		return result;
	}

	public void delete(final CreditCard creditCard) {
		Assert.notNull(creditCard);
		Assert.isTrue(creditCard.getId() != 0);
		Assert.isTrue(this.userService.findByPrincipal().getCreditCards().contains(creditCard));
		this.userService.findByPrincipal().getCreditCards().remove(creditCard);
		this.creditCardRepository.delete(creditCard);
	}

	// Other business methods

	public User findOwner(final int creditCardId) {
		final User creditCards = this.creditCardRepository.findOwner(creditCardId);

		return creditCards;
	}

	public void flush() {
		this.creditCardRepository.flush();
	}


	@Autowired
	private Validator	validator;


	public CreditCard reconstruct(final CreditCard creditCard, final BindingResult binding) {

		this.validator.validate(creditCard, binding);

		return creditCard;

	}

}
