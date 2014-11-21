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
import javax.swing.JApplet;
import netscape.javascript.JSObject;
/**
 * Classe de acao para efetuar Autenticacao com certificados em arquivo ou hardware
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
public class ActionAutenticacao implements ActionHandler {
	
	private JApplet applet;

	/**
	 * 
	 * @param applet
	 */
	public ActionAutenticacao(JApplet applet) {
		this.applet = applet;
	}

	/**
	 * @param arquivo -> informado se o certificado estiver armazenado em formato PKCS#12
	 * @param pin 
	 * @param isHardware -> true se o certificado estiver armazenado em hardware (smartcard/token)
	 */
	public boolean execute(String arquivo, String pin, boolean isHardware){
    	//Tenta pegar o keyStore que possui o certificado do usuario
		PrivateKeyAndCertChain privateKey;
		try {
			privateKey = TabeliaoAppletUtil.loadPrivateKeyAndCertChain(arquivo, pin, isHardware);
		} catch (DocumentSignException e) {
	        String mensagem = "Ocorreu um erro ao carregar a chave privada do certificado.\n" +
				"Motivo: " + e.getMessage();
			TabeliaoAppletUtil.showMensagem(applet, mensagem,2);
			return false;
		}
		
		//Aqui ja esta com o keyStore, pode assinar o UUID
		//E mandar para o tabelião autenticar o usuario.
		JSObject window   = JSObject.getWindow(applet);
		JSObject document = (JSObject)window.getMember("document");
        JSObject form     = (JSObject)document.getMember("tabeliaoForm");
        JSObject field    = null;
        field = (JSObject) form.getMember("TABELIAO_SESSION_UUID");
        String uuid = (String)field.getMember("value");

        //Assina o uuid e codifica em Base64 para mandar para o tabeliao.
        CertificationChainAndSignatureBase64 ccs64;
        try {
			ccs64 = TabeliaoAppletUtil.signDocument(uuid.getBytes(), privateKey, true);
		} catch (DocumentSignException e) {
			TabeliaoAppletUtil.showMensagem(applet, "Ocorreu um erro na autenticação do usuário.\nMotivo: " 
								+ e.getMessage(),2);
			return false;
		}
        String assinatura = Base64Utils.base64Encode(ccs64.mSignature);
        
        field = (JSObject) form.getMember("CHAVE");
        field.setMember("value", assinatura);
        
        privateKey = null;        
        //Manda um submit para a tela de autenticação
        window.eval("doLogin()");
              
       	return true;	   
	}	
}