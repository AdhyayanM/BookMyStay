import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;
    private double totalCost;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, int nights, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.totalCost = totalCost;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Nights: " + nights +
                ", Total Cost: ₹" + totalCost +
                (isCancelled ? " [CANCELLED]" : "");
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

    public boolean isRoomAvailable(String roomType, int requested) {
        return roomAvailability.getOrDefault(roomType, 0) >= requested;
    }

    public void allocateRoom(String roomType, int count) throws Exception {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available < count) {
            throw new Exception("Not enough rooms available. Requested: " + count + ", Available: " + available);
        }
        roomAvailability.put(roomType, available - count);
    }

    public void releaseRoom(String roomType, int count) {
        roomAvailability.put(roomType, roomAvailability.getOrDefault(roomType, 0) + count);
    }

    public void displayAvailability() {
        System.out.println("=== Current Room Availability ===");
        for (String type : roomAvailability.keySet()) {
            System.out.println(type + ": " + roomAvailability.get(type));
        }
    }
}

class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation res) {
        reservations.add(res);
        System.out.println("Reservation confirmed: " + res.getReservationId());
    }

    public Reservation findReservation(String reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equals(reservationId)) {
                return res;
            }
        }
        return null;
    }

    public void displayAll() {
        System.out.println("=== Booking History ===");
        for (Reservation res : reservations) {
            System.out.println(res);
        }
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelReservation(String reservationId) {
        Reservation res = history.findReservation(reservationId);
        if (res == null) {
            System.out.println("Cancellation failed: Reservation ID " + reservationId + " does not exist.");
            return;
        }
        if (res.isCancelled()) {
            System.out.println("Cancellation failed: Reservation ID " + reservationId + " is already cancelled.");
            return;
        }
        rollbackStack.push(reservationId);
        inventory.releaseRoom(res.getRoomType(), 1);
        res.cancel();
        System.out.println("Reservation " + reservationId + " cancelled successfully. Inventory restored.");
    }

    public void undoLastCancellation() {
        if (rollbackStack.isEmpty()) {
            System.out.println("No cancellations to undo.");
            return;
        }
        String reservationId = rollbackStack.pop();
        Reservation res = history.findReservation(reservationId);
        if (res != null && res.isCancelled()) {
            res.cancel();
            try {
                inventory.allocateRoom(res.getRoomType(), 1);
            } catch (Exception e) {}
            System.out.println("Undo successful: Reservation " + reservationId + " restored.");
        }
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) throws Exception {
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventory, history);

        Reservation res1 = new Reservation("RES301", "Alice", "Deluxe", 2, 5000);
        Reservation res2 = new Reservation("RES302", "Bob", "Standard", 1, 1500);
        Reservation res3 = new Reservation("RES303", "Charlie", "Suite", 3, 15000);

        history.addReservation(res1);
        inventory.allocateRoom(res1.getRoomType(), 1);

        history.addReservation(res2);
        inventory.allocateRoom(res2.getRoomType(), 1);

        history.addReservation(res3);
        inventory.allocateRoom(res3.getRoomType(), 1);

        System.out.println();
        inventory.displayAvailability();
        System.out.println();

        cancellationService.cancelReservation("RES302");
        cancellationService.cancelReservation("RES302");
        cancellationService.cancelReservation("RES999");

        System.out.println();
        inventory.displayAvailability();
        System.out.println();

        history.displayAll();
    }
}