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
import gov.pr.celepar.tabeliao.util.HashUtil;
import java.io.FileInputStream;
import javax.servlet.ServletRequest;

/**
 * Classe para geracao de Hash/Resumo em SHA1 do conteudo a ser assinado.
 * @author Thiago Meneghello - GIC/CELEPAR.
 *
 */
public class ConteudoHandlerHashImpl implements ConteudoHandler {
	
	/**
	 * @param request -> ServletRequest
	 * @param id -> Identificador do arquivo
	 * @return array de byte com o conteudo a ser assinado.
	 */
	public byte[] getConteudo(ServletRequest request, String id) {
		byte[] ret = null;
		try {
			FileInputStream fis = new FileInputStream(id);
			ret = new byte[(int) fis.getChannel().size()];
			fis.read(ret);
			fis.close();
			
			ret = HashUtil.sha1(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * @return true -> O conteudo eh um hash.
	 */
	public boolean isHash(){
		return true;
	}

}
