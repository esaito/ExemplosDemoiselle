package gov.pr.celepar.tabeliao.client.applet.action;
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
import gov.pr.celepar.tabeliao.client.applet.DocumentSignException;
import gov.pr.celepar.tabeliao.client.applet.TabeliaoAppletUtil;
import gov.pr.celepar.tabeliao.core.GerarEnvelopingXML;
import gov.pr.celepar.tabeliao.util.Base64Utils;
import gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain;
import gov.pr.celepar.tabeliao.util.XmlSigUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;
import javax.swing.JApplet;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.sun.org.apache.xml.internal.security.utils.XMLUtils;

/**
 * Classe de acao para executar assinatura em formato EnvelopingXML para ambiente WEB
 * 
 * @version 1.0
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */

public class ActionAssinarEnvelopingXmlWeb implements ActionHandler {
	
	private JApplet applet;
	
	/**
	 * Metodo de acao para executar assinatura em formato EnvelopingXML para ambiente WEB via applet
	 * 
	 * @param applet Applet do Tabeliao
	 */
	public ActionAssinarEnvelopingXmlWeb(JApplet applet) {
		this.applet = applet;
	}
	
	/**
	 * Metodo executar a assinatura
	 * 
	 * @param arquivo certificado armazenado em arquivo
	 * @param pin PIN do certificado
	 * @param isHardware boolean = true para certificado em token/smartcard

	 */
	public boolean execute(String arquivo, String pin, boolean isHardware){
		String conteudosArquivos = applet.getParameter("conteudosArquivos");
		String separadorArquivo = applet.getParameter("separadorArquivo");
		String varPoliticaId = applet.getParameter("politicaId");
		String varPoliticaUri = applet.getParameter("politicaUri");
		String nomeForm = applet.getParameter("nomeForm");		
		
    	//Tenta pegar o keyStore que possui o certificado do usuario
		PrivateKeyAndCertChain privateKey;
		try {
			privateKey = TabeliaoAppletUtil.loadPrivateKeyAndCertChain(arquivo, pin, isHardware);
		} catch (DocumentSignException e1) {
			TabeliaoAppletUtil.showMensagem(applet, "Ocorreu um erro ao carregar a chave privada do certificado.\nMotivo: " 
													+ e1.getCause().getMessage(),2);
			return false;
		}		
		//Prepara o formulario para carregar os arquivos XML assinados.
		JSObject window   = JSObject.getWindow(applet);
		JSObject document = (JSObject)window.getMember("document");
		Object obj = document.getMember(nomeForm);

		if(!(obj instanceof JSObject)) {
			TabeliaoAppletUtil.showMensagem(applet, "Não foi possível localizar o formulário '" 
											+ nomeForm + 
											"'.\n A taglib assinaturaEnvelopingXmlWeb não deve ser colocada dentro de uma tag <form>.",
											2);
			return false;
		}
		
        JSObject form = (JSObject)obj;        
		
        String script = "var elementXmlAssinado = document.createElement('input');\n" +
        		"elementXmlAssinado.type='hidden';\n" +
        		"elementXmlAssinado.name='campoArquivosXmlAssinados';\n" +
        		"elementXmlAssinado.value='${value}';\n" +
        		"document." + nomeForm + ".appendChild(elementXmlAssinado);";
		try {
			if(separadorArquivo != null) {
    			StringTokenizer token = new StringTokenizer(conteudosArquivos, separadorArquivo);
    			String[] conteudoArquivos = new String[token.countTokens()];
    			int i=0;
    			
    			while(token.hasMoreTokens()) {
    				conteudoArquivos[i++] = token.nextToken();
    			}
    			// Efetua a assinatura
        		for(int j=0 ; j< conteudoArquivos.length ; j++) {
        			
        			byte[] conteudo = Base64Utils.base64Decode(conteudoArquivos[j]);
        			InputSource novoImput = new InputSource(new ByteArrayInputStream(conteudo));
        			Document arquivoAssinar = XmlSigUtil.carregarArquivoXML(novoImput);
        			Document docEnvelopingXmlAssinado = GerarEnvelopingXML.assinarArquivoEnvelopingXml(arquivoAssinar, varPoliticaId, 
        																						varPoliticaUri, privateKey);
        			// Transforma o arquivo em base 64 para jogar no form.
        			ByteArrayOutputStream bos = new ByteArrayOutputStream();
        			XMLUtils.outputDOMc14nWithComments(docEnvelopingXmlAssinado, bos);
    				String texto = Base64Utils.base64Encode(bos.toByteArray());
    	    		window.eval(script.replace("${value}",texto));
        		}
    		} else {
    			// Efetua a assinatura.
    			byte[] conteudo = Base64Utils.base64Decode(conteudosArquivos);
    			InputSource novoImput = new InputSource(new ByteArrayInputStream(conteudo));
    			Document arquivoAssinar = XmlSigUtil.carregarArquivoXML(novoImput);
    			Document docEnvelopingXmlAssinado = GerarEnvelopingXML.assinarArquivoEnvelopingXml(arquivoAssinar, varPoliticaId, 
    																							   varPoliticaUri,privateKey);
    			// Transforma o arquivo em base 64 para jogar no form.
    			ByteArrayOutputStream bos = new ByteArrayOutputStream();
    			XMLUtils.outputDOMc14nWithComments(docEnvelopingXmlAssinado, bos);
				String texto = Base64Utils.base64Encode(bos.toByteArray());
	    		window.eval(script.replace("${value}",texto));
    		}		
		} 
		catch (Exception e) {
			TabeliaoAppletUtil.showMensagem(applet, "Ocorreu um erro ao assinar o arquivo.\nMotivo: "
											+ e.getCause().getMessage(),2);
			return false;
		}
		//Manda um submit para o form
        form.call("submit", new Object[0]);
        return true;
	}	
}