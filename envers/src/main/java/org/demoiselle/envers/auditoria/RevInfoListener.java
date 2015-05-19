package org.demoiselle.envers.auditoria;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.envers.RevisionListener;

public class RevInfoListener implements RevisionListener {
		
		public void newRevision(Object revisionEntity) {
			String remoteIp = "";
			try {
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
				remoteIp = request.getRemoteAddr();	
			} catch (Exception e) {
				remoteIp = "127.0.0.1";
			}
			
			RevInfo revEntity = (RevInfo) revisionEntity;
			
			System.out.println("timestamp : " + revEntity.getDataRevisao());
			System.out.println("RemoteIp  : " + remoteIp);

			revEntity.setIp(remoteIp);
			
		}
}
