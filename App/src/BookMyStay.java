import java.util.HashMap;
import java.util.Map;

// Base Room class
abstract class Room {
    abstract String getRoomType();
    abstract void displayRoomDetails();
}

// Room Implementations
class SingleRoom extends Room {
    String getRoomType() {
        return "Single";
    }

    void displayRoomDetails() {
        System.out.println("Single Room - 1 Bed, Free WiFi - ₹2000");
    }
}

class DoubleRoom extends Room {
    String getRoomType() {
        return "Double";
    }

    void displayRoomDetails() {
        System.out.println("Double Room - 2 Beds, TV, WiFi - ₹3500");
    }
}

class SuiteRoom extends Room {
    String getRoomType() {
        return "Suite";
    }

    void displayRoomDetails() {
        System.out.println("Suite Room - Luxury, King Bed, AC - ₹6000");
    }
}

// Centralized Inventory (Same as Use Case 3)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 0); // Example: Suite unavailable
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // No update methods used in search (important!)
}

// Search Service (Read-only logic)
class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {
        System.out.println("=== Available Rooms ===\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());

            // Defensive check: only show available rooms
            if (available > 0) {
                room.displayRoomDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}

// Main Class
public class BookMyStay {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Room objects (domain layer)
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // Search service (read-only)
        RoomSearchService searchService = new RoomSearchService();

        // Guest triggers search
        searchService.searchAvailableRooms(inventory, rooms);
    }
}