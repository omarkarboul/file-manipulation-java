package fr.gopartner.liquibase.domain.car;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    public static String TYPE = "text/csv";
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    public static List<Car> csvToCars(InputStream carCSV) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(carCSV, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<Car> cars = new ArrayList<Car>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            createListCarsFromCSV(cars, csvRecords);
            return cars;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private static void createListCarsFromCSV(List<Car> cars, Iterable<CSVRecord> csvRecords) {
        for (CSVRecord csvRecord : csvRecords) {
            Car car = getCar(csvRecord);
            cars.add(car);
        }
    }

    private static Car getCar(CSVRecord csvRecord) {
        Car car = new Car();
        car.setId(Integer.parseInt(csvRecord.get("id")));
        car.setModel(csvRecord.get("model"));
        car.setPower(Integer.parseInt(csvRecord.get("power")));
        car.setPersonId(Integer.parseInt(csvRecord.get("personId")));
        return car;
    }
}
