package com.ee_java.team_project.csv_parser;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVReader;

import javax.net.ssl.SSLEngineResult;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * The Csv parser.
 */
public class CodingCompCsvUtil {

	/**
	 * Read csv file with pojo.
	 *
	 * @param <T>       the type parameter
	 * @param filePath  the file path
	 * @param classType the class type
	 * @return the list
	 */
	/* #1
	 * readCsvFile() -- Read in a CSV File and return a list of entries in that file.
	 * @param filePath -- Path to file being read in.
	 * @param classType -- Class of entries being read in.
	 * @return -- List of entries being returned.
	 */
	//working with all classes that hasn't field list of objects(Customers)
	public <T> List<T> readCsvFileWithPojo(String filePath, Class<T> classType)  {
		CsvMapper csvMapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		ObjectReader objectReader = csvMapper
				.reader(classType)
				.with(schema);
		ArrayList<T> objects = new ArrayList<>();
		try (Reader reader = new FileReader(filePath)) {
			MappingIterator<T> mappingIterator = objectReader.readValues(reader);
			while  (mappingIterator.hasNext()) {
				T current = mappingIterator.next();
				objects.add(current);
				//System.out.println(current);
			}
		} catch (IOException notFoundException) {
			notFoundException.printStackTrace();
		}

		return objects;
	}

	/**
	 * Read csv file file without pojo.
	 *
	 * @param filePath the file path
	 * @return the map
	 */
	public Map<List<String>, String> readCsvFileFileWithoutPojo(String filePath) {
		List<String> lblList = null;
		ArrayList<String[]> dataList = new ArrayList<>();
		//CSVReader reader = null;
		StringBuilder sb = new StringBuilder();

		try(CSVReader reader = new CSVReader(new FileReader(filePath)))
		{
			//Get the CSVReader instance with specifying the delimiter to be used
			//reader = new CSVReader(new FileReader("/home/student/Desktop/java_ent_2021/2020-StateFarm-CodingCompetitionProblem/src/main/resources/DataFiles/agents_test.csv"));

			String [] nextLine; //csv row
			lblList = Arrays.asList(reader.readNext()); //csv columns
			sb.append("[");
			//Read one line at a time
			while ((nextLine = reader.readNext()) != null) {
				sb.append("{"); //start new object
				for (int i = 0; i < nextLine.length; i++) {
					String param = lblList.get(i);
					String value = nextLine[i];
					sb.append("\"").append(param).append("\"").append(":"); //param


					//appropriate wrappers for values
					if (isNumeric(value)) { // handle numbers
						sb.append(value);
					} else if (value.contains("[") || value.contains("{")) { // handle array or object
						sb.append(value);
					} else {
						sb.append("\"").append(value).append("\""); // otherwise handling string
					}

					//checks whether it is the last value in a row
					if (lblList.size() - 1 == i) {
						continue;
					} else {
						sb.append(",");
					}

					dataList.add(nextLine);
				}
				sb.append("}").append(","); // ends object

			}
			sb.setLength(sb.length() - 1); //get rid of comma that ends the object
			sb.append("]");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Map<List<String>, String> result = new HashMap<>();
		result.put(lblList, sb.toString());
		return result;
	}



	private boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 * @throws IOException the io exception
	 */
//rough testing
	public static void main(String[] args) throws IOException {

		CodingCompCsvUtil csvUtil = new CodingCompCsvUtil();

		//csv-parser for agents.csv, claims.csv, vendors.csv
		String filePathForAgents = "D:\\MATC\\ent-java-team-project-csv-parser-rest\\src\\main\\resources\\DataFiles\\agents.csv";
		String filePathForCustomers = "D:\\MATC\\ent-java-team-project-csv-parser-rest\\src\\main\\resources\\DataFiles\\customers.csv";
		String filePathForClaims = "D:\\MATC\\ent-java-team-project-csv-parser-rest\\src\\main\\resources\\DataFiles\\claims.csv";

		CodingCompCsvUtil csvUtil1 = new CodingCompCsvUtil();
		//String customersResult = csvUtil.readCsvFileFileWithoutPojo(filePathForCustomers);
		//String agentsResult = csvUtil.readCsvFileFileWithoutPojo(filePathForAgents);
		//String claimsResult = csvUtil.readCsvFileFileWithoutPojo(filePathForClaims);
		//System.out.println(customersResult);
	}
}
