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
import gov.pr.celepar.tabeliao.core.TabeliaoCertificate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class CertificateSoftware {

	
	private static String myCertificate = "MIIFNDCCBBygAwIBAgIEQsUgmDANBgkqhkiG9w0BAQUFADBaMQswCQYDVQQGEwJicjETMBEGA1UEChMKSUNQLUJyYXNpbDEgMB4GA1UECxMXQ2FpeGEgRWNvbm9taWNhIEZlZGVyYWwxFDASBgNVBAMTC0FDIENBSVhBIFBGMB4XDTA2MDkwNjEyNDQyMFoXDTA5MDkwNjEzMTQyMFowgYIxCzAJBgNVBAYTAmJyMRMwEQYDVQQKEwpJQ1AtQnJhc2lsMSAwHgYDVQQLExdDYWl4YSBFY29ub21pY2EgRmVkZXJhbDEUMBIGA1UECxMLQUMgQ0FJWEEgUEYxJjAkBgNVBAMTHVRISUFHTyBNRU5FR0hFTExPOjAzMzQzMjE3OTQ4MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoLY7ex3kwMQyVr+Vs43ePMr0oAGB6lcH5kbVxd1QIOv4BOYl6yea7lpOE2OLvFnXjxtJN3VuMDTYYm5lAA9/ZV9XCcKXZDDWF/v+w9OQdNKdX6d6PWP3uTz8ctOH04CNOGecbUP6nUqwLKI5hyJG7VdR/8b4/ljQHxC0hZGURNQIDAQABo4ICWzCCAlcwDgYDVR0PAQH/BAQDAgXgMFcGA1UdIARQME4wTAYGYEwBAgMIMEIwQAYIKwYBBQUHAgEWNGh0dHA6Ly9pY3AuY2FpeGEuZ292LmJyL3JlcG9zaXRvcmlvL2RwY2FjY2FpeGFwZi5wZGYwKQYDVR0lBCIwIAYIKwYBBQUHAwIGCCsGAQUFBwMEBgorBgEEAYI3FAICMIG+BgNVHREEgbYwgbOBInRoaWFnb21lbmVnaGVsbG9AY2VsZXBhci5wci5nb3YuYnKgPgYFYEwBAwGgNQQzMTUwMTE5ODEwMzM0MzIxNzk0ODEyOTAyNTI3NTM3MDAwMDAwMDAwMzQ3MTQwU1NQIEFDoDQGBWBMAQMFoCsEKTA3NTIwOTc4MDYwNDAxNTAxMjRQT05UQSBHUk9TU0EgICAgICAgIFBSoBcGBWBMAQMGoA4EDDAwMDAwMDAwMDAwMDCBuAYDVR0fBIGwMIGtMHGgb6BtpGswaTELMAkGA1UEBhMCYnIxEzARBgNVBAoTCklDUC1CcmFzaWwxIDAeBgNVBAsTF0NhaXhhIEVjb25vbWljYSBGZWRlcmFsMRQwEgYDVQQDEwtBQyBDQUlYQSBQRjENMAsGA1UEAxMEQ1JMODA4oDagNIYyaHR0cDovL2ljcC5jYWl4YS5nb3YuYnIvcmVwb3NpdG9yaW8vQUNDQUlYQVBGMS5jcmwwHwYDVR0jBBgwFoAUUMLwp1F6NCjO5zxYaNjRvc9mXhkwCQYDVR0TBAIwADAZBgkqhkiG9n0HQQAEDDAKGwRWNy4wAwIDqDANBgkqhkiG9w0BAQUFAAOCAQEAjZTUfEbfGF+BPzcL6crYfRqZV64bXzQrhxDuaXECDb4NwjgoVRxnMHQLxahlDUTBdz90J3fxMhSRAh/lhVLvmkz2DF8eV+QipjjQq5Q2dtPbB40N9cZR9DkoYsYDaDJIlBob6YfRQ2A6E4v3ejr+IXUcJHutJThlY8iSLoTxCUIczP0yHiaUwIvtTDlbi7+m/cZVQ28Iz792j5aSzXHCvD5D13rE3di/NhaL/5zKUGhoyYzZTxljNRG7XZh/ti7S0P/b7KRuOBUoVMviCCJScmRFqtLJfpAev8dfUYvXxFOJjj2wTRozIO3mKk8vEwoamhzegpFtpbMdIKOWmeqiOQ==";
	
	public CertificateSoftware(){
	}
	
	public X509Certificate carregaCertificado(){
		try {
			
			byte[] cert = Base64.decode(myCertificate);
			
			InputStream is = new ByteArrayInputStream(cert);
			
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			X509Certificate x509 = (X509Certificate) cf.generateCertificate(is);
			
			is.close();
			
			return x509;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CertificateSoftware cert = new CertificateSoftware();
		X509Certificate x509Cert = cert.carregaCertificado();

		try {
			@SuppressWarnings("unused")
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			
			TabeliaoCertificate tc = new TabeliaoCertificate(x509Cert);
			
			System.out.println(tc);
			
//			tc.validaCertificadoRevogado();
			
			List<TabeliaoCertificate> list = tc.getCadeiaCertificados();

			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			
			int i = 1;
			for(TabeliaoCertificate tabCert : list) {
				System.out.println("************************************************************************************************************************");
				System.out.println("LISTA DE CERTIFICADO [" + (i++) + "]");
				System.out.println(tabCert);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
