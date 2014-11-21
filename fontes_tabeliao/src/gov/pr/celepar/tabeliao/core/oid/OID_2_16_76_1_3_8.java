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
 * Classe OID 2.16.76.1.3.8 <br>
 * <br>
 * Possui alguns atributos de equipamento: <br>
 * <b>*</b> Nome empresarial constante do Cadastro Nacional de Pessoa Juridica (CNPJ),
 * sem abreviacoes, se o certificado for de pessoa juridica<br>
 * 
 * @author Thiago Meneghello - CELEPAR/GIC
 *
 */
public class OID_2_16_76_1_3_8 extends OIDGenerico {

	public static final String OID = "2.16.76.1.3.8";
	
	public OID_2_16_76_1_3_8(){
	}
	
	public void inicializa(){
//		super.inicializa(CAMPOS);
	}
	
	/**
	 * 
	 * @return Nome empresarial constante do Cadastro Nacional de Pessoa Juridica (CNPJ),
	 *  sem abreviacoes, se o certificado for de pessoa juridica
	 */
	public String getNome(){
		return super.getData();
	}
}