import java.util.HashMap;
import java.util.Map;

// Base abstract class
abstract class Room {
    abstract void displayRoomDetails();
}

// Room Types
class SingleRoom extends Room {
    void displayRoomDetails() {
        System.out.println("Single Room - 1 Bed, Free WiFi - ₹2000");
    }
}

class DoubleRoom extends Room {
    void displayRoomDetails() {
        System.out.println("Double Room - 2 Beds, TV, WiFi - ₹3500");
    }
}

class SuiteRoom extends Room {
    void displayRoomDetails() {
        System.out.println("Suite Room - Luxury, King Bed, AC - ₹6000");
    }
}

// Centralized Inventory Class (Actor)
class RoomInventory {

    // HashMap to store availability
    private HashMap<String, Integer> inventory;

    // Constructor (Initialize inventory)
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (controlled method)
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\nCurrent Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}

// Main Class (Version 3.1)
public class BookMyStay {

    public static void main(String[] args) {

        // Initialize inventory (Centralized)
        RoomInventory inventory = new RoomInventory();

        // Room objects (Polymorphism remains separate)
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("=== Hotel Room System (Version 3.1) ===\n");

        // Display room details + availability from inventory
        System.out.println("Single Room:");
        single.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Single"));
        System.out.println();

        System.out.println("Double Room:");
        dbl.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Double"));
        System.out.println();

        System.out.println("Suite Room:");
        suite.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Suite"));
        System.out.println();

        // Display full inventory
        inventory.displayInventory();

        // Example update (controlled)
        System.out.println("\nUpdating Single Room availability to 4...");
        inventory.updateAvailability("Single", 4);

        // Display updated inventory
        inventory.displayInventory();
    }
}