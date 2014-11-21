package gov.pr.celepar.tabeliao.client;
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
import gov.pr.celepar.tabeliao.core.TabeliaoCertificate;

/**
 * Classe que armazena as informacoes do Certificado que foi utilizado na autenticacao.
 * 
 * @version 1.0
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */

public class TabeliaoCertificadoAutenticado {
	
	private static ThreadLocal<TabeliaoCertificate> certificados = new ThreadLocal<TabeliaoCertificate>();
	
	private TabeliaoCertificadoAutenticado(){
		
	}
	
	/**
	 * Metodo para carregar o certificado
	 * 
	 * @param tc -> certificado digital
	 * @see TabeliaoCertificate
	 *  
	 */
	public static void setCertificadoAutenticado(TabeliaoCertificate tc){
		if(getCertificadoAutenticado() == null) {
			certificados.set(tc);
		}
	}
	
	/**
	 * Metodo para recuperar o certificado
	 * 
	 * @return  TabeliaoCertificate -> certificado digital
	 * @see TabeliaoCertificate
	 *  
	 */
	public static TabeliaoCertificate getCertificadoAutenticado(){
		return certificados.get();
	}
	
	/**
	 * Metodo para limpar as informacoes
	 *  
	 */
	public static void clearCertificadoAutenticado(){
		certificados.set(null);
	}

}
