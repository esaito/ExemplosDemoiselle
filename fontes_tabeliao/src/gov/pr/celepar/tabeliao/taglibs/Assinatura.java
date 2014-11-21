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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Logger;

/**
 * TagLib para geracao de assinatura do tipo PKCS7/CADES em ambiente local do usuario
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
public class Assinatura extends BodyTagSupport {
	
	private static Logger logger = Logger.getLogger(Assinatura.class);
	private static final long serialVersionUID = 133484565340795430L;
	private String corFundo;
	private String corDentro;
	private String corAbaSelecionada;
	private String corAba;
	private String tipoForm;
	private String valorBotao;
	private String caminhoBiblioteca;
	private String nomeArquivo   = null;
	private String separadorArquivo = null;
	private String anexaArquivo  = null;

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

	public String getAnexaArquivo() {
		return anexaArquivo;
	}

	/**
	 * 
	 * @param anexaArquivo -> Indica se a assinatura sera anexado ao arquivo.
	 */
	public void setAnexaArquivo(String anexaArquivo) {
		this.anexaArquivo = anexaArquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	/**
	 * 
	 * @param nomeArquivo -> Nome do(s) arquivo(s) a ser(em) assinado(s), caso seja
	 *  mais de um, eles devem estar separados pelo conjunto de caracteres definidos no
	 *  separadorArquivo. 
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public String getSeparadorArquivo() {
		return separadorArquivo;
	}

	/**
	 * 
	 * @param separadorArquivo -> Caractere ou conjunto de caracteres que sera utilizado para separar 
	 * os nomes dos arquivos
	 */
	public void setSeparadorArquivo(String separadorArquivo) {
		this.separadorArquivo = separadorArquivo;
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
		logger.debug("TAGLIB DE ASSINATURA");
		logger.debug("Nome Arquivo: " + this.nomeArquivo);
		logger.debug("Separador de Arquivo: " + this.separadorArquivo);
		logger.debug("Anexar Arquivo: " + this.anexaArquivo);
		
		if(this.anexaArquivo != null && !this.anexaArquivo.toLowerCase().equals("null") && this.anexaArquivo.length() > 0) {
			this.anexaArquivo = "true";
		} else {
			this.anexaArquivo = "false";
		}
		
		logger.debug("Anexar Arquivo filtrado: " + this.anexaArquivo);
		
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
		   .append("		acao=\"assinatura\"\n")
		   .append("		corFundo=\"" + corFundo + "\"\n")
		   .append("		corDentro=\"" + corDentro + "\"\n")
		   .append("		corAbaSelecionada=\"" + corAbaSelecionada + "\"\n")
		   .append("		corAba=\"" + corAba + "\"\n")
		   .append("		tipoForm=\"" + tipoForm + "\"\n")
		   .append("		valorBotao=\"" + valorBotao + "\"\n")
		   .append("		caminhoBiblioteca=\"" + caminhoBiblioteca + "\"\n")
		   .append("		nomeArquivo=\"")
		   		.append(this.nomeArquivo)
		   		.append("\"\n")
		   .append("		separadorArquivo=\"")
		   		.append(this.separadorArquivo)
		   		.append("\"\n")
		   .append("		anexaArquivo=\"")
		   		.append(this.anexaArquivo)
		   		.append("\">\n")
		   .append("	</embed>\n")
		   .append("	<noembed>\n")
		   .append("		Esse navegador não suporta o assinador de documentos Tabelião,\n")
		   .append("		É necessário que o Java Plugin 1.6 ou superior esteja instalado.\n")
		   .append("		<script type=\"text/javascript\">\n")
		   .append("			alert('Esse navegador não suporta o assinador de documentos Tabelião,\\n' +\n")
		   .append("				'É necessário que o Java Plugin 1.6 ou superior esteja instalado.');\n")
		   .append("		</script>\n")
		   .append("	</noembed>\n")
		   .append("    </comment>\n")
		   .append("</object>");
	}	
}