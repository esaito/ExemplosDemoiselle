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
import gov.pr.celepar.tabeliao.client.autenticacao.AutenticacaoTabeliao;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.SocketTimeoutException;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 * Classe utilitaria para download de LCR (Lista de Certificados Revogados) 
 * O metodos permitem tambem a execucao atraves de proxy (com ou sem autenticacao)
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 * @author Emerson Sachio Saito - GIC/CELEPAR
 *
 */
public class Download {
	
	private static Logger log = Logger.getLogger(Download.class);
	private static boolean inicializou = false;
	
	private Download(){
		
	}
	
	/**
	 * Inicializar as configuracoes de proxy com uso de arquivo do SENTINELA
	 * Este metodo interfere nas configurações globais do servidor 
	 * 
	 * @see fazDownload
	 */
	private static void inicializa(){
		if(!inicializou){
			
			final String portaProxy = AutenticacaoTabeliao.getLCRProxyPorta();
			final String proxy = AutenticacaoTabeliao.getLCRProxy();
			System.getProperties().put("http.proxySet", "true");
			System.getProperties().put("http.proxyHost", proxy);
			System.getProperties().put("http.proxyPort", portaProxy);
			inicializou = true;
			log.info("Modificou propriedades do sistema: " +proxy);
		}
	}
	
	/**
	 * Inicializar as configuracoes de proxy, para sistemas NAO integrados com sentinela
	 * Este metodo interfere nas configurações globais do servidor
	 * @see fazDownload
	 */
	private static void inicializa(String proxy, String proxyPorta){
		if(!inicializou){
			System.getProperties().put("http.proxySet", "true");
			System.getProperties().put("http.proxyHost", proxy);
			System.getProperties().put("http.proxyPort", proxyPorta);
			inicializou = true;
			log.info("Modificou propriedades do sistema: " +proxy);
		}
	}
	
	/**
	 * Executa o download, precisa da integracao com SENTINELA para uso de proxy (configurações globais do servidor)
	 * @param url -> endereco da LCR
	 * @return InputStream do arquivo de LCR
	 * @throws IOException
	 * @see fazDownload
	 */
	public static InputStream download(String url){
	
		final String usuarioProxy = AutenticacaoTabeliao.getLCRProxyUsuario();
		final String senhaProxy = AutenticacaoTabeliao.getLCRProxySenha();
		final String proxy = AutenticacaoTabeliao.getLCRProxy();
		
		InputStream isRetorno;
		//log.debug("Achou Proxy em download: " + proxy);
		if (proxy == null)
		{
			System.getProperties().put("http.proxySet", "false");
			log.debug("Sem Proxy!");			
		} else {
			inicializa();
			//log.debug("Com Proxy!");			
		}
		
		// codigo utilizado por causa do proxy0.celepar.parana
		if(usuarioProxy != null && senhaProxy != null) {
		    Authenticator.setDefault(new Authenticator() {
			      protected PasswordAuthentication getPasswordAuthentication() {
			        return new
			        PasswordAuthentication(usuarioProxy,senhaProxy.toCharArray());
			    }});
		    //log.debug("Com Autenticacao!");			
		}else{
			log.debug("Sem Autenticacao!");
		}
		java.net.HttpURLConnection con=null;
		try {
			URL http = new URL(url);
			con = (java.net.HttpURLConnection) http.openConnection();
			con.setConnectTimeout(30 * 1000); // tempo de timeout de conexao = 30 segundos
			con.setReadTimeout(120 * 1000); // tempo de timeout de leitura = 2 minutos
			//log.info("Resposta do servidor:"+con.getResponseCode());
			isRetorno = con.getInputStream();			
		}catch (SocketTimeoutException e1){
			log.error("Tempo excedido."+e1);
			isRetorno = null;
		}catch (MalformedURLException e) {
			log.error("Problema de Url."+e);
			isRetorno = null;
		} catch (IOException e) {
			log.error("Problemas de IO."+e);
			isRetorno = null;
		}
		return isRetorno;
		/* codigo utilizado com proxy.pr.gov.br 
		URL http = new URL(url);
		URLConnection conn = http.openConnection();
		
		if(usuarioProxy != null && senhaProxy != null) {
			Base64Encoder b64 = new Base64Encoder(usuarioProxy + ":" + senhaProxy);
			conn.setRequestProperty("Proxy-Authorization", "Basic " + b64.processString());
		}
		return conn.getInputStream();
		*/
	}
	/**
	 * Executa o download, NAO precisa estar integrado com o sentinela. 
	 * Se precisar utilizar proxy havera alteracao nas configuracoes globais do servidor
	 * @param url -> endereco da LCR
	 * @param proxy -> endereco de proxy
	 * @param proxyUser -> usuario do proxy
	 * @param proxyPass -> senha do proxy
	 * @return InputStream do arquivo de LCR
	 * @throws IOException
	 * @see fazDownload
	 */
	public static InputStream download(String url, String proxy, String proxyPorta, String proxyUser, String proxyPass){
		
		final String usuarioProxy = proxyUser;
		final String senhaProxy = proxyPass;
		
		InputStream isRetorno;
		
		if (proxy == null || proxy.length() == 0)
		{
			System.getProperties().put("http.timeout", 300000);
			System.getProperties().put("http.proxySet", "false");
			log.debug("Sem Proxy!");
		} else {
			inicializa(proxy, proxyPorta);
		//	log.debug("Com Proxy!");
		}
		if(usuarioProxy != null && senhaProxy != null) {
		    Authenticator.setDefault(new Authenticator() {
			      protected PasswordAuthentication getPasswordAuthentication() {
			        return new
			        PasswordAuthentication(usuarioProxy,senhaProxy.toCharArray());
			    }});
			}
		else{
			log.debug("Sem Autenticacao");
		}
		java.net.HttpURLConnection con = null;
		try {
			URL http = new URL(url);
			con = (java.net.HttpURLConnection) http.openConnection();
			con.setConnectTimeout(30 * 1000); // tempo de timeout de conexao = 10 segundos
			con.setReadTimeout(120 * 1000); // tempo de timeout de leitura = 2 minutos
			//log.info("Resposta do servidor:"+con.getResponseCode());
			isRetorno = con.getInputStream();
		}catch (SocketTimeoutException e1){
			log.error("Tempo excedido."+e1);
			isRetorno =  null;
		}catch (MalformedURLException e) {
			log.error("Problema de Url."+e);
			isRetorno = null;
		} catch (IOException e) {
			log.error("Problemas de IO."+e);
			isRetorno = null;
		}
	    return isRetorno;
	    /* codigo utilizado com proxy.pr.gov.br
		URL http = new URL(url);
		URLConnection conn = http.openConnection();
		
		if(usuarioProxy != null && senhaProxy != null) {
			Base64Encoder b64 = new Base64Encoder(usuarioProxy + ":" + senhaProxy);
			conn.setRequestProperty("Proxy-Authorization", "Basic " + b64.processString());
		}
		return conn.getInputStream();
		*/
	}
	
	/**
	 * Executa o download, COM integracao com o uso do Sentinela
	 * Este metodo nao interfere nas configuracoes globais do sistema, porem depende do uso da VM da SUN
	 * @param url -> endereco da LCR
	 * @return InputStream do arquivo de LCR
	 * @throws IOException
	 */
	public static InputStream downloadSun(String url){
	
		final String usuarioProxy = AutenticacaoTabeliao.getLCRProxyUsuario();
		final String senhaProxy = AutenticacaoTabeliao.getLCRProxySenha();
		final String proxy = AutenticacaoTabeliao.getLCRProxy();
		final int numPorta = Integer.parseInt(AutenticacaoTabeliao.getLCRProxyPorta());
		
		InputStream isRetorno;
		//log.debug("Achou Proxy em download: " + proxy);
		
		// codigo utilizado por causa do proxy0.celepar.parana
		if(usuarioProxy != null && senhaProxy != null) {
		    Authenticator.setDefault(new Authenticator() {
			      protected PasswordAuthentication getPasswordAuthentication() {
			        return new
			        PasswordAuthentication(usuarioProxy,senhaProxy.toCharArray());
			    }});
		    //log.debug("Com Autenticacao!");			
		}else{
			log.debug("Sem Autenticacao!");
		}
		sun.net.www.protocol.http.HttpURLConnection con=null;
		try {
			URL http = new URL(url);
			con = new sun.net.www.protocol.http.HttpURLConnection(http, proxy, numPorta);
			con.setConnectTimeout(30 * 1000); // tempo de timeout de conexao = 30 segundos
			con.setReadTimeout(120 * 1000); // tempo de timeout de leitura = 2 minutos
			//log.info("Resposta do servidor:"+con.getResponseCode());
			isRetorno = con.getInputStream();			
		}catch (SocketTimeoutException e1){
			log.error("Tempo excedido."+e1);
			isRetorno = null;
		}catch (MalformedURLException e) {
			log.error("Problema de Url."+e);
			isRetorno = null;
		} catch (IOException e) {
			log.error("Problemas de IO."+e);
			isRetorno = null;
		}
		return isRetorno;
	}
	
	/**
	 * Executa o download, SEM uso de arquivo de configuracao.
	 * Este metodo nao interfere nas configuracoes globais do sistema, porem depende do uso da VM da SUN 
	 * @param url -> endereco da LCR
	 * @param proxy -> endereco de proxy
	 * @param proxyUser -> usuario do proxy
	 * @param proxyPass -> senha do proxy
	 * @return InputStream do arquivo de LCR
	 * @throws IOException
	 */
	public static InputStream downloadSun(String url, String proxy, String proxyPorta, String proxyUser, String proxyPass){
		
		final String usuarioProxy = proxyUser;
		final String senhaProxy = proxyPass;
		final int numPorta = Integer.parseInt(proxyPorta);
		
		InputStream isRetorno;

		if(usuarioProxy != null && senhaProxy != null) {
		    Authenticator.setDefault(new Authenticator() {
			      protected PasswordAuthentication getPasswordAuthentication() {
			        return new
			        PasswordAuthentication(usuarioProxy,senhaProxy.toCharArray());
			    }});
			}
		else{
			log.debug("Sem Autenticacao");
		}
		sun.net.www.protocol.http.HttpURLConnection con = null;
		try {
			URL http = new URL(url);
			con = new sun.net.www.protocol.http.HttpURLConnection(http, proxy, numPorta);
			con.setConnectTimeout(30 * 1000); // tempo de timeout de conexao = 10 segundos
			con.setReadTimeout(120 * 1000); // tempo de timeout de leitura = 2 minutos
			//log.info("Resposta do servidor:"+con.getResponseCode());
			isRetorno = con.getInputStream();
		}catch (SocketTimeoutException e1){
			log.error("Tempo excedido."+e1);
			isRetorno =  null;
		}catch (MalformedURLException e) {
			log.error("Problema de Url."+e);
			isRetorno = null;
		} catch (IOException e) {
			log.error("Problemas de IO."+e);
			isRetorno = null;
		}
	    return isRetorno;
	}
	
	/**
	 * Chamada para os procedimentos do download
	 * Este metodo depende da integracao com o Sentinela
	 * Se NAO for VM da SUN, ira interferir nas configuracoes globais do servidor
	 * @param url -> endereco da LCR
	 * @return array de bytes do arquivo de LCR
	 * @throws IOException
	 */
	public static byte[] downloadByteArray(String url) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int tmp;
		InputStream is = null;
		
		String jvmVendor = System.getProperty("java.vm.vendor");
		if (jvmVendor.toLowerCase().contains("sun")){
			is = downloadSun(url);
        }else{
        	is = download(url);
        }
		
		if (is != null){
			while((tmp = is.read(buffer)) != -1) {
				bos.write(buffer, 0, tmp);
			}			
			buffer = bos.toByteArray();			
			is.close();
			bos.close();
			//log.info("Baixou:"+buffer.length);
		}else{
			log.error("download vazio");
		}
		return buffer;
	}
	
	/**
	 * Chamada para os procedimentos do download
	 * Download de LCR sem interacao com o Sentinela 
	 * Se for VM da SUN, Nao ira interferir nas configuracoes globais do servidor
	 * 
	 * @param url -> endereco da LCR
	 * @param proxy -> endereco do proxy
	 * @param proxyUser -> usuario do proxy
	 * @param proxyPass -> senha do proxy
	 * @return array de bytes do arquivo de LCR
	 * @throws IOException
	 */
	public static byte[] downloadByteArray(String url, String proxy, String proxyPorta, String proxyUser, String proxyPass) throws IOException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int tmp;
		InputStream is = null;
		
		String jvmVendor = System.getProperty("java.vm.vendor");
		if (jvmVendor.toLowerCase().contains("sun")){
			is = downloadSun(url, proxy, proxyPorta, proxyUser, proxyPass);
        }else{
        	is = download(url, proxy, proxyPorta, proxyUser, proxyPass);
        }
		
		if (is != null){
			while((tmp = is.read(buffer)) != -1) {
				bos.write(buffer, 0, tmp);
			}			
			buffer = bos.toByteArray();
			is.close();
			bos.close();			
			//log.info("Baixou:"+buffer.length);
		}
		else{
			//System.out.println("download vazio");
			log.error("download vazio");
		}		
		return buffer;
	}
}