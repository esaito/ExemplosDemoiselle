package gov.pr.celepar.tabeliao.dao.implementation;
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
import gov.pr.celepar.tabeliao.database.HibernateUtil;
import gov.pr.celepar.framework.exception.ApplicationException;

import java.util.*;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.log4j.Logger;

import gov.pr.celepar.tabeliao.pojo.*;
import gov.pr.celepar.tabeliao.dao.*;

/**
 * Classe de manipulacao de objetos da classe CertificadoPublico.
 * 
 * @author CodeGenerator - Esta classe foi gerada automaticamente
 * @since 1.0
 * @version 1.0, Fri Sep 01 13:27:14 BRT 2006
 *
 */
public class HibernateCertificadoPublicoDAO implements CertificadoPublicoDAO {
	
	private static Logger log = Logger.getLogger(CertificadoPublicoDAO.class);
	
	public HibernateCertificadoPublicoDAO() {
		
	}
	
	/**
	 * Salva objeto CertificadoPublico.
	 * 
	 * @param obj Objeto da classe CertificadoPublico
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void salvar(CertificadoPublico obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.save(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.salvar", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.salvar", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}

	
	/**
	 * Altera objeto CertificadoPublico.
	 * 
	 * @param obj Objeto da classe CertificadoPublico
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void alterar(CertificadoPublico obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.update(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.alterar", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.alterar", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	/**
	 * Exclui objeto CertificadoPublico.
	 * 
	 * @param obj Objeto da classe CertificadoPublico
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void excluir(CertificadoPublico obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.delete(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.excluir", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.excluir", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	/**
	 * Listagem de objetos CertificadoPublico.
	 * 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos CertificadoPublico
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<?> listar(Integer qtdPagina, Integer numPagina) throws ApplicationException, Exception {
		Collection<?> coll = new ArrayList<Object>();
		
		try {
			Session session = HibernateUtil.currentSession();
			Criteria q = session.createCriteria(CertificadoPublico.class);
			if (qtdPagina != null && numPagina != null) {
				q.setMaxResults(qtdPagina.intValue());
				q.setFirstResult( (numPagina.intValue()-1) * qtdPagina.intValue() );
			}
			coll = q.list();
		} catch (HibernateException he) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.listar", he);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.listar", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);				
			}
		}
		return coll;
	}
	
	/**
	 * Listagem de objetos CertificadoPublico.
	 * 
	 * @return Lista de objetos CertificadoPublico
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<?> listar() throws ApplicationException, Exception {
		return this.listar(null, null);	
	}
		
	/**
	 * Obtem um objeto CertificadoPublico.
	 * 
	 * @param obj Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public CertificadoPublico obter(long obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			return (CertificadoPublico)session.get(CertificadoPublico.class, obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.obter", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.obter", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	private static final String buscarCertificadoPublicoPorCertificadoSerialSql = "SELECT certPub " +
			"FROM gov.pr.celepar.tabeliao.pojo.CertificadoPublico as certPub " +
			"WHERE certPub.certificadoAc = :certificadoAc " +
			"  AND certPub.numSerie = :numSerie";
	
	public CertificadoPublico buscarCertificadoPublicoPorCertificadoSerial(CertificadoAc certificadoAc, String numSerie) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarCertificadoPublicoPorCertificadoSerialSql).setCacheable(true);
			query.setParameter("certificadoAc", certificadoAc);
			query.setString("numSerie", numSerie);
			return (CertificadoPublico)query.uniqueResult();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.buscarPorSerial", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoPublico.buscarPorSerial", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	
}
