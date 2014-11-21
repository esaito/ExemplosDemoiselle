
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import gov.pr.celepar.tabeliao.dao.CertificadoRevogadoDAO;
import gov.pr.celepar.tabeliao.database.HibernateUtil;
import gov.pr.celepar.tabeliao.pojo.CertificadoRevogado;
import gov.pr.celepar.framework.exception.ApplicationException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.log4j.Logger;
import gov.pr.celepar.framework.util.Data;

/**
 * Classe de manipulacao de objetos da classe CertificadoRevogado.
 * 
 * @author CodeGenerator - Esta classe foi gerada automaticamente
 * @since 1.0
 * @version 1.0, Fri Sep 01 13:27:14 BRT 2006
 *
 */
public class HibernateCertificadoRevogadoDAO implements CertificadoRevogadoDAO {
	
	private static Logger log = Logger.getLogger(CertificadoRevogadoDAO.class);
	
	public HibernateCertificadoRevogadoDAO() {
		
	}
	
	/**
	 * Salva objeto CertificadoRevogado.
	 * 
	 * @param obj Objeto da classe CertificadoRevogado
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void salvar(CertificadoRevogado obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.save(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.salvar", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.salvar", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}

	
	/**
	 * Altera objeto CertificadoRevogado.
	 * 
	 * @param obj Objeto da classe CertificadoRevogado
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void alterar(CertificadoRevogado obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.update(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.alterar", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.alterar", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	/**
	 * Exclui objeto CertificadoRevogado.
	 * 
	 * @param obj Objeto da classe CertificadoRevogado
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void excluir(CertificadoRevogado obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.delete(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.excluir", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.excluir", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	/**
	 * Listagem de objetos CertificadoRevogado.
	 * 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos CertificadoRevogado
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public Collection<CertificadoRevogado> listar(Integer qtdPagina, Integer numPagina) throws ApplicationException, Exception {
		Collection<CertificadoRevogado> coll = new ArrayList<CertificadoRevogado>();
		
		try {
			Session session = HibernateUtil.currentSession();
			Criteria q = session.createCriteria(CertificadoRevogado.class);
			if (qtdPagina != null && numPagina != null) {
				q.setMaxResults(qtdPagina.intValue());
				q.setFirstResult( (numPagina.intValue()-1) * qtdPagina.intValue() );
			}
			coll = q.list();
		} catch (HibernateException he) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.listar", he);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.listar", e, ApplicationException.ICON_ERRO);
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
	 * Listagem de objetos CertificadoRevogado.
	 * 
	 * @return Lista de objetos CertificadoRevogado
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<CertificadoRevogado> listar() throws ApplicationException, Exception {
		return this.listar(null, null);	
	}
		
	/**
	 * Obtem um objeto CertificadoRevogado.
	 * 
	 * @param obj Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public CertificadoRevogado obter(long obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			return (CertificadoRevogado)session.get(CertificadoRevogado.class, obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.obter", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.obter", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	private static final String buscarPorKeyIdSql = "SELECT lcr " +
			"FROM gov.pr.celepar.tabeliao.pojo.CertificadoRevogado as lcr " +
			"WHERE lcr.certificadoAc.keyId = :key ";
	public CertificadoRevogado buscarPorKeyId(String key) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarPorKeyIdSql).setCacheable(true);
			
			query.setString("key", key);
			
			return (CertificadoRevogado)query.uniqueResult();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.buscarPorKeyId", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.buscarPorKeyId", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	private static final String buscarCertificadosVencidosSql = "SELECT lcr " +
			"FROM gov.pr.celepar.tabeliao.pojo.CertificadoRevogado as lcr " +
			"WHERE lcr.dtProximaAtualizacao <= :dataAtual " +
			"  AND lcr.certificadoAc.dtValidadeFim >= :dataAtual2 ";
	@SuppressWarnings("unchecked")
	/**
	 * Busca os certificados vencidos
	 */
	public List<CertificadoRevogado> buscarCertificadosVencidos() throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarCertificadosVencidosSql);
			
			query.setTimestamp("dataAtual", new Date());
			query.setTimestamp("dataAtual2", new Date());
			
			return query.list();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.buscarCertificadosVencidos", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.buscarCertificadosVencidos", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}

	private static final String buscarCertificadosVencidosSql2 = "SELECT lcr " +
			"FROM gov.pr.celepar.tabeliao.pojo.CertificadoRevogado as lcr " +
			"WHERE lcr.dtProximaAtualizacao <= :dataAtual " +
			"  AND lcr.certificadoAc.dtValidadeFim >= :dataAtual2 ";
	
	@SuppressWarnings("unchecked")
	/**
	 *  Busca os certificados vencidos e à vencer no prazo de 15 minutos, utilizando assim o recurso
	 *  da lista sobreposta.
	 */
	public List<CertificadoRevogado> buscarCertificadosVencidos2() throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarCertificadosVencidosSql2).setCacheable(true);
			
			Date data = new Date();
			Date dataMais = null;
			int qtdMin = +15;  // Aumentará 15 minutos
			Calendar calendar = Data.getCalendar(data);
	        calendar.add(Calendar.MINUTE, qtdMin);
	        dataMais = calendar.getTime();
			
		
			query.setTimestamp("dataAtual", dataMais);
			query.setTimestamp("dataAtual2", data);
			
			return query.list();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.buscarCertificadosVencidos2", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.certificadoRevogado.buscarCertificadosVencidos2", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}	
}
