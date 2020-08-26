package sv.zuniga.trx.compare;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;

import sv.zuniga.trx.compare.obj.UploadDetail;
import sv.zuniga.trx.compare.obj.UploadDetailRepository;
import sv.zuniga.trx.compare.obj.UploadHeader;
import sv.zuniga.trx.compare.obj.UploadHeaderRepository;

/**
 * A {@link SpringBootTest} to perform some TDD actions before to move the code snippet to a {@link Controller}.
 * 
 * @author mzuniga
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
class CompareApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(CompareApplicationTests.class);
	public static final SimpleDateFormat sdf_yyyy_MM_dd__HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Resource
	private UploadHeaderRepository uhRepository;
	@Resource
	private UploadDetailRepository udRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void contextLoads() {
	}

	/**
	 * A {@link Test} that reads the two files from the resource folder and then perform a mockup call to the server to retrieve the unmatched report.
	 * 
	 * @throws Exception If an error occurs.
	 */
	@Test
	public void filesReadAndReportUnmatched() throws Exception {
		File file1 = new File("src/test/java/resources/firstMarkoffFile.csv");
		assertTrue(file1.exists());

		File file2 = new File("src/test/java/resources/secondMarkoffFile.csv");
		assertTrue(file2.exists());
		
		Long file1Id = storeDataFile(file1);
		Long file2Id = storeDataFile(file2);
		
		mockMvc.perform(
			post("/unmatchedReport")
			.param("uhId1", String.valueOf(file1Id))
			.param("uhId2", String.valueOf(file2Id))
		)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.success").value(true))
		.andExpect(jsonPath("$.message").value("Data reporting done!"));
	}
	
	/**
	 * Method used to store the CSV data into a table using in-memory database.
	 * 
	 * @param file The {@link File} read from the resource folder in CSV format.
	 * @return The ID assigned by the database engine to the file. 
	 */
	private Long storeDataFile(File file) {
		List<UploadDetail> uploadDetails = new ArrayList<>();
		Long uploadHeaderId = null;
		if (file != null) {
			UploadHeader uploadHeader = new UploadHeader();
			uploadHeader.setUhName(file.getName());
			uploadHeader.setUhDate(sdf_yyyy_MM_dd__HH_mm_ss.format(new Date()));
			uhRepository.save(uploadHeader);
			logger.info("Upload Header ID [" + uploadHeader.getUhId() + "]");
			BufferedReader bufferedReader = null;
			UploadDetail uploadDetail = null;
			int rowCount = 0;
			try {
				bufferedReader = new BufferedReader(new FileReader(file));
				Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(bufferedReader);
				for (CSVRecord csvRecord : records) {
					rowCount++;
					uploadDetail = new UploadDetail();
					uploadDetail.setUdTransactionDate(csvRecord.get(1));
					uploadDetail.setUdTransactionAmount(csvRecord.get(2));
					uploadDetail.setUdTransactionId(csvRecord.get(5));
					uploadDetail.setUdTransactionDescription(csvRecord.get(4));
					uploadDetail.setUdWalletReference(csvRecord.get(7));
					uploadDetail.setUploadHeader(uploadHeader);
					uploadDetails.add(uploadDetail);
				}
				udRepository.saveAll(uploadDetails);
				logger.info("Saved [" + uploadDetails.size() + "] records");
			} catch (Exception e) {
				logger.error("Error at [" + rowCount + "]", e);
			}
			uploadHeaderId = uploadHeader.getUhId();
		}
		return uploadHeaderId;
	}
	
}
