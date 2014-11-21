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
import gov.pr.celepar.tabeliao.core.validacao.TabeliaoResultadoValidacao;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Logger;

/**
 *  TagLib para formatacao e apresentacao de informacoes de validacao de certificado.
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
public class InfoValidacao extends BodyTagSupport {
	
	private static Logger logger = Logger.getLogger(InfoValidacao.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 133484565340795430L;
	
	private TabeliaoResultadoValidacao resultadoValidacao = null;
	private String margem = "";
	private String corOk = "#12AA12";
	private String corAviso = "#AAAA22";
	private String corErro = "#AA1212";
	private String corExcecao = "#FF4400";
	
	public TabeliaoResultadoValidacao getResultadoValidacao() {
		return resultadoValidacao;
	}

	public void setResultadoValidacao(TabeliaoResultadoValidacao resultadoValidacao) {
		this.resultadoValidacao = resultadoValidacao;
	}
	
	public String getCorAviso() {
		return corAviso;
	}

	public void setCorAviso(String corAviso) {
		this.corAviso = corAviso;
	}

	public String getCorErro() {
		return corErro;
	}

	public void setCorErro(String corErro) {
		this.corErro = corErro;
	}

	public String getCorOk() {
		return corOk;
	}

	public void setCorOk(String corOk) {
		this.corOk = corOk;
	}

	public String getCorExcecao() {
		return corExcecao;
	}

	public void setCorExcecao(String corExcecao) {
		this.corExcecao = corExcecao;
	}
	
	public String getMargem() {
		return margem;
	}

	public void setMargem(String margem) {
		this.margem = margem;
	}

	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	public int doEndTag() throws JspException {
		StringBuffer out = new StringBuffer();
		
		drawScreenResultadoValidacao(out);

		try {
			this.pageContext.getOut().write(out.toString());
		} catch (Exception ex) {
			logger.error("", ex);
			throw new JspException(ex);
		}

		return super.doEndTag();
	}
	
	/**
	 *  Desenha na tela o resultado da Validacao 
	 * @param out
	 */
	private void drawScreenResultadoValidacao(StringBuffer out){
		
		if(resultadoValidacao == null) {
			return;
		}
		
		for(String key : resultadoValidacao.getValidacoesOk()) {
			out.append(margem)
				.append("<font color=\"" + corOk + "\">")
				.append(key)
				.append(": ")
				.append("OK")
				.append("</font><br>\n");
		}
		
		for(String key : resultadoValidacao.getValidacoesAviso()) {
			out.append(margem)
			.append("<font color=\"" + corAviso + "\">")
			.append(key)
			.append(": ")
			.append("AVISO [\"" + resultadoValidacao.getDescricao(key) + "\"]")
			.append("</font><br>\n");
		}

		for(String key : resultadoValidacao.getValidacoesErro()) {
			out.append(margem)
			.append("<font color=\"" + corErro + "\">")
			.append(key)
			.append(": ")
			.append("ERRO [\"" + resultadoValidacao.getDescricao(key) + "\"]")
			.append("</font><br>\n");
		}
		
		for(String key : resultadoValidacao.getValidacoesExcecao()) {
			out.append(margem)
			.append("<font color=\"" + corExcecao + "\">")
			.append(key)
			.append(": ")
			.append("EXCEÇÃO [\"" + resultadoValidacao.getDescricao(key) + "\"]")
			.append("</font><br>\n");
		}
	}
	
}
