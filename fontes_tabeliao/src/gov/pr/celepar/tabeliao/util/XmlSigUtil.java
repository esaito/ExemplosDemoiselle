package gov.pr.celepar.tabeliao.util;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Classe utilitaria com funcoes de tratamento de XML 
 * 
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */
public class XmlSigUtil {

	private XmlSigUtil(){
		
	}
	/**
     * Criar um elemento XML
     * @param doc -> Arquivo xml para assinatura
     * @param tag -> nome da tag para a qual sera gerado o elemento 
     * @param prefix -> prefixo do elemento
     * @param nsURI -> Uri da tag
     * @return Element ->  org.w3c.dom.Element
     * @see org.w3c.dom.Element
     */
    public static Element criarElemento(Document doc, String tag, String prefix, String nsURI) {
		String qName = prefix == null ? tag : prefix + ":" + tag;
		return doc.createElementNS(nsURI, qName);
	}
	
	/**
	 * Cria um documento XML vazio
	 * @return Document -> novo documento XML 
	 * @throws Exception
	 */
	public static Document criarDocumentoXMl() throws Exception {
		DocumentBuilderFactory dbf = 
		DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		return doc;
	}
	
	/**
	 * Metodo para carregar o arquivo XML assinado e garantir que o XML esta bem formatado.
	 * 
	 * @param xmlFile arquivo XML assinado.
	 * 
	 * @return org.w3c.dom.Document 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	
	public static Document carregarArquivoXML(InputStream xmlFile) 
					throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = null;
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		doc = db.parse(xmlFile);
		return doc;
	}

	/**
	 * Metodo para carregar uma string contendo um arquivo xml assinado.
	 * 
	 * @param xmlString -> XML assinado.
	 * 
	 * @return org.w3c.dom.Document 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	
	public static Document carregarArquivoXML(String xmlString) 
					throws ParserConfigurationException, SAXException, IOException{
		
		DocumentBuilderFactory dbf =DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = null;
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		doc = db.parse(new InputSource(new StringReader(xmlString)));
		return doc;
	}


	/**
	 * Metodo para carregar o arquivo XML assinado e garantir que o XML esta bem formatado.
	 * 
	 * @param xmlSource arquivo XML assinado.
	 * 
	 * @return org.w3c.dom.Document 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	
	public static Document carregarArquivoXML(InputSource xmlSource) 
				throws ParserConfigurationException, SAXException, IOException{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = null;
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		doc = db.parse(xmlSource);
		return doc;
	}
	
	
	
	/**
	 * Revalida um objeto Document 
	 * @param documento
	 * @return Document
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws TransformerConfigurationException 
	 */
	public static Document revalidaDocument(Document documento) 
				throws ParserConfigurationException, SAXException, IOException, 
					   TransformerConfigurationException, TransformerException, 
					   TransformerFactoryConfigurationError{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = null;
		DocumentBuilder db;
		db = dbf.newDocumentBuilder();
		InputSource is = converterDoc(documento);
		if (is != null){
			doc = db.parse(is);
		}		
		return doc;
	}
	
	/**
	 *  Classe utilitaria para converter Document em InputSource
	 * @param documento
	 * @return InputSource
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws TransformerConfigurationException 
	 */
	private static InputSource converterDoc (Document documento)
							throws TransformerConfigurationException, 
								   TransformerException, TransformerFactoryConfigurationError{

			Document doc = documento;
			DOMSource source = new DOMSource(doc);
			StringWriter xmlAsWriter = new StringWriter();
			StreamResult result = new StreamResult(xmlAsWriter);
			TransformerFactory.newInstance().newTransformer().transform(source, result);
			StringReader xmlReader = new StringReader(xmlAsWriter.toString());
			InputSource iS = new InputSource(xmlReader);
			return iS;
	}
}
