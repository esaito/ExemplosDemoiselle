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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CRL;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;

public class TesteCertificadoRaiz {
	
	public static Certificate carregaCertificado(InputStream is) {
		Certificate c = null;
		
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			c = cf.generateCertificate(is);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return c;
	}

	public static Certificate carregaCertificado(String nomeArquivo) {
		Certificate c = null;
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(nomeArquivo);
			c = carregaCertificado(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return c;
	}
	
	public static String toString(byte[] data){
		String hexTable = "0123456789ABCDEF";
		String buffer = "";
		
		for(int i=0 ; i<data.length ; i++) {
			if(i % 16 == 0) {
				if(i > 0) {
					buffer += "\n";
				}
				
				buffer += hexTable.charAt((i & 0xF000) >> 12); 
				buffer += hexTable.charAt((i & 0x0F00) >> 8); 
				buffer += hexTable.charAt((i & 0x00F0) >> 4); 
				buffer += hexTable.charAt(i & 0x000F);
				buffer += ":";
			}
			
			byte b = data[i];
			
			if(i % 16 == 8) {
				buffer += "  ";
			}

			buffer += " ";
			buffer += hexTable.charAt((b & 0xF0) >> 4);
			buffer += hexTable.charAt(b & 0x0F);
		}
		
		return buffer;
	}
	
	
	public static void main(String args[]) {
		String nomeArq1 = "/home/thiagom/workspace/gtf-tabeliao/gestao/certificados_teste/accaixa.crt";
		String nomeArq2 = "/home/thiagom/workspace/gtf-tabeliao/gestao/certificados_teste/ACCAIXAPF_2005.crt";
		@SuppressWarnings("unused")
		String nomeArq3 = "/home/thiagom/workspace/gtf-tabeliao/gestao/certificados_teste/ACCAIXA.cer";
		String nomeArq4 = "/home/thiagom/workspace/gtf-tabeliao/gestao/certificados_teste/ACCAIXAPF1.crt";
		FileInputStream fis = null;
		
		try {
			Certificate c1, c2, c3 = null;
			X509CertImpl x1, x2, x3;
			
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			fis = new FileInputStream(nomeArq4);
			CRL crl = cf.generateCRL(fis);
			@SuppressWarnings("unused")
			X509CRLImpl xcrl = (X509CRLImpl) crl;
			
			
///			c = cf.generateCertificate(is);
			
			
			c1 = carregaCertificado(nomeArq1);
			c2 = carregaCertificado(nomeArq2);
//			c3 = carregaCertificado(nomeArq3);
			
			System.out.println(c2);
			c3.getType();
			
			x1 = (X509CertImpl) c1;
			x2 = (X509CertImpl) c2;
			x3 = (X509CertImpl) c3;
			
//			x1.verify(x1.getPublicKey());
//			x2.verify(x1.getPublicKey());
//			x3.verify(x2.getPublicKey());
			
			System.out.println("******************************************");
			System.out.println(x1);
			System.out.println("******************************************");
			System.out.println(x2);
			System.out.println("******************************************");
			System.out.println(x3);
			System.out.println("******************************************");
			
			System.out.println("");
			
			System.out.println("Certificado[1]: " + x1.getSubjectDN().getName());
			System.out.println("Certificado[2]: " + x2.getSubjectDN().getName());
			System.out.println("Certificado[3]: " + x3.getSubjectDN().getName());
			
			System.out.println("Cadeia valida.");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

}
