
package br.gov.serpro.inscricao.view;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractPageBean;
import br.gov.serpro.inscricao.security.Credenciais;


@ViewController
@NextView("/after_login.jsf") 
public class LoginMB extends AbstractPageBean{

	private static final long serialVersionUID = 1L;

	private String usuario  = new String();
	private String senha = new String();
	
	@Inject
	private Credenciais credentials;
	
	@Inject
	private SecurityContext context;
	
	public String doLogin() {
		credentials.setNome(this.getUsuario());
		credentials.setSenha(this.senha);
		context.login();
		return getNextView();
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha() {
		return senha;
	}
	
	public void doLogout() {
		context.logout();
	}
	

}
