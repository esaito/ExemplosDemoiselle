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
 * Classe OID 1.3.6.1.4.1.311.20.2.3 <br>
 * <br>
 * Atributo conhecido como UPN: Utilizado pela micro$oft para logon com SmartCard, <br>
 * Presente em alguns cartoes de determinadas autoridades.
 * NAO eh padrao da ICP-BRASIL
 * <b>*</b> <br>
 * 
 * @author Emerson Sachio Saito - CELEPAR/GIC
 *
 */
public class OID_1_3_6_1_4_1_311_20_2_3 extends OIDGenerico {

	public static final String OID = "1.3.6.1.4.1.311.20.2.3";
	
	public OID_1_3_6_1_4_1_311_20_2_3(){
	}
	
	public void inicializa(){
//		super.inicializa(CAMPOS);
	}
	
	/**
	 * 
	 * @return UPN 
	 */
	public String getUPN(){
		return super.getData();
	}
}