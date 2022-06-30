package fr.gopartner.liquibase.domain.car;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ICarService {

    List<Car> retrieveAllCars();

    Car addCar(Car s);

    Car updateCar(Car u);

    Optional<Car> retrieveCar(int id);

    void deleteCar(int id);

    List<Car> retrieveCarsByPerson(int personId);

    void saveCarsFromCSVAsObject(MultipartFile carCsvFile);

    void writeCSVFileLowLevel() throws IOException;

    void readCSVByByte(MultipartFile carCsvFile);


    void writeCSVFileHighLevel(HttpServletResponse response) throws IOException;

    void readCSVCarsHighLevel(MultipartFile file);

    void exportCarsToCSV(int type, HttpServletResponse response) throws IOException;

    void readCSVCars(MultipartFile file, int type);
}
