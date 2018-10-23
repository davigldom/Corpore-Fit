
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.ArticleRating;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository			actorRepository;

	@Autowired
	private AdministratorService	adminService;

	@Autowired
	private UserAccountService		userAccountService;


	public Actor findOne(final int actorId) {
		Actor result;
		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);
		return result;
	}

	public Actor findOneToEdit(final int actorId) {
		Actor result;
		result = this.findOne(actorId);
		Assert.isTrue(this.findByPrincipal().equals(result));

		return result;
	}

	public Actor findByPrincipal() {
		Actor result;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Actor saveRating(final Actor actor, final ArticleRating ar) {
		final Actor principal = this.findByPrincipal();
		Assert.isTrue(actor.equals(principal));

		principal.getArticleRatings().add(ar);
		final Actor saved = this.actorRepository.save(actor);

		return saved;

	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Actor result;
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Actor findByUserAccountId(final int actorId) {
		Actor result;
		result = this.actorRepository.findByUserAccountId(actorId);
		return result;
	}

	public Actor findByUsername(final String username) {
		final UserAccount ua = this.userAccountService.findByUsername(username);

		final Actor actor = this.findByUserAccount(ua);
		Assert.notNull(actor);

		return actor;
	}

	public boolean findIsBannedByUsername(final String username) {
		boolean banned = false;

		final UserAccount ua = this.userAccountService.findByUsername(username);

		if (ua != null)
			if (this.actorRepository.findBannedUsernames().contains(ua.getUsername()))
				banned = true;

		return banned;

	}

	public Collection<String> findBannedUsernames() {
		return this.actorRepository.findBannedUsernames();
	}

	public Actor banActor(final int actorId) {
		Assert.notNull(this.adminService.findByPrincipal());

		final Actor actor = this.findOne(actorId);
		Assert.notNull(actor);
		Assert.isTrue(!actor.isBanned());

		actor.setBanned(true);

		final Actor banned = this.actorRepository.save(actor);

		Assert.isTrue(banned.isBanned());

		return banned;
	}

	public Actor unbanActor(final int actorId) {
		Assert.notNull(this.adminService.findByPrincipal());

		final Actor actor = this.findOne(actorId);
		Assert.notNull(actor);
		Assert.isTrue(actor.isBanned());

		actor.setBanned(false);

		final Actor unBanned = this.actorRepository.save(actor);

		Assert.isTrue(!unBanned.isBanned());

		return unBanned;
	}

	public boolean isAuthenticated() {
		boolean result = true;
		final Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS")))
			result = false;

		return result;
	}

	public boolean isUser() {
		boolean result = false;
		final Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().toArray()[0].toString().equals("USER"))
			result = true;

		return result;
	}

	public boolean isNutritionist() {
		boolean result = false;
		final Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().toArray()[0].toString().equals("NUTRITIONIST"))
			result = true;

		return result;
	}

	public boolean isManager() {
		boolean result = false;
		final Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().toArray()[0].toString().equals("MANAGER"))
			result = true;

		return result;
	}

	public boolean isEditor() {
		boolean result = false;
		final Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().toArray()[0].toString().equals("EDITOR"))
			result = true;

		return result;
	}

	public boolean isProvider() {
		boolean result = false;
		final Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().toArray()[0].toString().equals("PROVIDER"))
			result = true;

		return result;
	}

	public boolean isAdmin() {
		boolean result = false;
		final Authentication authentication;

		authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().toArray()[0].toString().equals("ADMIN"))
			result = true;

		return result;
	}

	public Collection<Actor> findAll() {
		return this.actorRepository.findAll();
	}

	public Collection<Actor> findAllBanned() {
		Assert.isTrue(this.isAdmin());
		return this.actorRepository.findAllBanned();
	}

	public Collection<Actor> findAllNotBanned() {
		return this.actorRepository.findAllNotBanned();
	}

	public Collection<Actor> findTop() {
		return this.actorRepository.findAllNotBanned();
	}

	public Collection<Actor> findAllByKeyword(String keyword) {
		if (keyword == null)
			keyword = "%";

		Collection<Actor> result = null;
		result = this.actorRepository.findAllByKeyword(keyword);
		return result;
	}

}
