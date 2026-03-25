import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;
    private double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, int nights, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.totalCost = totalCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalCost() {
        return totalCost;
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

    public boolean isRoomAvailable(String roomType, int requestedRooms) {
        return roomAvailability.getOrDefault(roomType, 0) >= requestedRooms;
    }

    public void allocateRoom(String roomType, int count) throws InvalidBookingException {
        if (!roomAvailability.containsKey(roomType)) {
            throw new InvalidBookingException("Room type '" + roomType + "' does not exist.");
        }

        int available = roomAvailability.get(roomType);
        if (available < count) {
            throw new InvalidBookingException(
                    "Not enough rooms available. Requested: " + count + ", Available: " + available
            );
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

class InvalidBookingValidator {

    private static final Set<String> validRoomTypes = Set.of("Standard", "Deluxe", "Suite");

    public static void validateRoomType(String roomType) throws InvalidBookingException {
        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public static void validateNights(int nights) throws InvalidBookingException {
        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than zero.");
        }
    }

    public static void validateGuestName(String guestName) throws InvalidBookingException {
        if (guestName == null || guestName.isBlank()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
    }
}

public class BookMyStay {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        System.out.println("Initial room availability:");
        inventory.displayAvailability();
        System.out.println();

        List<Map<String, Object>> bookingRequests = List.of(
                Map.of("reservationId", "RES201", "guestName", "Alice", "roomType", "Deluxe", "nights", 3, "count", 1),
                Map.of("reservationId", "RES202", "guestName", "Bob", "roomType", "Suite", "nights", 2, "count", 3), // Invalid: not enough rooms
                Map.of("reservationId", "RES203", "guestName", "", "roomType", "Standard", "nights", 2, "count", 1), // Invalid guest name
                Map.of("reservationId", "RES204", "guestName", "Charlie", "roomType", "Premium", "nights", 1, "count", 1) // Invalid room type
        );

        for (Map<String, Object> request : bookingRequests) {
            try {

                InvalidBookingValidator.validateGuestName((String) request.get("guestName"));
                InvalidBookingValidator.validateRoomType((String) request.get("roomType"));
                InvalidBookingValidator.validateNights((int) request.get("nights"));

                inventory.allocateRoom((String) request.get("roomType"), (int) request.get("count"));

                double ratePerNight = switch ((String) request.get("roomType")) {
                    case "Standard" -> 1500;
                    case "Deluxe" -> 2500;
                    case "Suite" -> 5000;
                    default -> 0;
                };
                double totalCost = ratePerNight * (int) request.get("nights") * (int) request.get("count");

                Reservation res = new Reservation(
                        (String) request.get("reservationId"),
                        (String) request.get("guestName"),
                        (String) request.get("roomType"),
                        (int) request.get("nights"),
                        totalCost
                );

                System.out.println("Booking successful: " + res);

            } catch (InvalidBookingException e) {
                System.out.println("Booking failed: " + e.getMessage());
            }

            System.out.println();
        }

        System.out.println("Room availability after processing requests:");
        inventory.displayAvailability();
    }
}