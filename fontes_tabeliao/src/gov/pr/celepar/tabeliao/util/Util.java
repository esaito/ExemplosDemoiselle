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
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Classe utilitaria com funcoes de conversoes 
 * 
 * @version 1.0
 * @author Thiago Meneghello - GIC/CELEPAR
 *
 */
public class Util {

	private static final String VALUE_SEPARATOR = ";";
	private static final String CHARSET_NAME = "ISO-8859-1";
	
	/**
	 * Codifica para base64
	 * @param data -> array de string
	 * @return array de string
	 */
	public static String[] encodeBase64(String[] data) {
		String[] ret = new String[data.length];
		
		try {
			for(int i=0 ; i<data.length ; i++) {
				ret[i] = Base64Utils.base64Encode(data[i].getBytes(CHARSET_NAME));
			}
		} catch (UnsupportedEncodingException e) {
			//Não deve entrar aqui...
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Decodifica a partir da Base64
	 * @param data -> array de string em base64
	 * @return array de string
	 */
	public static String[] decodeBase64(String[] data) {
		String[] ret = new String[data.length];
		
		try {
			for(int i=0 ; i<data.length ; i++) {
				ret[i] = new String(Base64Utils.base64Decode(data[i]), CHARSET_NAME);
			}
		} catch (UnsupportedEncodingException e) {
			//Não deve entrar aqui...
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Converte array de string para string
	 * @param a -> array de string
	 * @return string
	 */
	public static String arrayToString(String[] a) {
		StringBuffer sb = new StringBuffer();
		for(String s : a) {
			if(sb.length() > 0) {
				sb.append(VALUE_SEPARATOR);
			}
			sb.append(s);
		}
		return sb.toString();
	}
	
	/**
	 * Converte de string para array de string
	 * @param s -> string
	 * @return array de string
	 */
	public static String[] stringToArray(String s) {
		return s.split(VALUE_SEPARATOR);
	}
	
	/**
	 * Converte de array de byte para string
	 * @param data -> array de byte
	 * @return string
	 */
	public static String toString(byte[] data) {
		return toString(new BigInteger(1, data));
	}
	
	/**
	 * Converte de BigInteger para String
	 * @param bi -> BigInteger
	 * @return string
	 */
	public static String toString(BigInteger bi) {
		return bi.toString(16);
	}
	
	/**
	 * Funcao auxiliar mostrar o conteudo em hexa e string 
	 * @param data -> array de byte
	 */
	public static void debug(byte[] data) {
		if(data == null) {
			data = new byte[0];
		}
		
		String buffer = "";
		for(int i=0 ; i<data.length ; i++) {
			if((i % 16) == 0){
				if(i > 0) {
					buffer = buffer.replaceAll("[^a-z^A-Z^0-9]", ".");
					
					System.out.println("  " + buffer);
					buffer = "";
				}
				
				String hex = Integer.toHexString(i);
				
				while(hex.length() < 4) {
					hex = "0" + hex;
				}
				
				System.out.print(hex + ": ");
			}

			String hex = Integer.toHexString(data[i] & 0xFF);
			if(hex.length() == 1) {
				hex = "0" + hex;
			}
			System.out.print(hex + " ");
			
			buffer += new String(data, i, 1);
		}
		
		if(buffer.length() > 0) {
			
			for(int i=buffer.length() ; i<16 ; i++) {
				System.out.print("   ");
			}

			buffer = buffer.replaceAll("[^a-z^A-Z^0-9]", ".");
			
			System.out.println("  " + buffer);
		} else {
			System.out.println("");
		}
		
	}
	/**
	 * Executa a funcao auxiliar debug desta classe, para um arquivo informado no parametro
	 * @param file -> localizacao do arquivo.
	 */
	public static void debugFile(String file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] bufferIn = new byte[(int) fis.getChannel().size()];
			fis.read(bufferIn);
			Util.debug(bufferIn);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}