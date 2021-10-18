package br.com.fnbrandao.artigo_job;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(indexes = {
		@Index(columnList = "processed"),
		@Index(columnList = "retries, creationDate, id")
})
public class OutboxEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OUTBOX_SEQ")
	@SequenceGenerator(sequenceName = "OUTBOX_SEQ", allocationSize = 1, name = "OUTBOX_SEQ")
	private Long id;

	@Column
	private boolean processed = false;

	@Column
	private int retries = 0;

	@Column
	@Temporal(TemporalType.DATE)
	private Date creationDate = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
