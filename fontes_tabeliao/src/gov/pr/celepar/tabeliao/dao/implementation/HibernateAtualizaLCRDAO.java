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
import gov.pr.celepar.tabeliao.dao.AtualizaLCRDAO;
import gov.pr.celepar.tabeliao.database.HibernateUtil;
import gov.pr.celepar.tabeliao.pojo.AtualizaLCR;
import gov.pr.celepar.framework.exception.ApplicationException;
import gov.pr.celepar.framework.util.Data;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.log4j.Logger;

/**
 * Classe de manipulacao de objetos da classe AtualizaLCR - implementacao.
 * 
 *
 */
public class HibernateAtualizaLCRDAO implements AtualizaLCRDAO {
	
	private static Logger log = Logger.getLogger(AtualizaLCRDAO.class);
	
	public HibernateAtualizaLCRDAO() {
		
	}
	
	/**
	 * Salva objeto AtualizaLCR.
	 * 
	 * @param obj Objeto da classe AtualizaLCR
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void salvar(AtualizaLCR obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.save(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.salvar", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.salvar", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	/**
	 * Altera objeto AtualizaLCR.
	 * 
	 * @param obj Objeto da classe AtualizaLCR
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void alterar(AtualizaLCR obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.update(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.alterar", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.alterar", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	/**
	 * Exclui objeto AtualizaLCR.
	 * 
	 * @param obj Objeto da classe AtualizaLCR
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public void excluir(AtualizaLCR obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			session.delete(obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.excluir", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.excluir", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	
	/**
	 * Listagem de objetos AtualizaLCR.
	 * 
	 * @param qtdPagina quantidade de itens listados em cada pagina 
	 * @param numPagina numero da pagina a ser buscada
	 * @return Lista de objetos AtualizaLCR
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	@SuppressWarnings("unchecked")
	public Collection<AtualizaLCR> listar(Integer qtdPagina, Integer numPagina) throws ApplicationException, Exception {
		Collection<AtualizaLCR> coll = new ArrayList<AtualizaLCR>();
		
		try {
			Session session = HibernateUtil.currentSession();
			Criteria q = session.createCriteria(AtualizaLCR.class);
			if (qtdPagina != null && numPagina != null) {
				q.setMaxResults(qtdPagina.intValue());
				q.setFirstResult( (numPagina.intValue()-1) * qtdPagina.intValue() );
			}
			coll = q.list();
		} catch (HibernateException he) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.listar", he);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.listar", e, ApplicationException.ICON_ERRO);
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
	 * Listagem de objetos AtualizaLCR.
	 * 
	 * @return Lista de objetos AtualizaLCR
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public Collection<AtualizaLCR> listar() throws ApplicationException, Exception {
		return this.listar(null, null);	
	}
		
	/**
	 * Obtehm um objeto AtualizaLCR.
	 * 
	 * @param obj Chave primaria do objeto
	 * @throws Exception Caso ocorra erro com hibernate/conexao.
	 */
	public AtualizaLCR obter(long obj) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			return (AtualizaLCR)session.get(AtualizaLCR.class, obj);
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.obter", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.obter", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}

	private static String buscarPorIpSql = "SELECT AtualizaLCR " +
			"FROM gov.pr.celepar.tabeliao.pojo.AtualizaLCR as AtualizaLCR " +
			"WHERE AtualizaLCR.ip = :ip ";
	/**
	 * Busca pelo endereco IP
	 */
	public AtualizaLCR buscarPorIp(String ip) throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarPorIpSql);
			query.setString("ip", ip);
			return (AtualizaLCR)query.uniqueResult();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.buscarPorIp", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.buscarPorIp", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	
	private static final String buscarServicosParados = "SELECT servicos " +
	"FROM gov.pr.celepar.tabeliao.pojo.AtualizaLCR as servicos " +
	"WHERE servicos.dtExecucao < :dataAtualiza";

	/**
	 * Busca os servicos parados a mais de 10 minutos.
	 */
	@SuppressWarnings("unchecked")
	public List<AtualizaLCR> buscarServicosParados() throws ApplicationException, Exception {
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(buscarServicosParados);
			
			Date dataHora = new Date(); // data e hora atual
			Date dataHoraMenos = null;
			int qtdMin = -10;  // Diminui 10 minutos
			Calendar calendar = Data.getCalendar(dataHora);
			calendar.add(Calendar.MINUTE, qtdMin);
			dataHoraMenos = calendar.getTime();
			query.setTimestamp("dataAtualiza", dataHoraMenos);
			
			return query.list();
		} catch (HibernateException e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.buscarServicosParados", e);
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.buscarServicosParados", e, ApplicationException.ICON_ERRO);
		} finally {
			try {
				HibernateUtil.closeSession();
			} catch (Exception e) {
				log.error("Erro ao Fechar Conexao com o Hibernate: ", e);
			}
		}	
	}
	/**
	 * Busca a quantidade de Servicos cadastrados
	 */
	private static final String quantidadeServicos = "SELECT count(*) " +
	"FROM gov.pr.celepar.tabeliao.pojo.AtualizaLCR as servicos";
	public Long buscarQtdLista() throws ApplicationException {
		Long qtd = null;
		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(quantidadeServicos);
			qtd = (Long) query.uniqueResult();
		} catch (Exception e) {
			throw new ApplicationException("mensagem.erro.atualizaLCR.qtdServicos", e);
		}catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				HibernateUtil.closeSession();
			}catch (Exception e) {
				log.error("Problema ao tentar fechar conexao com o banco de dados: buscarQtdServicos", e);
			}
		}		
		return qtd;		
	}	
}