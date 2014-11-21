package gov.pr.celepar.tabeliao.dao.factory;
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
import gov.pr.celepar.tabeliao.dao.implementation.*;
import gov.pr.celepar.tabeliao.dao.*;

/**
 * Classe Factory para DAOs que utilizam Hibernate.
 * 
 * @author CodeGenerator - Esta classe foi gerada automaticamente
 * @since 1.0
 * @version 1.0, Fri Sep 01 13:27:14 BRT 2006
 *
 */
public class HibernateDAOFactory extends DAOFactory {	

	public CertificadoAcDAO getCertificadoAcDAO() {
		 return new HibernateCertificadoAcDAO();
	}

	public AcDAO getAcDAO() {
		 return new HibernateAcDAO();
	}

	public CertificadoPublicoDAO getCertificadoPublicoDAO() {
		 return new HibernateCertificadoPublicoDAO();
	}

	public CertificadoRevogadoDAO getCertificadoRevogadoDAO() {
		 return new HibernateCertificadoRevogadoDAO();
	}

	public AtualizaLCRDAO getAtualizaLCRDAO() {
		return new HibernateAtualizaLCRDAO();
	}

}
