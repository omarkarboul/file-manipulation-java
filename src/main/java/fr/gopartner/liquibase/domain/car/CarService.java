package fr.gopartner.liquibase.domain.car;

import fr.gopartner.liquibase.core.properties.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CarService implements ICarService {

    private static String HEADERS = "id,model,power,personId\r\n";
    private static String[] CSV_HEADER = {"Car ID", "Model", "Power", "person ID"};
    private static String[] NAME_MAPPING = {"id", "model", "power", "personId"};
    private static String SEPARATOR = "\r\n";

    private final CarRepository carRepository;
    private final FileProperties fileProperties;

    public CarService(CarRepository carRepository, FileProperties fileProperties) {
        this.carRepository = carRepository;
        this.fileProperties = fileProperties;
    }

    @Override
    public List<Car> retrieveAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car addCar(Car s) {
        return carRepository.save(s);
    }

    @Override
    public Car updateCar(Car u) {
        return carRepository.save(u);
    }

    @Override
    public Optional<Car> retrieveCar(int id) {
        return carRepository.findById(id);
    }

    @Override
    public void deleteCar(int id) {
        carRepository.deleteById(id);
    }

    @Override
    public List<Car> retrieveCarsByPerson(int personId) {
        return carRepository.findByPersonId(personId);
    }

    @Override
    public void saveCarsFromCSVAsObject(MultipartFile carCsvFile) {
        try {
            carRepository.deleteAll();
            List<Car> cars = CSVHelper.csvToCars(carCsvFile.getInputStream());
            carRepository.saveAll(cars);
        } catch (IOException e) {
            log.error("error occurred when saving cars from csv file", e);
        }
    }

    @Override
    public void writeCSVFileLowLevel() throws IOException {
        var file = new File(fileProperties.getNameFileLowLevel());
        var result = HEADERS + getCarsAsString();
        FileUtils.writeByteArrayToFile(file,result.getBytes());
    }

    private String getCarsAsString() {
        return carRepository.findAll().stream().map(Car::toString).reduce("",(subResult, element)->subResult + element);
    }


    @Override
    public void readCSVByByte(MultipartFile carCsvFile) {
        if (!carCsvFile.isEmpty()) {
            try {
                saveCarFromCSVFileAsByte(carCsvFile);
            } catch (IOException e) {
                log.error("error occurred when reading CSV cars byte per byte", e);
            }

        }
    }

    private void saveCarFromCSVFileAsByte(MultipartFile carCsvFile) throws IOException {
        byte[] bytes = carCsvFile.getBytes();
        String completeData = new String(bytes);
        String[] rows = completeData.split(SEPARATOR);
        for (int i = 1; i < rows.length; i++) {
            Car c = convertToCar(rows[i]);
            carRepository.save(c);
        }
    }

    @Override
    public void writeCSVFileHighLevel(HttpServletResponse response) throws IOException {
        List<Car> listCars = retrieveAllCars();
        try (ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE)) {
            csvWriter.writeHeader(CSV_HEADER);
            for (Car car : listCars) {
                csvWriter.write(car, NAME_MAPPING);
            }
        }
    }

    @Override
    public void readCSVCarsHighLevel(MultipartFile file) {
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                saveCarsFromCSVAsObject(file);
            } catch (Exception e) {
                log.error("error occurred when reading CSV cars", e);
            }
        }
    }

    @Override
    public void exportCarsToCSV(int type, HttpServletResponse response) throws IOException {
        switch (type) {
            case 1: {
                writeCSVFileHighLevel(response);
                break;
            }
            case 2: {
                writeCSVFileLowLevel();
                break;
            }
        }
    }

    @Override
    public void readCSVCars(MultipartFile file, int type) {
        switch (type) {
            case 1: {
                readCSVCarsHighLevel(file);
                break;
            }
            case 2: {
                readCSVByByte(file);
                break;
            }
        }
    }

    private Car convertToCar(String row) {
        String[] columns = row.split(",");
        Car car = new Car();
        car.setModel(columns[1]);
        car.setPower(Integer.parseInt(columns[2]));
        car.setPersonId(Integer.parseInt(columns[3]));
        return car;
    }
}
