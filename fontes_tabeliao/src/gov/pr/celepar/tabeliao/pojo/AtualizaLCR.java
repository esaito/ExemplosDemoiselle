package gov.pr.celepar.tabeliao.pojo;
// Generated 01/09/2006 13:27:07 by J-Querena using Hibernate Tools 3.1.0.beta5

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
import java.util.Date;

/**
 * Classe POJO para servico de atualizacao de LCR
 */
public class AtualizaLCR  implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 950245328620169148L;
	// Fields    
     /**
	 * 
	 */
     private String ip;
     private Date dtExecucao;
     private String servidor;
     // Constructors

    /** default constructor */
    public AtualizaLCR() {
    }

	/**
	 * 
	 * Construtor Minimo
	 * @param ip
	 * @param dtExecucao
	 */
	 
    public AtualizaLCR(String ip, Date dtExecucao) {
        this.ip = ip;
        this.dtExecucao = dtExecucao;
    }

    /**
	 * Construtor Completo
	 * @param ip
	 * @param dtExecucao
	 * @param servidor
	 */
    public AtualizaLCR(String ip, Date dtExecucao, String servidor) {
        this.ip = ip;
        this.dtExecucao = dtExecucao;
        this.servidor = servidor;
    }

	/**
	 * @return Endereco IP do Servidor de Atualização
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip => Atribui o endereço IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return Data e hora da última execução do serviço
	 */
	public Date getDtExecucao() {
		return dtExecucao;
	}

	/**
	 * @param dtExecucao => atribui a data e hora da última execução do serviço
	 */
	public void setDtExecucao(Date dtExecucao) {
		this.dtExecucao = dtExecucao;
	}

	/**
	 * @return Nome completo do Servidor de serviço de atualização
	 */
	public String getServidor() {
		return servidor;
	}

	/**
	 * @param servidor => atribui Nome completo do Servidor de serviço de atualização
	 */
	public void setServidor(String servidor) {
		this.servidor = servidor;
	}   
    
}