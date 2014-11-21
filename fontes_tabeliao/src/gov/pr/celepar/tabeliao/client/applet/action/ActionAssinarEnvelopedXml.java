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
import gov.pr.celepar.tabeliao.core.GerarEnvelopedXML;
import gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain;
import gov.pr.celepar.tabeliao.util.XmlSigUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.swing.JApplet;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import netscape.javascript.JSException;
import org.w3c.dom.Document;

/**
 * Classe de acao para efetuar Assinatura XML no formato Enveloped, em ambiente local do usuario
 * 
 * @version 1.0
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */
public class ActionAssinarEnvelopedXml implements ActionHandler {
	
	private JApplet applet;
	private String[] arquivos;
	private String tagAssinar;
	private String politicaId;
	private String politicaUri;
	private boolean contraAssinatura;
	
	/**
	 * Construtor da classe
	 * @param applet
	 * @param arquivo -> Arquivo a ser assinado.
	 * @param tagAssinar -> Nome da TAG a ser assinada, e preciso ID
	 * @param politicaId -> Identificador da politica de assinatura
	 * @param politicaUri -> Endereco web onde esta armazenada a politica
	 * @param contraAssinatura -> Indica se eh uma contra-Assinatura 
	 */
	public ActionAssinarEnvelopedXml(JApplet applet, String arquivo, String tagAssinar, String politicaId, 
									 String politicaUri, boolean contraAssinatura) {
		this(applet, new String[] { arquivo }, tagAssinar, politicaId, politicaUri, contraAssinatura);
	}

	/**
	 * 
	 * @param applet
	 * @param arquivos -> Lista de Arquivos a serem assinados.
	 * @param tagAssinar -> Nome da TAG a ser assinada, e preciso ID e eh o mesmo para todos os arquivos
	 * @param politicaId -> Identificador da politica de assinatura
	 * @param politicaUri -> Endereco web onde esta armazenada a politica
	 * @param contraAssinatura -> Indica se eh uma contra-Assinatura
	 */
	public ActionAssinarEnvelopedXml(JApplet applet, String[] arquivos, String tagAssinar, String politicaId, 
									 String politicaUri, boolean contraAssinatura) {
		this.applet = applet;
		this.arquivos = arquivos;
		this.tagAssinar = tagAssinar;
		this.politicaId = politicaId;
		this.politicaUri = politicaUri;
		this.contraAssinatura = contraAssinatura;
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
			String mensagem = "Ocorreu um erro ao carregar a chave privada do certificado.\nMotivo: " + e1.getCause().getMessage();
			TabeliaoAppletUtil.showMensagem(applet, mensagem,2);
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
    			Document docEnvelopedXmlAssinado = GerarEnvelopedXML.assinarArquivoEnvelopedXml(documentoCarregado, tagAssinar, 
    																					  politicaId, politicaUri, 
    					                                                                  contraAssinatura, privateKeyAndCertChain);
                if (docEnvelopedXmlAssinado != null) {
                	// Gravando o Documento XML assinado 
                	int tamNomeArquivo = nomeArquivo.length() - 4 ;
                	nomeArquivo = nomeArquivo.substring(0, tamNomeArquivo);
                	OutputStream os = new FileOutputStream(nomeArquivo+"_Enveloped_assi.xml");
            		TransformerFactory tf = TransformerFactory.newInstance();
            		Transformer trans = tf.newTransformer();
            		trans.transform(new DOMSource(docEnvelopedXmlAssinado), new StreamResult(os));
                	
                	if(arquivosAssinados.length() > 0) {
                		arquivosAssinados = arquivosAssinados + "Tag" +tagAssinar + "\n";
                	}
                	qtdeArquivos++;
                	arquivosAssinados += nomeArquivo;
                } else {
                	
                	if(arquivosAssinados.length() > 0) {
                		if(qtdeArquivos == 1) {
                			TabeliaoAppletUtil.showMensagem(applet, "Arquivo XML assinado com sucesso: \n" + arquivosAssinados + "",0);
                		} else {
                			TabeliaoAppletUtil.showMensagem(applet, qtdeArquivos + " arquivos XML assinados com sucesso:\n" + arquivosAssinados + "",0);                			
                		}
                	}                	
                	throw new DocumentSignException("Ocorreu um erro ao assinar o arquivo '" + nomeArquivo + "'.");
                }
    		}
        	if(arquivosAssinados.length() > 0) {
        		if(qtdeArquivos == 1) {
        			TabeliaoAppletUtil.showMensagem(applet, "Arquivo XML assinado com sucesso: \n" + arquivosAssinados + "",0);
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