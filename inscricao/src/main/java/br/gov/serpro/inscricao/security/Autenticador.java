package br.gov.serpro.inscricao.security;

import java.security.Principal;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.security.AuthenticationException;
import br.gov.frameworkdemoiselle.security.Authenticator;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

public class Autenticador implements Authenticator {

	private static final long serialVersionUID = 1L;

	@Inject
	private Credenciais credenciais;

	@Inject
	private ResourceBundle bundle;

	private static boolean authenticated = false;

	@Override
	public void authenticate() throws Exception {

		if (credenciais.getNome().equals("secretaria")
				&& credenciais.getSenha().equals("segredo")) {
			authenticated = true;
		}
		if (!authenticated) {
			throw new AuthenticationException(
					bundle.getString("usuarioNaoAutenticado"));
		}

	}

	@Override
	public Principal getUser() {

		if (authenticated) {

			return new Principal() {

				@Override
				public String getName() {
					return credenciais.getNome();
				}

			};
		} else {
			return null;
		}

	}

	@Override
	public void unauthenticate() throws Exception {
		credenciais.limpar();
		authenticated = false;
	}
}
