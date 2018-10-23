
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FriendRequestRepository;
import domain.FriendRequest;
import domain.User;

@Service
@Transactional
public class FriendRequestService {

	@Autowired
	private FriendRequestRepository	friendRequestRepository;

	@Autowired
	private UserService				userService;


	public FriendRequest create() {
		final FriendRequest result;
		result = new FriendRequest();
		result.setAccepted(false);
		return result;
	}

	public FriendRequest findOne(final int friendRequestId) {
		FriendRequest result;
		Assert.isTrue(friendRequestId != 0);
		result = this.friendRequestRepository.findOne(friendRequestId);
		Assert.notNull(result);
		return result;
	}

	public Collection<FriendRequest> findAll() {
		return this.friendRequestRepository.findAll();
	}

	public FriendRequest findByUsers(final int userId1, final int userId2) {
		return this.friendRequestRepository.findByUsers(userId1, userId2);
	}

	public FriendRequest save(final FriendRequest friendRequest) {
		FriendRequest result;
		Assert.notNull(friendRequest);

		//Check that the sender and the receiver are different users.
		Assert.isTrue(!friendRequest.getSender().equals(friendRequest.getReceiver()));

		//Check that the sender is the logged user.
		Assert.isTrue(friendRequest.getSender().equals(this.userService.findByPrincipal()));

		//Check that the receiver hasn't got any other request from the sender.
		Assert.isTrue(this.friendRequestRepository.numberOfRequests(friendRequest.getSender().getId(), friendRequest.getReceiver().getId()) == 0);

		//Check that the sender has no requests from the receiver.
		Assert.isTrue(this.friendRequestRepository.numberOfRequests(friendRequest.getReceiver().getId(), friendRequest.getSender().getId()) == 0);

		result = this.friendRequestRepository.save(friendRequest);
		this.userService.findByPrincipal().getSentRequests().add(result);
		friendRequest.getReceiver().getReceivedRequests().add(result);

		return result;
	}

	public FriendRequest accept(final FriendRequest friendRequest) {
		FriendRequest result;
		Assert.notNull(friendRequest);
		Assert.isTrue(this.userService.findByPrincipal().getReceivedRequests().contains(friendRequest));
		Assert.isTrue(!friendRequest.isAccepted());

		friendRequest.setAccepted(true);
		result = this.friendRequestRepository.save(friendRequest);

		return result;
	}

	public int numberOfRequests(final int user1Id, final int user2Id) {
		return this.friendRequestRepository.numberOfRequests(user1Id, user2Id);
	}

	//Returns 1 if user1 and user2 are friends
	public boolean areFriends(final User user1, final User user2) {
		final int user1Id = user1.getId();
		final int user2Id = user2.getId();

		if (this.friendRequestRepository.areFriends(user1Id, user2Id) == 1)
			return true;
		else
			return false;
	}

	public void delete(final FriendRequest friendRequest) {
		Assert.isTrue(this.userService.findByPrincipal().equals(friendRequest.getSender()));
		Assert.isTrue(!friendRequest.isAccepted());
		this.friendRequestRepository.delete(friendRequest);
	}

	public void reject(final FriendRequest friendRequest) {
		Assert.isTrue(this.userService.findByPrincipal().equals(friendRequest.getReceiver()));
		Assert.isTrue(!friendRequest.isAccepted());
		this.friendRequestRepository.delete(friendRequest);
	}

	public void removeFriend(final FriendRequest friendRequest) {
		Assert.isTrue(friendRequest.isAccepted());
		Assert.isTrue(this.userService.findByPrincipal().equals(friendRequest.getSender()) || this.userService.findByPrincipal().equals(friendRequest.getReceiver()));
		this.friendRequestRepository.delete(friendRequest);
	}

	public Collection<FriendRequest> getSentRequests() {
		final User principal = this.userService.findByPrincipal();
		return this.friendRequestRepository.getSentRequests(principal.getId());
	}

	public Collection<FriendRequest> getReceivedRequests() {
		final User principal = this.userService.findByPrincipal();
		return this.friendRequestRepository.getReceivedRequests(principal.getId());
	}

	public Collection<FriendRequest> getAcceptedRequests() {
		final User principal = this.userService.findByPrincipal();
		return this.friendRequestRepository.getAcceptedRequests(principal.getId());
	}

	public void flush() {
		this.friendRequestRepository.flush();
	}


	@Autowired
	private Validator	validator;


	public FriendRequest reconstruct(final FriendRequest friendRequest, final BindingResult binding) {

		if (friendRequest.getId() != 0) {
			final FriendRequest stored = this.findOne(friendRequest.getId());
			friendRequest.setReceiver(stored.getReceiver());
			friendRequest.setSender(stored.getSender());
		}

		this.validator.validate(friendRequest, binding);

		return friendRequest;

	}
}
