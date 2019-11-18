package sv.zuniga.code.challenge.obj;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface RockPaperScissorDataRepository extends CrudRepository<RockPaperScissor, Long> {

	@RestResource(path = "bySessionId")
	public Iterable<RockPaperScissor> findBySessionId(@Param("sessionId") String sessionId);

	@Transactional
	public void deleteBySessionId(String sessionId);
}
