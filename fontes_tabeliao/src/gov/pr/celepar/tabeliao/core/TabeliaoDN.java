package gov.pr.celepar.tabeliao.core;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * Classe de propriedades para DN - Distinguished Name
 * 
 * @author Thiago Meneghello <thiagomeneghello@ecelepar.pr.gov.br> CELEPAR/GIC
 *
 * 
 *
 */
public class TabeliaoDN extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dn = null;

	/**
	 * Construtor do CertificadoExtra
	 * @param dn
	 * @throws IOException
	 */
	
	public TabeliaoDN(String dn) throws IOException {
		super();
		this.dn = dn;
		ByteArrayInputStream bis = new ByteArrayInputStream(dn.replaceAll(",", "\n").getBytes());
		load(bis);
		bis.close();
	}
	
	/**
	 * 
	 * @return devolve em formato string
	 */
	public String toString(){
		return dn;
	}
}
