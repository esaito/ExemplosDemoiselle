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

public class TabeliaoAssinaturaEnvelopedXML extends TabeliaoAssinaturaXML {

	/**
	 * Instancia a classe a partir de um array de bytes
	 * @param arquivoAs -> Arquivo a ser assinado
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 */
	public TabeliaoAssinaturaEnvelopedXML(byte[] arquivoAs) throws Exception {
		try {
			this.setXmlAssinado(XmlSigUtil.carregarArquivoXML(new ByteArrayInputStream(arquivoAs)));
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
	 * Instancia a classe a partir de um InputStream 
	 * @param arquivoAs -> Arquivo a ser assinado
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 */
		
	public TabeliaoAssinaturaEnvelopedXML(InputStream arquivoAs) throws Exception{
		try {
			this.setXmlAssinado(XmlSigUtil.carregarArquivoXML(arquivoAs));
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
	 * Instancia a classe a partir de um objeto Document contendo o XML Assinado
	 * @param arquivoAs
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 * @throws IOException 
	 */
	public TabeliaoAssinaturaEnvelopedXML(Document arquivoAs) throws Exception, IOException {
		this.setXmlAssinado(arquivoAs);
	}
	
	/**
	 * Instancia a classe a partir de uma String contendo o XML Assinado
	 * @param arquivoAs
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 * @throws IOException 
	 */
	public TabeliaoAssinaturaEnvelopedXML(String arquivoAs) throws Exception, IOException {
		try {
			this.setXmlAssinado(XmlSigUtil.carregarArquivoXML(arquivoAs));
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
	 * @param arquivoAs
	 * @throws XMLSignatureException
	 * @throws XMLSecurityException
	 * @throws IOException 
	 */
	public TabeliaoAssinaturaEnvelopedXML(InputSource arquivoAs) throws Exception, IOException {
		try {
			this.setXmlAssinado(XmlSigUtil.carregarArquivoXML(arquivoAs));
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
	 * Retorna o nome da TAG que foi assinada, na assinatura de indice ix, 
	 * A identificacao da TAG e feita pelo ID referente a URI (getUriTagAssinada(ix))
	 * este ID deve estar registrado (IdResolver.registerElementById)
	 * se vazio ou null significa que o arquivo todo foi assinado.
	 * 
	 * 
	 * @param ix indica de qual a assinatura.
	 * @return String com o Nome ou a situacao da TAG.
	 * 
	 * @throws XMLSecurityException
	 */
	
	public String getNomeTagAssinada(int ix) throws Exception {
		
		String nomeTagAssinada = null;		
		
		// Verifica se ha assinaturas no arquivo.
	
		if (this.getAssinaturas().isEmpty()) {
			nomeTagAssinada = "sem assinatura";
			return nomeTagAssinada;
		}
		
		if (this.getAssinaturas().get(ix).getSignedInfo().getReferences().isEmpty()){
			nomeTagAssinada = "";
		}else{
    		String idTag = this.getUriTagAssinada(ix).replaceFirst("#", "").trim();
	   		Element Elem = getXmlAssinado().getElementById(idTag);
	   		if (Elem != null){
	   			nomeTagAssinada = Elem.getLocalName();
	   		}
	   	}		
		return nomeTagAssinada;		
	}
	
	
	/**
	 * Verifica se a assinatura informada eh uma contra assinatura.  
	 * @param ix
	 * @return true se eh uma contra assinatura. False se nao eh.
	 * @throws Exception
	 */
	
	public boolean isContraAssintura(int ix) throws Exception{
		
		if (this.getContraAssinatura(ix) != null){
			return true;
		}else{
			return false;
		}			
	}
	
	/**
	 *  Retorna um NodeList da propriedade CounterSignature, se houver.
	 *  esta lista contem o elemento Signature que foi contra assinado, este por sua vez
	 *  tambem pode possuir um elemento CounterSignature recursivamente igual.
	 *  
	 * @param ix -> indice da assinatura 
	 * @return NodeList -> CounterSignature ou nulo
	 * @throws Exception
	 */
	
	public NodeList getContraAssinatura(int ix) throws Exception{
		
		NodeList nodeContraAssiantura = null;
		
		if (this.getAssinaturas().isEmpty()){
			return nodeContraAssiantura;
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
		        				nodeContraAssiantura = el.getElementsByTagName("CounterSignature");
		        				break;
		        			}
		        		}
		        		// se a lista estiver vazia retornará nulo
		        		if (nodeContraAssiantura.getLength() < 1){
		        			nodeContraAssiantura = null;
		        		}
		        	}
		        }		    
	    }
		return nodeContraAssiantura;
	}
	/**
	 * Retorna a data de Assinatura contida em SigningTime 
	 * Nao eh carimbo de tempo.
	 * @param ix indica de qual a assinatura.
	 * @return Date com data da assinatura
	 * @throws ParseException 
	 * @throws DOMException 
	 */
	
	public Date getDataAssinatura(int ix) throws DOMException, ParseException {
		
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
	        				if (nodeQualifying.getLength() > 0){
		        				Locale locale = new Locale("pt","BR");
			        			SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",locale);
			        			dataAssinatura = (Date)formatador.parse(nodeQualifying.item(0).getTextContent());
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
	        				break;
	        			}
	        		}
	        		// se a lista estiver vazia retornará nulo
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