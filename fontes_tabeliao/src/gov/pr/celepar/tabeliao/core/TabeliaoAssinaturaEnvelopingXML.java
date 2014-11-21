package gov.pr.celepar.tabeliao.core;
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
import gov.pr.celepar.tabeliao.util.XmlSigUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.parsers.ParserConfigurationException;

import org.jcp.xml.dsig.internal.dom.DOMXMLObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * Classe para tratamento e validacao de assinatura em formato EnvelopedXML
 * 
 * @version 1.0
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */

public class TabeliaoAssinaturaEnvelopingXML extends TabeliaoAssinaturaXML {

	/**
	 * Instancia a classe a partir de um array de bytes
	 * @param arquivoAs -> Arquivo a ser assinado byte[]
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 */
	public TabeliaoAssinaturaEnvelopingXML(byte[] arquivoAs) throws Exception {
		try {
			setXmlAssinado(XmlSigUtil.carregarArquivoXML(new ByteArrayInputStream(arquivoAs)));
		} catch (ParserConfigurationException e) {
			String errorMessage = "Erro de ParserConfiguration ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		} catch (SAXException e) {
			String errorMessage = "Erro de SAX ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		} catch (IOException e) {
			String errorMessage = "Erro de IO ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		}
	}

	/**
	 * Instancia a classe a partir de um java.io.InputStream
	 * @param arquivoAs -> Arquivo a ser assinado java.io.InputStream
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 */
		
	public TabeliaoAssinaturaEnvelopingXML(InputStream arquivoAs) throws Exception {
		try {
			setXmlAssinado(XmlSigUtil.carregarArquivoXML(arquivoAs));
		} catch (ParserConfigurationException e) {
			String errorMessage = "Erro de ParserConfiguration ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		} catch (SAXException e) {
			String errorMessage = "Erro de SAX ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		} catch (IOException e) {
			String errorMessage = "Erro de IO ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		}
	}
	
	/**
	 * Instancia a classe a partir de um documento XML Assinado
	 * @param arquivoAs
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 * @throws IOException 
	 */
	public TabeliaoAssinaturaEnvelopingXML(Document arquivoAs) throws Exception {
		setXmlAssinado(arquivoAs);
	}
	
	/**
	 * Instancia a classe a partir de uma String contendo o XML Assinado
	 * @param arquivoAs -> java.lang.String
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 * @throws IOException 
	 */
	public TabeliaoAssinaturaEnvelopingXML(String arquivoAs) throws Exception {
		try {
			setXmlAssinado(XmlSigUtil.carregarArquivoXML(arquivoAs));
		} catch (ParserConfigurationException e) {
			String errorMessage = "Erro de ParserConfiguration ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		} catch (SAXException e) {
			String errorMessage = "Erro de SAX ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		}
	}
	
	/**
	 * Instancia a classe a partir de um org.xml.sax.InputSource contendo o XML Assinado
	 * @param arquivoAs -> org.xml.sax.InputSource
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 * @throws IOException 
	 */
	public TabeliaoAssinaturaEnvelopingXML(InputSource arquivoAs) throws Exception {
		try {
			setXmlAssinado(XmlSigUtil.carregarArquivoXML(arquivoAs));
		} catch (ParserConfigurationException e) {
			String errorMessage = "Erro de ParserConfiguration ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		} catch (SAXException e) {
			String errorMessage = "Erro de SAX ao carregar o arquivo assinado\n" +
	        "Detalhes: " + e.getMessage();
	        throw new Exception(errorMessage, e);
		}
	}
	
	/**
	 * Retorna a data de Assinatura contida em SigningTime 
	 * Nao eh carimbo de tempo.
	 * @param ix indica de qual a assinatura.
	 * @return Date com data da assinatura
	 * @throws ParseException 
	 * @throws DOMException 
	 */
	
	public Date getDataAssinatura(int ix) {
		
		Date dataAssinatura = null;
		
		if (this.getAssinaturas().isEmpty()){
			return dataAssinatura;
		}
		
		if (!this.getAssinaturas().get(ix).getObjects().isEmpty()){
        	DOMXMLObject objDX = null;
        	
        	Iterator<?> it = this.getAssinaturas().get(ix).getObjects().iterator();
	        while (it.hasNext()) {
	        	Object ob1 = it.next();
		       	if (ob1 instanceof DOMXMLObject){
		       		objDX = (DOMXMLObject) ob1;		       		
	        		for (int i=0; i < objDX.getContent().size();i++){
	        			DOMStructure Dos = (DOMStructure) objDX.getContent().get(i);	
	        			if (Dos.getNode().getLocalName() ==	"QualifyingProperties"){        				
	        				Element el = (Element) Dos.getNode();
	        				NodeList nodeQualifying = el.getElementsByTagName("SigningTime");
	        			// Identifica o tamanho do elemento node, em co-assinaturas a ultima contém as anteriores recursivamente
	        			// assim o elemento da assinatura ix é sempre o último.
	        				int to = nodeQualifying.getLength();
	        				if (to < 1 )
	        				{
	        					return null;	
	        				}
	        				Locale locale = new Locale("pt","BR");
	        				SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",locale);
	        				try {
	        					dataAssinatura = (Date)formatador.parse(nodeQualifying.item(to-1).getTextContent());
	        				} catch (DOMException e) {
	        					System.err.println("DOMException"+e.getMessage());
	        					return null;
	        				} catch (ParseException e) {
	        					System.err.println("ParseException"+e.getMessage());
	        					return null;
	        				}
	        				break;
	        			}
	        		}
	        	}
		    }	
		}	
		
		return dataAssinatura;
	}

	/**
	 * Retorna o XML que contem SigningCertificate, que eh uma das propriedades assinadas obrigatorias da ICP-BRASIL. 
	 * contem cert = conforme DOC-ICP-04; CertDigest: /DigestMethod e /DigestValue; IssuerSerial: /X509IssuerName e /X509SerialNumber   
	 * @param ix indica de qual a assinatura.
	 * @return org.w3c.NodeList contendo SigningCertificate ou nulo
	 * @throws Exception
	 */
	
	public NodeList getSigningCertificate(int ix) throws Exception{
		
		
		NodeList nodeSigningCertificate = null;
		if (this.getAssinaturas().isEmpty()){
			return null;
		}
		if (!this.getAssinaturas().get(ix).getObjects().isEmpty()){
        	DOMXMLObject objDX = null;
        	Iterator<?> it = this.getAssinaturas().get(ix).getObjects().iterator();
	        while (it.hasNext()) {
	        	Object ob1 = it.next();
		       	if (ob1 instanceof DOMXMLObject){
	        		objDX = (DOMXMLObject) ob1;
	        		for (int i=0; i < objDX.getContent().size();i++){
	        			DOMStructure Dos = (DOMStructure) objDX.getContent().get(i);	
	        			if (Dos.getNode().getLocalName() ==	"QualifyingProperties"){        				
	        				Element el = (Element) Dos.getNode();
	        				nodeSigningCertificate = el.getElementsByTagName("SigningCertificate");
	        				break;
	        			}
	        		}
	        		// se a lista estiver vazia retornará nulo
	        		if (nodeSigningCertificate.getLength() < 1){
	        			nodeSigningCertificate = null;
	        		}	        		
	        	}
	        }
		}
		return nodeSigningCertificate;
	}
	
	/**
	 * Retorna SignaturePolicyIdentifier, que identifica qual eh a politica de assinatura.  
	 * Eh uma das propriedades assinadas obrigatorias da ICP-BRASIL. Atualmente contem dos atributos:
	 * SigPolicyId e SPURI
	 *  
	 * @param ix indica de qual a assinatura.
	 * @return NodeList da politica de assinatura ou nulo.
	 * @throws DOMException
	 */
	
	public NodeList getSignaturePolicyIdentifier(int ix) throws DOMException {
		
		NodeList signaturePolicyIdentifier = null;
		
		if (this.getAssinaturas().isEmpty()){
			return signaturePolicyIdentifier;
		}
		if (!this.getAssinaturas().get(ix).getObjects().isEmpty()){
        	DOMXMLObject objDX = null;
        	Iterator<?> it = this.getAssinaturas().get(ix).getObjects().iterator();
	        while (it.hasNext()) {
	        	Object ob1 = it.next();
		       	if (ob1 instanceof DOMXMLObject){
	        		objDX = (DOMXMLObject) ob1;
	        		for (int i=0; i < objDX.getContent().size();i++){
	        			DOMStructure Dos = (DOMStructure) objDX.getContent().get(i);	
	        			if (Dos.getNode().getLocalName() ==	"QualifyingProperties"){        				
	        				Element el = (Element) Dos.getNode();
	        				signaturePolicyIdentifier = el.getElementsByTagName("SignaturePolicyIdentifier");
	        				// se a lista estiver vazia retornará nulo
	        				break;
	        			}
	        		}
	        		if (signaturePolicyIdentifier.getLength() < 1){
	        			signaturePolicyIdentifier = null;
	        		}
	        	}
	        }
	    }
				
		return signaturePolicyIdentifier;
	}
	
	/**
	 * Retorna SigPolicyId, que e uma das propriedades de SignaturePolicyIdentifier
	 * @param ix indica de qual a assinatura.
	 * @return String de SigPolicyId 
	 * @throws DOMException
	 */
	
	public String getSigPolicyId(int ix) throws DOMException {
		
		String sigPolicyId = null;
		
		if (this.getAssinaturas().isEmpty()){
			return sigPolicyId;
		}
		if (!this.getAssinaturas().get(ix).getObjects().isEmpty()){
        	DOMXMLObject objDX = null;
        	Iterator<?> it = this.getAssinaturas().get(ix).getObjects().iterator();
	        while (it.hasNext()) {
	        	Object ob1 = it.next();
		       	if (ob1 instanceof DOMXMLObject){
	        		objDX = (DOMXMLObject) ob1;
	        		for (int i=0; i < objDX.getContent().size();i++){
	        			DOMStructure Dos = (DOMStructure) objDX.getContent().get(i);	
	        			if (Dos.getNode().getLocalName() ==	"QualifyingProperties"){        				
	        				Element el = (Element) Dos.getNode();
	        				NodeList nodeSigId = el.getElementsByTagName("SigPolicyId");
	        				if (nodeSigId.getLength() > 0){
			        			sigPolicyId = nodeSigId.item(0).getTextContent();
			        		}
	        				break;
	        			}	        			
	        		}	        		
	        	}
	        }
	    }			
		return sigPolicyId;
	}
	
	/**
	 * Retorna SPURI, que eh uma das propriedades de SignaturePolicyIdentifier
	 * @param ix indica de qual a assinatura.
	 * @return String de SPURI 
	 * @throws DOMException
	 */
	
	public String getSPURI(int ix) throws DOMException {
		
		String SPURI = null;
		
		if (this.getAssinaturas().isEmpty()){
			return SPURI;
		}
		if (!this.getAssinaturas().get(ix).getObjects().isEmpty()){
        	DOMXMLObject objDX = null;
        	Iterator<?> it = this.getAssinaturas().get(ix).getObjects().iterator();
	        while (it.hasNext()) {
	        	Object ob1 = it.next();
		       	if (ob1 instanceof DOMXMLObject){
	        		objDX = (DOMXMLObject) ob1;
	        		for (int i=0; i < objDX.getContent().size();i++){
	        			DOMStructure Dos = (DOMStructure) objDX.getContent().get(i);	
	        			if (Dos.getNode().getLocalName() ==	"QualifyingProperties"){
	        				Element el = (Element) Dos.getNode();
	        				NodeList nodeSPURI = el.getElementsByTagName("SPURI");
	        				if (nodeSPURI.getLength() > 0){
	        					SPURI = nodeSPURI.item(0).getTextContent();
	        				}
	        				break;
	        			}	        			
	        		}
	        	}
	        }
		}		
		return SPURI;
	}
	
	/**
	 * Retorna DataObjectFormat do objeto Assinado  
	 * Eh uma das propriedades assinadas obrigatorias da ICP-BRASIL. Nao faz muito sentido para ENVELOPEDXML.
	 * @param ix indica de qual a assinatura.
	 * @return String com formato (MimeTye) do conteudo assinado.
	 * @throws Exception
	 */
	public String getDataObjectFormat(int ix) throws DOMException {
		
		String dataObjectFormat = null;
		
		
		if (this.getAssinaturas().isEmpty()){
			return dataObjectFormat;
		}
		if (!this.getAssinaturas().get(ix).getObjects().isEmpty()){
        	DOMXMLObject objDX = null;
        	Iterator<?> it = this.getAssinaturas().get(ix).getObjects().iterator();
	        while (it.hasNext()) {
	        	Object ob1 = it.next();
		       	if (ob1 instanceof DOMXMLObject){
	        		objDX = (DOMXMLObject) ob1;
	        		for (int i=0; i < objDX.getContent().size();i++){
	        			DOMStructure Dos = (DOMStructure) objDX.getContent().get(i);	
	        			if (Dos.getNode().getLocalName() ==	"QualifyingProperties"){
	        				Element el = (Element) Dos.getNode();
	        				NodeList nodeMimeType = el.getElementsByTagName("MimeType");
	        				if (nodeMimeType.getLength() > 0 ){
	        					dataObjectFormat = nodeMimeType.item(0).getTextContent();
	        				}
	        				break;
	        			}
	        		}
	        	}
	        }
		}
		return dataObjectFormat;		
	}
}