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
import gov.pr.celepar.tabeliao.util.Base64Utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;

/**
 * Classe para tratar Lista de Certificados Revogados, trabalha com o formato 
 * X509CRL (java.security.cert.x509CRL)
 *  
 * @author Thiago Meneghello <thiagomeneghello@ecelepar.pr.gov.br> CELEPAR/GIC
 *
 */

public class TabeliaoLCR {
	
	private X509CRL crl = null;
	
	/**
	 * 
	 * @param is -> InputStream contendo a LCR
	 * @throws CRLException
	 * @throws CertificateException
	 */
	public TabeliaoLCR(InputStream is) throws CRLException, CertificateException{
		this.crl = getInstance(is);
	}

	/**
	 * 
	 * @param data -> array de bytes com conteudo da LRC.
	 * @throws CRLException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public TabeliaoLCR(byte[] data) throws CRLException, CertificateException, IOException{
		this.crl = getInstance(data);
	}
	
	/**
	 *  Instancia um objeto do tipo X509CRL
	 * @param data -> array de bytes com conteudo da LRC. 
	 * @return Objeto X509CRL
	 * @see java.security.cert.X509CRL
	 * @throws CRLException
	 * @throws IOException
	 * @throws CertificateException
	 */
	private X509CRL getInstance(byte[] data) throws CRLException, IOException, CertificateException{
		X509CRL crl = null;
		
		try {
			//Tenta carregar a CRL como se fosse um arquivo binario!
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			crl = getInstance(bis);
			bis.close();
			bis = null;
		} catch (CRLException e) {
			//Nao conseguiu carregar o arquivo. Verifica se ele esta codificado em Base64
			byte[] data2 = null;
			try {
				data2 = Base64Utils.base64Decode(new String(data));
			} catch (Exception e2) {
				//Nao foi possivel decodificar o arquivo em Base64
				throw e;
			}
			
			ByteArrayInputStream bis = new ByteArrayInputStream(data2);
			crl = getInstance(bis);
			bis.close();
			bis = null;
		}
		
		return crl;
	}
	
	/**
	 * Instancia um objeto do tipo X509CRL
	 * @param is -> InputStream contendo a LCR
	 * @return Objeto X509CRL
	 * @see java.security.cert.X509CRL
	 * @throws CRLException
	 * @throws CertificateException
	 */
	private X509CRL getInstance(InputStream is) throws CRLException, CertificateException{
		X509CRL crl = null;
		
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		crl = (X509CRL)cf.generateCRL(is);
		
		return crl;
	}
	
	/**
	 * retorna a LCR
	 * @return Objeto X509CRL
	 * @see java.security.cert.X509CRL
	 */
	public X509CRL getCRL(){
		return crl;
	}
}
