package gov.pr.celepar.tabeliao.dao.factory;
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
import gov.pr.celepar.tabeliao.dao.*;

/**
 * Classe Factory.
 * 
 * @author CodeGenerator - Esta classe foi gerada automaticamente
 * @since 1.0
 * @version 1.0, Fri Sep 01 13:27:14 BRT 2006
 *
 */
public abstract class DAOFactory {
	public static final int HIBERNATE = 1;
	/* public static final int POSTGRESQL = 2; */

	public static DAOFactory getDAOFactory(int whichFactory) {  
		switch (whichFactory) {
	    	case HIBERNATE: 
	    		return new HibernateDAOFactory();
	    	/* case POSTGRESQL:
	    		return new PostgreDAOFactory(); */
	    	default: 
	    		return null;
	    }
	}
	
	public abstract CertificadoAcDAO getCertificadoAcDAO();
	public abstract AcDAO getAcDAO();
	public abstract CertificadoPublicoDAO getCertificadoPublicoDAO();
	public abstract CertificadoRevogadoDAO getCertificadoRevogadoDAO();
	public abstract AtualizaLCRDAO getAtualizaLCRDAO();

}
