
package services;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EditorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.ArticleRating;
import domain.Editor;
import forms.RegisterActor;

@Service
@Transactional
public class EditorService {

	@Autowired
	private EditorRepository		editorRepository;

	@Autowired
	private UserAccountService		userAccountService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	adminService;


	public Editor create() {

		final UserAccount userAccount = this.userAccountService.create();
		final Authority authority = new Authority();
		authority.setAuthority(Authority.EDITOR);
		userAccount.addAuthority(authority);

		final Editor result;
		result = new Editor();
		result.setUserAccount(userAccount);
		return result;
	}

	public Editor findOne(final int editorId) {
		Editor result;
		Assert.isTrue(editorId != 0);
		result = this.editorRepository.findOne(editorId);
		Assert.notNull(result);
		return result;
	}

	public Editor save(final Editor editor) {
		Editor result;
		Assert.notNull(editor);
		Assert.notNull(editor.getUserAccount().getUsername());
		Assert.notNull(editor.getUserAccount().getPassword());
		if (editor.getId() == 0) {
			Assert.notNull(this.adminService.findByPrincipal());
			String passwordHashed = null;
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			passwordHashed = encoder.encodePassword(editor.getUserAccount().getPassword(), null);
			editor.getUserAccount().setPassword(passwordHashed);
		} else
			Assert.isTrue(this.findByPrincipal().equals(editor));

		result = this.editorRepository.save(editor);
		return result;
	}

	public void delete(final Editor editor) {
		Assert.isTrue(editor.getId() != 0);

		if (this.actorService.isEditor())
			Assert.isTrue(this.findByPrincipal().equals(editor));
		else
			Assert.isTrue(this.actorService.isAdmin());

		this.editorRepository.delete(editor);
		Assert.isTrue(!this.editorRepository.findAll().contains(editor));
	}

	public Editor findByPrincipal() {
		Editor result;
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Editor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Editor result;
		result = this.editorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}


	//Reconstruct 

	@Autowired
	private Validator	validator;


	public Editor reconstructRegister(final RegisterActor editor, final BindingResult binding) {
		Editor result;
		Assert.isTrue(editor.getPassword().equals(editor.getRepeatedPassword()));
		result = this.create();

		result.setName(editor.getName());
		result.setSurname(editor.getSurname());
		result.setPhone(editor.getPhone());
		result.setEmail(editor.getEmail());
		result.setAddress(editor.getaddress());
		result.setPhoto(editor.getPhoto());
		result.setBirthdate(editor.getBirthdate());

		result.setArticleRatings(new ArrayList<ArticleRating>());

		result.getUserAccount().setUsername(editor.getUsername());
		result.getUserAccount().setPassword(editor.getPassword());

		this.validator.validate(result, binding);

		return result;
	}

	public Editor reconstruct(final Editor editor, final BindingResult binding) {
		Editor editorStored;

		if (editor.getId() != 0) {
			editorStored = this.editorRepository.findOne(editor.getId());

			editor.setId(editorStored.getId());
			editor.setUserAccount(editorStored.getUserAccount());
			editor.setVersion(editorStored.getVersion());

			editor.setArticleRatings(editorStored.getArticleRatings());
			editor.setBanned(editorStored.isBanned());
		}
		this.validator.validate(editor, binding);
		return editor;

	}

	public void flush() {
		this.editorRepository.flush();
	}

}
