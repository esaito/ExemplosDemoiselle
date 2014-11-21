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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Classe para geracao do arquivo XML Assinado no formato Enveloped
 * Criando as propriedades XADES reconhecidas pela ICP-BRASIL
 * de acordo com os documentos DOC-ICP-15.05 e DOC-ICP-15.02
 * 
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */
public class GerarEnvelopedXML {
	
	private GerarEnvelopedXML(){
		
	}	
	
    /**
     * Prepara o arquivo XML para assinatura e retorna o documento
     * 
     * @deprecated Interface sem opção de contra-assinatura, mudou a assinatura para: assinarArquivoEnvelopedXml
     * @param aFileName -> Nome do arquivo
     * @param tagToSign -> nome da tag a ser assinada, se nulo assina o documento todo.
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 
     * 

     */
    public static Document signFileEnvelopedXml(String aFileName, String tagToSign, String politicaId, String politicaUri, 
    		                                    PrivateKeyAndCertChain privateKeyAndCertChain) throws XMLSignatureException
	{

    	// Carrega o arquivo XML a ser assinado
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			Document docAssinar = dbf.newDocumentBuilder().parse(new FileInputStream(aFileName));
			Document signingResult = signDocumentEnvelopedXml(docAssinar, tagToSign, politicaId, politicaUri, privateKeyAndCertChain);
			return signingResult;
		}catch (ParserConfigurationException pcex) {
			String errorMessage = "Erro no Parser do arquivo para assinar, verificar se é arquivo XML válido" + aFileName + ".";
	    	throw new XMLSignatureException(errorMessage, pcex);			
		} catch (FileNotFoundException fnfe) {
			String errorMessage = "Não foi possível ler o arquivo para assinar " + aFileName + ".";
	    	throw new XMLSignatureException(errorMessage, fnfe);
		} catch (SAXException saxe) {
			String errorMessage = "Erro ao tentar ler o arquivo para assinar, verificar se é arquivo XML válido" + aFileName + ".";
	    	throw new XMLSignatureException(errorMessage, saxe);
		} catch (IOException ioe) {
			String errorMessage = "Erro ao tentar ler o arquivo para assinar, problema de IO:" + aFileName + ".";
	    	throw new XMLSignatureException(errorMessage, ioe);
		}	    	
	}
	
    /**
     * Prepara o arquivo XML para assinatura e retorna o documento,
     * Na contra-assinatura a TAG da assinatura anterior faz parte da tag CounterSignture
     * 
     * @param aFile -> java.io.InputStream com XML Assinado
     * @param tagToSign -> nome da tag a ser assinada, se nulo assina o documento todo.
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param contraAssinatura -> se true faz contra-assinatura
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 

     */
    public static Document assinarArquivoEnvelopedXml(InputStream aFile, String tagToSign, String politicaId, String politicaUri, boolean contraAssinatura, 
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
		Document signingResult = assinandoDocumentoEnvelopedXml(docAssinar, tagToSign, politicaId, politicaUri, contraAssinatura, privateKeyAndCertChain);
		return signingResult;	    	
	}

    /**
     * Prepara o arquivo XML para assinatura e retorna o documento,
     * Na contra-assinatura a TAG da assinatura anterior faz parte da tag CounterSignture
     * 
     * @param arquivoDocument -> Arquivo do tipo org.w3c.dom.Document
     * @param tagToSign -> nome da tag a ser assinada, se nulo assina o documento todo.
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param contraAssinatura -> se true faz contra-assinatura
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 

     */
    public static Document assinarArquivoEnvelopedXml(Document arquivoDocument, String tagToSign, String politicaId, String politicaUri, boolean contraAssinatura, 
    		                                    PrivateKeyAndCertChain privateKeyAndCertChain) throws XMLSignatureException
	{

    	Document docAssinar = arquivoDocument;
		Document signingResult = assinandoDocumentoEnvelopedXml(docAssinar, tagToSign, politicaId, politicaUri, contraAssinatura, privateKeyAndCertChain);
		return signingResult;	    	
	}
    
    /**
     * Prepara o arquivo XML para assinatura e retorna o documento,
     * Na contra-assinatura a TAG da assinatura anterior faz parte da tag CounterSignture
     * 
     * @param conteudo -> Conteudo para assinatura do tipo byte[]
     * @param tagToSign -> nome da tag a ser assinada, se nulo assina o documento todo.
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param contraAssinatura -> se true faz contra-assinatura
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 

     */
    public static Document assinarArquivoEnvelopedXml(byte[] conteudo, String tagToSign, String politicaId, String politicaUri, boolean contraAssinatura, 
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
		Document signingResult = assinandoDocumentoEnvelopedXml(docAssinar, tagToSign, politicaId, politicaUri, contraAssinatura, privateKeyAndCertChain);
		return signingResult;	    	
	}

    /**
     * Prepara o arquivo XML para assinatura e retorna o documento,
     * Na contra-assinatura a TAG da assinatura anterior faz parte da tag CounterSignture
     * 
     * @param conteudoString -> Conteudo para assinatura do tipo String
     * @param tagToSign -> nome da tag a ser assinada, se nulo assina o documento todo.
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param contraAssinatura -> se true faz contra-assinatura
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 

     */
    public static Document assinarArquivoEnvelopedXml(String conteudoString, String tagToSign, String politicaId, String politicaUri, boolean contraAssinatura, 
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
		Document signingResult = assinandoDocumentoEnvelopedXml(docAssinar, tagToSign, politicaId, politicaUri, contraAssinatura, privateKeyAndCertChain);
		return signingResult;	    	
	}
    
    /**
     * Prepara o arquivo XML para assinatura e retorna o documento,
     * Na contra-assinatura a TAG da assinatura anterior faz parte da tag CounterSignture
     * 
     * @param conteudoInputSource -> Conteudo para assinatura do tipo org.xml.sax.InputSource 
     * @param tagToSign -> nome da tag a ser assinada, se nulo assina o documento todo.
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param contraAssinatura -> se true faz contra-assinatura
     * @param privateKeyAndCertChain -> KeyStore
     * @see gov.pr.celepar.tabeliao.util.PrivateKeyAndCertChain
     * @return Document
     * @throws XMLSignatureException 

     */
    public static Document assinarArquivoEnvelopedXml(InputSource conteudoInputSource, String tagToSign, String politicaId, String politicaUri, boolean contraAssinatura, 
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
		Document signingResult = assinandoDocumentoEnvelopedXml(docAssinar, tagToSign, politicaId, politicaUri, contraAssinatura, privateKeyAndCertChain);
		return signingResult;	    	
	}
    
	/**
     * Assina o arquivo XML e retorna o documento
     * 
     * @deprecated Interface sem opção de contra-assinatura, mudou a interface para: assinandoDocumentoEnvelopedXml
     * @param aDocumentToSign -> Arquivo xml para assinatura
     * @param tagToSign -> nome da tag a ser assinada, se nulo assina o documento todo. Se usada, necessida de um identificador para a TAG so e permitido: Id/ID/id.
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura 
     * @param privateKeyAndCertChain -> KeyStore
     * @return Document
     * @throws XMLSignatureException 
     */
    public static Document signDocumentEnvelopedXml(Document aDocumentToSign, String tagToSign, String politicaId, String politicaUri, PrivateKeyAndCertChain privateKeyAndCertChain)
    throws XMLSignatureException
	{
    	// constante para metodo de transformação XML 
    	String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
		
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
				
		// Criar uma DOM XMLSignatureFactory que será usada para a assinatura envolopada
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
		// Criar uma Referencia para o enveloped document (se a URI for "", significa que 
		// a assinatura e para o documento todos.
		// tambem está sendo especificado o algoritimo de hash SHA1 e
		// a transformacao ENVELOPED para o XML.
		
		// Obtem id do elemento (TAG) do documento a ser assinado,
		String id = "";
		if (tagToSign != null){
			if (tagToSign.trim().length() > 0 && !tagToSign.trim().equalsIgnoreCase("null")){
				NodeList elements = aDocumentToSign.getElementsByTagName(tagToSign);  
				org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(0);
				if( (id = el.getAttribute("id")) != "" || (id = el.getAttribute("iD")) != "" ||
						(id = el.getAttribute("Id")) != "" || (id = el.getAttribute("ID")) != ""){
						id = "#"+id;
				}	
			}			
		}
		Reference ref;
		try {
			ArrayList<Transform> transformList = new ArrayList<Transform>();  
	        TransformParameterSpec tps = null;  
	        Transform envelopedTransform = fac.newTransform(Transform.ENVELOPED, tps);  
	        Transform c14NTransform = fac.newTransform(C14N_TRANSFORM_METHOD, tps);  
	        transformList.add(envelopedTransform);  
	        transformList.add(c14NTransform);
	        ArrayList<Reference> refList = new ArrayList<Reference>();  
	        DigestMethod digestMethod = fac.newDigestMethod(  
                    DigestMethod.SHA1, null);
	        ref = fac.newReference(id,digestMethod, transformList, null, null);
		    refList.add(ref);
			
			// Criar a SignedInfo.
			SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
			   (C14NMethodParameterSpec) null),fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
			   Collections.singletonList(ref));
			List<Certificate> list  = new ArrayList<Certificate>();
	    	for(int i=0 ; i<certChain.length ; i++) {
	        	list.add(certChain[i]);
	    	}
	    	X509Certificate cert = (X509Certificate)certChain[0];
			// Cria o  KeyInfo contido em X509Data.
			KeyInfoFactory kif = fac.getKeyInfoFactory();
			List<Serializable> x509Content = new ArrayList<Serializable>();
			x509Content.add(cert.getSubjectX500Principal().getName());
			x509Content.add(cert);
			X509Data xd = kif.newX509Data(x509Content);
			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));
		
			// Cria o  DOMSignContext e especifica a chave RSA e
			// a localizacao da Assinatura XML resultante do elemento pai
			DOMSignContext dsc = new DOMSignContext
			    (privateKey, aDocumentToSign.getDocumentElement());
			//  
			Element QPElement = XmlSigUtil.criarElemento(aDocumentToSign,"QualifyingProperties",null,xadesNS);
			Element SPElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignedProperties", null,xadesNS);
			SPElement.setAttributeNS(null, "Id", "SignedProperties");
			QPElement.appendChild(SPElement);
			Element SSPElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignedSignatureProperties", null,xadesNS);
			SPElement.appendChild(SSPElement);
			Element STElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigningTime", null,xadesNS);
			Locale locale = new Locale("pt","BR");
			GregorianCalendar calendar = new GregorianCalendar();
			SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",locale); 
			STElement.setTextContent(formatador.format(calendar.getTime()));
			SSPElement.appendChild(STElement);
			Element SCElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigningCertificate", null,xadesNS);
			Element certElement = XmlSigUtil.criarElemento(aDocumentToSign, "cert", null,xadesNS);
			Element certDigestElement = XmlSigUtil.criarElemento(aDocumentToSign, "CertDigest", null,xadesNS);
			Element DigestMethodElement = XmlSigUtil.criarElemento(aDocumentToSign, "DigestMethod", null,xadesNS);
			DigestMethodElement.setAttributeNS(null,"Algorithm", cert.getSigAlgName());
			certDigestElement.appendChild(DigestMethodElement);
			Element DigestValueElement = XmlSigUtil.criarElemento(aDocumentToSign, "DigestValue", null,xadesNS);
			DigestValueElement.setTextContent(Integer.toString(cert.hashCode()));
			certDigestElement.appendChild(DigestValueElement);
			certElement.appendChild(certDigestElement);
			Element certIssuerSerial = XmlSigUtil.criarElemento(aDocumentToSign, "IssuerSerial", null,xadesNS);
			Element X509IssuerName = XmlSigUtil.criarElemento(aDocumentToSign, "X509IssuerName", null,xadesNS);
			X509IssuerName.setTextContent(cert.getIssuerDN().getName());
			certIssuerSerial.appendChild(X509IssuerName);
			Element X509SerialNumber = XmlSigUtil.criarElemento(aDocumentToSign, "X509SerialNumber", null,xadesNS);
			X509SerialNumber.setTextContent(cert.getSerialNumber().toString());
			certIssuerSerial.appendChild(X509SerialNumber);
			certElement.appendChild(certIssuerSerial);
			SCElement.appendChild(certElement);
			SSPElement.appendChild(SCElement);
			if (politicaId != null){
				if (politicaId.trim().length() > 0 && !politicaId.trim().equalsIgnoreCase("null")){
					Element SPIElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignaturePolicyIdentifier", null,xadesNS);
					Element SPIdElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignaturePolicyId", null,xadesNS);
					Element SiPIdElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigPolicyId", null,xadesNS);
					SiPIdElement.setTextContent(politicaId);
					//Element SPHElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigPolicyHash", null,xadesNS);
					Element SPQElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigPolicyQualifiers", null,xadesNS);
					Element SPQrElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigPolicyQualifier", null,xadesNS);
					Element SPUriElement = XmlSigUtil.criarElemento(aDocumentToSign, "SPURI", null,xadesNS);
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
			//Element SPPElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignatureProductionPlace", null,xadesNS);
			//SSPElement.appendChild(SPPElement);
			//Element SRElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignerRole", null,xadesNS);
			//SSPElement.appendChild(SRElement);
			Element SDOPElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignedDataObjectProperties", null,xadesNS);
			SPElement.appendChild(SDOPElement);
			Element DOFElement = XmlSigUtil.criarElemento(aDocumentToSign, "DataObjectFormat", null,xadesNS);
		    Element mimetype = XmlSigUtil.criarElemento(aDocumentToSign,"MimeType",null,xadesNS);
		    mimetype.setTextContent(varMimeType);
		    DOFElement.appendChild(mimetype);
			SDOPElement.appendChild(DOFElement);			
			//Element UPElement = XmlSigUtil.criarElemento(aDocumentToSign, "UnsignedProperties", null,xadesNS);
			//QPElement.appendChild(UPElement);
			DOMStructure qualifPropStruct = new DOMStructure(QPElement);
			List<DOMStructure> xmlObj = new ArrayList<DOMStructure>();
			xmlObj.add(qualifPropStruct);
			XMLObject object = fac.newXMLObject(xmlObj,null,null,null);
			List<XMLObject> objects = Collections.singletonList(object);
			   
			// Cria a assinatura XML, mas ainda não assina.
			XMLSignature signature = fac.newXMLSignature(si, ki,objects,null,null);
			// efetua a assinatura.
			signature.sign(dsc);
		} catch (NoSuchAlgorithmException nsae) {
	        String errorMessage = "Algoritmo não encontrado ao assinar o arquivo.\n" +
	        "Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + nsae.getMessage();
	        throw new XMLSignatureException(errorMessage, nsae);
		} catch (InvalidAlgorithmParameterException iape) {
			String errorMessage = "Algoritmo inválido ao assinar o arquivo.\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + iape.getMessage();
	        throw new XMLSignatureException(errorMessage, iape);
		} catch (MarshalException me) {
			String errorMessage = "Erro do tipo Marshal ao assinar o arquivo XML.\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + me.getMessage();
	        throw new XMLSignatureException(errorMessage, me);
		}catch (Exception e) {
			String errorMessage = "Erro do tipo Exception ao assinar o arquivo XML.\n" +
			"Anote este erro e contate com o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		}
	    return aDocumentToSign;
	}
    
    /**
     * Assina o arquivo XML e retorna o documento, com opção de contra-assinatura
     * 
     * @param aDocumentToSign -> Arquivo xml para assinatura
     * @param tagToSign -> nome da tag a ser assinada, se nulo assina o documento todo. Se usada, necessida de um identificador para a TAG so e permitido: Id/ID/id.
     * @param politicaId -> Id da politica de Assinatura
     * @param politicaUri -> url onde se entra a politica de assinatura
     * @param contraAssinatura -> se true, faz a contra assinatura 
     * @param privateKeyAndCertChain -> KeyStore
     * @return Document
     * @throws XMLSignatureException 
     */
    public static Document assinandoDocumentoEnvelopedXml(Document aDocumentToSign, String tagToSign, String politicaId, String politicaUri, boolean contraAssinatura,
    												PrivateKeyAndCertChain privateKeyAndCertChain)  throws XMLSignatureException
	{
    	// constante para metodo de transformação XML 
    	String C14N_TRANSFORM_METHOD = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
		
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
				
		// Criar uma DOM XMLSignatureFactory que será usada para a assinatura envolopada
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
		// Criar uma Referencia para o enveloped document (se a URI for "", significa que 
		// a assinatura e para o documento todos.
		// tambem está sendo especificado o algoritimo de hash SHA1 e
		// a transformacao ENVELOPED para o XML.
		
		// Obtem id do elemento (TAG) do documento a ser assinado,
		String id = "";
		if (tagToSign != null){
			if (tagToSign.trim().length() > 0 && !tagToSign.trim().equalsIgnoreCase("null")){
				NodeList elements = aDocumentToSign.getElementsByTagName(tagToSign);  
				org.w3c.dom.Element el = (org.w3c.dom.Element) elements.item(0);
				if( (id = el.getAttribute("id")) != "" || (id = el.getAttribute("iD")) != "" ||
						(id = el.getAttribute("Id")) != "" || (id = el.getAttribute("ID")) != ""){
						id = "#"+id;
				}	
			}			
		}
		NodeList contraAssinaturaNL = null;
		if (contraAssinatura) {
			contraAssinaturaNL = aDocumentToSign.getElementsByTagName("Signature");
			if (contraAssinaturaNL.getLength() < 1){
				try {
					throw new Exception("Não há assinatura para contra-assinar");
				} catch (Exception e) {
					String errorMessage = "Erro ao gerar Contra-Assinatura.\n" +
					   "Não foi encontrada uma assinatura no arquivo XML!\n" +
					   "Verifique o arquivo ou não utilize a opção: Contra-Assinatura!\n" +
					   "Detalhes: " + e.getMessage();
						throw new XMLSignatureException(errorMessage, e);
				}
			}
		}		
		Reference ref;
		try {
			ArrayList<Transform> transformList = new ArrayList<Transform>();  
	        TransformParameterSpec tps = null;  
	        Transform envelopedTransform = fac.newTransform(Transform.ENVELOPED, tps);  
	        Transform c14NTransform = fac.newTransform(C14N_TRANSFORM_METHOD, tps);  
	        transformList.add(envelopedTransform);  
	        transformList.add(c14NTransform);
	        ArrayList<Reference> refList = new ArrayList<Reference>();  
	        DigestMethod digestMethod = fac.newDigestMethod(  
                    DigestMethod.SHA1, null);
	        ref = fac.newReference(id,digestMethod, transformList, null, null);
		    refList.add(ref);
			
			// Criar a SignedInfo.
			SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
			   (C14NMethodParameterSpec) null),fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
			   Collections.singletonList(ref));
			List<Certificate> list  = new ArrayList<Certificate>();
	    	for(int i=0 ; i<certChain.length ; i++) {
	        	list.add(certChain[i]);
	    	}
	    	X509Certificate cert = (X509Certificate)certChain[0];
			// Cria o  KeyInfo contido em X509Data.
			KeyInfoFactory kif = fac.getKeyInfoFactory();
			List<Serializable> x509Content = new ArrayList<Serializable>();
			x509Content.add(cert.getSubjectX500Principal().getName());
			x509Content.add(cert);
			X509Data xd = kif.newX509Data(x509Content);
			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));
		
			// Cria o  DOMSignContext e especifica a chave RSA e
			// a localizacao da Assinatura XML resultante do elemento pai
			DOMSignContext dsc = new DOMSignContext
			    (privateKey, aDocumentToSign.getDocumentElement());
			//  
			Element QPElement = XmlSigUtil.criarElemento(aDocumentToSign,"QualifyingProperties",null,xadesNS);
			Element SPElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignedProperties", null,xadesNS);
			SPElement.setAttributeNS(null, "Id", "SignedProperties");
			QPElement.appendChild(SPElement);
			Element SSPElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignedSignatureProperties", null,xadesNS);
			SPElement.appendChild(SSPElement);
			Element STElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigningTime", null,xadesNS);
			Locale locale = new Locale("pt","BR");
			GregorianCalendar calendar = new GregorianCalendar();
			SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",locale); 
			STElement.setTextContent(formatador.format(calendar.getTime()));
			SSPElement.appendChild(STElement);
			Element SCElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigningCertificate", null,xadesNS);
			Element certElement = XmlSigUtil.criarElemento(aDocumentToSign, "cert", null,xadesNS);
			Element certDigestElement = XmlSigUtil.criarElemento(aDocumentToSign, "CertDigest", null,xadesNS);
			Element DigestMethodElement = XmlSigUtil.criarElemento(aDocumentToSign, "DigestMethod", null,xadesNS);
			DigestMethodElement.setAttributeNS(null,"Algorithm", cert.getSigAlgName());
			certDigestElement.appendChild(DigestMethodElement);
			Element DigestValueElement = XmlSigUtil.criarElemento(aDocumentToSign, "DigestValue", null,xadesNS);
			DigestValueElement.setTextContent(Integer.toString(cert.hashCode()));
			certDigestElement.appendChild(DigestValueElement);
			certElement.appendChild(certDigestElement);
			Element certIssuerSerial = XmlSigUtil.criarElemento(aDocumentToSign, "IssuerSerial", null,xadesNS);
			Element X509IssuerName = XmlSigUtil.criarElemento(aDocumentToSign, "X509IssuerName", null,xadesNS);
			X509IssuerName.setTextContent(cert.getIssuerDN().getName());
			certIssuerSerial.appendChild(X509IssuerName);
			Element X509SerialNumber = XmlSigUtil.criarElemento(aDocumentToSign, "X509SerialNumber", null,xadesNS);
			X509SerialNumber.setTextContent(cert.getSerialNumber().toString());
			certIssuerSerial.appendChild(X509SerialNumber);
			certElement.appendChild(certIssuerSerial);
			SCElement.appendChild(certElement);
			SSPElement.appendChild(SCElement);
			if (politicaId != null){
				if (politicaId.trim().length() > 0 && !politicaId.trim().equalsIgnoreCase("null")){
					Element SPIElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignaturePolicyIdentifier", null,xadesNS);
					Element SPIdElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignaturePolicyId", null,xadesNS);
					Element SiPIdElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigPolicyId", null,xadesNS);
					SiPIdElement.setTextContent(politicaId);
					//Element SPHElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigPolicyHash", null,xadesNS);
					Element SPQElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigPolicyQualifiers", null,xadesNS);
					Element SPQrElement = XmlSigUtil.criarElemento(aDocumentToSign, "SigPolicyQualifier", null,xadesNS);
					Element SPUriElement = XmlSigUtil.criarElemento(aDocumentToSign, "SPURI", null,xadesNS);
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
			//Element SPPElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignatureProductionPlace", null,xadesNS);
			//SSPElement.appendChild(SPPElement);
			//Element SRElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignerRole", null,xadesNS);
			//SSPElement.appendChild(SRElement);
			Element SDOPElement = XmlSigUtil.criarElemento(aDocumentToSign, "SignedDataObjectProperties", null,xadesNS);
			SPElement.appendChild(SDOPElement);
			Element DOFElement = XmlSigUtil.criarElemento(aDocumentToSign, "DataObjectFormat", null,xadesNS);
		    Element mimetype = XmlSigUtil.criarElemento(aDocumentToSign,"MimeType",null,xadesNS);
		    mimetype.setTextContent(varMimeType);
		    DOFElement.appendChild(mimetype);
			SDOPElement.appendChild(DOFElement);			
			if (contraAssinaturaNL != null) {
				Element UPElement = XmlSigUtil.criarElemento(aDocumentToSign, "UnsignedProperties", null,xadesNS);
				Element CSElement = XmlSigUtil.criarElemento(aDocumentToSign, "CounterSignature", null,xadesNS);
				CSElement.appendChild(contraAssinaturaNL.item(0));
				UPElement.appendChild(CSElement);
				QPElement.appendChild(UPElement);
			}
			DOMStructure qualifPropStruct = new DOMStructure(QPElement);
			List<DOMStructure> xmlObj = new ArrayList<DOMStructure>();
			xmlObj.add(qualifPropStruct);
			XMLObject object = fac.newXMLObject(xmlObj,null,null,null);
			List<XMLObject> objects = Collections.singletonList(object);
			   
			// Cria a assinatura XML, mas ainda não assina.
			XMLSignature signature = fac.newXMLSignature(si, ki,objects,null,null);
			// efetua a assinatura.
			signature.sign(dsc);
		} catch (NoSuchAlgorithmException nsae) {
	        String errorMessage = "Algoritmo não encontrado ao assinar o arquivo.\n" +
	        "Anote este erro e contate o responsável pelo sistema!\n" +
	        "Detalhes: " + nsae.getMessage();
	        throw new XMLSignatureException(errorMessage, nsae);
		} catch (InvalidAlgorithmParameterException iape) {
			String errorMessage = "Algoritmo inválido ao assinar o arquivo.\n" +
			"Anote este erro e contate o responsável pelo sistema!\n" +
	        "Detalhes: " + iape.getMessage();
	        throw new XMLSignatureException(errorMessage, iape);
		} catch (MarshalException me) {
			String errorMessage = "Erro do tipo Marshal ao assinar o arquivo XML.\n" +
						"Anote este erro e contate o responsável pelo sistema!\n" +
				        "Detalhes: " + me.getMessage();
	        throw new XMLSignatureException(errorMessage, me);
		}catch (Exception e) {
			String errorMessage = "Erro do tipo Exception, ao assinar o arquivo XML.\n" +
								   "Anote este erro e contate o responsável pelo sistema!\n" +
	        "Detalhes: " + e.getMessage();
	        throw new XMLSignatureException(errorMessage, e);
		}
	    return aDocumentToSign;
	}
}