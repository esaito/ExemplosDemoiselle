package br.gov.serpro.inscricao.security;

import javax.inject.Inject;

import br.gov.frameworkdemoiselle.security.AuthenticationException;
import br.gov.frameworkdemoiselle.security.Authorizer;
import br.gov.frameworkdemoiselle.security.SecurityContext;

public class Autorizador implements Authorizer {

	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityContext securityContext;

	@Override 
        public boolean hasPermission(String res, String op) throws Exception {
    	   boolean autorizado = false;
    	   
   		if (securityContext != null)
   		{
   		  try{
   			if (securityContext.isLoggedIn()){    	  
       		    String usr = securityContext.getUser().getName();
    		   if (usr.equals("secretaria") && res.equals("aluno") && (op.equals("consultar")||op.equals("matricular"))) {
    			   autorizado = true;
    		   }
    		   else {
   				throw new AuthenticationException("usuarioNaoAutenticado");
   				}
   			}
   		  }catch (Exception e) {
			  throw new AuthenticationException("usuarioNaoAutenticado");
			  //throw new AuthorizationException(e.getMessage());
		  }
		}else{
			throw new AuthenticationException("usuarioNaoAutenticado");		
   		}
   		return autorizado;
	}
	@Override
	public boolean hasRole(String rol) throws Exception {
		return true;
	}
}
