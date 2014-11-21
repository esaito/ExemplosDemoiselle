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
import gov.pr.celepar.tabeliao.util.Base64Utils;
import gov.pr.celepar.tabeliao.util.CertificationChainAndSignatureBase64;
import gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain;
import gov.pr.celepar.tabeliao.util.Util;
import javax.swing.JApplet;
import netscape.javascript.JSObject;

/**
 * Classe de acao para efetuar Assinatura PKCS#7/CMS , em ambiente Servidor de Aplicacao
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */

public class ActionAssinaturaWeb implements ActionHandler {
	
	private JApplet applet;
	
	/**
	 * 
	 * @param applet
	 */
	public ActionAssinaturaWeb(JApplet applet) {
		this.applet = applet;
	}

	/**
	 * 
	 * @param arquivo -> arquivo a ser assinado
	 * @param pin 
	 * @param isHardware -> true se o certificado estiver armazenado em hardware (smartcard/token)
	 */	
	public boolean execute(String arquivo, String pin, boolean isHardware){
		String[] hashs = Util.stringToArray(applet.getParameter("hash"));
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
		
		//Aqui o documento ja esta assinado! Coloca ele no form
		JSObject window   = JSObject.getWindow(applet);
		JSObject document = (JSObject)window.getMember("document");
		Object obj = document.getMember(nomeForm);

		if(!(obj instanceof JSObject)) {
			TabeliaoAppletUtil.showMensagem(applet, "Não foi possível localizar o formulário '" 
										+ nomeForm + "'.\nVerifique se a taglib assinaturaWeb não foi colocada dentro de uma tag <form>.",2);
			return false;
		}
		
        JSObject form = (JSObject)obj;
		
        String script = "var elementAssinatura = document.createElement('input');\n" +
        		"elementAssinatura.type='hidden';\n" +
        		"elementAssinatura.name='campoAssinaturas';\n" +
        		"elementAssinatura.value='${value}';\n" +
        		"document." + nomeForm + ".appendChild(elementAssinatura);";
		try {
			for(int i=0 ; i<hashs.length ; i++) {
				CertificationChainAndSignatureBase64 signature;
				signature = TabeliaoAppletUtil.signHashDocument(Base64Utils.base64Decode(hashs[i]), privateKey);

				String valorAssinatura = Base64Utils.base64Encode(signature.mSignature);
				
				window.eval(script.replace("${value}", valorAssinatura));
			}
		} catch (DocumentSignException e) {
			TabeliaoAppletUtil.showMensagem(applet, "Ocorreu um erro ao assinar o arquivo.\nMotivo: " 
										+ e.getCause().getMessage(),2);
			return false;
		}
		
        //Manda um submit para o form
        form.call("submit", new Object[0]);
        return true;
	}	
}