package org.demoiselle.envers.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Table(name="TB_Estudante_Auditoria")
@IdClass(EstudanteAuditoriaID.class)
@Entity
public class EstudanteAuditoria implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Id
	private Long rev;
	
	private Long cpf;

	private String nome;
	
	@Temporal(value = TemporalType.DATE)
	private Date dataNascimento;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private Genero genero;
	
    @Temporal(value=TemporalType.DATE)
    private Date dataMatricula;
	
	@Column
	private Integer numeroMatricula;

	public EstudanteAuditoria() {
		super();
	}

	public EstudanteAuditoria(Long id, Long rev, Long cpf, String nome,
			Date dataNascimento, Genero genero, Date dataMatricula,
			Integer numeroMatricula) {
		super();
		this.id = id;
		this.rev = rev;
		this.cpf = cpf;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.genero = genero;
		this.dataMatricula = dataMatricula;
		this.numeroMatricula = numeroMatricula;
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

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
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

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
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
	
	
	

}
