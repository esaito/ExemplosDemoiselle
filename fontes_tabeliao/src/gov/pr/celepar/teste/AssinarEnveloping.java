package gov.pr.celepar.teste;
/*
Este programa é licenciado de acordo com a 
LPG-AP (LICENÇA PÚBLICA GERAL PARA PROGRAMAS DE COMPUTADOR DA ADMINISTRAÇÃO PÚBLICA), 
versão 1.1 ou qualquer versão posterior.
A LPG-AP deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste Programa.
Caso uma cópia da LPG-AP não esteja disponível junto com este Programa, 
você pode contatar o LICENCIANTE ou então acessar diretamente:
http://www.celepar.pr.gov.br/licenca/LPG-AP.pdf
Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa 
é preciso estar de acordo com os termos da LPG-AP
*/
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.signature.ObjectContainer;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.IdResolver;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AssinarEnveloping {
	
	public static Document createXMLDocument() throws Exception {
		javax.xml.parsers.DocumentBuilderFactory dbf = 
			javax.xml.parsers.DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		javax.xml.parsers.DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		org.w3c.dom.Document doc = db.newDocument();
		return doc;
	}

	
	public static void main(String[] args) throws Exception {
		Init.init();
		
		String xadesNS="http://uri.etsi.org/01903/v1.3.2#";
		String signedPropID="SignedProperties";
		
		String endNomeArquivo= "/home/esaito/arquivosXML_Assinatura/Enveloping_Contra.xml";
					
		//Document docXML = carregaXML(new FileInputStream("/home/esaito/arquivosXML_Assinatura/content.xml"));
		//Document docXML = carregaXML(new FileInputStream("/home/esaito/arquivosXML_Assinatura/nfe_assinar.xml"));
		//Document docXML = carregaXML(new FileInputStream("/home/esaito/arquivosXML_Assinatura/NotaDeCompra.xml"));
		Document docXML = carregaXML(new FileInputStream(endNomeArquivo));
		
		int tamNomeArquivo = endNomeArquivo.length() - 4 ;
		endNomeArquivo = endNomeArquivo.substring(0, tamNomeArquivo);
		
//		final String prefix = "";

		Document xmlDocument = createXMLDocument();
		Node oldDocument = xmlDocument.importNode(docXML.getFirstChild(), true);
		Constants.setSignatureSpecNSprefix("");
		XMLSignature xmlSignature = new XMLSignature(xmlDocument, "", XMLSignature.ALGO_ID_SIGNATURE_RSA);
		xmlDocument.appendChild(xmlSignature.getElement());
		
        ObjectContainer obj = new ObjectContainer(xmlDocument);
        obj.appendChild((Element)oldDocument);
		xmlSignature.appendObject(obj);
		
		String idObjeto = "";
		// Verifica se há outras assinaturas, isso influenciará no Id do Objeto.
		
		NodeList xmlSignatureElements =  docXML.getElementsByTagName("Signature");
		if (xmlSignatureElements.getLength() > 0){
			Element sigElement = (Element) xmlSignatureElements.item(0); 
			XMLSignature assinatura = new XMLSignature(sigElement, "");
			// Verifica se existe o objeto envelopado
			if (assinatura.getObjectLength() > 0){
				idObjeto = assinatura.getObjectItem(0).getId()+assinatura.getObjectLength();
				System.out.println("idObjeto : "+idObjeto);
			}				
		}else{
			idObjeto="object";
		}

		    //docXML.getElementsByTagNameNS(XMLSignature...XMLNS, "Signature");
		
		// A principio não há Contra-Assinatura em enveloping
		
		/*
		String contraAssinatura = "S";
		NodeList contraAssinaturaNL = null;
		if (contraAssinatura != null && !contraAssinatura.equalsIgnoreCase("")) {
			if (xmlSignatureElements.getLength() < 1){
				throw new Exception("Não há assinatura para contra-assinar");
			}else{
				contraAssinaturaNL = xmlSignatureElements;
			}
			
		}*/		
		
		// carrega cert.
		
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream("/home/esaito/workspace/exercicios/exercicios/assinaturaXML/tabeliao_icp.jks"),
				"pinhaoprod".toCharArray());
		KeyStore.PrivateKeyEntry keyEntry =
		    (KeyStore.PrivateKeyEntry) ks.getEntry
		        ("www.tabeliao.eparana.parana", new KeyStore.PasswordProtection("pinhaoprod".toCharArray()));
		X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
		
		// gera o objeto de propriedades assinadas.
		
		Element QPElement = createElement(xmlDocument,"QualifyingProperties",null,xadesNS);
		Element SPElement = createElement(xmlDocument, "SignedProperties", null,xadesNS);
		//SPElement.setAttributeNS(null, "Id", signedPropID);
		//SPElement.setAttributeNS(null, "Target", signatureID);
		IdResolver.registerElementById(SPElement, signedPropID);
		QPElement.appendChild(SPElement);
		Element SSPElement = createElement(xmlDocument, "SignedSignatureProperties", null,xadesNS);
		SPElement.appendChild(SSPElement);
		Element STElement = createElement(xmlDocument, "SigningTime", null,xadesNS);
		Locale locale = new Locale("pt","BR");
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",locale);
		STElement.setTextContent(formatador.format(calendar.getTime()));
		SSPElement.appendChild(STElement);
		Element SCElement = createElement(xmlDocument, "SigningCertificate", null,xadesNS);
		Element certElement = createElement(xmlDocument, "cert", null,xadesNS);
		Element certDigestElement = createElement(xmlDocument, "CertDigest", null,xadesNS);
		Element DigestMethodElement = createElement(xmlDocument, "DigestMethod", null,xadesNS);
		DigestMethodElement.setAttributeNS(null,"Algorithm", cert.getSigAlgName());
		certDigestElement.appendChild(DigestMethodElement);
		Element DigestValueElement = createElement(xmlDocument, "DigestValue", null,xadesNS);
		DigestValueElement.setTextContent(Integer.toString(cert.hashCode()));
		certDigestElement.appendChild(DigestValueElement);
		certElement.appendChild(certDigestElement);
		Element certIssuerSerial = createElement(xmlDocument, "IssuerSerial", null,xadesNS);
		Element X509IssuerName = createElement(xmlDocument, "X509IssuerName", null,xadesNS);
		X509IssuerName.setTextContent(cert.getIssuerDN().getName());
		certIssuerSerial.appendChild(X509IssuerName);
		Element X509SerialNumber = createElement(xmlDocument, "X509SerialNumber", null,xadesNS);
		X509SerialNumber.setTextContent(cert.getSerialNumber().toString());
		certIssuerSerial.appendChild(X509SerialNumber);
		certElement.appendChild(certIssuerSerial);
		SCElement.appendChild(certElement);
//		SCElement.setTextContent(cert.getSubjectX500Principal().getName());
		SSPElement.appendChild(SCElement);
		Element SPIElement = createElement(xmlDocument, "SignaturePolicyIdentifier", null,xadesNS);
		Element SPIdElement = createElement(xmlDocument, "SignaturePolicyId", null,xadesNS);
		Element SiPIdElement = createElement(xmlDocument, "SigPolicyId", null,xadesNS);
		SiPIdElement.setTextContent("xmlDocument-ICP-15.02");
		//Element SPHElement = createElement(xmlDocument, "SigPolicyHash", null,xadesNS);
		Element SPQElement = createElement(xmlDocument, "SigPolicyQualifiers", null,xadesNS);
		Element SPQrElement = createElement(xmlDocument, "SigPolicyQualifier", null,xadesNS);
		Element SPUriElement = createElement(xmlDocument, "SPURI", null,xadesNS);
		SPUriElement.setTextContent("www.iti.gov.br");
		SPQrElement.appendChild(SPUriElement);
		SPQElement.appendChild(SPQrElement);
		SPIdElement.appendChild(SiPIdElement);
		//SPIdElement.appendChild(SPHElement);
		SPIdElement.appendChild(SPQElement);
		SPIElement.appendChild(SPIdElement);
		SSPElement.appendChild(SPIElement);
		Element SPPElement = createElement(xmlDocument, "SignatureProductionPlace", null,xadesNS);
		SSPElement.appendChild(SPPElement);
		Element SRElement = createElement(xmlDocument, "SignerRole", null,xadesNS);
		SSPElement.appendChild(SRElement);
		Element SDOPElement = createElement(xmlDocument, "SignedDataObjectProperties", null,xadesNS);
		SPElement.appendChild(SDOPElement);
		Element DOFElement = createElement(xmlDocument, "DataObjectFormat", null,xadesNS);
		//DOFElement.setAttributeNS("null","ObjectReference", "#reference-1-1");
	    Element mimetype = createElement(xmlDocument,"MimeType",null,xadesNS);
	    mimetype.setTextContent("application/xml");
	    DOFElement.appendChild(mimetype);
		SDOPElement.appendChild(DOFElement);			
		/*if (contraAssinaturaNL != null ) {
			Node contraNode = contraAssinaturaNL.item(0);
			Element UPElement = createElement(xmlDocument, "UnsignedProperties", null,xadesNS);
			Element CSElement = createElement(xmlDocument, "CounterSignature", null,xadesNS);
			//CSElement.appendChild(contraNode);
			CSElement.setNodeValue(contraNode.toString());
			UPElement.appendChild(CSElement);
			QPElement.appendChild(UPElement);
		}*/						
        obj.appendChild(QPElement);
        obj.setId(idObjeto);
		xmlSignature.appendObject(obj);
		
		Transforms transforms = new Transforms(xmlDocument);
		transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
		xmlSignature.addDocument("#" + idObjeto, transforms, Constants.ALGO_ID_DIGEST_SHA1);
					
		// Load the KeyStore and get the signing key and certificate.
		
		xmlSignature.addKeyInfo(cert);
		xmlSignature.sign(keyEntry.getPrivateKey());
		XMLUtils.outputDOMc14nWithComments(xmlDocument, new FileOutputStream(endNomeArquivo+"_Co_assinado_teste.xml"));
		//XMLUtils.outputDOM(xmlDocument, new FileOutputStream("/home/esaito/arquivosXML_Assinatura/Enveloping_assinado.xml"));
		System.out.println("OK");
	}
	
	public static org.w3c.dom.Document carregaXML(InputStream xmlFile) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory dbf = 
			javax.xml.parsers.DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		org.w3c.dom.Document doc = null;
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(xmlFile);
		return doc;
	}
	
	public static Element createElement(Document doc, String tag, String prefix, String nsURI) {
		String qName = prefix == null ? tag : prefix + ":" + tag;
		return doc.createElementNS(nsURI, qName);
	}

}
