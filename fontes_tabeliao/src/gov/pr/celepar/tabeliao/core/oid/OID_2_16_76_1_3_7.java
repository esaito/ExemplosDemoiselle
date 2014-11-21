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
 * Classe OID 2.16.76.1.3.7 <br>
 * <br>
 * Possui alguns atributos de pessoa juridica: <br>
 * <b>*</b> Numero do Cadastro Especifico do INSS (CEI) da pessoa juridica titular do certificado <br>
 * 
 * @author Thiago Meneghello - CELEPAR/GIC
 *
 */
public class OID_2_16_76_1_3_7 extends OIDGenerico {

	public static final String OID = "2.16.76.1.3.7";
	
	protected static final Object CAMPOS[] = {"inss",   (int) 12};

	public OID_2_16_76_1_3_7(){
	}
	
	public void inicializa(){
		super.inicializa(CAMPOS);
	}
	
	/**
	 * 
	 * @return Numero do Cadastro Especifico do INSS (CEI) da pessoa juridica titular do certificado
	 */
	public String getInss(){
		return (String)propriedades.get("inss");
	}
}
