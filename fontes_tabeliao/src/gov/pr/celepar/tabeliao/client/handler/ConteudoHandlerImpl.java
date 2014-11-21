package gov.pr.celepar.tabeliao.client.handler;
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
import java.io.FileInputStream;
import javax.servlet.ServletRequest;

/**
 * Implementacao da classe para geracao do ConteudoHandler
 * @author Thiago Meneghello - GIC/CELEPAR.
 *
 */
public class ConteudoHandlerImpl implements ConteudoHandler {
	
	/**
	 * 
	 * @param request -> ServletRequest
	 * @param id -> Identificador do arquivo
	 * @return array de byte com o conteudo para assinatura
	 */
	public byte[] getConteudo(ServletRequest request, String id) {
		byte[] ret = null;
		try {
			FileInputStream fis = new FileInputStream(id);
			ret = new byte[(int) fis.getChannel().size()];
			fis.read(ret);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * @return false -> O conteudo nao eh um hash. 
	 */
	public boolean isHash(){
		return false;
	}

}
