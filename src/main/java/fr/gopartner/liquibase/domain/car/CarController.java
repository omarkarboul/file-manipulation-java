package fr.gopartner.liquibase.domain.car;

import fr.gopartner.liquibase.core.properties.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("car")
public class CarController {

    private final String DISPOSITION_TYPE = "Content-Disposition";

    private final FileProperties fileProperties;

    @Autowired
    ICarService carService;

    public CarController(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @GetMapping("/retrieve-all-cars")
    @ResponseBody
    public List<Car> getCars() {
        return carService.retrieveAllCars();
    }

    @GetMapping("/retrieve-car/{car-id}")
    @ResponseBody
    public Optional<Car> retrieveCar(@PathVariable("car-id") int carId) {
        return carService.retrieveCar(carId);
    }

    @GetMapping("/retrieve-cars-by-person/{person-id}")
    @ResponseBody
    public List<Car> retrieveCarsByPerson(@PathVariable("person-id") int personId) {
        return carService.retrieveCarsByPerson(personId);
    }

    @PostMapping("/add-car")
    @ResponseBody
    public Car addClient(@RequestBody Car c) {
        return carService.addCar(c);
    }

    @DeleteMapping("/remove-car/{car-id}")
    @ResponseBody
    public void removeCar(@PathVariable("car-id") int carId) {
        carService.deleteCar(carId);
    }

    @PutMapping("/update-car")
    @ResponseBody
    public Car modifyClient(@RequestBody Car car) {
        return carService.updateCar(car);
    }

    @GetMapping("/export-cars/{type}")
    @ResponseBody
    public void exportCarsToCSV(HttpServletResponse response , @PathVariable("type") int type) throws IOException {
        response.setContentType("text/csv");
        response.setHeader(DISPOSITION_TYPE, fileProperties.getNameFileHighLevel());
            carService.exportCarsToCSV(type,response);
    }

    @PostMapping("/upload-csv-car/{type}")
    public void uploadCarsCSVFile(@PathVariable("type") int type , @RequestParam("file") MultipartFile file) throws IOException {
            carService.readCSVCars(file,type);
    }
}
