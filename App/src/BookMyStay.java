import java.util.*;

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

    public String getGuestName() {
        return guestName;
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

class BookingHistory {

    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
        System.out.println("Booking confirmed and added to history: " + reservation.getReservationId());
    }

    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(confirmedReservations);
    }
}

class BookingReportService {

    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void printAllReservations() {
        System.out.println("=== All Confirmed Reservations ===");
        List<Reservation> reservations = bookingHistory.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        for (Reservation res : reservations) {
            System.out.println(res);
        }
    }

    public void printSummaryReport() {
        List<Reservation> reservations = bookingHistory.getAllReservations();
        double totalRevenue = 0;

        for (Reservation res : reservations) {
            totalRevenue += res.getTotalCost();
        }

        System.out.println("\n=== Booking Summary Report ===");
        System.out.println("Total Bookings: " + reservations.size());
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}


public class BookMyStay {

    public static void main(String[] args) {

        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService(bookingHistory);

        Reservation res1 = new Reservation("RES101", "Alice", "Deluxe", 3, 4500);
        Reservation res2 = new Reservation("RES102", "Bob", "Standard", 2, 3000);
        Reservation res3 = new Reservation("RES103", "Charlie", "Suite", 5, 12500);

        bookingHistory.addReservation(res1);
        bookingHistory.addReservation(res2);
        bookingHistory.addReservation(res3);

        System.out.println();

        reportService.printAllReservations();

        reportService.printSummaryReport();
    }
}