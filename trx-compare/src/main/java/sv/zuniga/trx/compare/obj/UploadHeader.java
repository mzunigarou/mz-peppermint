package sv.zuniga.trx.compare.obj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * An {@link Entity} representing the header of a CSV file.
 * 
 * @author mzuniga
 *
 */
@Entity
@Table(name = "upload_header")
public class UploadHeader {
	private Long uhId;
	private String uhName;
	private String uhDate;

	public UploadHeader() {
	}

	public UploadHeader(Long uhId) {
		this.uhId = uhId;
	}

	public UploadHeader(Long uhId, String uhName, String uhDate) {
		this.uhId = uhId;
		this.uhName = uhName;
		this.uhDate = uhDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getUhId() {
		return uhId;
	}

	public void setUhId(Long uhId) {
		this.uhId = uhId;
	}

	@Column(name = "uh_name", nullable = false)
	public String getUhName() {
		return uhName;
	}

	public void setUhName(String uhName) {
		this.uhName = uhName;
	}

	@Column(name = "uh_date", nullable = false)
	public String getUhDate() {
		return uhDate;
	}

	public void setUhDate(String uhDate) {
		this.uhDate = uhDate;
	}

}
