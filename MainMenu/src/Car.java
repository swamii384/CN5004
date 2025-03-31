public class Car {
    private String registrationNumber;
    private String ownerName;
    private boolean isParked;

    // Constructor
    public Car(String registrationNumber, String ownerName) {
        this.registrationNumber = registrationNumber;
        this.ownerName = ownerName;
        this.isParked = false;  // Initially not parked
    }

    // Getters
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public boolean isParked() {
        return isParked;
    }

    // Setters
    public void setParked(boolean parked) {
        isParked = parked;
    }

    // String representation of the car
    @Override
    public String toString() {
        return "Car [Registration Number: " + registrationNumber + ", Owner: " + ownerName + "]";
    }

    // Convert Car to String to save in the file
    public String toFileString() {
        return registrationNumber + "," + ownerName + "," + isParked;
    }

    // Static method to convert file line into a Car object
    public static Car fromString(String line) {
        String[] parts = line.split(",");
        Car car = new Car(parts[0], parts[1]);
        car.setParked(Boolean.parseBoolean(parts[2]));
        return car;
    }
}
