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
import gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain;
import gov.pr.celepar.tabeliao.util.XmlSigUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JApplet;
import netscape.javascript.JSException;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.Document;

/**
 * Classe de acao para efetuar Assinatura XML no formato Enveloping, em ambiente local do usuario
 * 
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */
public class ActionAssinarEnvelopingXml implements ActionHandler {
	
	private JApplet applet;
	private String[] arquivos;
	private String politicaId;
	private String politicaUri;
	
	/**
	 * Construtor da classe
	 * @param applet
	 * @param arquivo -> Arquivo a ser assinado.
	 * @param politicaId -> Identificador da politica de assinatura
	 * @param politicaUri -> Endereco web onde esta armazenada a politica
	 */
	public ActionAssinarEnvelopingXml(JApplet applet, String arquivo, String politicaId, 
									 String politicaUri) {
		this(applet, new String[] { arquivo }, politicaId, politicaUri);
	}

	/**
	 * 
	 * @param applet
	 * @param arquivos -> Lista de Arquivos a serem assinados.
	 * @param politicaId -> Identificador da politica de assinatura
	 * @param politicaUri -> Endereco web onde esta armazenada a politica
	 */
	public ActionAssinarEnvelopingXml(JApplet applet, String[] arquivos, String politicaId, 
									 String politicaUri) {
		this.applet = applet;
		this.arquivos = arquivos;
		this.politicaId = politicaId;
		this.politicaUri = politicaUri;
	}
	
	/**
	 * 
	 * @param arquivo -> arquivo a ser assinado
	 * @param pin 
	 * @param isHardware -> true se o certificado estiver armazenado em hardware (smartcard/token)
	 * 
	 */
	public boolean execute(String arquivo, String pin, boolean isHardware){
    	//Tenta pegar o keyStore que possui o certificado do camarada
		PrivateKeyAndCertChain privateKeyAndCertChain;
		try {
			privateKeyAndCertChain = TabeliaoAppletUtil.loadPrivateKeyAndCertChain(arquivo, pin, isHardware);
		} catch (DocumentSignException e1) {
			TabeliaoAppletUtil.showMensagem(applet, "Ocorreu um erro ao carregar a chave privada do certificado.\nMotivo: " + e1.getCause().getMessage(),2);
			return false;
		}	
    	try {
            // Efetua a assinatura
    		int qtdeArquivos = 0;
    		String arquivosAssinados = "";
    		for(int i=0 ; i<arquivos.length ; i++) {
    			String arquivoCarregar = arquivos[i];  
    			String nomeArquivo = arquivoCarregar;
    			Document documentoCarregado = XmlSigUtil.carregarArquivoXML(new FileInputStream(arquivoCarregar));
    			Document docEnvelopedXmlAssinado = GerarEnvelopingXML.assinarArquivoEnvelopingXml(
    												documentoCarregado, politicaId,  politicaUri, 
    												privateKeyAndCertChain);
                if (docEnvelopedXmlAssinado != null) {
          	
                	// Gravando o Documento XML assinado 
                	// retira o .xml do nome do arquivo.
                	int tamNomeArquivo = nomeArquivo.length() - 4 ;
                	nomeArquivo = nomeArquivo.substring(0, tamNomeArquivo);
  	
                	XMLUtils.outputDOMc14nWithComments(docEnvelopedXmlAssinado, 
                				new FileOutputStream(nomeArquivo+"_Enveloping_assi.xml"));
                	
                	if(arquivosAssinados.length() > 0) {
                		arquivosAssinados = arquivosAssinados + "\n";
                	}
                	qtdeArquivos++;
                	arquivosAssinados += nomeArquivo;
                } else {
                	
                	if(arquivosAssinados.length() > 0) {
                		if(qtdeArquivos == 1) {
                			TabeliaoAppletUtil.showMensagem(applet, "Arquivo Enveloping XML assinado com sucesso: \n" + arquivosAssinados + "",0);
                		} else {
                			TabeliaoAppletUtil.showMensagem(applet, qtdeArquivos + " arquivos XML assinados com sucesso:\n" + arquivosAssinados + "",0);
                		}
                	}
                	
                	throw new DocumentSignException("Ocorreu um erro ao assinar o arquivo '" + nomeArquivo + "'.");
                }
    		}
        	if(arquivosAssinados.length() > 0) {
        		if(qtdeArquivos == 1) {
        			TabeliaoAppletUtil.showMensagem(applet, "Arquivo Enveloping XML assinado com sucesso: \n" + arquivosAssinados + "",0);
        		} else {
        			TabeliaoAppletUtil.showMensagem(applet, qtdeArquivos + " arquivos XML assinados com sucesso:\n" + arquivosAssinados + "",0);
        		}
        	}
        }
        catch (DocumentSignException dse) {
            // Document signing failed. Display error message
            String errorMessage = dse.getMessage();
			TabeliaoAppletUtil.showMensagem(applet, errorMessage,2);
			return false;
        }
        catch (SecurityException se) {
            se.printStackTrace();
			TabeliaoAppletUtil.showMensagem(applet, 
                "Erro ao acessar a unidade de disco local.\n" +
                "Esta applet deve ser inicializado com as permissões de seguranca total.\n" +
                "Por favor, aceite o certificado quando o Plug-In do Java perguntar.",2);
			return false;
        }
        catch (JSException jse) {
            jse.printStackTrace();
            TabeliaoAppletUtil.showMensagem(applet,
                "Não foi possível acessar alguns campos do formulário HTML.\n" +
                "Cheque os parâmetros.",2);
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            TabeliaoAppletUtil.showMensagem(applet, "Erro inesperado: " + e.getMessage(),2);
            return false;
        }
        return true;
	}	
}