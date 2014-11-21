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
import gov.pr.celepar.framework.exception.ApplicationException;

import gov.pr.celepar.tabeliao.pojo.*;

/**
 * Classe de manipulacao de objetos da classe CertificadoPublico.
 * 
 * @author CodeGenerator - Esta classe foi gerada automaticamente
 * @since 1.0
 * @version 1.0, Fri Sep 01 13:27:14 BRT 2006
 *
 */
public interface CertificadoPublicoDAO {
	
	public void salvar(CertificadoPublico obj) throws ApplicationException, Exception;

	public void alterar(CertificadoPublico obj) throws ApplicationException, Exception;
	
	public void excluir(CertificadoPublico obj) throws ApplicationException, Exception;
	
	public Collection<?> listar(Integer qtdPagina, Integer numPagina) throws ApplicationException, Exception;
	
	public Collection<?> listar() throws ApplicationException, Exception;
	
	public CertificadoPublico obter(long obj) throws ApplicationException, Exception;
	
	public CertificadoPublico buscarCertificadoPublicoPorCertificadoSerial(CertificadoAc certificadoAc, String numSerie) throws ApplicationException, Exception;
}
