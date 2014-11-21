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
import javax.servlet.ServletRequest;
/**
 * Classe de interface para ConteudoHandler, conteudo para o qual a assinatura sera gerada em formato CADES.
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
public interface ConteudoHandler {
	
	public byte[] getConteudo(ServletRequest request, String id);
	
	public boolean isHash();

}
