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
import gov.pr.celepar.tabeliao.util.Base64Utils;
import gov.pr.celepar.tabeliao.util.CertificationChainAndSignatureBase64;
import gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain;
import gov.pr.celepar.tabeliao.util.XmlSigUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
import com.sun.org.apache.xml.internal.security.signature.ObjectContainer;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;



/**
 * Classe para geracao do arquivo XML Assinado no formato Enveloping XML
 * Criando as propriedades XADES reconhecidas pela ICP-BRASIL
 * de acordo com os documentos DOC-ICP-15.05 e DOC-ICP-15.02
 * 
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */

public class GerarEnvelopingXML {
	
	private GerarEnvelopingXML(){
		
	}
	
	/**
     * Prepara o arquivo XML para assinatura e retorna o novo documento assinado
     *  
     * @param aFile -> InputStream com XMLAssinador
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 
     */
    public static Document assinarArquivoEnvelopingXml(InputStream aFile, String politicaId, String politicaUri, 
    		                                    PrivateKeyAndCertChain privateKeyAndCertChain) throws XMLSignatureException
	{

    	Document docAssinar;
		try {
			docAssinar = XmlSigUtil.carregarArquivoXML(aFile);
		} catch (ParserConfigurationException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		} catch (SAXException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
			throw new XMLSignatureException(errorMessage, e);
		} catch (IOException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		} 
		Document signingResult = assinandoDocumentoEnvelopingXml(docAssinar, politicaId, politicaUri, privateKeyAndCertChain);
		return signingResult;	    	
	}
    
    /**
     * Prepara o arquivo XML para assinatura e retorna o novo documento assinado
     *  
     * @param documento -> objeto do tipo org.w3c.dom.Document
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 
     */
    public static Document assinarArquivoEnvelopingXml(Document documento, String politicaId, String politicaUri, 
    		                                    PrivateKeyAndCertChain privateKeyAndCertChain) throws XMLSignatureException
	{

    	Document docAssinar = documento;
		Document signingResult = assinandoDocumentoEnvelopingXml(docAssinar, politicaId, politicaUri, privateKeyAndCertChain);
		return signingResult;	    	
	}
    
    /**
     * Prepara o arquivo XML para assinatura e retorna o novo documento assinado
     *  
     * @param conteudo -> Conteudo para assinatura do tipo byte[]
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 
     */
    public static Document assinarArquivoEnvelopingXml(byte[] conteudo, String politicaId, String politicaUri, 
    		                                    PrivateKeyAndCertChain privateKeyAndCertChain) throws XMLSignatureException
	{

    	Document docAssinar;
		try {
			docAssinar = XmlSigUtil.carregarArquivoXML(new ByteArrayInputStream(conteudo));
		} catch (ParserConfigurationException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		} catch (SAXException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		} catch (IOException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		}
		Document signingResult = assinandoDocumentoEnvelopingXml(docAssinar, politicaId, politicaUri, privateKeyAndCertChain);
		return signingResult;	    	
	}
    
    /**
     * Prepara o arquivo XML para assinatura e retorna o novo documento assinado
     *  
     * @param conteudoString -> Conteudo para assinatura do tipo String
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 
     */
    public static Document assinarArquivoEnvelopingXml(String conteudoString, String politicaId, String politicaUri, 
    		                                    PrivateKeyAndCertChain privateKeyAndCertChain) throws XMLSignatureException
	{

    	Document docAssinar;
		try {
			docAssinar = XmlSigUtil.carregarArquivoXML(conteudoString);
		} catch (ParserConfigurationException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		} catch (SAXException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		} catch (IOException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		}
		Document signingResult = assinandoDocumentoEnvelopingXml(docAssinar, politicaId, politicaUri, privateKeyAndCertChain);
		return signingResult;	    	
	}
    
    /**
     * Prepara o arquivo XML para assinatura e retorna o novo documento assinado
     *  
     * @param conteudoInputSource -> Conteudo para assinatura do tipo org.xml.sax.InputSource
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 
     */
    public static Document assinarArquivoEnvelopingXml(InputSource conteudoInputSource, String politicaId, String politicaUri, 
    		                                    PrivateKeyAndCertChain privateKeyAndCertChain) throws XMLSignatureException
	{

    	Document docAssinar;
		try {
			docAssinar = XmlSigUtil.carregarArquivoXML(conteudoInputSource);
		} catch (ParserConfigurationException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		} catch (SAXException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		} catch (IOException e) {
			String errorMessage = "Erro ao carregar o arquivo a ser assinado\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		}
		Document signingResult = assinandoDocumentoEnvelopingXml(docAssinar, politicaId, politicaUri, privateKeyAndCertChain);
		return signingResult;	    	
	}
    
    /**
     * Assina o arquivo XML e retorna o documento
     * 
     * @param aDocumentToSign -> Arquivo xml para assinatura
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura
     * @param privateKeyAndCertChain -> KeyStore
     * @return Document
     * @throws XMLSignatureException 
     */
    public static Document assinandoDocumentoEnvelopingXml(Document aDocumentToSign, String politicaId, String politicaUri,
    												PrivateKeyAndCertChain privateKeyAndCertChain) throws XMLSignatureException {
    	
    	Init.init();
    	// Checa se a chave privada está disponível 
	    PrivateKey privateKey = privateKeyAndCertChain.mPrivateKey;
	    if (privateKey == null) {
	        String errorMessage = "Não achou a chave privada do smart card.";
	        throw new XMLSignatureException(errorMessage);
	    }

	    // Checa se certificado X.509 está disponível
	    Certificate[] certChain = privateKeyAndCertChain.mCertificationChain;
	    if (certChain == null) {
	        String errorMessage = "Não achou o certificado público.";
	        throw new XMLSignatureException(errorMessage);
	    }	    
	    
	    // Criando o result object
	    CertificationChainAndSignatureBase64 signingResult =
	        new CertificationChainAndSignatureBase64();

	    // Salva a cadeia de certificados X.509 em Base64
	    try {
	        signingResult.mCertificationChain = Base64Utils.encodeX509CertChainToBase64(certChain);
	    } catch (CertificateException cee) {
	        String errorMessage = "Certificado inválido.";
	        throw new XMLSignatureException(errorMessage);
	    }
	    
        // Cria as constantes.
		String xadesNS="http://uri.etsi.org/01903/v1.3.2#";
		String varMimeType = "text/xml";
				
		// cria um novo documento para que estará envelopando os dados
		Document xmlDocument = null;
		try {
			xmlDocument = XmlSigUtil.criarDocumentoXMl();
		} catch (Exception e) {
			String errorMessage = "Erro ao criar o documento enveloping: "+e.getMessage();
	        throw new XMLSignatureException(errorMessage);
		}
		Node oldDocument = xmlDocument.importNode(aDocumentToSign.getFirstChild(), true);
		try {
			Constants.setSignatureSpecNSprefix("");
		} catch (XMLSecurityException e2) {
				String errorMessage = "Erro ao setar prefixo: "+e2.getMessage();
		        throw new XMLSignatureException(errorMessage);
		}
		// cria o container e a assinatura para o novo documento
		ObjectContainer obj = null;
		XMLSignature xmlSignature = null;
		try {
			    xmlSignature = new XMLSignature(xmlDocument, "", XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
				xmlDocument.appendChild(xmlSignature.getElement());
				obj = new ObjectContainer(xmlDocument);
				obj.appendChild((Element)oldDocument);
				xmlSignature.appendObject(obj);	
		} catch (XMLSecurityException se) {
			String errorMessage = "Erro ao criar os objetos: "+se.getMessage();
		    throw new XMLSignatureException(errorMessage);
		}
			
		// Verifica se há outras assinaturas, isso influenciará no Id do Objeto.
		String idObjeto = "";
		NodeList xmlSignatureElements =  aDocumentToSign.getElementsByTagName("Signature");
		if (xmlSignatureElements.getLength() > 0){
			// recupera a ultima assinatura que está envelopando o objeto
			Element sigElement = (Element) xmlSignatureElements.item(0); 
			XMLSignature assinatura;
			try {
				assinatura = new XMLSignature(sigElement, "");
			} catch (XMLSecurityException e) {
				String errorMessage = "Erro ao criar elemento assinatura: "+e.getMessage();
		        throw new XMLSignatureException(errorMessage);
			}
			// Verifica se existe o objeto envelopando
			if (assinatura.getObjectLength() > 0){
				idObjeto = assinatura.getObjectItem(0).getId()+xmlSignatureElements.getLength();
			}				
		}else{
			idObjeto="object";
		}
					
		// gera o objeto de propriedades assinadas.
		//
		X509Certificate cert = (X509Certificate)certChain[0];
		Element QPElement = XmlSigUtil.criarElemento(xmlDocument,"QualifyingProperties",null,xadesNS);
		Element SPElement = XmlSigUtil.criarElemento(xmlDocument, "SignedProperties", null,xadesNS);
		SPElement.setAttributeNS(null, "Id", "SignedProperties");
		QPElement.appendChild(SPElement);
		Element SSPElement = XmlSigUtil.criarElemento(xmlDocument, "SignedSignatureProperties", null,xadesNS);
		SPElement.appendChild(SSPElement);
		Element STElement = XmlSigUtil.criarElemento(xmlDocument, "SigningTime", null,xadesNS);
		Locale locale = new Locale("pt","BR");
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",locale); 
		STElement.setTextContent(formatador.format(calendar.getTime()));
		SSPElement.appendChild(STElement);
		Element SCElement = XmlSigUtil.criarElemento(xmlDocument, "SigningCertificate", null,xadesNS);
		Element certElement = XmlSigUtil.criarElemento(xmlDocument, "cert", null,xadesNS);
		Element certDigestElement = XmlSigUtil.criarElemento(xmlDocument, "CertDigest", null,xadesNS);
		Element DigestMethodElement = XmlSigUtil.criarElemento(xmlDocument, "DigestMethod", null,xadesNS);
		DigestMethodElement.setAttributeNS(null,"Algorithm", cert.getSigAlgName());
		certDigestElement.appendChild(DigestMethodElement);
		Element DigestValueElement = XmlSigUtil.criarElemento(xmlDocument, "DigestValue", null,xadesNS);
		DigestValueElement.setTextContent(Integer.toString(cert.hashCode()));
		certDigestElement.appendChild(DigestValueElement);
		certElement.appendChild(certDigestElement);
		Element certIssuerSerial = XmlSigUtil.criarElemento(xmlDocument, "IssuerSerial", null,xadesNS);
		Element X509IssuerName = XmlSigUtil.criarElemento(xmlDocument, "X509IssuerName", null,xadesNS);
		X509IssuerName.setTextContent(cert.getIssuerDN().getName());
		certIssuerSerial.appendChild(X509IssuerName);
		Element X509SerialNumber = XmlSigUtil.criarElemento(xmlDocument, "X509SerialNumber", null,xadesNS);
		X509SerialNumber.setTextContent(cert.getSerialNumber().toString());
		certIssuerSerial.appendChild(X509SerialNumber);
		certElement.appendChild(certIssuerSerial);
		SCElement.appendChild(certElement);
		SSPElement.appendChild(SCElement);
		if (politicaId != null){
			if (politicaId.trim().length() > 0 && !politicaId.trim().equalsIgnoreCase("null")){
				Element SPIElement = XmlSigUtil.criarElemento(xmlDocument, "SignaturePolicyIdentifier", null,xadesNS);
				Element SPIdElement = XmlSigUtil.criarElemento(xmlDocument, "SignaturePolicyId", null,xadesNS);
				Element SiPIdElement = XmlSigUtil.criarElemento(xmlDocument, "SigPolicyId", null,xadesNS);
				SiPIdElement.setTextContent(politicaId);
				//Element SPHElement = XmlSigUtil.criarElemento(xmlDocument, "SigPolicyHash", null,xadesNS);
				Element SPQElement = XmlSigUtil.criarElemento(xmlDocument, "SigPolicyQualifiers", null,xadesNS);
				Element SPQrElement = XmlSigUtil.criarElemento(xmlDocument, "SigPolicyQualifier", null,xadesNS);
				Element SPUriElement = XmlSigUtil.criarElemento(xmlDocument, "SPURI", null,xadesNS);
				SPUriElement.setTextContent(politicaUri);
				SPQrElement.appendChild(SPUriElement);
				SPQElement.appendChild(SPQrElement);
				SPIdElement.appendChild(SiPIdElement);
				//SPIdElement.appendChild(SPHElement);
				SPIdElement.appendChild(SPQElement);
				SPIElement.appendChild(SPIdElement);
				SSPElement.appendChild(SPIElement);
			}				
		}
		//Element SPPElement = XmlSigUtil.criarElemento(xmlDocument, "SignatureProductionPlace", null,xadesNS);
		//SSPElement.appendChild(SPPElement);
		//Element SRElement = XmlSigUtil.criarElemento(xmlDocument, "SignerRole", null,xadesNS);
		//SSPElement.appendChild(SRElement);
		Element SDOPElement = XmlSigUtil.criarElemento(xmlDocument, "SignedDataObjectProperties", null,xadesNS);
		SPElement.appendChild(SDOPElement);
		Element DOFElement = XmlSigUtil.criarElemento(xmlDocument, "DataObjectFormat", null,xadesNS);
		Element mimetype = XmlSigUtil.criarElemento(xmlDocument,"MimeType",null,xadesNS);
		mimetype.setTextContent(varMimeType);
		DOFElement.appendChild(mimetype);
		SDOPElement.appendChild(DOFElement);			
		obj.appendChild(QPElement);
	    obj.setId(idObjeto);		
		try {
			xmlSignature.appendObject(obj);
		} catch (com.sun.org.apache.xml.internal.security.signature.XMLSignatureException e1) {
			String errorMessage = "Erro ao inserir objeto: "+e1.getMessage();
	        throw new XMLSignatureException(errorMessage);
		}			
		Transforms transforms = new Transforms(xmlDocument);
		try {
			transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
		} catch (TransformationException e1) {
			String errorMessage = "Erro ao criar os transformadores: "+e1.getMessage();
		    throw new XMLSignatureException(errorMessage);
		}
		try {
			xmlSignature.addDocument("#" + idObjeto, transforms, Constants.ALGO_ID_DIGEST_SHA1);
		} catch (com.sun.org.apache.xml.internal.security.signature.XMLSignatureException e) {
			String errorMessage = "Erro ao adicionar os objetos: "+e.getMessage();
		    throw new XMLSignatureException(errorMessage);
		}
		try {
			xmlSignature.addKeyInfo(cert);
		} catch (XMLSecurityException e) {
			String errorMessage = "Erro ao adicionar o KeyInfo do Certificado: "+e.getMessage();
		    throw new XMLSignatureException(errorMessage);
		}
		try {
			xmlSignature.sign(privateKey);
		} catch (com.sun.org.apache.xml.internal.security.signature.XMLSignatureException e) {
			String errorMessage = "Erro assinar o documento Enveloping: "+e.getMessage();
		    throw new XMLSignatureException(errorMessage);
		}
		return xmlDocument;
	}
}
