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
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import gov.pr.celepar.tabeliao.core.TabeliaoAssinaturaPKCS7;
import gov.pr.celepar.tabeliao.core.TabeliaoCertificate;
import gov.pr.celepar.tabeliao.core.validacao.TabeliaoResultadoValidacao;

public class TesteLeituraAssinaturaPKCS7 {
	
	public static void main(String[] args) {
		String nomeArquivo = "/tmp/arquivo.txt.pkcs7.software";
//		String nomeArquivo = "/tmp/arquivo.chain.enc.txt.pkcs7";
//		String nomeArquivo = "/tmp/arquivo.chain.noenc.txt.pkcs7";
//		String nomeArquivo = "/tmp/arquivo.nochain.enc.txt.pkcs7";
//		String nomeArquivo = "/tmp/arquivo.nochain.noenc.txt.pkcs7";
		
		Security.addProvider(new BouncyCastleProvider());
		
		try {
			
			TabeliaoAssinaturaPKCS7 tabAssinatura = new TabeliaoAssinaturaPKCS7(new FileInputStream("/tmp/arquivo.txt"), new FileInputStream(nomeArquivo));
			
			TabeliaoCertificate tc = new TabeliaoCertificate(tabAssinatura.getCertificadoAssinante());
			
			System.out.println(tc.getCadeiaCertificados().size());
			
			TabeliaoResultadoValidacao trvCert = tc.valida();

			System.out.println("Carregando o certificado assinante");
			X509Certificate cert = tabAssinatura.getCertificadoAssinante();
			
			System.out.println(cert);
			
			TabeliaoResultadoValidacao trvAss = tabAssinatura.valida();

			System.out.println("*** Validação do certificado utilizado na assinatura: ");
			System.out.println(trvCert);
			
			System.out.println("*** Validação da assinatura: ");
			System.out.println(trvAss);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
