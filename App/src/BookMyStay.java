import java.util.*;

class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

class AddOnServiceManager {

    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added: " + service.getServiceName()
                + " to Reservation ID: " + reservationId);
    }

    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0.0;

        for (AddOnService service : services) {
            total += service.getCost();
        }

        return total;
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation ID: " + reservationId);
        for (AddOnService service : services) {
            System.out.println(" - " + service);
        }
    }
}


public class BookMyStay {

    public static void main(String[] args) {

        String reservationId = "RES123";

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService spaAccess = new AddOnService("Spa Access", 2000);

        serviceManager.addService(reservationId, breakfast);
        serviceManager.addService(reservationId, airportPickup);
        serviceManager.addService(reservationId, spaAccess);

        System.out.println();

        serviceManager.displayServices(reservationId);

        System.out.println();

        double totalCost = serviceManager.calculateTotalServiceCost(reservationId);

        System.out.println("Total Add-On Cost: ₹" + totalCost);

    }
}