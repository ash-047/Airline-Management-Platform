package com.airline.view;

import com.airline.controller.BookingController;
import com.airline.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BookingView {
    private BorderPane view;
    private Customer customer;
    private Flight flight;
    private Booking booking;
    
    public BookingView(Customer customer, Flight flight) {
        this.customer = customer;
        this.flight = flight;
        
        // Create a booking object
        BookingController bookingController = BookingController.getInstance();
        this.booking = bookingController.createBooking(customer, flight);
        
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        
        // Top section - Title and flight info
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(10));
        
        Text title = new Text("Book Flight");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Text flightInfo = new Text(String.format("Flight: %s - %s to %s", 
            flight.getFlightNumber(),
            flight.getSource().getCode(), 
            flight.getDestination().getCode()));
        flightInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        Text departureInfo = new Text("Departure: " + flight.getDepartureTime().toString());
        Text arrivalInfo = new Text("Arrival: " + flight.getArrivalTime().toString());
        
        Button backButton = new Button("Back to Search");
        backButton.setOnAction(e -> {
            FlightSearchView searchView = new FlightSearchView(customer);
            view.getScene().setRoot(searchView.getView());
            
            // Cancel the pending booking since user went back
            BookingController bookingController = BookingController.getInstance();
            bookingController.cancelBooking(booking.getBookingId());
        });
        
        HBox backBox = new HBox(backButton);
        backBox.setAlignment(Pos.CENTER_LEFT);
        
        topSection.getChildren().addAll(backBox, title, flightInfo, departureInfo, arrivalInfo);
        view.setTop(topSection);
        
        // Center section - Seat selection
        VBox centerSection = new VBox(20);
        centerSection.setPadding(new Insets(10));
        
        Text seatsTitle = new Text("Select Seats");
        seatsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Seat class selection
        HBox seatClassBox = new HBox(20);
        seatClassBox.setAlignment(Pos.CENTER_LEFT);
        
        Label classLabel = new Label("Seat Class:");
        ComboBox<SeatCategory> classCombo = new ComboBox<>();
        classCombo.getItems().addAll(SeatCategory.values());
        classCombo.setValue(SeatCategory.ECONOMY); // Default
        
        seatClassBox.getChildren().addAll(classLabel, classCombo);
        
        // Seat grid (simplified for demo)
        GridPane seatGrid = new GridPane();
        seatGrid.setHgap(10);
        seatGrid.setVgap(10);
        seatGrid.setPadding(new Insets(10));
        
        // Generate some sample seats (in a real app, would get from database)
        List<CheckBox> seatCheckboxes = new ArrayList<>();
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 1; i <= 5; i++) {
            for (int j = 0; j < rows.length; j++) {
                String seatNumber = i + String.valueOf(rows[j]);
                CheckBox seatCheck = new CheckBox(seatNumber);
                seatCheckboxes.add(seatCheck);
                seatGrid.add(seatCheck, j % 3 + (j > 2 ? 1 : 0), i * 2 + (j / 3));
                
                // Add aisle
                if (j == 2) {
                    Label aisle = new Label(" ");
                    seatGrid.add(aisle, 3, i * 2 + (j / 3));
                }
            }
        }
        
        // Passenger details section
        VBox passengerSection = new VBox(10);
        
        Text passengerTitle = new Text("Passenger Details");
        passengerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane passengerForm = new GridPane();
        passengerForm.setHgap(10);
        passengerForm.setVgap(10);
        passengerForm.setPadding(new Insets(10));
        
        Label nameLabel = new Label("Full Name:");
        passengerForm.add(nameLabel, 0, 0);
        
        TextField nameField = new TextField();
        nameField.setText(customer.getUsername()); // Pre-fill with customer name
        passengerForm.add(nameField, 1, 0);
        
        Label ageLabel = new Label("Age:");
        passengerForm.add(ageLabel, 0, 1);
        
        TextField ageField = new TextField();
        passengerForm.add(ageField, 1, 1);
        
        Label idLabel = new Label("ID Type:");
        passengerForm.add(idLabel, 0, 2);
        
        ComboBox<String> idTypeCombo = new ComboBox<>();
        idTypeCombo.getItems().addAll("Passport", "Driver's License", "National ID");
        idTypeCombo.setValue("Passport"); // Default
        passengerForm.add(idTypeCombo, 1, 2);
        
        Label idNumberLabel = new Label("ID Number:");
        passengerForm.add(idNumberLabel, 0, 3);
        
        TextField idNumberField = new TextField();
        passengerForm.add(idNumberField, 1, 3);
        
        passengerSection.getChildren().addAll(passengerTitle, passengerForm);
        
        // Price summary section
        VBox priceSummary = new VBox(10);
        
        double basePrice = flight.getPrice().get(SeatCategory.ECONOMY); // Default to economy price
        
        Label priceLabel = new Label("Base Price: $" + basePrice);
        Label taxLabel = new Label("Taxes & Fees: $" + (basePrice * 0.1));
        Label totalLabel = new Label("Total: $" + (basePrice * 1.1));
        
        priceSummary.getChildren().addAll(
            new Text("Price Summary"),
            priceLabel,
            taxLabel,
            totalLabel
        );
        
        Button continueButton = new Button("Continue to Payment");
        continueButton.setOnAction(e -> {
            // Validate inputs
            if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || idNumberField.getText().isEmpty()) {
                showAlert("Input Error", "Please fill in all passenger details.");
                return;
            }
            
            // Get selected seats
            List<String> selectedSeats = new ArrayList<>();
            for (CheckBox cb : seatCheckboxes) {
                if (cb.isSelected()) {
                    selectedSeats.add(cb.getText());
                }
            }
            
            if (selectedSeats.isEmpty()) {
                showAlert("No Seats Selected", "Please select at least one seat.");
                return;
            }
            
            // Update booking with selected seats
            BookingController bookingController = BookingController.getInstance();
            bookingController.selectSeats(booking.getBookingId(), selectedSeats);
            
            // Navigate to payment view
            PaymentView paymentView = new PaymentView(customer, booking);
            view.getScene().setRoot(paymentView.getView());
        });
        
        centerSection.getChildren().addAll(
            seatsTitle, 
            seatClassBox, 
            new ScrollPane(seatGrid), 
            passengerSection, 
            priceSummary, 
            continueButton
        );
        
        view.setCenter(centerSection);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public Parent getView() {
        return view;
    }
}