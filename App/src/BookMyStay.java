import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    String reservationId;
    String guestName;
    String roomType;
    int nights;
    double totalCost;
    boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, int nights, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.totalCost = totalCost;
        this.isCancelled = false;
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

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Standard", 5);
        roomAvailability.put("Deluxe", 3);
        roomAvailability.put("Suite", 2);
    }

    public boolean allocateRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available > 0) {
            roomAvailability.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void releaseRoom(String roomType) {
        roomAvailability.put(roomType, roomAvailability.getOrDefault(roomType, 0) + 1);
    }

    public void displayAvailability() {
        System.out.println("=== Room Availability ===");
        for (String type : roomAvailability.keySet()) {
            System.out.println(type + ": " + roomAvailability.get(type));
        }
    }
}

class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation res) {
        reservations.add(res);
        System.out.println("Reservation confirmed: " + res.reservationId);
    }

    public void displayAll() {
        System.out.println("=== Booking History ===");
        for (Reservation res : reservations) {
            System.out.println(res);
        }
    }
}

class PersistenceService {
    private static final String FILE_NAME = "system_state.ser";

    public static void saveState(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save system state: " + e.getMessage());
        }
    }

    public static Object[] restoreState() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("System state restored successfully.");
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to restore system state: " + e.getMessage());
            return null;
        }
    }
}

public class BookMyStay {
    public static void main(String[] args) {
        Object[] restored = PersistenceService.restoreState();
        RoomInventory inventory;
        BookingHistory history;

        if (restored == null) {
            inventory = new RoomInventory();
            history = neBookingHistory();
        } else {
            inventory = (RoomInventory) restored[0];
            history = (BookingHistory) restored[1];
        }

        Reservation res1 = new Reservation("RES501", "Alice", "Deluxe", 2, 5000);
        Reservation res2 = new Reservation("RES502", "Bob", "Standard", 1, 1500);

        if (inventory.allocateRoom(res1.roomType)) history.addReservation(res1);
        if (inventory.allocateRoom(res2.roomType)) history.addReservation(res2);

        System.out.println();
        inventory.displayAvailability();
        System.out.println();
        history.displayAll();

        PersistenceService.saveState(inventory, history);
    }
}