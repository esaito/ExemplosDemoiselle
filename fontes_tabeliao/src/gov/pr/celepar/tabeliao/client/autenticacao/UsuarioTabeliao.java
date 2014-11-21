package gov.pr.celepar.tabeliao.client.autenticacao;
/*
Este programa é licenciado de acordo com a 
LPG-AP (LICENÇA PÚBLICA GERAL PARA PROGRAMAS DE COMPUTADOR DA ADMINISTRAÇÃO PÚBLICA), 
versão 1.1 ou qualquer versão posterior.
A LPG-AP deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa.
Caso uma cópia da LPG-AP não esteja disponível junto com este Programa, você pode contatar o LICENCIANTE ou então acessar diretamente:
http://www.celepar.pr.gov.br/licenca/LPG-AP.pdf
Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa 
é preciso estar de acordo com os termos da LPG-AP
*/
import gov.pr.celepar.sentinela.client.pojo.UsuarioAutenticado;

/**
 * Classe do Tabeliao que implementa gov.pr.celepar.sentinela.client.pojo.UsuarioAutenticado
 * para uso com o proto-agente SENTINELA
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */

public class UsuarioTabeliao implements UsuarioAutenticado {

	private String nome;
	private String email;
	private String cpf;
	
	/**
	 * 
	 * @param nome
	 * @param email
	 * @param cpf
	 */
	public UsuarioTabeliao(String nome, String email, String cpf){
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
	}
	
	@Override
	public String getCpf() {
		return cpf;
	}

	@Override
	public String getEmail() {
		return email;
	}
	
	@Override
	public String getLogin() {
		return null;
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public String getOrgaoExpedidor() {
		return null;
	}

	@Override
	public String getRg() {
		return null;
	}

	@Override
	public void setLogin(String arg0) {
		// Auto-generated method stub 
	}

}
