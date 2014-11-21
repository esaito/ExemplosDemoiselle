package gov.pr.celepar.tabeliao.client.applet;
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
import gov.pr.celepar.tabeliao.client.applet.action.ActionAssinarEnvelopedXml;
import gov.pr.celepar.tabeliao.client.applet.action.ActionAssinarEnvelopedXmlWeb;
import gov.pr.celepar.tabeliao.client.applet.action.ActionAssinarEnvelopingXml;
import gov.pr.celepar.tabeliao.client.applet.action.ActionAssinarEnvelopingXmlWeb;
import gov.pr.celepar.tabeliao.client.applet.action.ActionAssinatura;
import gov.pr.celepar.tabeliao.client.applet.action.ActionAssinaturaWeb;
import gov.pr.celepar.tabeliao.client.applet.action.ActionAutenticacao;
import gov.pr.celepar.tabeliao.client.applet.action.ActionHandler;
import java.util.StringTokenizer;
import javax.security.auth.login.LoginException;
import javax.swing.JApplet;

/**
 * Classe principal da Applet de assinatura do Tabeliao
 * 
 * @version 1.2
 * @author Thiago Meneghello - GIC/CELEPAR
 * @author Emerson Sachio Saito (alteracoes) - GIC/CELEPAR
 *
 */
public class TabeliaoApplet extends JApplet {

	private static final long serialVersionUID = 6020811179954029028L;
	
	private static final String ACAO_AUTENTICACAO    			= "autenticacao";
	private static final String ACAO_ASSINATURA     			= "assinatura";
	private static final String ACAO_ASSINATURA_WEB  			= "assinaturaWeb";
	private static final String ACAO_DECRIPTOGRAFIA  			= "decriptografia";
	private static final String ACAO_ASSINAR_ENVELOPEDXML 		= "assinarEnvelopedXml";
	private static final String ACAO_ASSINAR_ENVELOPEDXML_WEB 	= "assinarEnvelopedXmlWeb";
	private static final String ACAO_ASSINAR_ENVELOPINGXML 		= "assinarEnvelopingXml";
	private static final String ACAO_ASSINAR_ENVELOPINGXML_WEB 	= "assinarEnvelopingXmlWeb";
	
    /**
     * Inicializa a  applet - cria e inicializa sua interface grafica.
     * Atualmente a applet possui apenas um botao.
     * O valor do botao vem do parametro VALOR_BOTAO.
     */
    public void init() {
    	
    	String acao = this.getParameter("acao");
    	boolean semAcao = false;
    	
    	if(ACAO_AUTENTICACAO.equals(acao)) {
    		handleAutenticacao();    		    		
    	} else if(ACAO_ASSINATURA_WEB.equals(acao)) {
			handleAssinaturaWeb();    		
    	} else if (ACAO_ASSINATURA.equals(acao)) {
    		handleAssinatura();    		
    	} else if(ACAO_DECRIPTOGRAFIA.equals(acao)) {
    		handleDecriptografia();
    	} else if(ACAO_ASSINAR_ENVELOPEDXML.equals(acao)) {
    		handleAssinarEnvelopedXml();    		
    	} else if(ACAO_ASSINAR_ENVELOPEDXML_WEB.equals(acao)) {
    		handleAssinarEnvelopedXmlWeb();
    	} else if(ACAO_ASSINAR_ENVELOPINGXML.equals(acao)) {
    		handleAssinarEnvelopingXml();    		
    	} else if(ACAO_ASSINAR_ENVELOPINGXML_WEB.equals(acao)) {
    		handleAssinarEnvelopingXmlWeb();
    	} else {
    		semAcao = true;
            String errorMessage = "Parametro acao '" + acao + "' não pode ser consumido.";
            TabeliaoAppletUtil.showMensagem(this, errorMessage,2);            
    	}
    	if (!semAcao){
    		try {
    			TabeliaoAppletUtil.logoutSmart();
    		} catch (LoginException e1) {
    			String errorMessage = "Erro ao acessar o Cartão ou Token." +
								   "\n Verifique a Leitora/Cartão ou Token!" +
								   "\n Reinicie a Aplicação ou acione o suporte técnico!" + e1.getMessage();
    			TabeliaoAppletUtil.showMensagem(this, errorMessage,2);
    			e1.printStackTrace();
    		}
    		try {
    			this.stop();
    			this.finalize();
    			this.destroy();
    		} catch (Throwable e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    /**
     * Retorna o valor da cor
     * @param cor - Nome da cor.
     * @param def
     * @return int
     */
    private int getCor(String cor, int def){
    	if(cor != null && cor.startsWith("#") && cor.length() == 7) {
    		try {
    			return Integer.parseInt(cor.substring(1), 16);
			} catch (Exception e) {
			}
    	}
    	return def;
    }
    
    /**
     * Exibe a applet de acordo com a ActionHandler
     * @param actionHandler -> acao a ser executada
     */
    private void show(ActionHandler actionHandler){
    	String tipoForm          = this.getParameter("tipoForm");
    	String caminhoBiblioteca = this.getParameter("caminhoBiblioteca");
    	String valorBotao        = this.getParameter("valorBotao");
    	String corFundo          = this.getParameter("corFundo");
    	String corDentro         = this.getParameter("corDentro");
    	String corAbaSelecionada = this.getParameter("corAbaSelecionada");
    	String corAba            = this.getParameter("corAba");
    	
    	int iTipoForm = CertificadoPanel.AUTENTICACAO_TOKEN_ARQUIVO;
    	if(tipoForm != null && tipoForm.length() > 0) {
    		try {
    			iTipoForm = Integer.parseInt(tipoForm);
    			if(iTipoForm <= 0 || iTipoForm >= 4) {
    				iTipoForm = CertificadoPanel.AUTENTICACAO_TOKEN_ARQUIVO;
    			}
			} catch (Exception e) { }
    	}
    	
    	if(caminhoBiblioteca == null || caminhoBiblioteca.length() == 0 || caminhoBiblioteca.equals("null")) {
    		caminhoBiblioteca = CertificadoPanel.DEFAULT_LIB_NAME;
    	}
    	
    	if(valorBotao == null || valorBotao.length() == 0 || valorBotao.equals("null")) {
    		valorBotao = CertificadoPanel.DEFAULT_LABEL_BUTTON;
    	}
    	 
    	int valCorFundo          = getCor(corFundo,          0xFFFFFF);
    	int valCorDentro         = getCor(corDentro,         0xEEEEEE);
    	int valCorAbaSelecionada = getCor(corAbaSelecionada, 0xEEEEEE);
    	int valCorAba            = getCor(corAba,            0xFFFFFF);

    	CertificadoPanel certificadoPanel = new CertificadoPanel(actionHandler,
    			iTipoForm,
    			valorBotao,
    			caminhoBiblioteca,
    			valCorFundo,
    			valCorDentro,
    			valCorAbaSelecionada,
    			valCorAba);
    	
    	setContentPane(certificadoPanel);
    	certificadoPanel.init();
    }
    
    /**
     * Acao para exibir a applet para autenticacao
     * 
     */
    private void handleAutenticacao(){
    	ActionAutenticacao actionAutenticacao = new ActionAutenticacao(this);
    	show(actionAutenticacao);
    }
    

    /**
     * Acao para exibir a applet para gerar assinatura PCKS7/CMS em ambiente servidor de aplicacao.
     */
    private void handleAssinaturaWeb(){
    	ActionAssinaturaWeb actionAssinaturaWeb = new ActionAssinaturaWeb(this);
    	show(actionAssinaturaWeb);
    }

    /**
     * Acao para exibir a applet para gerar assinatura PKCS7/CMS em ambiente local do usuario
     */
    private void handleAssinatura(){
		try {
    		String nomeArquivo = this.getParameter("nomeArquivo");
    		String separadorArquivo = this.getParameter("separadorArquivo");

    		boolean anexa = true;
    		String anexaArquivo = this.getParameter("anexaArquivo");
    		if(anexaArquivo == null || anexaArquivo.equals("false")) {
    			anexa = false;
    		}
    		
    		if(separadorArquivo != null) {
    			StringTokenizer token = new StringTokenizer(nomeArquivo, separadorArquivo);
    			String[] nomeArquivos = new String[token.countTokens()];
    			int i=0;
    			
    			while(token.hasMoreTokens()) {
    				nomeArquivos[i++] = token.nextToken();
    			}

    			show(new ActionAssinatura(this, nomeArquivos, anexa));
    		} else {
    			show(new ActionAssinatura(this, nomeArquivo, anexa));
    		}
    		
		} catch (Exception e) {
            String errorMessage = "Erro ao realizar a assinatura PKCS7.\n" + e.getMessage();
            TabeliaoAppletUtil.showMensagem(this, errorMessage,2);
		}
    }
    
    /**
     * Acao para exibir a applet para gerar assinatura EnvelopedXML em ambiente local do usuario.
     */
    private void handleAssinarEnvelopedXml(){
		try {
    		String nomeArquivo = this.getParameter("nomeArquivo");
    		String separadorArquivo = this.getParameter("separadorArquivo");
    		String nomeTagAssinar = this.getParameter("nomeTagAssinar");
    		String varPoliticaId = this.getParameter("politicaId");
    		String varPoliticaUri = this.getParameter("politicaUri");
    		String varContraAssinatura = this.getParameter("contraAssinatura");
    		boolean pContraAssinatura = false;

    		if(varContraAssinatura.equalsIgnoreCase("true") || varContraAssinatura.equalsIgnoreCase("on")) {
    			pContraAssinatura = true;
    		}
    		
    		if(separadorArquivo != null) {
    			StringTokenizer token = new StringTokenizer(nomeArquivo, separadorArquivo);
    			String[] nomeArquivos = new String[token.countTokens()];
    			int i=0;
    			
    			while(token.hasMoreTokens()) {
    				nomeArquivos[i++] = token.nextToken();
    			}    			

    			show(new ActionAssinarEnvelopedXml(this, nomeArquivos, nomeTagAssinar, 
    											   varPoliticaId, varPoliticaUri, pContraAssinatura));
    		} else {
    			show(new ActionAssinarEnvelopedXml(this, nomeArquivo, nomeTagAssinar, 
    											   varPoliticaId, varPoliticaUri, pContraAssinatura));
    		}
    		
		} catch (Exception e) {
            String errorMessage = "Erro ao realizar a assinatura EnvelopedXML.\n" + e.getMessage();
            TabeliaoAppletUtil.showMensagem(this, errorMessage,2);
		}
    }
    
    /**
     * Acao para exibir a applet para gerar assinatura EnvelopedXML em ambiente Servidor de aplicacao
     */
    private void handleAssinarEnvelopedXmlWeb(){
    	ActionAssinarEnvelopedXmlWeb actionAssinarEnvelopedXmlWeb = new ActionAssinarEnvelopedXmlWeb(this);
    	show(actionAssinarEnvelopedXmlWeb);
    }
    
    /**
     * Acao para exibir a applet para gerar assinatura EnvelopingXML em ambiente local do usuario.
     */
    private void handleAssinarEnvelopingXml(){
		try {
    		String nomeArquivo = this.getParameter("nomeArquivo");
    		String separadorArquivo = this.getParameter("separadorArquivo");
    		String varPoliticaId = this.getParameter("politicaId");
    		String varPoliticaUri = this.getParameter("politicaUri");
    		
    		if(separadorArquivo != null) {
    			StringTokenizer token = new StringTokenizer(nomeArquivo, separadorArquivo);
    			String[] nomeArquivos = new String[token.countTokens()];
    			int i=0;
    			
    			while(token.hasMoreTokens()) {
    				nomeArquivos[i++] = token.nextToken();
    			}    			

    			show(new ActionAssinarEnvelopingXml(this, nomeArquivos, varPoliticaId, varPoliticaUri));
    		} else {
    			show(new ActionAssinarEnvelopingXml(this, nomeArquivo, varPoliticaId, varPoliticaUri));
    		}
    		
		} catch (Exception e) {
            String errorMessage = "Erro ao realizar a assinatura EnvelopingXML.\n" + e.getMessage();
            TabeliaoAppletUtil.showMensagem(this, errorMessage,2);
		}
    }
    
    /**
     * Acao para exibir a applet para gerar assinatura EnvelopingXML em ambiente Servidor de aplicacao
     */
    private void handleAssinarEnvelopingXmlWeb(){
    	ActionAssinarEnvelopingXmlWeb actionAssinarEnvelopingXmlWeb = new ActionAssinarEnvelopingXmlWeb(this);
    	show(actionAssinarEnvelopingXmlWeb);
    }
    
    
    //TODO - Criptografia na v2.0
    /**
     * Acao para exibir a applet para Decriptografia.
     */
    private void handleDecriptografia(){
    	//ActionAssinaturaWeb actionAssinaturaWeb = new ActionAssinaturaWeb(this);
    	//show(actionAssinaturaWeb);
    }    
}