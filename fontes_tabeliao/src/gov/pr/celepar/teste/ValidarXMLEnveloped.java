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
import gov.pr.celepar.tabeliao.core.TabeliaoAssinaturaEnvelopedXML;
import gov.pr.celepar.tabeliao.core.TabeliaoCertificate;
import gov.pr.celepar.tabeliao.core.validacao.TabeliaoResultadoValidacao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValidarXMLEnveloped {
	
	final static String DIRETORIO = "/home/esaito/arquivosXML_Assinatura/";
	
	//final static String DIRETORIO = "/home/esaito/arquivosXML_Assinatura/assinadosWeb/";
	
	//final static String DIRETORIO = "/home/esaito/workspace/exercicios/exercicios/assinaturaXML/";

	public static void main(String[] args) {
		
			//Byte[] arrayAss;
			
			//Document docAss;
			//Document newDocAss;
			
			String fileName = "NfeEmLote.xml";
					

				//"nota-cert-revogado.xml";
			
				//"certificado_vencido.xml";
			//"copacol.xml";;
			//"NotaDeCompra_Itens_F_Contra_E.xml";
			//"PessoaJuridicaXml_borracheiro.xml";
			//"lote_33712_PR_alt.xml";
			//"nota_erro_cnpj.xml";
			//"nota-cert-revogado.xml";
			//"nota_certisign_alt.xml";
			//nfe-globoaves-loterecepcao.xml
			//"NotaDeCompra_Itens_E_Vend_F.xml";
			//"nfe_serasa.xml";
				
						
			try {
				//InputStream iS = new FileInputStream(DIRETORIO + fileName);
				//String texto = convertStreamToString(iS);
				//docAss = XmlSigUtil.carregarArquivoXML(iS);
				// 
					//inputStreamAsString(iS); 
				//System.out.println(texto);
								
				//newDocAss = revalidaDocument(docAss);
						
				TabeliaoAssinaturaEnvelopedXML assinaturaEnvelopedXML;
				List<TabeliaoResultadoValidacao> resultados = new ArrayList<TabeliaoResultadoValidacao>();
				List<TabeliaoCertificate> certificados = new ArrayList<TabeliaoCertificate>();
				assinaturaEnvelopedXML = new TabeliaoAssinaturaEnvelopedXML(new FileInputStream(DIRETORIO + fileName));
				assinaturaEnvelopedXML.valida();
				assinaturaEnvelopedXML.validarVigencias();		
				resultados = assinaturaEnvelopedXML.getResultadosValidacoes();
				certificados = assinaturaEnvelopedXML.getCertificadosAssinantes();
//				tags = assinaturaEnvelopedXML.getTagsAssinadas();
				
				for(int j=0; j< assinaturaEnvelopedXML.getQuantidadeAssinaturas(); j++){
					System.out.println("Assinatura: "+j);
					System.out.println("Valida: "+ resultados.get(j).toString());
					System.out.println("Tag Assinada: "+assinaturaEnvelopedXML.getUriTagAssinada(j));
					System.out.println("Nome Tag Assinada: "+assinaturaEnvelopedXML.getNomeTagAssinada(j));
					System.out.println("Nome no Certificado: "+ certificados.get(j).getNome());
					System.out.println("KeyUsage: "+ certificados.get(j).getTabeliaoKeyUsage());
					System.out.println("TipoCertificado: "+ certificados.get(j).getTipoCertificado());
					System.out.println("E-mail : "+certificados.get(j).getEmail());
					//System.out.println("Certificado : "+certificados.get(j).getX509Certificate());
					System.out.println("CRL : "+certificados.get(j).getCRLDistributionPoint());
					System.out.println("IDAutority: "+certificados.get(j).getAuthorityKeyIdentifier());
					
					//se pessoa jurídica.
					if (certificados.get(j).hasDadosPJ()){
						System.out.println("Responsável : "+certificados.get(j).getTabeliaoDadosPJ().getNomeResponsavel());
						System.out.println("CNPJ : "+certificados.get(j).getTabeliaoDadosPJ().getCNPJ());
						DateFormat formatter ; 
					    Date date = certificados.get(j).getTabeliaoDadosPJ().getDataNascimento();    
					    formatter = new SimpleDateFormat("dd-MMM-yyyy");
					    String s = formatter.format(date);
					    System.out.println("Data Nascimento : "+s);
					}
					//se pessoa física
					if (certificados.get(j).hasDadosPF()){
						System.out.println("CPF : "+certificados.get(j).getTabeliaoDadosPF().getCPF());
						
						DateFormat formatter ; 
					    Date date = certificados.get(j).getTabeliaoDadosPF().getDataNascimento();    
					    formatter = new SimpleDateFormat("dd-MMM-yyyy");
					    String s = formatter.format(date);
					    System.out.println("Data Nascimento : "+s);
					}
					//System.out.println("Certificado: "+ certificados.get(j));
					System.out.println("Validade Ate : "+certificados.get(j).getValidadeAte());
					System.out.println("Contra? : "+assinaturaEnvelopedXML.isContraAssintura(j));
					System.out.println("SignaturePolicyIdentifier : "+assinaturaEnvelopedXML.getSignaturePolicyIdentifier(j));
					System.out.println("SigningCertificate : "+assinaturaEnvelopedXML.getSigningCertificate(j));
					System.out.println("Data da Assinatura : "+assinaturaEnvelopedXML.getDataAssinatura(j));
					System.out.println("ObjectFormat : "+assinaturaEnvelopedXML.getDataObjectFormat(j));
					System.out.println(" --------------- xxx -----------------");
					System.out.println();
				}
			} catch (Exception e) {
				System.out.println("Erro grave: ");
				e.printStackTrace();
			}
		}
	
	
	public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        return sb.toString();
    }
	

	/*
	public void  valida() throws Exception {
				
		Init.init();
		Element nscontext = XMLUtils.createDSctx(xmlAssinado, "ds", Constants.SignatureSpecNS);
		NodeList xmlSignatureElements = XPathAPI.selectNodeList(xmlAssinado, "//ds:Signature", nscontext);
		
		for (int ix=0; ix <xmlSignatureElements.getLength(); ix++){
			try{				
				TabeliaoResultadoValidacao resultVal = new TabeliaoResultadoValidacao();
				resultadosValidacao.add(resultVal);
				Node node = xmlSignatureElements.item(ix);
				XMLSignature sig = new XMLSignature((Element)node, "");
				assinaturas.add(sig);
			}catch (Exception e) {
				throw new Exception(
				"Erro ao iniciar processo de validação de assinatura.");
			}	
		
			// Verifica se o certificado está OK.
			if (assinaturas.get(ix).getKeyInfo().getX509Certificate() == null) {
				throw new Exception(
				"Não foi possível encontrar o certificado na assinatura.");
			}			
			try {
				if (assinaturas.get(ix).checkSignatureValue(assinaturas.get(ix).getKeyInfo().getX509Certificate())){
					resultadosValidacao.get(ix).addOk(TabeliaoResultadoValidacao.VAL_CONTEUDO);
					resultadosValidacao.get(ix).addOk(TabeliaoResultadoValidacao.VAL_VALIDADE);
					TabeliaoCertificate cert  = new TabeliaoCertificate(assinaturas.get(ix).getKeyInfo().getX509Certificate());
					certificadosDasAssinaturas.add(cert);
				}else {
					//Não deve entrar neste ELSE.
					resultadosValidacao.get(ix).addErro(TabeliaoResultadoValidacao.VAL_CONTEUDO, 
						          "Erro na validação da assinatura com o conteúdo para o certificado de:"+assinaturas.get(ix).getKeyInfo().getX509Certificate().getSubjectDN().getName());
				}
				} catch (CertificateExpiredException e) {
					resultadosValidacao.get(ix).addErro(TabeliaoResultadoValidacao.VAL_VALIDADE, e.getMessage());
				} catch (CertificateNotYetValidException e) {
					resultadosValidacao.get(ix).addErro(TabeliaoResultadoValidacao.VAL_VALIDADE, e.getMessage());
				} catch (Exception e) {
					resultadosValidacao.get(ix).addErro(TabeliaoResultadoValidacao.VAL_CONTEUDO, e.getMessage());
				}				
		}
	}*/
}
