package gov.pr.celepar.teste;
/*
Este programa é licenciado de acordo com a 
LPG-AP (LICENÇA PÚBLICA GERAL PARA PROGRAMAS DE COMPUTADOR DA ADMINISTRAÇÃO PÚBLICA), 
versão 1.1 ou qualquer versão posterior.
A LPG-AP deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa.
Caso uma cópia da LPG-AP não esteja disponível junto com este Programa, 
você pode contatar o LICENCIANTE ou então acessar diretamente:
http://www.celepar.pr.gov.br/licenca/LPG-AP.pdf
Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa 
é preciso estar de acordo com os termos da LPG-AP
*/
import java.io.IOException;

import gov.pr.celepar.tabeliao.util.Download;

/**
 * @author esaito
 *
 */
public class Tdownload {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String url = "http://www.certificadodigital.com.br/repositorio/lcr/SerasaSRF.crl";
		try {
			byte[] buffer = Download.downloadByteArray(url,"proxy-02.pr.gov.br","8000", null, null);
			//byte[] buffer = Download.downloadByteArray(url,"proxy.pr.gov.br","8080", "usuario","senha");
			System.out.println(buffer.length);
			} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
