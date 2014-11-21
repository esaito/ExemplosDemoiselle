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
import java.io.IOException;
import gov.pr.celepar.framework.util.Data;
import gov.pr.celepar.tabeliao.Constants;
import gov.pr.celepar.tabeliao.client.TabeliaoCertificadoAutenticado;
import gov.pr.celepar.tabeliao.core.TabeliaoCertificate;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Logger;

/**
 * TagLib para formatacao e apresentacao de informacoes gerais de certificado.
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
public class InfoCertificado extends BodyTagSupport {
	
	private static Logger logger = Logger.getLogger(InfoCertificado.class);
	private static final long serialVersionUID = 133484565340795430L;
	private String type = "";
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	public int doEndTag() throws JspException {
		
		if(type == null) {
			throw new JspException("O parâmetro type não foi definido.");
		}
		
		StringBuffer out = new StringBuffer();

		TabeliaoCertificate tc = TabeliaoCertificadoAutenticado.getCertificadoAutenticado();
		if(tc != null) {
			String lowerType = type.toLowerCase();
			
			if(lowerType.equals("nome")) {
				
				out.append(tc.getNome());
				
			} else if(lowerType.equals("cpf")) {
				
				if(tc.getTabeliaoDadosPF() != null) {
					
					out.append(tc.getTabeliaoDadosPF().getCPF());
					
				}
				
			} else if(lowerType.equals("tipocertificado")) {
				
				out.append(tc.getTipoCertificado());
				
			} else if(lowerType.equals("datavalidade")) {
				
				out.append(Data.formataData(tc.getX509Certificate().getNotAfter()));
				
			} else if(lowerType.equals("emitido")) {
				
				try {
					out.append((String)tc.getCertificadoDe().get("CN"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}

		try {
			this.pageContext.getOut().write(out.toString());
		} catch (Exception ex) {
			logger.error("", ex);
			throw new JspException(ex);
		}

		return super.doEndTag();
	}
	
	@SuppressWarnings("unused")
	private void drawScreenLogin(StringBuffer out){
		out.append("<script language=\"JavaScript\">\n")
		   .append("	function doLogin(){\n")
		   .append("		document.tabeliaoForm.submit();\n")
		   .append("	}\n")
		   .append("</script>\n")
		   .append("<form name=\"tabeliaoForm\" action=\"\" method=\"post\">\n")
		   .append("<input type=\"hidden\" name=\"SENTINELA\" value=\"SENTINELA_REQUEST_LOGIN\">")
		   .append("<input type=\"hidden\" name=\"")
		   		.append(Constants.SESSION_UUID)
		   		.append("\" value=\"")
		   		.append(this.getSessionUUID())
		   		.append("\">\n")
		   .append("<input type=\"hidden\" name=\"TABELIAO\">\n")
		   .append("<input type=\"hidden\" name=\"TABELIAO_ASSINATURA\">\n")
		   .append("</form>\n")
		   .append("<object codebase=\"http://java.sun.com/update/1.6.0/jinstall-6-windows-i586.cab\" name=\"TabeliaoApplet\">\n")
		   .append("	<param name=\"code\" value=\"TabeliaoApplet\">\n")
		   .append("	<param name=\"archive\" value=\"TabeliaoApplet.jar\">\n")
		   .append("	<param name=\"type\" value=\"application/x-java-applet;version=1.5\">\n")
		   .append("	<param name=\"mayscript\" value=\"true\">\n")
		   .append("	<param name=\"scriptable\" value=\"true\">\n")
		   .append("	<comment>\n")
		   .append("	<embed\n")
		   .append("		type=\"application/x-java-applet;version=1.5\"\n")
		   .append("		code=\"TabeliaoApplet\"\n")
		   .append("		archive=\"TabeliaoApplet.jar\"\n")
		   .append("		width=\"0\"\n")
		   .append("		height=\"0\"\n")
		   .append("		mayscript=\"true\"\n")
		   .append("		scriptable=\"true\"\n")
		   .append("		pluginspage=\"http://java.sun.com/products/plugin/index.html#download\"\n")
		   .append("		acao=\"autenticacao\">\n")
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
	
	private String getSessionUUID(){
	
		HttpSession session = this.pageContext.getSession();
		String uuid = (String)session.getAttribute(Constants.SESSION_UUID);
		logger.debug("Carregando o UUID para a página de login: " + uuid);
		return uuid;
	}
}