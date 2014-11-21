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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Logger;

/**
 * TagLib para geracao de assinatura do tipo EnvelopedXML em ambiente servidor de aplicacao (WEB)
 * Os conteudos dos arquivos devem estar codificados em base64 (use a classe gov.pr.celepar.tabeliao.util.Base64Utils.java)
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */

public class AssinaturaEnvelopedXmlWeb extends BodyTagSupport {
	
	private static final long serialVersionUID = 3693432883091578954L;
	private static Logger logger = Logger.getLogger(AssinaturaEnvelopedXmlWeb.class);
	private String corFundo;
	private String corDentro;
	private String corAbaSelecionada;
	private String corAba;
	private String tipoForm;
	private String valorBotao;
	private String caminhoBiblioteca;
	private String action;
	private String separadorArquivo  = null;
	private String conteudosArquivos  = null;
	private String nomeTagAssinar 	 = null;
	private String politicaId = null;
	private String politicaUri = null;
	private String contraAssinatura = null;
	
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
	 * O valor indica:
	 * Se 1 - Apenas a aba para uso de SmartCard/Token sera apresentada, 
	 * Se 2 - Apenas a aba para uso de Arquivo sera apresentada,
	 * Se 3 - Ambas as abas para uso de SmartCard/Token ou Arquivos serao apresentadas.
	 * @param tipoForm -> Tipo do formulario de autenticacao
	 * 
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
	 * @param action Nome da classe de acao que sera acionada na execucao da assinatura
	 */
	public void setAction(String action) {
		this.action = action;
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
	
	public String getConteudoArquivos() {
		return conteudosArquivos;
	}
	
	/**
	 * 
	 * @param conteudosArquivos -> Conteudo(s) do(s) arquivo(s) a ser(em) assinado(s) codificados em Base64, 
	 * caso seja mais de um, eles devem estar separados pelo conjunto de caracteres definidos no
	 *  separadorArquivo. 
	 */
	public void setConteudosArquivos(String conteudosArquivos) {
		this.conteudosArquivos = conteudosArquivos;
	}
	
	public String getNomeTagAssinar() {
		return nomeTagAssinar;
	}
    
	/**
	 *  Para assinar uma TAG, a mesma deve possuir um ID/id/Id
	 * @param nomeTagAssinar -> Nome da Tag do arquivo XML que sera assinada.
	 */
	public void setNomeTagAssinar(String nomeTagAssinar) {
		this.nomeTagAssinar = nomeTagAssinar;
	}

	public String getPoliticaId(){
		return politicaId;
	}
	
	/**
	 * Para informar o OID da politica de assinatura utilizada.
	 * @param politicaId -> OID da politica
	 */
	public void setPoliticaId(String politicaId){
		this.politicaId = politicaId;
	}
	
	public String getPoliticaUri(){
		return politicaUri;
	}
	
	/**
	 * Para informar o endereco wed da politica de assinatura utilizada.
	 * @param politicaUri -> URI/URL da politica
	 */
	public void setPoliticaUri(String politicaUri){
		this.politicaUri = politicaUri;
	}
	
	public String getContraAssinatura(){
		return contraAssinatura;
	}
	/**
	 * Indica se eh uma contra assinatura
	 * @param  contraAssinatura -> e receber os valores "True" ou "on", faz a Contra-Assinatura
	 */
	public void setContraAssinatura(String contraAssinatura){
		this.contraAssinatura = contraAssinatura;
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
		logger.info("TAGLIB DE ASSINATURA XML ENVELOPED WEB");
		logger.info("Action: " + this.action);
		logger.info("Tag: " + this.nomeTagAssinar);
		logger.info("polId: " + this.politicaId);
		logger.info("polUri: " + this.politicaUri);
	
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
				.append("		acao=\"assinarEnvelopedXmlWeb\"\n")
				.append("		corFundo=\"" + corFundo + "\"\n")
				.append("		corDentro=\"" + corDentro + "\"\n")
				.append("		corAbaSelecionada=\"" + corAbaSelecionada + "\"\n")
				.append("		corAba=\"" + corAba + "\"\n")
				.append("		tipoForm=\"" + tipoForm + "\"\n")
				.append("		valorBotao=\"" + valorBotao + "\"\n")
				.append("		caminhoBiblioteca=\"" + caminhoBiblioteca + "\"\n")
				.append("		nomeForm=\"" + nomeForm + "\"\n")
		   		.append("		separadorArquivo=\"")
		   			.append(this.separadorArquivo)
		   			.append("\"\n")
		   		.append("		conteudosArquivos=\"")
		   			.append(this.conteudosArquivos)
		   			.append("\"\n")
		   		.append("		nomeTagAssinar=\"")
		   			.append(this.nomeTagAssinar)
		   			.append("\"\n")
		   		.append("		politicaId=\"")
		   			.append(this.politicaId)
		   			.append("\"\n")
		   		.append("		politicaUri=\"")
		   			.append(this.politicaUri)
		   			.append("\"\n")
		   		.append("		contraAssinatura=\"")
		   			.append(this.contraAssinatura)
		   			.append("\"\n")
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
				.append("<form name=\"" + nomeForm + "\" action=\"" + action + "\" method=\"post\">\n</form>\n");
	}	
}