abstract class Room {
    abstract void displayRoomDetails();
}

// Single Room class
class SingleRoom extends Room {
    @Override
    void displayRoomDetails() {
        System.out.println("Type: Single Room");
        System.out.println("Features: 1 Bed, Free WiFi");
        System.out.println("Price: ₹2000 per night");
    }
}

// Double Room class
class DoubleRoom extends Room {
    @Override
    void displayRoomDetails() {
        System.out.println("Type: Double Room");
        System.out.println("Features: 2 Beds, Free WiFi, TV");
        System.out.println("Price: ₹3500 per night");
    }
}

// Suite Room class
class SuiteRoom extends Room {
    @Override
    void displayRoomDetails() {
        System.out.println("Type: Suite Room");
        System.out.println("Features: Luxury Room, King Bed, AC, TV, Mini Bar");
        System.out.println("Price: ₹6000 per night");
    }
}

public class BookMyStay {
    public static void main(String[] args) {

        // Create room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("Hotel Room Initialization\n");

        // Single Room
        System.out.println("Single Room:");
        singleRoom.displayRoomDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println();

        // Double Room
        System.out.println("Double Room:");
        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println();

        // Suite Room
        System.out.println("Suite Room:");
        suiteRoom.displayRoomDetails();
        System.out.println("Available: " + suiteAvailable);
    }
}
