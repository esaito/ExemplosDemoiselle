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
import java.security.MessageDigest;

/**
 * Classe utilitaria para codificacao e decodificacao em base64 de dados binarios
 * 
 * @author Thiago Meneghello - GIC/CELEPAR
 */
public class HashUtil {

	/**
	 * Gera resumo em algoritimo SHA1
	 * @param data -> array de byte
	 * @return array de byte com o resumo gerado.
	 */
	public static byte[] sha1(byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
