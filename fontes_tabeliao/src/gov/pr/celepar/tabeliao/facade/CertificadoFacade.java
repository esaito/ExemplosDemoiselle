package gov.pr.celepar.tabeliao.facade;
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
import gov.pr.celepar.framework.exception.ApplicationException;
import gov.pr.celepar.tabeliao.core.TabeliaoCertificate;
import gov.pr.celepar.tabeliao.dao.CertificadoAcDAO;
import gov.pr.celepar.tabeliao.dao.CertificadoPublicoDAO;
import gov.pr.celepar.tabeliao.dao.factory.DAOFactory;
import gov.pr.celepar.tabeliao.database.HibernateUtil;
import gov.pr.celepar.tabeliao.pojo.CertificadoAc;
import gov.pr.celepar.tabeliao.pojo.CertificadoPublico;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * classe FACADE de servicos para manipulacao dos certificados armazenados na base de dados do Tabeliao.
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */

public class CertificadoFacade{
		

	/**
	 * Busca de certificado de Autoridade Certificadora.
	 * @param keyId
	 * @return CertificadoAc
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public static CertificadoAc buscarCertificadoAcPorKeyId(String keyId) throws ApplicationException, Exception {
		CertificadoAcDAO certificadoAcDAO = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCertificadoAcDAO();
		return certificadoAcDAO.buscarPorKeyId(keyId);
	}
	
	/**
	 * Busca por certificado Publico da Autoridade certificadora
	 * @param certificadoAc -> Certificado da autoridade
	 * @param numSerie
	 * @return  CertificadoPublico (gov.pr.celepar.tabeliao.pojo.CertificadoPublico)
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public static CertificadoPublico buscarCertificadoPublico(CertificadoAc certificadoAc, String numSerie) throws ApplicationException, Exception {
		CertificadoPublicoDAO certificadoPublicoDAO = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCertificadoPublicoDAO();
		return certificadoPublicoDAO.buscarCertificadoPublicoPorCertificadoSerial(certificadoAc, numSerie);
	}
	
	/**
	 * Insere o certificado publico na base de dados do Tabeliao
	 * @param certificadoPublico
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public static void inserirCertificadoPublico(CertificadoPublico certificadoPublico) throws ApplicationException, Exception{
		HibernateUtil.currentTransaction();
		try {
			//Cria uma instancia de certificado
			TabeliaoCertificate tc = new TabeliaoCertificate(certificadoPublico);
			X509Certificate xCert = tc.getX509Certificate();
			
			CertificadoAc certificadoAc = buscarCertificadoAcPorKeyId(tc.getAuthorityKeyIdentifier());
			if(certificadoAc == null) {
				throw new Exception("Não foi possivel localizar o certificado da Ac.");
			}
			certificadoPublico.setCertificadoAc(certificadoAc);
			certificadoPublico.setDn(xCert.getSubjectDN().getName());
			certificadoPublico.setDtInclusao(new Date());
			certificadoPublico.setDtValidadeFim(xCert.getNotAfter());
			certificadoPublico.setDtValidadeIni(xCert.getNotBefore());
			certificadoPublico.setEmail(tc.getEmail());
			certificadoPublico.setIdUsuSentinela(null);
			certificadoPublico.setNome(tc.getCertificadoPara().getProperty("CN"));
			certificadoPublico.setNumSerie(tc.getSerialNumber());
			certificadoPublico.setRevogado(false);
			
			CertificadoPublicoDAO certificadoPublicoDAO = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getCertificadoPublicoDAO();

			certificadoPublicoDAO.salvar(certificadoPublico);
			
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
}