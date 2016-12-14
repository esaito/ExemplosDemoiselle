package org.demoiselle.artigoJM.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.frameworkdemoiselle.validation.annotation.TituloEleitor;

@Entity
public class Assinante implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	//@SequenceGenerator(name = "assinante_sequence", sequenceName = "assinante_sequence", allocationSize = 1, initialValue = 0)
	//@GeneratedValue(strategy = SEQUENCE, generator="assinante_sequence")
	@GeneratedValue
	private Long codigoAssinante;
	
	@Column(nullable=false,  length=255)
    private String nome;
    
    @Column(nullable=false)
    @Temporal(value=TemporalType.DATE)
    private Date dataNascimento;
    
	@TituloEleitor
	private String tituloEleitor;
    
    @Enumerated(EnumType.STRING)
	private Genero genero;
    
    //@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true) // OK no eclipselink
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY) 
	@JoinColumn(name = "assinante_fk")
    private List<Assinatura> assinaturas = new ArrayList<Assinatura>();

    public Assinante() {
		super();
	}

	public Assinante(String nome, Date dataNascimento, String tituloEleitor,
			Genero genero, List<Assinatura> assinaturas) {
		super();
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.tituloEleitor = tituloEleitor;
		this.genero = genero;
		this.assinaturas = assinaturas;
	}

	public Long getCodigoAssinante() {
		return codigoAssinante;
	}

	public void setCodigoAssinante(Long codigoAssinante) {
		this.codigoAssinante = codigoAssinante;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
		
	public String getTituloEleitor() {
		return tituloEleitor;
	}

	public void setTituloEleitor(String tituloEleitor) {
		this.tituloEleitor = tituloEleitor;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public List<Assinatura> getAssinaturas() {
		return assinaturas;
	}

	public void setAssinaturas(List<Assinatura> assinaturas) {
		this.assinaturas = assinaturas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assinaturas == null) ? 0 : assinaturas.hashCode());
		result = prime * result
				+ ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((genero == null) ? 0 : genero.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((tituloEleitor == null) ? 0 : tituloEleitor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Assinante)) {
			return false;
		}
		Assinante other = (Assinante) obj;
		if (assinaturas == null) {
			if (other.assinaturas != null) {
				return false;
			}
		} else if (!assinaturas.equals(other.assinaturas)) {
			return false;
		}
		if (dataNascimento == null) {
			if (other.dataNascimento != null) {
				return false;
			}
		} else if (!dataNascimento.equals(other.dataNascimento)) {
			return false;
		}
		if (genero != other.genero) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		if (tituloEleitor == null) {
			if (other.tituloEleitor != null) {
				return false;
			}
		} else if (!tituloEleitor.equals(other.tituloEleitor)) {
			return false;
		}
		return true;
	}   
}