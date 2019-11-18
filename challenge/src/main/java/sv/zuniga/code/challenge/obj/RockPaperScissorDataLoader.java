package sv.zuniga.code.challenge.obj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class RockPaperScissorDataLoader implements CommandLineRunner {
	@SuppressWarnings("unused")
	private final RockPaperScissorDataRepository repository;
	
	public RockPaperScissorDataLoader(RockPaperScissorDataRepository repository) {
		this.repository = repository;
	}
	
	public void run(String... args) throws Exception {
	}

}
