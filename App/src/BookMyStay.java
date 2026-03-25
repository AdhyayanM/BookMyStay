import java.util.*;
import java.util.concurrent.*;

class ReservationRequest {
    String reservationId;
    String guestName;
    String roomType;
    int nights;

    public ReservationRequest(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }
}

class Reservation {
    String reservationId;
    String guestName;
    String roomType;
    int nights;
    double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, int nights, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Nights: " + nights +
                ", Total Cost: ₹" + totalCost;
    }
}

class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Standard", 5);
        roomAvailability.put("Deluxe", 3);
        roomAvailability.put("Suite", 2);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available > 0) {
            roomAvailability.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayAvailability() {
        System.out.println("=== Current Room Availability ===");
        for (String type : roomAvailability.keySet()) {
            System.out.println(type + ": " + roomAvailability.get(type));
        }
    }
}

class BookingHistory {
    private List<Reservation> reservations = Collections.synchronizedList(new ArrayList<>());

    public void addReservation(Reservation res) {
        reservations.add(res);
        System.out.println("Reservation confirmed: " + res.reservationId + " by " + res.guestName);
    }

    public void displayAll() {
        System.out.println("=== Booking History ===");
        synchronized(reservations) {
            for (Reservation res : reservations) {
                System.out.println(res);
            }
        }
    }
}

class BookingProcessor implements Runnable {
    private ReservationRequest request;
    private RoomInventory inventory;
    private BookingHistory history;

    public BookingProcessor(ReservationRequest request, RoomInventory inventory, BookingHistory history) {
        this.request = request;
        this.inventory = inventory;
        this.history = history;
    }

    @Override
    public void run() {
        if (inventory.allocateRoom(request.roomType)) {
            double ratePerNight = switch (request.roomType) {
                case "Standard" -> 1500;
                case "Deluxe" -> 2500;
                case "Suite" -> 5000;
                default -> 0;
            };
            double totalCost = ratePerNight * request.nights;
            Reservation res = new Reservation(request.reservationId, request.guestName, request.roomType, request.nights, totalCost);
            history.addReservation(res);
        } else {
            System.out.println("Booking failed for " + request.guestName + ": " + request.roomType + " not available.");
        }
    }
}

public class BookMyStay {
    public static void main(String[] args) throws InterruptedException {
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        List<ReservationRequest> requests = List.of(
                new ReservationRequest("RES401", "Alice", "Deluxe", 2),
                new ReservationRequest("RES402", "Bob", "Standard", 1),
                new ReservationRequest("RES403", "Charlie", "Suite", 3),
                new ReservationRequest("RES404", "David", "Standard", 2),
                new ReservationRequest("RES405", "Eve", "Suite", 1),
                new ReservationRequest("RES406", "Frank", "Deluxe", 1),
                new ReservationRequest("RES407", "Grace", "Standard", 2)
        );

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (ReservationRequest req : requests) {
            executor.execute(new BookingProcessor(req, inventory, history));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println();
        inventory.displayAvailability();
        System.out.println();
        history.displayAll();
    }
}