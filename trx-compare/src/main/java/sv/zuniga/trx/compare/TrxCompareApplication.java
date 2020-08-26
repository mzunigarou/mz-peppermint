package sv.zuniga.trx.compare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Class used to invoke the Web Application itself using Spring Boot
 * 
 * @author mzuniga
 * 
 */
@SpringBootApplication
public class TrxCompareApplication {

	
	/**
	 * The main method of the Spring Boot application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TrxCompareApplication.class, args);
	}

}
