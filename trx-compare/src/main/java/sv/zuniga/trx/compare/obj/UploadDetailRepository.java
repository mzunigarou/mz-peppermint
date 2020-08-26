package sv.zuniga.trx.compare.obj;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A {@link Repository} used to perform queries to the in-memory database.
 * 
 * It has been added in this repository two methods to retrieve mismatched records on both files:
 * 
 * <ul>
 * <li><code>getLeftSideItems</code>, The method to match records present in the first file but not in the second file.</li>
 * <li><code>getRightSideItems</code>, The method to match records present in the second file but not in the first file.</li>
 * </ul>
 * 
 * @author mzuniga
 *
 */
@Repository
public interface UploadDetailRepository extends JpaRepository<UploadDetail, Long> {

	@Query(
		"select ud from UploadDetail ud where ud.uploadHeader.uhId = ?1 and ud.udTransactionId not in ("
			+ "select ud.udTransactionId from UploadDetail ud where ud.uploadHeader.uhId = ?2"
		+ ")"
		+ " order by ud.udTransactionDate, ud.udWalletReference, ud.udTransactionAmount")
	List<UploadDetail> getLeftSideItems(Long uhId1, Long uhId2);
	
	@Query(
		"select ud from UploadDetail ud where ud.uploadHeader.uhId = ?2 and ud.udTransactionId not in ("
			+ "select ud.udTransactionId from UploadDetail ud where ud.uploadHeader.uhId = ?1"
		+ ")"
		+ " order by ud.udTransactionDate, ud.udWalletReference, ud.udTransactionAmount")
	List<UploadDetail> getRightSideItems(Long uhId1, Long uhId2);
	
}
