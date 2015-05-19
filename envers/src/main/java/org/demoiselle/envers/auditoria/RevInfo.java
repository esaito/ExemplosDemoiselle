package org.demoiselle.envers.auditoria;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.envers.*;

@Entity
@RevisionEntity(RevInfoListener.class)
@SequenceGenerator(name="revInfo_seq", sequenceName="revInfo_seq", allocationSize=1)
public class RevInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@RevisionNumber
	private Long codRev;

	@RevisionTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRevisao;
	
	private String ip;
	
	
	public RevInfo(Date dataRevisao, String nomeUsuario, String ip) {
		super();
		this.dataRevisao = dataRevisao;
		this.ip = ip;
	}

	public RevInfo() {
		super();
	}

	public Long getCodRev() {
		return codRev;
	}

	public void setCodRev(Long codRev) {
		this.codRev = codRev;
	}

	public Date getDataRevisao() {
		return dataRevisao;
	}

	public void setDataRevisao(Date dataRevisao) {
		this.dataRevisao = dataRevisao;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String toString() {
		return "DefaultRevisionEntity(codRev = " + codRev + ", revisionDate = "
				+ DateFormat.getDateTimeInstance().format(this.getDataRevisao()) + ")";
	}

}
