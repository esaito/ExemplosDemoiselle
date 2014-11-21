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
 * Classe de manipulacao de objetos da classe Ac.
 * 
 * @author CodeGenerator - Esta classe foi gerada automaticamente
 * @since 1.0
 * @version 1.0, Fri Sep 01 13:27:14 BRT 2006
 *
 */
public class HibernateAcDAO implements AcDAO {
	
	private static Logger log = Logger.getLogger(AcDAO.class);
	
	private String formataString(String nome) {
		if(nome == null) {
			return "";
		}
		return nome;
	}
	
	public HibernateAcDAO() {
		
	}
	
	/**
	 * Listagem de objetos Ac.
	 * 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos Ac
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<?> listar(Integer qtdPagina, Integer numPagina) throws ApplicationException, Exception {
		Collection<?> coll = new ArrayList<Object>();
		
		try {
			Session session = HibernateUtil.currentSession();
			Criteria q = session.createCriteria(Ac.class);
			if (qtdPagina != null && numPagina != null) {
				q.setMaxResults(qtdPagina.intValue());
				q.setFirstResult( (numPagina.intValue()-1) * qtdPagina.intValue() );
			}
			coll = q.list();
		} catch (HibernateException he) {
			throw new ApplicationException("mensagem.erro.ac.listar", he);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.ac.listar", e, ApplicationException.ICON_ERRO);
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
	 * Listagem de objetos Ac.
	 * 
	 * @return Lista de objetos Ac
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<?> listar() throws ApplicationException, Exception {
		return this.listar(null, null);	
	}
		
	/**
	 * Obtehm um objeto Ac.
	 * 
	 * @param obj Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Ac obter(long obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			return (Ac)session.get(Ac.class, obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.ac.obter", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.ac.obter", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}

	private static String buscarQtdeAcsPorIdPaiSql = "SELECT count(*) " +
		"FROM gov.pr.celepar.tabeliao.pojo.Ac as ac ";
//		"WHERE ac.pai = :pai ";
	public int buscarQtdeAcsPorIdPai(int idPai) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			String sql = buscarQtdeAcsPorIdPaiSql;
			
			if(idPai == 0) {
				sql = sql + "WHERE ac.pai IS NULL";
			} else {
				sql = sql + "WHERE ac.pai = :pai";
			}
			
			Query query = session.createQuery(sql).setCacheable(true);
			
			if(idPai != 0) {
				query.setInteger("pai", idPai);
			}
			
			return (Integer)query.uniqueResult();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.ac.buscarQtdPorPai", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.ac.buscarQtdPorPai", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}

	private static String buscarAcsPorIdPaiSql = "SELECT ac " +
		"FROM gov.pr.celepar.tabeliao.pojo.Ac as ac ";
//		"WHERE ac.nome LIKE :nome ";
	@SuppressWarnings("unchecked")
	public Collection<Ac> buscarAcsPorIdPai(int idPai) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			String sql = buscarAcsPorIdPaiSql;
			
			if(idPai == 0) {
				sql = sql + "WHERE ac.pai IS NULL ";
			} else {
				sql = sql + "WHERE ac.pai = :pai ";
			}
			sql = sql + "ORDER BY ac.nome";
			
			Query query = session.createQuery(sql).setCacheable(true);
			
			if(idPai != 0) {
				query.setInteger("pai", idPai);
			}
			
			return query.list();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.ac.buscarPorPai", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.ac.buscarPorPai", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	private static String buscarQtdeAcsPorNomeSql = "SELECT count(*) " +
		"FROM gov.pr.celepar.tabeliao.pojo.Ac as ac " +
		"WHERE ac.nome LIKE :nome ";
	public int buscarQtdeAcsPorNome(String nome) throws ApplicationException, Exception {
		nome = "%" + formataString(nome) + "%";
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarQtdeAcsPorNomeSql).setCacheable(true);
			query.setString("nome", nome);
			
			return (Integer)query.uniqueResult();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.ac.buscarQtdPorNome", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.ac.buscarQtdPorNome", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	private static String buscarAcsPorNomeSql = "SELECT ac " +
		"FROM gov.pr.celepar.tabeliao.pojo.Ac as ac " +
		"WHERE ac.nome LIKE :nome ";
	@SuppressWarnings("unchecked")
	public Collection<Ac> buscarAcsPorNome(String nome, int qtdePagina, int indice) throws ApplicationException, Exception {
		nome = "%" + formataString(nome) + "%";
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarAcsPorNomeSql).setCacheable(true);

			query.setMaxResults(qtdePagina);
			query.setFirstResult((indice - 1) * qtdePagina);
			
			query.setString("nome", nome);
			return query.list();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.ac.buscarPorNome", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.ac.buscarPorNome", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	private static String buscarAcPorDNSql = "SELECT ac " +
			"FROM gov.pr.celepar.tabeliao.pojo.Ac as ac " +
			"WHERE ac.dn = :dn ";
	public Ac buscarAcPorDN(String dn) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarAcPorDNSql).setCacheable(true);
			query.setString("dn", dn);
			return (Ac)query.uniqueResult();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.ac.buscarAcPorDN", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.ac.buscarAcPorDN", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	
}
