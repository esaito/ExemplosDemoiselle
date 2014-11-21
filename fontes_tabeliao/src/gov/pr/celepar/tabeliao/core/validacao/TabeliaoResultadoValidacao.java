package gov.pr.celepar.tabeliao.core.validacao;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe para tratamento dos resultados da validacao de um certificado digital
 * popula o objeto com os resultados obtidos.
 * Retorna Mensagens de 
 * OK.
 * AVISO = Quando ha algum alerta (As mensagens podem ser interpretadas como erros dependendo das normas de cada sistema) 
 * ERRO = Quando ocorre erros nas validacoes de conteudo, cadeia e LCR.
 * EXCECAO = Quando houver excecoes.
 * @author Thiago Meneghello GIC/CELEPAR
 *
 */

public class TabeliaoResultadoValidacao {

	public static final String MSG_OK    = "OK";
	public static final String MSG_AVISO = "AVISO";
	public static final String MSG_ERRO  = "ERRO";
	public static final String MSG_EXCECAO  = "EXCECAO";
	
	public static final String VAL_CONTEUDO = "Assinatura";                      //Certificate    | Assinatura 
	public static final String VAL_CADEIA   = "Cadeia de certificação";          //Certificate OK | Assinatura 
	public static final String VAL_VALIDADE = "Validade do certificado";         //Certificate OK | Assinatura 
	public static final String VAL_LCR      = "Lista de Certificados Revogados"; //Certificate OK | Assinatura 
	
	private List<String> listOk       = new LinkedList<String>();
	private List<String> listAviso    = new LinkedList<String>();
	private List<String> listErro     = new LinkedList<String>();
	private List<String> listExcecao  = new LinkedList<String>();
	private Map<String, String> mapDescricao = new HashMap<String, String>();
	
	public TabeliaoResultadoValidacao(){
	}
	
	/**
	 * Adiciona uma mensagem de sucesso para uma validacao
	 * @param validacao -> Validacao executada
	 * @throws Exception
	 */
	public void addOk(String validacao) throws Exception{
		if(getMensagem(validacao) != null) {
			throw new Exception("Validação '" + validacao + "' já foi incluída.");
		}
		listOk.add(validacao);
	}
	
	/**
	 * Adiciona uma mensagem de aviso para uma validacao
	 * @param validacao -> Validacao executada
	 * @param descricao -> Descricao do aviso
	 * @throws Exception
	 */
	public void addAviso(String validacao, String descricao) throws Exception{
		if(getMensagem(validacao) != null) {
			throw new Exception("Validação '" + validacao + "' já foi incluída.");
		}
		listAviso.add(validacao);
		mapDescricao.put(validacao, descricao);
	}
	
	/**
	 * Adiciona uma mensagem de Erro para uma validacao
	 * @param validacao -> Validacao executada
	 * @param descricao -> Descricao do Erro
	 * @throws Exception
	 */
	public void addErro(String validacao, String descricao) throws Exception{
		if(getMensagem(validacao) != null) {
			throw new Exception("Validação '" + validacao + "' já foi incluída.");
		}
		listErro.add(validacao);
		mapDescricao.put(validacao, descricao);
	}
	
	
	/**
	 * Adiciona uma mensagem de Excecao para uma validacao
	 * @param validacao -> Validacao executada
	 * @param descricao -> Descricao da Excecao
	 * @throws Exception
	 */
	public void addExcecao(String validacao, String descricao) throws Exception{
		if(getMensagem(validacao) != null) {
			throw new Exception("Validação '" + validacao + "' já foi incluída.");
		}
		listExcecao.add(validacao);
		mapDescricao.put(validacao, descricao);
	}
	/**
	 * 
	 * @return true, se ha mensagem de aviso
	 */
	public boolean hasAviso(){
		return (! listAviso.isEmpty());
	}

	/**
	 * 
	 * @return true, se ha mensagem de Erro
	 */
	public boolean hasErro(){
		return (! listErro.isEmpty());
	}
	
	/**
	 * 
	 * @return true, se ha mensagem de Erro
	 */
	public boolean hasExcecao(){
		return (! listExcecao.isEmpty());
	}
	
	/**
	 * 
	 * @param validacao -> Validacao para a qual obtem a mensagem
	 * @return mensagem atribuida a validacao recebida por parametro.
	 */
	public String getMensagem(String validacao){
		if(listOk.contains(validacao)) {
			return MSG_OK;
		} else if(listAviso.contains(validacao)) {
			return MSG_AVISO;
		} else if(listErro.contains(validacao)) {
			return MSG_ERRO;
		} else if(listExcecao.contains(validacao)) {
			return MSG_EXCECAO;
		}
		return null;
	}
	
	/**
	 * 
	 * @param validacao -> validacao para a qual se obtem a descricao
	 * @return descricao atribuida a validacao recebido por parametro
	 */
	public String getDescricao(String validacao){
		return mapDescricao.get(validacao);
	}
	
	/**
	 * 
	 * @return lista com todas as validacoes com sucesso/corretas.
	 */
	public List<String> getValidacoesOk(){
		return Collections.unmodifiableList(listOk);
	}
	
	/**
	 * 
	 * @return Lista com todas as validacoes com aviso.
	 */
	public List<String> getValidacoesAviso(){
		return Collections.unmodifiableList(listAviso);
	}
	
	/**
	 * 
	 * @return Lista com todas as validacoes com erro. 
	 */
	public List<String> getValidacoesErro(){
		return Collections.unmodifiableList(listErro);
	}
	
	/**
	 * 
	 * @return Lista com todas as validacoes com excecao. 
	 */
	public List<String> getValidacoesExcecao(){
		return Collections.unmodifiableList(listExcecao);
	}
	
	/**
	 * @return todas as validacoes em formato texto.
	 */
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		for(String msg : listOk) {
			buffer.append(msg + ": " + MSG_OK + "\n");
		}

		for(String msg : listAviso) {
			buffer.append(msg + ": " + MSG_AVISO + " {" + getDescricao(msg) + "}" + "\n");
		}

		for(String msg : listErro) {
			buffer.append(msg + ": " + MSG_ERRO + " {" + getDescricao(msg) + "}" + "\n");
		}
		
		for(String msg : listExcecao) {
			buffer.append(msg + ": " + MSG_EXCECAO + " {" + getDescricao(msg) + "}" + "\n");
		}
		
		return buffer.toString();
	}
}
