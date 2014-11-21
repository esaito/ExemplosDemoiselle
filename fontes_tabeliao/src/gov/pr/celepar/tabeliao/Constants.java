package gov.pr.celepar.tabeliao;
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

/**
 * Classe de constantes de uso geral.
 * 
 * @version 1.0
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
public class Constants {
	
	public static final String SESSION_UUID = "TABELIAO_SESSION_UUID";

	public static final String TABELIAO_MENSAGEM_AVISO = "TABELIAO_MENSAGEM_AVISO";
	public static final String TABELIAO_MENSAGEM_ERRO  = "TABELIAO_MENSAGEM_ERRO";

	/* Constantes do arquivo de configuração */
	public static final String CONFIG_VALIDA_CADEIA_CERTIFICADO   = "tabeliao.client.valida.cadeia.certificado";
	public static final String CONFIG_VALIDA_LCR                  = "tabeliao.client.valida.LCR";
	public static final String CONFIG_VALIDA_CERTIFICADO_EXPIRADO = "tabeliao.client.valida.certificado.expirado";
	public static final String CONFIG_VALIDA_NIVEL_ACESSO         = "tabeliao.client.valida.nivel.acesso";

	public static final String CONFIG_DIAS_MENSAGEM_EXPIRANDO = "tabeliao.client.dias.mensagem.expirando";
	public static final String CONFIG_NIVEL_ACESSO            = "tabeliao.client.nivel.acesso";
}
