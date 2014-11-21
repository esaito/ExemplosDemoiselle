package gov.pr.celepar.tabeliao.dao;
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
import java.util.Collection;
import java.util.List;
import gov.pr.celepar.framework.exception.ApplicationException;
import gov.pr.celepar.tabeliao.pojo.AtualizaLCR;

/**
 * Classe de manipulacao de objetos da classe AtualizaLCR.
 * 
 * @version 1.0
 *
 */
public interface AtualizaLCRDAO {	
	
	public void salvar(AtualizaLCR obj) throws ApplicationException, Exception;

	public void alterar(AtualizaLCR obj) throws ApplicationException, Exception;
	
	public void excluir(AtualizaLCR obj) throws ApplicationException, Exception;
	
	public Collection<AtualizaLCR> listar(Integer qtdPagina, Integer numPagina) throws ApplicationException, Exception;
	
	public Collection<AtualizaLCR> listar() throws ApplicationException, Exception;
	
	public Long buscarQtdLista() throws ApplicationException;
	
	public AtualizaLCR obter(long obj) throws ApplicationException, Exception;

	public AtualizaLCR buscarPorIp(String ip) throws ApplicationException, Exception;
	
	public List<AtualizaLCR> buscarServicosParados() throws ApplicationException, Exception;
}
