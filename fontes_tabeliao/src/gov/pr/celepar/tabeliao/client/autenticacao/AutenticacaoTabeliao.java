package gov.pr.celepar.tabeliao.client.autenticacao;
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
import gov.pr.celepar.sentinela.client.SentinelaXml;
import gov.pr.celepar.sentinela.client.autenticacao.AutenticacaoInterface;
import gov.pr.celepar.sentinela.client.pojo.UsuarioAutenticado;
import gov.pr.celepar.sentinela.excecao.SentinelaException;
import gov.pr.celepar.tabeliao.core.TabeliaoCertificate;
import gov.pr.celepar.tabeliao.core.validacao.TabeliaoResultadoValidacao;
import gov.pr.celepar.tabeliao.facade.CertificadoFacade;
import gov.pr.celepar.tabeliao.pojo.CertificadoAc;
import gov.pr.celepar.tabeliao.pojo.CertificadoPublico;
import gov.pr.celepar.tabeliao.util.Base64Utils;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.jcetaglib.lib.X509Cert;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.jboss.logging.Logger;
/**
 * Classe de autenticacao para uso com o proto-agente SENTINELA
 * implementa gov.pr.celepar.sentinela.client.autenticacao.AutenticacaoInterface
 * @author Thiago Meneghello - GIC/CELEPAR
 * @author Emerson Sachio Saito (alteracoes) - GIC/CELEPAR 
 *
 */
public class AutenticacaoTabeliao implements AutenticacaoInterface {
	
	private static Logger log = Logger.getLogger(AutenticacaoTabeliao.class);

//	private static final String SESSION_CERTIFICADO = "TABELIAO_SESSION_CERTIFICADO";
	private String nivelAcesso = null;
    private String validaCadeiaCertificado = null;
    private String validaLCR = null;
    private String validaCertificadoExpirado = null;
    private String validaNivelAcesso = null;
    private static String diasMensagemExpirando = null;
    private static String LCRTempo = null;
    private static String LCRProxy = null;
    private static String LCRProxyUsuario = null;
    private static String LCRProxySenha = null;
    private static String LCRProxyPorta = null;
    
    /**
	 * Recupera o tempo de atualizacao das LCRs, configurado em sentinela.xml
	 * @return Tempo em milisegundos
	 */
    public static String getLCRTempo(){
		return LCRTempo;
	}
    
    /**
     * Recupera o endereco de proxy configurado em sentinela.xml
     * @return endereco do proxy
     */
    public static String getLCRProxy(){
    	return LCRProxy;
    }
    
    /**
     * Recupera o usuario de proxy configurado em sentinela.xml
     * @return nome do usuario do proxy
     */
    public static String getLCRProxyUsuario(){
    	return LCRProxyUsuario;
    }
    
    /**
     * Recupera a senha do usuario de proxy configurado em sentinela.xml
     * @return senha do usuario do proxy
     */
    public static String getLCRProxySenha(){
    	return LCRProxySenha;
    }
    
    /**
     * Recupera o numero da porta usada pelo proxy configurado em sentinela.xml
     * @return numero da porta usada pelo proxy
     */
    public static String getLCRProxyPorta(){
    	return LCRProxyPorta;
    }
    
    /**
     * retorna a quantidade de dias que a mensagem de aviso de expiracao eh emitida.
     * @return numero de dias antes da expiracao do certificado
     */
    public static String getDiasMensagemExpirando(){
    	return diasMensagemExpirando;
    }
    
	/**
	 * @param certificado
	 * @param CPF
	 * @param securityCode
	 * @param trocandoSenha
	 * @param arg5
	 * @param arg6
	 * @return UsuarioAutenticado
	 * @throws SentinelaException 
	 * @see gov.pr.celepar.sentinela.client.pojo.UsuarioAutenticado
	 */
    
    @Override
    @SuppressWarnings("unchecked")
	public UsuarioAutenticado autentica(String certificado, String CPF, String securityCode, boolean trocandoSenha, String arg5, Long arg6) throws SentinelaException {
		
		if(certificado == null) {
			log.info("Exceção: Sem Certificado");
			throw new SentinelaException("A assinatura do usuario nao foi repassada para realizar a autenticacao.");
		}
		
		if(securityCode == null) {
			log.info("Exceção: Sem securityCode");
			throw new SentinelaException("O sentinela nao repassou o securityCode para autenticacao.");
		}
			
        UsuarioTabeliao usuarioTabeliao = null;

		log.info("Chamando metodo autentica AutenticacaoTabeliao");
        try {
            byte[] assinatura = Base64Utils.base64Decode(certificado);

			CMSSignedData sig;
			
			sig = new CMSSignedData(assinatura);
			
			String uuidAssinado = new String((byte[]) sig.getSignedContent().getContent()); 

			log.info("UUID da assinatura: " + uuidAssinado);
			
			//UUID
			if(! securityCode.equals(uuidAssinado)){
				log.info("Exceção: UUID diferentes");
				throw new SentinelaException("Erro ao realizar a autenticacao.\nOs ids gerado e assinado nao sao iguais.");
			}
			
			X509Certificate cert = null;
			CertStore certStore = sig.getCertificatesAndCRLs("Collection", "SUN");
			SignerInformationStore sis = sig.getSignerInfos();
			
			for(SignerInformation si : (Collection<SignerInformation>)sis.getSigners()) {
				Collection<?> c2 = certStore.getCertificates(si.getSID());
				Iterator<?> it2 = c2.iterator();
				if(it2.hasNext()) {
					cert = (X509Certificate) it2.next();
				}
			}
			
        	if(cert != null) {              
        		log.info("Instância o Certificado no Tabelião.");
				TabeliaoCertificate tc = new TabeliaoCertificate(cert);
				
				log.info("Executa a validação da Cadeia do Certificado.");
				TabeliaoResultadoValidacao tabResVal = tc.valida();
				
				//Verifica a cadeia de certificados
				try {
					String valida = validaCadeiaCertificado;
					if("1".equals(valida)) {
						String res = tabResVal.getMensagem(TabeliaoResultadoValidacao.VAL_CADEIA);
						log.info("Verificando a cadeia de certificados.");
						if(TabeliaoResultadoValidacao.MSG_ERRO.equals(res)) {
							log.info("Erro na cadeia");
							throw new Exception(tabResVal.getDescricao(TabeliaoResultadoValidacao.VAL_CADEIA));
						}
						if(TabeliaoResultadoValidacao.MSG_EXCECAO.equals(res)) {
							log.info("Exceção para cadeia");
							throw new Exception(tabResVal.getDescricao(TabeliaoResultadoValidacao.VAL_CADEIA));
						}
						
					} else {
						log.info("Nao está verificando a cadeia de certificados.");
					}
				} catch (Exception e) {
					throw new SentinelaException("Erro ao realizar a autenticacao.\nEste certificado nao está na cadeia de certificados ICP Brasil.", e);
				}
				
				//Verifica se o certificado esta revogado
				try {
					String valida = validaLCR;
					if("1".equals(valida)) {
						String res = tabResVal.getMensagem(TabeliaoResultadoValidacao.VAL_LCR);
						
						log.info("Verificando lista na base ou web.");
						if(TabeliaoResultadoValidacao.MSG_EXCECAO.equals(res)) {
							log.info("Exceção");
							throw new Exception(tabResVal.getDescricao(TabeliaoResultadoValidacao.VAL_LCR));
						}
						log.info("Verificando se o certificado esta revogado.");
						if(TabeliaoResultadoValidacao.MSG_ERRO.equals(res)) {
							log.info("Certificado Revogado");
							throw new Exception(tabResVal.getDescricao(TabeliaoResultadoValidacao.VAL_LCR));
						}						
						log.info("Verificando se a lista está atualizada.");
						if(TabeliaoResultadoValidacao.MSG_AVISO.equals(res)) {
							log.info("Aviso na LCR");
							throw new Exception(tabResVal.getDescricao(TabeliaoResultadoValidacao.VAL_LCR));
						}
						
					} else {
						log.info("Nao está verificando se o certificado esta revogado.");
					}
				} catch (Exception e) {
					throw new SentinelaException("Erro ao realizar a autenticacao.\n" +
												 "Problema encontrado ao validar LCR.", e);
				}
				
				//Verifica a validade do certificado
				try {
					String valida = validaCertificadoExpirado;
					String res = tabResVal.getMensagem(TabeliaoResultadoValidacao.VAL_VALIDADE);
					if("1".equals(valida)) {
						log.info("Verificando a data de validade do certificado.");
						if(TabeliaoResultadoValidacao.MSG_ERRO.equals(res)) {
							log.info("Certificado Vencido");
							throw new Exception(tabResVal.getDescricao(TabeliaoResultadoValidacao.VAL_VALIDADE));
						}
						if(TabeliaoResultadoValidacao.MSG_EXCECAO.equals(res)) {
							log.info("Exceção na validade");
							throw new Exception(tabResVal.getDescricao(TabeliaoResultadoValidacao.VAL_VALIDADE));
						}
						// 				
//						if(TabeliaoResultadoValidacao.MSG_AVISO.equals(res)) {
//							httpRequest.setAttribute(Constants.TABELIAO_MENSAGEM_AVISO, tabResVal.getDescricao(TabeliaoResultadoValidacao.VAL_VALIDADE));
//						}

					} else {
						log.info("Nao está verificando a data de validade do certificado.");
					}

				} catch (Exception e) {
					throw new SentinelaException("Erro ao realizar a autenticacao.\nEste certificado está com a validade expirada.", e);
				}

				//Verifica se o tipo de certificado utilizado esta dentro do esperado
				try {
					String valida = validaNivelAcesso;
					if("1".equals(valida)) {
						log.info("Verificando se o tipo do certificado pode ser utilizado.");
						String tipoCertificado = tc.getTipoCertificado();
						String tipoCertificadoApp = nivelAcesso;
						
						if(tipoCertificadoApp == null || tipoCertificado.equals("")) {
							tipoCertificadoApp = "A3";
						}						
						if(tipoCertificadoApp.length() != 2) {
							throw new Exception("Tipo de certificado inválido na aplicação.");
						}						
						if(tipoCertificado.charAt(0) != tipoCertificadoApp.charAt(0)) {
							throw new Exception("Tipo de certificado não compatível com a aplicação.");
						}					
						if(tipoCertificado.charAt(1) < tipoCertificadoApp.charAt(1)) {
							throw new Exception("Tipo de certificado do usuário '" + tipoCertificado + "' não compatível com o esperado '" + tipoCertificadoApp + "' ou superior.");						
						}
					} else {
						log.info("Não está verificando se o tipo do certificado pode ser utilizado.");
					}
					
				} catch (Exception e) {
					throw new SentinelaException("Erro ao realizar a autenticação.\n" + e.getMessage(), e);
				}

				//Grava o certificado publico no tabeliao
				log.info("Verificando o certificado publico para incluir no banco de dados.");
				try {
					CertificadoAc certificadoAc = CertificadoFacade.buscarCertificadoAcPorKeyId(tc.getAuthorityKeyIdentifier());
					
					CertificadoPublico cp = CertificadoFacade.buscarCertificadoPublico(certificadoAc, tc.getSerialNumber());
					
					if(cp == null){

						log.info("Este certificado nao existe. Incluindo no banco de dados.");
						
						cp = new CertificadoPublico();
						
						String pem = X509Cert.getCertificateAsPem(cert);
						
						cp.setArquivo(pem.getBytes());
						
						CertificadoFacade.inserirCertificadoPublico(cp);

						log.info("Certificado incluido com sucesso.");
					} else {
						log.info("Certificado já existe, pulando o passo de incluir certificado.");
					}
				} catch (Exception e) {
					System.err.println("Não foi possível inserir o certificado público '" + cert.getSubjectDN().getName() + "' no banco de dados.\n" +
							"Motivo: " + e.getMessage());
				}				
                if (! tc.hasDadosPF()) {
                	throw new SentinelaException("O certificado do usuário não possui dados de Pessoa Física.");
                }
                usuarioTabeliao = new UsuarioTabeliao(
                        tc.getNome(),
                        tc.getEmail(),
                        tc.getTabeliaoDadosPF().getCPF());
               
                log.info("Usuario autenticado.");
            }               
        } catch (CMSException e) {
            throw new SentinelaException("Erro ao autenticar usuario.",e);             
		} catch (NoSuchAlgorithmException e) {
            throw new SentinelaException("Erro ao autenticar usuario.",e);             
		} catch (NoSuchProviderException e) {
            throw new SentinelaException("Erro ao autenticar usuario.",e);             
		} catch (CertStoreException e) {
            throw new SentinelaException("Erro ao autenticar usuario.",e);
		} catch (Exception e) {
			throw new SentinelaException("Erro ao autenticar usuario.",e);
		}
        return usuarioTabeliao;
	}
    
    /**
     * Inicializa com as informacoes contidas no arquivo de configuracao do sentinela.
     * @param sentinelaXml
     * @see gov.pr.celepar.sentinela.client.SentinelaXml
     * @throws SentinelaException
     */
    @Override
	public void inicializar(SentinelaXml sentinelaXml) throws SentinelaException {
		log.info("Chamando metodo inicializar AutenticacaoTabeliao");

		this.nivelAcesso = sentinelaXml.recupera("tabeliao.client.nivel.acesso");
        this.validaCadeiaCertificado = sentinelaXml.recupera("tabeliao.client.valida.cadeia.certificado");
        this.validaLCR = sentinelaXml.recupera("tabeliao.client.valida.LCR");
        this.validaCertificadoExpirado = sentinelaXml.recupera("tabeliao.client.valida.certificado.expirado");
        this.validaNivelAcesso = sentinelaXml.recupera("tabeliao.client.valida.nivel.acesso");
        LCRTempo = sentinelaXml.recupera("tabeliao.client.lcr.tempo");
        LCRProxy = sentinelaXml.recupera("tabeliao.client.lcr.proxy");
        LCRProxyUsuario = sentinelaXml.recupera("tabeliao.client.lcr.proxy.usuario");
        LCRProxySenha = sentinelaXml.recupera("tabeliao.client.lcr.proxy.senha");
        LCRProxyPorta = sentinelaXml.recupera("tabeliao.client.lcr.proxy.porta");
        diasMensagemExpirando = sentinelaXml.recupera("tabeliao.client.dias.mensagem.expirando");
	}
	
	/**
	 * Nao permitido por estar utilizando Certificado Digital
	 */
	@Override
	public boolean permitirTrocarSenha() {
		return false;
	}

	/**
	 * Nao permitido por estar utilizando Certificado Digital
	 */
	@Override
	public boolean permitirEnvioNovaSenha() {
		return false;
	}
	
	/**
	 *  Nao permitido por estar utilizando Certificado Digital
	 */
	@Override
	public String enviarNovaSenha(String arg0, String arg1, String arg2,
			long arg3, long arg4) throws SentinelaException {
					throw new SentinelaException("Funcionalidade não disponível, com uso do TABELIÃO!");
	}

	/**
	 * Nao permitido por estar utilizando Certificado Digital
	 */
	@Override
	public boolean trocarSenha(String arg0, String arg1, String arg2,
			String arg3, long arg4, long arg5) throws SentinelaException {
		throw new SentinelaException("Funcionalidade não disponível, com uso do TABELIÃO!");
	}
}
