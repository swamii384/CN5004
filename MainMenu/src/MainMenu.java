import java.io.*;
import java.util.*;

public class MainMenu {

    static List<Car> cars = new ArrayList<>();
    static List<String> parkedCars = new ArrayList<>();
    static final int CAR_PARK_CAPACITY = 10;

    public static void main(String[] args) {
        loadFiles();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Car Parking System ---");
            System.out.println("1. List Available Cars");
            System.out.println("2. Register a Car");
            System.out.println("3. Park a Car");
            System.out.println("4. View Existing Parking Bookings");
            System.out.println("5. Check if Car Park is Full");
            System.out.println("6. Remove a Car from Parking");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int choice = -1;
            while (choice == -1) {
                try {
                    choice = sc.nextInt();
                    sc.nextLine();
                    if (choice < 1 || choice > 6) {
                        System.out.println("Invalid choice! Please enter a number between 1 and 6.");
                        choice = -1;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    sc.nextLine();
                }
            }

            switch (choice) {
                case 1:
                    listAvailableCars();
                    break;
                case 2:
                    registerCar(sc);
                    break;
                case 3:
                    parkCar(sc);
                    break;
                case 4:
                    viewParkingBookings();
                    break;
                case 5:
                    checkIfCarParkIsFull();
                    break;
                case 6:
                    removeCarFromParking(sc);
                    break;
                case 7:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }


    private static void loadFiles() {

        try (Scanner carScanner = new Scanner(new File("cars.txt"))) {
            while (carScanner.hasNextLine()) {
                String line = carScanner.nextLine();
                Car car = Car.fromString(line);
                cars.add(car);
            }
        } catch (Exception e) {
            System.out.println("Error loading cars file: " + e.getMessage());
        }


        try (Scanner bookScanner = new Scanner(new File("bookings.txt"))) {
            while (bookScanner.hasNextLine()) {
                String line = bookScanner.nextLine();
                parkedCars.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error loading bookings file: " + e.getMessage());
        }
    }

    private static void listAvailableCars() {
        System.out.println("\n--- Available Cars ---");
        boolean found = false;
        for (Car car : cars) {
            if (!parkedCars.contains(car.getRegistrationNumber())) {
                System.out.println(car);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No available cars at the moment.");
        }
    }

    private static void registerCar(Scanner sc) {
        System.out.print("\nEnter Registration Number: ");
        String regNumber = sc.nextLine();
        System.out.print("Enter Owner Name: ");
        String ownerName = sc.nextLine();


        for (Car car : cars) {
            if (car.getRegistrationNumber().equalsIgnoreCase(regNumber)) {
                System.out.println("Car is already registered.");
                return;
            }
        }

        Car car = new Car(regNumber, ownerName);
        cars.add(car);
        saveCars();
        System.out.println("Car registered successfully!");
    }

    private static void parkCar(Scanner sc) {
        if (parkedCars.size() >= CAR_PARK_CAPACITY) {
            System.out.println("The car park is full. Cannot park a new car.");
            return;
        }

        System.out.println("\n--- Available Cars to Park ---");
        listAvailableCars();

        String regNumber;
        Car selectedCar;
        do {
            System.out.print("\nEnter Car Registration Number to park: ");
            regNumber = sc.nextLine();

            selectedCar = null;
            for (Car car : cars) {
                if (car.getRegistrationNumber().equalsIgnoreCase(regNumber) && !parkedCars.contains(car.getRegistrationNumber())) {
                    selectedCar = car;
                    break;
                }
            }

            if (selectedCar == null) {
                System.out.println("Car not available or invalid registration number. Please try again.");
            }
        } while (selectedCar == null);

        parkedCars.add(regNumber);
        saveBookings();
        System.out.println("Car parked successfully!");
    }

    private static void viewParkingBookings() {
        if (parkedCars.isEmpty()) {
            System.out.println("\nNo parking bookings available.");
        } else {
            System.out.println("\n--- Parking Bookings ---");
            for (String regNumber : parkedCars) {
                System.out.println("Car ID: " + regNumber);
            }
        }
    }

    private static void checkIfCarParkIsFull() {
        if (parkedCars.size() >= CAR_PARK_CAPACITY) {
            System.out.println("The car park is full.");
        } else {
            System.out.println("The car park is not full. "+(parkedCars.size())+" sports are not available. "  + (CAR_PARK_CAPACITY - parkedCars.size()) + " spots are available.");
        }
    }
    private static void removeCarFromParking(Scanner sc) {
        if (parkedCars.isEmpty()) {
            System.out.println("No cars are parked.");
            return;
        }

        System.out.println("\n--- Currently Parked Cars ---");
        for (String regNumber : parkedCars) {
            System.out.println("Car ID: " + regNumber);
        }

        System.out.print("\nEnter Car Registration Number to remove: ");
        String regNumber = sc.nextLine();

        if (parkedCars.contains(regNumber)) {
            parkedCars.remove(regNumber);
            saveBookings();
            System.out.println("Car removed from parking successfully!");
        } else {
            System.out.println("Car not found in parking list.");
        }
    }

    private static void saveCars() {
        try (PrintWriter writer = new PrintWriter("cars.txt")) {
            for (Car car : cars) {
                writer.println(car.toFileString());
            }
        } catch (Exception e) {
            System.out.println("Error saving cars file: " + e.getMessage());
        }
    }

    private static void saveBookings() {
        try (PrintWriter writer = new PrintWriter("bookings.txt")) {
            for (String regNumber : parkedCars) {
                writer.println(regNumber);
            }
        } catch (Exception e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }
}
