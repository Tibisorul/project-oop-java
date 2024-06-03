package Model;

import java.util.*;

public class CarService {
    private List<Car> carList;
    private Map<Integer, Car> carMap;

    public CarService() {
        carList = new ArrayList<>();
        carMap = new HashMap<>();
    }

    public void addCar(Car car) {
        carList.add(car);
        carMap.put(car.getID(), car);
    }

    public Car getCarById(int id) {
        return carMap.get(id);
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(carList);
    }

    public void removeCar(int id) {
        Car car = carMap.remove(id);
        if (car != null) {
            carList.remove(car);
            car.setStatus(CarStatus.DELETED); // Setăm statusul la DELETED
        }
    }

    public void rentCar(int id) {
        Car car = carMap.get(id);
        if (car != null && car.getStatus() == CarStatus.AVAILABLE) {
            car.setStatus(CarStatus.RENTED);
        }
    }

    public void returnCar(int id) {
        Car car = carMap.get(id);
        if (car != null && car.getStatus() == CarStatus.RENTED) {
            car.setStatus(CarStatus.AVAILABLE);
        }
    }
}
