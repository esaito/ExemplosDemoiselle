package gov.pr.celepar.tabeliao.taglibs;
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
import java.util.UUID;
import gov.pr.celepar.tabeliao.util.Base64Utils;
import gov.pr.celepar.tabeliao.util.HashUtil;
import gov.pr.celepar.tabeliao.util.Util;
import gov.pr.celepar.tabeliao.client.handler.ConteudoHandler;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Logger;

/**
 * TagLib para geracao de assinatura do tipo PKCS7/CADES em ambiente servidor de aplicacoes (WEB)
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
public class AssinaturaWeb extends BodyTagSupport {
	
	private static Logger logger = Logger.getLogger(AssinaturaWeb.class);
	private static final long serialVersionUID = 133484565340795430L;
	private String corFundo;
	private String corDentro;
	private String corAbaSelecionada;
	private String corAba;		   
	private String tipoForm;
	private String valorBotao;
	private String caminhoBiblioteca;
	private String action;
	private String id;
	private String idSeparador;
	private String conteudoHandler;

	public String getCorFundo() {
		return corFundo;
	}

	/**
	 * 
	 * @param corFundo ->  Define a cor de fundo para a applet. Valores em hexadecimal
	 */
	public void setCorFundo(String corFundo) {
		this.corFundo = corFundo;
	}
	
	public String getCorDentro() {
		return corDentro;
	}

	/**
	 * 
	 * @param corDentro ->  Define a cor interna para a applet. Valores em hexadecimal
	 */
	public void setCorDentro(String corDentro) {
		this.corDentro = corDentro;
	}

	public String getCorAbaSelecionada() {
		return corAbaSelecionada;
	}

	/**
	 * 
	 * @param corAbaSelecionada ->  Define a cor da aba quando selecionada na applet. Valores em hexadecimal
	 */
	public void setCorAbaSelecionada(String corAbaSelecionada) {
		this.corAbaSelecionada = corAbaSelecionada;
	}

	public String getCorAba() {
		return corAba;
	}

	/**
	 * 
	 * @param corAba ->  Define a cor da aba na applet. Valores em hexadecimal
	 */
	public void setCorAba(String corAba) {
		this.corAba = corAba;
	}

	public String getTipoForm() {
		return tipoForm;
	}

	/**
	 * Cujo valor indica:
	 * Se 1 - Apenas a aba para uso de SmartCard/Token sera apresentada, 
	 * Se 2 - Apenas a aba para uso de Arquivo sera apresentada,
	 * Se 3 - Ambas as abas para uso de SmartCard/Token ou Arquivos serao apresentadas.
	 * @param tipoForm -> Tipo do formulario de autenticacao	 *  
	 */
	public void setTipoForm(String tipoForm) {
		this.tipoForm = tipoForm;
	}

	public String getValorBotao() {
		return valorBotao;
	}

	/**
	 * 
	 * @param valorBotao -> Define o valor ou label que aparecera no botao de acao.
	 */
	public void setValorBotao(String valorBotao) {
		this.valorBotao = valorBotao;
	}

	public String getCaminhoBiblioteca() {
		return caminhoBiblioteca;
	}

	/**
	 * 
	 * @param caminhoBiblioteca -> Define o caminho da biblioteca (.so ou .dll) usada pela Leitora de Smartcard
	 *  ou Token.
	 */
	public void setCaminhoBiblioteca(String caminhoBiblioteca) {
		this.caminhoBiblioteca = caminhoBiblioteca;
	}

	public String getAction() {
		return action;
	}

	/**
	 * 
	 * @param action indicar a acao que sera acionada ao executar a assinatura.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	public String getConteudoHandler() {
		return conteudoHandler;
	}

	/**
	 * 
	 * @param conteudoHandler -> conteudo da assinatura
	 */
	public void setConteudoHandler(String conteudoHandler) {
		this.conteudoHandler = conteudoHandler;
	}

	/**
	 * @return Id do documento para assinatura
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id -> Id do documento para assinatura
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIdSeparador() {
		return idSeparador;
	}

	/**
	 * 
	 * @param idSeparador -> Caractere ou conjunto de caracteres que sera utilizado para separar 
	 * os nomes dos arquivos
	 */
	public void setIdSeparador(String idSeparador) {
		this.idSeparador = idSeparador;
	}

	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	public int doEndTag() throws JspException {
		StringBuffer out = new StringBuffer();
		
		drawScreenAssinatura(out);

		try {
			this.pageContext.getOut().write(out.toString());
		} catch (Exception ex) {
			logger.error("", ex);
			throw new JspException(ex);
		}

		return super.doEndTag();
	}
	
	private void drawScreenAssinatura(StringBuffer out){
		logger.debug("TAGLIB DE ASSINATURA WEB");
		logger.debug("Action: " + this.action);
		logger.debug("Id: " + this.id);
		logger.debug("conteudoHandler: " + this.conteudoHandler);

		String[] ids = idSeparador != null ? id.split(idSeparador) : new String[] { id };
		String[] hashs = new String[ids.length];
		boolean isHash = false;
		
		try {
			ConteudoHandler handler = (ConteudoHandler)Class.forName(conteudoHandler).newInstance();
			isHash = handler.isHash();

			for(int i=0 ; i<ids.length ; i++) {
				byte[] conteudo;
				conteudo = handler.getConteudo(pageContext.getRequest(), ids[i]);
				hashs[i] = isHash ? Base64Utils.base64Encode(conteudo) : Base64Utils.base64Encode(HashUtil.sha1(conteudo));  
			}
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
			return;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			return;
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
			return;
		}
		
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String nomeForm = "tabeliaoForm_" + uuid;

		out.append("<object codebase=\"http://java.sun.com/update/1.6.0/jinstall-6-windows-i586.cab\" name=\"TabeliaoApplet\">\n")
				.append("	<param name=\"code\" value=\"gov.pr.celepar.tabeliao.client.applet.TabeliaoApplet\">\n")
				.append("	<param name=\"archive\" value=\"TabeliaoApplet.jar\">\n")
				.append("	<param name=\"type\" value=\"application/x-java-applet;version=1.5\">\n")
				.append("	<param name=\"mayscript\" value=\"true\">\n")
				.append("	<param name=\"scriptable\" value=\"true\">\n")
				.append("	<comment>\n")
				.append("	<embed\n")
				.append("		type=\"application/x-java-applet;version=1.5\"\n")
				.append("		code=\"gov.pr.celepar.tabeliao.client.applet.TabeliaoApplet\"\n")
				.append("		archive=\"TabeliaoApplet.jar\"\n")
				.append("		width=\"440\"\n")
				.append("		height=\"181\"\n")
				.append("		mayscript=\"true\"\n")
				.append("		scriptable=\"true\"\n")
				.append("		pluginspage=\"http://java.sun.com/products/plugin/index.html#download\"\n")
				.append("		acao=\"assinaturaWeb\"\n")
				.append("		corFundo=\"" + corFundo + "\"\n")
				.append("		corDentro=\"" + corDentro + "\"\n")
				.append("		corAbaSelecionada=\"" + corAbaSelecionada + "\"\n")
				.append("		corAba=\"" + corAba + "\"\n")
				.append("		tipoForm=\"" + tipoForm + "\"\n")
				.append("		valorBotao=\"" + valorBotao + "\"\n")
				.append("		caminhoBiblioteca=\"" + caminhoBiblioteca + "\"\n")
				.append("		nomeForm=\"" + nomeForm + "\"\n")
				.append("		hash=\"" + Util.arrayToString(hashs) + "\">\n")
				.append("	</embed>\n")
				.append("	<noembed>\n")
				.append("		Esse navegador não suporta o assinador de documentos Tabelião,\n")
				.append("		É necessário que o Java Plugin 1.6 ou superior esteja instalado.\n")
				.append("		<script type=\"text/javascript\">\n")
				.append("			alert('Esse navegador não suporta o assinador de documentos Tabelião,\\n' + \n")
				.append("				'É necessário que o Java Plugin 1.6 ou superior esteja instalado.');\n")
				.append("		</script>\n")
				.append("	</noembed>\n")
				.append("	</comment>\n")
				.append("</object>\n")
				.append("<form name=\"" + nomeForm + "\" action=\"" + action + "\" method=\"post\">\n");
		
		ids = Util.encodeBase64(ids);
		for(int i=0 ; i<ids.length ; i++) {
			out.append("<input type=\"hidden\" name=\"campoIds\" value=\"" + ids[i] + "\">\n");
		}
		out.append("</form>\n");
	}
	
}
