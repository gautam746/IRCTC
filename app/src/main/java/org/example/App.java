package org.example;

import org.example.entities.Train;
import org.example.entities.User;
import org.example.services.UserBookingService;
import org.example.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args){
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try{
            userBookingService = new UserBookingService();
        } catch (IOException ex){
            System.out.println("There is something wrong");
            return;
        }

        // ✅ declare trainSelectedForBooking
        Train trainSelectedForBooking = null;

        while (option != 7){
            System.out.println("Choose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Booking");
            System.out.println("4. Search Train");
            System.out.println("5. Book Seat");
            System.out.println("7. Exit the App");
            option = scanner.nextInt();

            switch (option){
                case 1:
                    System.out.println("Enter the user name to signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignUp = new User(nameToSignUp, passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp),
                            new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingService.signUp(userToSignUp);
                    break;

                case 2:
                    System.out.println("Enter the Username to login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(nameToLogin, passwordToLogin,
                            UserServiceUtil.hashPassword(passwordToLogin),
                            new ArrayList<>(), UUID.randomUUID().toString());
                    try {
                        userBookingService = new UserBookingService(userToLogin);
                    } catch (IOException ex){
                        return;
                    }
                    break;

                case 3:
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBooking();
                    break;

                case 4:
                    System.out.println("Type your source station");
                    String source = scanner.next();
                    System.out.println("Type your destination station");
                    String dest = scanner.next();
                    List<Train> trains = userBookingService.getTrains(source, dest);
                    int index = 1;
                    for (Train t: trains){
                        System.out.println(index + " Train id : " + t.getTrainId());
                        for (Map.Entry<String, String> entry: t.getStationTimes().entrySet()){
                            System.out.println("station " + entry.getKey() + " time: " + entry.getValue());
                        }
                        index++;
                    }
                    System.out.println("Select a train by typing 1,2,3...");
                    int trainChoice = scanner.nextInt();
                    if(trainChoice > 0 && trainChoice <= trains.size()){
                        trainSelectedForBooking = trains.get(trainChoice - 1); // ✅ fixed
                    } else {
                        System.out.println("Invalid train selection");
                    }
                    break;

                case 5:
                    if(trainSelectedForBooking == null){
                        System.out.println("No train selected! Please search and select a train first.");
                        break;
                    }
                    System.out.println("Select a seat out of these seats:");
                    List<List<Integer>> seats = trainSelectedForBooking.getSeats(); // ✅ fixed
                    for (List<Integer> rowList: seats){
                        for (Integer val: rowList){
                            System.out.print(val + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Enter the row index:");
                    int row = scanner.nextInt();
                    System.out.println("Enter the column index:");
                    int col = scanner.nextInt();
                    System.out.println("Booking your seat....");
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                    if(booked.equals(Boolean.TRUE)){
                        System.out.println("Booked! Enjoy your journey");
                    }else{
                        System.out.println("Can't book this seat");
                    }
                    break;

                case 7:
                    System.out.println("Exiting application...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
