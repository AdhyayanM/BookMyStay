import java.util.*;

class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: $" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("---------------------------");
    }
}

class Inventory {
    private Map<String, Integer> availabilityMap;

    public Inventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availabilityMap.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return availabilityMap.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availabilityMap.keySet();
    }
}

class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {
            int availableCount = inventory.getAvailability(type);

            // Defensive Programming: filter invalid or unavailable rooms
            if (availableCount > 0 && roomCatalog.containsKey(type)) {
                Room room = roomCatalog.get(type);

                room.displayDetails();
                System.out.println(" Available Count: " + availableCount);
                System.out.println("===========================");
            }
        }
    }
}

// Main Class
public class BookMyStay {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0); // Should be filtered out
        inventory.addRoom("Suite", 2);

        // Step 2: Setup Room Catalog (Domain Model)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 100.0, Arrays.asList("WiFi", "TV")));

        roomCatalog.put("Double",
                new Room("Double", 150.0, Arrays.asList("WiFi", "TV", "Mini Bar")));

        roomCatalog.put("Suite",
                new Room("Suite", 300.0, Arrays.asList("WiFi", "TV", "Mini Bar", "Jacuzzi")));

        SearchService searchService = new SearchService(inventory, roomCatalog);

        searchService.searchAvailableRooms();
    }
}