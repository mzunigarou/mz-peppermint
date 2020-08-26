package sv.zuniga.trx.compare.obj;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * An {@link Entity} representing the most valuable fields of a CSV file.
 * 
 * @author mzuniga
 *
 */
@Entity
@Table(name = "upload_detail")
public class UploadDetail {
	private Long udId;
	private String udTransactionDate;
	private String udTransactionAmount;
	private String udTransactionId;
	private String udTransactionDescription;
	private String udWalletReference;
	private UploadHeader uploadHeader;

	public UploadDetail() {
	}

	public UploadDetail(Long udId, String udTransactionDate, String udTransactionAmount, String udTransactionId, String udTransactionDescription, String udWalletReference, UploadHeader uploadHeader) {
		this.udId = udId;
		this.udTransactionDate = udTransactionDate;
		this.udTransactionAmount = udTransactionAmount;
		this.udTransactionId = udTransactionId;
		this.udTransactionDescription = udTransactionDescription;
		this.udWalletReference = udWalletReference;
		this.uploadHeader = uploadHeader;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getUdId() {
		return udId;
	}

	public void setUdId(Long udId) {
		this.udId = udId;
	}

	@Column(name = "ud_transaction_date", nullable = false)
	public String getUdTransactionDate() {
		return udTransactionDate;
	}

	public void setUdTransactionDate(String udTransactionDate) {
		this.udTransactionDate = udTransactionDate;
	}

	@Column(name = "ud_transaction_amount", nullable = false)
	public String getUdTransactionAmount() {
		return udTransactionAmount;
	}

	public void setUdTransactionAmount(String udTransactionAmount) {
		this.udTransactionAmount = udTransactionAmount;
	}

	@Column(name = "ud_transaction_id", nullable = false)
	public String getUdTransactionId() {
		return udTransactionId;
	}

	public void setUdTransactionId(String udTransactionId) {
		this.udTransactionId = udTransactionId;
	}

	@Column(name = "ud_transaction_description", nullable = false)
	public String getUdTransactionDescription() {
		return udTransactionDescription;
	}

	public void setUdTransactionDescription(String udTransactionDescription) {
		this.udTransactionDescription = udTransactionDescription;
	}

	@Column(name = "ud_wallet_reference", nullable = false)
	public String getUdWalletReference() {
		return udWalletReference;
	}

	public void setUdWalletReference(String udWalletReference) {
		this.udWalletReference = udWalletReference;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uh_id", nullable = false)
	public UploadHeader getUploadHeader() {
		return uploadHeader;
	}

	public void setUploadHeader(UploadHeader uploadHeader) {
		this.uploadHeader = uploadHeader;
	}

	@Override
	public String toString() {
		return
			"UploadDetail ["
				+ "udTransactionDate=" + udTransactionDate + ", udTransactionAmount=" + udTransactionAmount + ", udTransactionId=" + udTransactionId
				+ ", udTransactionDescription=" + udTransactionDescription + ", udWalletReference=" + udWalletReference
			+ "]";
	}

	
	
}
