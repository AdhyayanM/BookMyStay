import java.util.*;

// Booking Request Model
class BookingRequest {
    String requestId;
    String roomType;

    public BookingRequest(String requestId, String roomType) {
        this.requestId = requestId;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService(Map<String, Integer> initialInventory) {
        this.inventory.putAll(initialInventory);
    }

    public synchronized boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public synchronized void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void printInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking Service
class BookingService {

    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private Set<String> allAllocatedRoomIds = new HashSet<>();
    private InventoryService inventoryService;

    private int roomCounter = 1;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType + "-" + roomCounter++;
        } while (allAllocatedRoomIds.contains(roomId)); // ensure uniqueness

        return roomId;
    }

    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll(); // FIFO

            System.out.println("\nProcessing Request: " + request.requestId);

            synchronized (this) { // Atomic allocation block

                // Step 1: Check availability
                if (!inventoryService.isAvailable(request.roomType)) {
                    System.out.println("Booking Failed: No rooms available for " + request.roomType);
                    continue;
                }

                // Step 2: Generate unique room ID
                String roomId = generateRoomId(request.roomType);

                // Step 3: Record allocation
                allAllocatedRoomIds.add(roomId);

                allocatedRooms
                        .computeIfAbsent(request.roomType, k -> new HashSet<>())
                        .add(roomId);

                // Step 4: Update inventory immediately
                inventoryService.decrement(request.roomType);

                // Step 5: Confirm reservation
                System.out.println("Booking Confirmed!");
                System.out.println("Request ID: " + request.requestId);
                System.out.println("Room Type: " + request.roomType);
                System.out.println("Assigned Room ID: " + roomId);
            }
        }
    }

    public void printAllocations() {
        System.out.println("\nFinal Allocations:");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Main Class
public class BookMyStay {

    public static void main(String[] args) {

        // Initial Inventory
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("DELUXE", 2);
        initialInventory.put("STANDARD", 1);

        InventoryService inventoryService = new InventoryService(initialInventory);
        BookingService bookingService = new BookingService(inventoryService);

        // Add Booking Requests (FIFO)
        bookingService.addRequest(new BookingRequest("REQ1", "DELUXE"));
        bookingService.addRequest(new BookingRequest("REQ2", "DELUXE"));
        bookingService.addRequest(new BookingRequest("REQ3", "DELUXE")); // should fail
        bookingService.addRequest(new BookingRequest("REQ4", "STANDARD"));
        bookingService.addRequest(new BookingRequest("REQ5", "STANDARD")); // should fail

        // Process Bookings
        bookingService.processBookings();

        // Print Results
        bookingService.printAllocations();
        inventoryService.printInventory();
    }
}
