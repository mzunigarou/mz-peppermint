package sv.zuniga.trx.compare.obj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * A {@link Repository} used to perform queries to the in-memory database.
 * 
 * @author mzuniga
 *
 */
@Repository
public interface UploadHeaderRepository extends JpaRepository<UploadHeader, Long> {

}
