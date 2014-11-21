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
import gov.pr.celepar.tabeliao.dao.CertificadoAcDAO;
import gov.pr.celepar.tabeliao.dao.CertificadoPublicoDAO;
import gov.pr.celepar.tabeliao.dao.factory.DAOFactory;
import gov.pr.celepar.tabeliao.pojo.CertificadoAc;
import gov.pr.celepar.tabeliao.pojo.CertificadoPublico;

public class TesteTipoCertificado {
	
	public static void main(String args[]) {
		
		DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
		CertificadoAcDAO certificadoAcDAO = dao.getCertificadoAcDAO();
		CertificadoPublicoDAO certificadoPublicoDAO = dao.getCertificadoPublicoDAO();
		
		try {
			CertificadoAc ac = certificadoAcDAO.obter(814);
			TabeliaoCertificate tc = new TabeliaoCertificate(ac);

			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("Certificado de AC:");
			System.out.println(tc);
			
			CertificadoPublico cp = certificadoPublicoDAO.obter(15);
			tc = new TabeliaoCertificate(cp);
			 
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println("Certificado de Usuario:");
			System.out.println(tc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
