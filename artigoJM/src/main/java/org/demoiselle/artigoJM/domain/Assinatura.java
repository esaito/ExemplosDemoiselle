package org.demoiselle.artigoJM.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Assinatura implements Serializable {	
	
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name = "assinatura_sequence", sequenceName = "assinatura_sequence", allocationSize = 1, initialValue = 0)
	//@GeneratedValue(strategy = SEQUENCE, generator="assinatura_sequence")
   @GeneratedValue
	private Long codigoAssinatura;
	
	@Column(nullable=false,  length=255)
    private String nomeRevista;
	
	@Column(nullable=false)
    @Temporal(value=TemporalType.DATE)
    private Date dataInicio;
    
    @Temporal(value=TemporalType.DATE)
    private Date dataFim;
    
    @ManyToOne (cascade=CascadeType.ALL)     
	@JoinColumn (name= "assinante_fk")
	private Assinante assinante;
	
    public Assinatura() {
		super();
	}
    
    public Assinatura(String nomeRevista, Date dataInicio, Date dataFim,
			Assinante assinante) {
		super();
		this.nomeRevista = nomeRevista;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.assinante = assinante;
	}



	public Long getCodigoAssinatura() {
		return codigoAssinatura;
	}

	public void setCodigoAssinatura(Long codigoAssinatura) {
		this.codigoAssinatura = codigoAssinatura;
	}

	public String getNomeRevista() {
		return nomeRevista;
	}

	public void setNomeRevista(String nomeRevista) {
		this.nomeRevista = nomeRevista;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Assinante getAssinante() {
		return assinante;
	}

	public void setAssinante(Assinante assinante) {
		this.assinante = assinante;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((assinante == null) ? 0 : assinante.hashCode());
		result = prime * result + ((dataFim == null) ? 0 : dataFim.hashCode());
		result = prime * result
				+ ((dataInicio == null) ? 0 : dataInicio.hashCode());
		result = prime * result
				+ ((nomeRevista == null) ? 0 : nomeRevista.hashCode());
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
		if (!(obj instanceof Assinatura)) {
			return false;
		}
		Assinatura other = (Assinatura) obj;
		if (assinante == null) {
			if (other.assinante != null) {
				return false;
			}
		} else if (!assinante.equals(other.assinante)) {
			return false;
		}
		if (dataFim == null) {
			if (other.dataFim != null) {
				return false;
			}
		} else if (!dataFim.equals(other.dataFim)) {
			return false;
		}
		if (dataInicio == null) {
			if (other.dataInicio != null) {
				return false;
			}
		} else if (!dataInicio.equals(other.dataInicio)) {
			return false;
		}
		if (nomeRevista == null) {
			if (other.nomeRevista != null) {
				return false;
			}
		} else if (!nomeRevista.equals(other.nomeRevista)) {
			return false;
		}
		return true;
	}

	
}
