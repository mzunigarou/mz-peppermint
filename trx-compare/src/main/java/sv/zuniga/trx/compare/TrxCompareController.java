package sv.zuniga.trx.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import sv.zuniga.trx.compare.obj.UploadDetail;
import sv.zuniga.trx.compare.obj.UploadDetailRepository;
import sv.zuniga.trx.compare.obj.UploadHeader;
import sv.zuniga.trx.compare.obj.UploadHeaderRepository;

/**
 * {@link Controller} used to handle all request from then web.
 * This {@link Controller} has 3 mappings
 * <ul>
 * <li><code>/</code>, The default home page</li>
 * <li><code>/compare</code>, The method to upload the two files to be compared to the server and generate the analysis</li>
 * <li><code>/unmatchedReport</code>, The method to get a report of unmatched records on both files</li>
 * </ul>
 * 
 * @author mzuniga
 *
 */
@Controller
public class TrxCompareController {
	private Logger logger = LoggerFactory.getLogger(TrxCompareController.class);
	public static final String VALID_EXTENSION = "csv";
	public static final SimpleDateFormat sdf_yyyy_MM_dd__HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Resource
	private UploadHeaderRepository uhRepository;
	@Resource
	private UploadDetailRepository udRepository;
	
	/**
	 * The default mapping for the home page
	 * 
	 * @return
	 */
	@RequestMapping(value = "/")
	public String compare() {
		return "compare";
	}
	
	/**
	 * Request mapping used to upload the files to be compared.
	 * 
	 * @param response The {@link HttpServletResponse} to be used.
	 * @param file1 The first file to be uploaded in CSV format.
	 * @param file2 The second file to be uploaded in CSV format.
	 * @throws Exception If an exception occurs
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/compare")
	public void compare(HttpServletResponse response, @RequestParam MultipartFile file1, @RequestParam MultipartFile file2) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		String ext1 = FilenameUtils.getExtension(file1.getOriginalFilename());
		String ext2 = FilenameUtils.getExtension(file2.getOriginalFilename());
		
		// It is necessary that both files match the CSV extension 
		if (StringUtils.equalsIgnoreCase(VALID_EXTENSION, ext1) && StringUtils.equalsIgnoreCase(VALID_EXTENSION, ext2)) {
			// The information of both files is stored using an in-memory database
			logger.info(StringUtils.rightPad("Preparing File 1... ", 30, "*"));
			Long uhId1 = storeDataFile(file1);
			logger.info(StringUtils.rightPad("End... ", 30, "*"));
			logger.info(StringUtils.rightPad("Preparing File 2... ", 30, "*"));
			Long uhId2 = storeDataFile(file2);
			logger.info(StringUtils.rightPad("End... ", 30, "*"));
			
			// The matching process is performed querying the in-memory database
			logger.info(StringUtils.rightPad("Reporting File 1... ", 30, "*"));
			Map<String, Object> map1 = getBriefDetails(file1.getOriginalFilename(), uhId1, uhId2, true);
			logger.info(StringUtils.rightPad("End... ", 30, "*"));
			logger.info(StringUtils.rightPad("Reporting File 2... ", 30, "*"));
			Map<String, Object> map2 = getBriefDetails(file2.getOriginalFilename(), uhId1, uhId2, false);
			logger.info(StringUtils.rightPad("End... ", 30, "*"));
			
			// Return to view
			map.put("success", true);
			map.put("message", "Data analysis done!");
			map.put("map1", map1);
			map.put("map2", map2);
			
		}
		else {
			// Something wrong happens...
			map.put("success", false);
			map.put("message", "Files must be in CSV format");
		}
		
		// Serialize the response using JSON
		Gson gson = new Gson();
		response.getOutputStream().write(gson.toJson(map).getBytes());
	}

	/**
	 * Request mapping used to create a most detailed information about the comparison of two files.
	 * 
	 * @param response The {@link HttpServletResponse} to be used.
	 * @param uhId1 The ID of the first file stored in the internal database.
	 * @param uhId2 The ID of the second file stored in the internal database.
	 * @throws Exception If an exception occurs
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/unmatchedReport")
	public void unmatchedReport(HttpServletResponse response, @RequestParam Long uhId1, @RequestParam Long uhId2) throws Exception {
		HashMap<String, Object> map = new HashMap<>();
		List<Map<String, Object>> list = null;
		HashMap<String, Object> iMap = null;
		
		// It is mandatory to have both files ID from the view using the in-memory database
		if (uhId1 != null && new Long(0L).compareTo(uhId1) < 0 && uhId2 != null && new Long(0L).compareTo(uhId2) < 0) {
			UploadHeader uploadHeader = uhRepository.getOne(uhId1);
			if (uploadHeader != null) {
				// First file found
				map.put("file1", uploadHeader.getUhName());
				// Find the records of the first file which are not in the second file
				List<UploadDetail> uploadDetails1 = udRepository.getLeftSideItems(uhId1, uhId2);
				if (uploadDetails1 != null && !uploadDetails1.isEmpty()) {
					list = new ArrayList<>();
					for (UploadDetail uploadDetail : uploadDetails1) {
						iMap = new HashMap<>();
						iMap.put("date", uploadDetail.getUdTransactionDate());
						iMap.put("reference", uploadDetail.getUdWalletReference());
						iMap.put("amount", uploadDetail.getUdTransactionAmount());
						list.add(iMap);
					}
					map.put("details1", list);
				}
			}
			uploadHeader = uhRepository.getOne(uhId2);
			if (uploadHeader != null) {
				// Second file found
				map.put("file2", uploadHeader.getUhName());
				// Find the records of the second file which are not in the first file
				List<UploadDetail> uploadDetails2 = udRepository.getRightSideItems(uhId1, uhId2);
				if (uploadDetails2 != null && !uploadDetails2.isEmpty()) {
					list = new ArrayList<>();
					for (UploadDetail uploadDetail : uploadDetails2) {
						iMap = new HashMap<>();
						iMap.put("date", uploadDetail.getUdTransactionDate());
						iMap.put("reference", uploadDetail.getUdWalletReference());
						iMap.put("amount", uploadDetail.getUdTransactionAmount());
						list.add(iMap);
					}
					map.put("details2", list);
				}
			}
			// Return to view
			map.put("success", true);
			map.put("message", "Data reporting done!");
		}
		else {
			// Something is wrong...
			map.put("success", false);
			map.put("message", "File information is required!");
		}
		
		Gson gson = new Gson();
		response.getOutputStream().write(gson.toJson(map).getBytes());
	}
	
	/**
	 * Method used to retrieve the mismatched information on both files 
	 * 
	 * @param fileName The filename of the file to be analyzed. 
	 * @param uhId1 The ID of the first file stored in the internal database. 
	 * @param uhId2 The ID of the second file stored in the internal database.
	 * @param leftSide A flag indicating if the comparison is being performed to the first file or not.
	 * @return A {@link Map} containing the necessary values to be displayed on the browser.
	 */
	private Map<String, Object> getBriefDetails(String fileName, Long uhId1, Long uhId2, boolean leftSide) {
		HashMap<String, Object> map = new HashMap<>(); 
		long matchingRecords = 0;
		long unmatchedRecords = 0;
		long totalRecords = 0;
		UploadDetail uploadDetail = new UploadDetail();
		uploadDetail.setUploadHeader(new UploadHeader(leftSide ? uhId1 : uhId2));
		map.put("uhId", uploadDetail.getUploadHeader().getUhId());
		map.put("filename", fileName);
		totalRecords = udRepository.count(Example.of(uploadDetail));
		map.put("totalRecords", totalRecords);
		List<UploadDetail> uploadDetails = leftSide ? udRepository.getLeftSideItems(uhId1, uhId2) : udRepository.getRightSideItems(uhId1, uhId2);
		if (uploadDetails != null && !uploadDetails.isEmpty()) {
			unmatchedRecords = uploadDetails.size();
			matchingRecords = totalRecords - unmatchedRecords;
			map.put("matchingRecords", matchingRecords);
			map.put("unmatchedRecords", unmatchedRecords);
			// Logging unmatched records...
			uploadDetails.forEach(item -> { logger.info("Record [" + item.toString() + "]"); });
		}
		return map;
	}

	/**
	 * Method used to store the CSV data into a table using in-memory database.
	 * 
	 * @param file The {@link MultipartFile} uploaded to the server in CSV format.
	 * @return The ID assigned by the database engine to the file. 
	 */
	private Long storeDataFile(MultipartFile file) {
		List<UploadDetail> uploadDetails = new ArrayList<>();
		Long uploadHeaderId = null;
		if (file != null) {
			// The master table
			UploadHeader uploadHeader = new UploadHeader();
			uploadHeader.setUhName(file.getOriginalFilename());
			uploadHeader.setUhDate(sdf_yyyy_MM_dd__HH_mm_ss.format(new Date()));
			uhRepository.save(uploadHeader);
			logger.info("Upload Header ID [" + uploadHeader.getUhId() + "]");
			File tempFile = null;
			BufferedReader bufferedReader = null;
			UploadDetail uploadDetail = null;
			int rowCount = 0;
			try {
				// Copy the uploaded file to a temporary folder
				tempFile = File.createTempFile(generateCode(10) + "_upload", null);
				FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
				bufferedReader = new BufferedReader(new FileReader(tempFile));
				// Read the file using Apache Commons CSV
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
				// Save the records into a table
				udRepository.saveAll(uploadDetails);
				logger.info("Saved [" + uploadDetails.size() + "] records");
			} catch (Exception e) {
				logger.error("Error at [" + rowCount + "]", e);
			}
			uploadHeaderId = uploadHeader.getUhId();
		}
		return uploadHeaderId;
	}
	
	/**
	 * Method used to generate random values with an specific length.
	 * 
	 * @param length The desired length of the {@link Long} number.
	 * @return A {@link String} representing the {@link Long} number.
	 */
	public static String generateCode(int length) {
		return StringUtils.substring(String.valueOf(Math.abs(new Random().nextLong())), 0, length);
	}
	
}
