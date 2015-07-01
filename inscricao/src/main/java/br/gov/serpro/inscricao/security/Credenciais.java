package br.gov.serpro.inscricao.security;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class Credenciais implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nome;
	private String senha;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void limpar() {
		nome = null;
		senha = null;
	}
}
