
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialNetworkRepository;
import domain.SNetwork;
import domain.SocialNetwork;

@Service
@Transactional
public class SocialNetworkService {

	@Autowired
	private SocialNetworkRepository	socialNetworkRepository;


	public SocialNetwork create() {
		final SocialNetwork result;
		result = new SocialNetwork();
		return result;
	}

	public SocialNetwork findOne(final int socialNetworkId) {
		SocialNetwork result;
		Assert.isTrue(socialNetworkId != 0);
		result = this.socialNetworkRepository.findOne(socialNetworkId);
		Assert.notNull(result);
		return result;
	}

	public Collection<SocialNetwork> findAll() {
		return this.socialNetworkRepository.findAll();
	}

	public SocialNetwork save(final SocialNetwork socialNetwork) {
		SocialNetwork result;
		Assert.notNull(socialNetwork);
		
		result = this.socialNetworkRepository.save(socialNetwork);

//		this.userService.save(this.userService.findByPrincipal());
		return result;
	}

	public void delete(final SocialNetwork socialNetwork) {
		Assert.isTrue(socialNetwork.getId() != 0);

		this.socialNetworkRepository.delete(socialNetwork);
		Assert.isTrue(!this.socialNetworkRepository.findAll().contains(socialNetwork));
	}
	
	
	public SocialNetwork findByTypeAndUser(int userId, SNetwork snType){
		return this.socialNetworkRepository.findByTypeAndUser(userId, snType);
	}
	
	public SocialNetwork findByTypeAndGym(int gymId, SNetwork snType){
		return this.socialNetworkRepository.findByTypeAndGym(gymId, snType);
	}


	//Reconstruct
	@Autowired
	private Validator	validator;


	public SocialNetwork reconstruct(final SocialNetwork socialNetwork, final BindingResult binding) {

		socialNetwork.setId(0);
		socialNetwork.setVersion(0);

		this.validator.validate(socialNetwork, binding);

		return socialNetwork;

	}

}
