package org.demoiselle.envers.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity
@Table(name="TB_Estudante")
@Audited
@AuditTable(value="TB_Estudante_Auditoria")
@SequenceGenerator(name="estudante_seq", sequenceName="estudante_seq", allocationSize=1)
public class Estudante extends Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
    @Temporal(value=TemporalType.DATE)
    private Date dataMatricula;
	
	@Column
	private Integer numeroMatricula;
	
	public Estudante(){
		super();
	}

	public Estudante(Date dataMatricula, Integer numeroMatricula, Long cpf, String nome,
				Date dataNascimento, Genero genero) {
		super(cpf, nome, dataNascimento, genero);
		this.dataMatricula = dataMatricula;
		this.numeroMatricula = numeroMatricula;
	}
		

	public Date getDataMatricula() {
		return dataMatricula;
	}

	public void setDataMatricula(Date dataMatricula) {
		this.dataMatricula = dataMatricula;
	}

	public Integer getNumeroMatricula() {
		return numeroMatricula;
	}

	public void setNumeroMatricula(Integer numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dataMatricula == null) ? 0 : dataMatricula.hashCode());
		result = prime * result
				+ ((numeroMatricula == null) ? 0 : numeroMatricula.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estudante other = (Estudante) obj;
		if (dataMatricula == null) {
			if (other.dataMatricula != null)
				return false;
		} else if (!dataMatricula.equals(other.dataMatricula))
			return false;
		if (numeroMatricula == null) {
			if (other.numeroMatricula != null)
				return false;
		} else if (!numeroMatricula.equals(other.numeroMatricula))
			return false;
		return true;
	}
	
	
	
	
}