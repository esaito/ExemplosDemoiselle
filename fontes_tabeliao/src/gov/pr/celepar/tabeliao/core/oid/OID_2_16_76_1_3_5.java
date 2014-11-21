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
 * Classe OID 2.16.76.1.3.5 <br>
 * <br>
 * Possui alguns atributos de pessoa fisica: <br>
 * <b>*</b> Numero de inscricao do Titulo de Eleitor <br>
 * <b>*</b> Zona Eleitoral <br>
 * <b>*</b> Secao <br>
 * <b>*</b> Municipio do titulo <br>
 * <b>*</b> UF do titulo <br>
 * 
 * @author Thiago Meneghello - CELEPAR/GIC
 *
 */
public class OID_2_16_76_1_3_5 extends OIDGenerico {

	public static final String OID = "2.16.76.1.3.5";
	
	protected static final Object CAMPOS[] = {"titulo",    (int)12,
		                                      "zona",      (int) 3,
		                                      "secao",     (int) 4,
		                                      "municipioUf", (int)22};

	public OID_2_16_76_1_3_5(){
	}
	
	public void inicializa(){
		super.inicializa(CAMPOS);
	}
	/**
	 * 
	 * @return String de 12 posicoes com o numero do Titulo de eleitor
	 */
	public String getTitulo(){
		return (String)propriedades.get("titulo");
	}
	
	/**
	 * 
	 * @return String de 3 posicoes com o numero da zona eleitoral
	 */
	public String getZona(){
		return (String)propriedades.get("zona");
	}
	
	/**
	 * 
	 * @return String de 4 posicoes com o numero da secao eleitoral
	 */
	public String getSecao(){
		return (String)propriedades.get("secao");
	}
	
	/**
	 * 
	 * @return String de 22 posicoes com o nome do municipio e a UF correspondente.
	 */
	public String getMunicipioUf(){
		return (String)propriedades.get("municipioUf").trim();
	}
}