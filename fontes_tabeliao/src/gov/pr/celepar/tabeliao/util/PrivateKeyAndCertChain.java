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
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Estrutura de dados que ira deter o par de chaves e 
 * a cadeia de certificacao correspondente as chaves.
 * @author Thiago Meneghello - GIC/CELEPAR
 */
public class PrivateKeyAndCertChain {
    public PrivateKey mPrivateKey;
    public Certificate[] mCertificationChain;
}
