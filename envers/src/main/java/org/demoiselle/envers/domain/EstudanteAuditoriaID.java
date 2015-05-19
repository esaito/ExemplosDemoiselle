package org.demoiselle.envers.domain;

import java.io.Serializable;

public class EstudanteAuditoriaID implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	private Long rev;
			
	public EstudanteAuditoriaID() {
		super();
	}
	public EstudanteAuditoriaID(Long id, Long rev) {
		super();
		this.id = id;
		this.rev = rev;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRev() {
		return rev;
	}
	public void setRev(Long rev) {
		this.rev = rev;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((rev == null) ? 0 : rev.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EstudanteAuditoriaID other = (EstudanteAuditoriaID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (rev == null) {
			if (other.rev != null)
				return false;
		} else if (!rev.equals(other.rev))
			return false;
		return true;
	}
	
	
	

}
