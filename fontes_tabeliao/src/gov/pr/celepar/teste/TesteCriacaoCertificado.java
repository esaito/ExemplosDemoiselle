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

//import gov.pr.celepar.tabeliao.facade.CertificadoFacade;
//import gov.pr.celepar.tabeliao.pojo.CertificadoAc;

public class TesteCriacaoCertificado {
	
	public static void main(String args[]) {
		
		try {
			//CertificadoAc certAc = new CertificadoAc();
			
			FileInputStream fis = new FileInputStream("/home/thiagom/workspace/gtf-tabeliao/gestao/certificados_teste/certificadoACRaiz.crt");
			byte[] data = new byte[(int)fis.getChannel().size()];
			fis.read(data);
//			certAc.setArquivo(data);
			fis.close();
			
//			CertificadoFacade.inserirCertificado(certAc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
