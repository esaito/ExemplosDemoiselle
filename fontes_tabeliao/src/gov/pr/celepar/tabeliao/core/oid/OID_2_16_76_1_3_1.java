package gov.pr.celepar.tabeliao.core.oid;
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
/**
 * Classe OID 2.16.76.1.3.1 <br>
 * <br>
 * Possui alguns atributos de pessoa fisica: <br>
 * <b>*</b> Data de nascimento do titular "DDMMAAAA" <br>
 * <b>*</b> Cadastro de pessoa fisica (CPF) do titular <br>
 * <b>*</b> Numero de Identidade Social - NIS (PIS, PASEP ou CI) <br>
 * <b>*</b> Numero do Registro Geral (RG) do titular <br>
 * <b>*</b> Sigla do orgao expedidor do RG <br>
 * <b>*</b> UF do orgao expedidor do RG <br>
 * <b>*</b> Conforme:<br>
 * nas primeiras 8 (oito) posicoes, a data de nascimento do titular, no formato ddmmaaa; 
 * nas 11 (onze) posicoes subsequentes, o Cadastro de Pessoa Fisica (CPF) do titular; 
 * nas 11 (onze) posicoes subsequentes, o numero de Identificacao Social - NIS (PIS, PASEP ou CI); 
 * nas 15 (quinze) posicoes subsequentes, o numero do Registro Geral - RG do titular; 
 * nas 6 (seis) posicoes subsequentes, as siglas do orgao expedidor do RG e respectiva UF
 * 
 * @author Thiago Meneghello - CELEPAR/GIC
 *
 */
public class OID_2_16_76_1_3_1 extends OIDGenerico {

	public static final String OID = "2.16.76.1.3.1";
	
	protected static final Object CAMPOS[] = {"dtNascimento",     (int) 8,
											  "cpf",              (int)11,
											  "nis",              (int)11,
											  "rg",               (int)15,
											  "orgaoUfExpedidor", (int) 6};

	public OID_2_16_76_1_3_1(){
	}
	
	public void inicializa(){
		super.inicializa(CAMPOS);
	}
	
	/**
	 * 
	 * @return a data de nascimento do titular
	 */
	public String getDataNascimento(){
		return (String)propriedades.get("dtNascimento");
	}

	/**
	 * 
	 * @return numero do Cadastro de Pessoa Fisica (CPF) do titular;
	 */
	public String getCPF(){
		return (String)propriedades.get("cpf");
	}
	
	/**
	 * 
	 * @deprecated Mudou para getNis para seguir a nomenclatura da ICP-BRASIL
	 * @return o numero de Identificacao Social - NIS (PIS, PASEP ou CI)
	 */
	public String getPIS(){
		return (String)propriedades.get("nis");
	}
	
	/**
	 * 
	 * @return o numero de Identificacao Social - NIS (PIS, PASEP ou CI)
	 */
	public String getNIS(){
		return (String)propriedades.get("nis");
	}
	
	/**
	 * 
	 * @return numero do Registro Geral - RG do titular
	 */
	public String getRg(){
		return (String)propriedades.get("rg");
	}
	
	/**
	 * 
	 * @return as siglas do orgao expedidor do RG e respectiva UF
	 */
	public String getOrgaoUfExpedidor(){
		return (String)propriedades.get("orgaoUfExpedidor").trim();
	}
	
}
