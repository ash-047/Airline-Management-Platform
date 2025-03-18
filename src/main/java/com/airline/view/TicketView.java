package com.airline.view;

// Add this import at the top of the file
import javafx.scene.shape.Rectangle;

import com.airline.controller.BookingController;
import com.airline.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;



import java.time.format.DateTimeFormatter;

public class TicketView {
    private BorderPane view;
    private Customer customer;
    private Booking booking;
    private Ticket ticket;
    
    public TicketView(Customer customer, Booking booking) {
        this.customer = customer;
        this.booking = booking;
        
        // Generate ticket
        BookingController bookingController = BookingController.getInstance();
        this.ticket = bookingController.generateTicket(booking);
        
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        
        // Top section
        HBox topSection = new HBox(20);
        topSection.setAlignment(Pos.CENTER_LEFT);
        
        Text title = new Text("Your Ticket");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Button dashboardButton = new Button("Return to Dashboard");
        dashboardButton.setOnAction(e -> {
            CustomerDashboardView dashboardView = new CustomerDashboardView(customer);
            view.getScene().setRoot(dashboardView.getView());
        });
        
        topSection.getChildren().addAll(title, dashboardButton);
        view.setTop(topSection);
        
        // Center content - Ticket display
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        
        // Ticket container with boarding pass style
        VBox ticketContainer = new VBox(10);
        ticketContainer.setPadding(new Insets(20));
        ticketContainer.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        
        HBox airlineHeader = new HBox(20);
        airlineHeader.setAlignment(Pos.CENTER_LEFT);
        
        Text airlineName = new Text("Skyward Airlines");
        airlineName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        Text ticketId = new Text("Ticket #: " + (ticket != null ? ticket.getTicketId() : "N/A"));
        
        airlineHeader.getChildren().addAll(airlineName, ticketId);
        
        // Flight information
        GridPane flightInfo = new GridPane();
        flightInfo.setHgap(20);
        flightInfo.setVgap(10);
        flightInfo.setPadding(new Insets(10, 0, 10, 0));
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        // Row 0 - Flight Number
        Text flightLabel = new Text("FLIGHT");
        flightLabel.setStyle("-fx-font-weight: bold;");
        flightInfo.add(flightLabel, 0, 0);
        flightInfo.add(new Text(booking.getFlight().getFlightNumber()), 0, 1);
        
        // Row 0 - Date
        Text dateLabel = new Text("DATE");
        dateLabel.setStyle("-fx-font-weight: bold;");
        flightInfo.add(dateLabel, 1, 0);
        flightInfo.add(new Text(booking.getFlight().getDepartureTime().toLocalDate().format(dateFormatter)), 1, 1);
        
        // Row 0 - From
        Text fromLabel = new Text("FROM");
        fromLabel.setStyle("-fx-font-weight: bold;");
        flightInfo.add(fromLabel, 2, 0);
        flightInfo.add(new Text(booking.getFlight().getSource().getCode()), 2, 1);
        
        // Row 0 - To
        Text toLabel = new Text("TO");
        toLabel.setStyle("-fx-font-weight: bold;");
        flightInfo.add(toLabel, 3, 0);
        flightInfo.add(new Text(booking.getFlight().getDestination().getCode()), 3, 1);
        
        // Row 2 - Departure Time
        Text depLabel = new Text("DEPARTURE");
        depLabel.setStyle("-fx-font-weight: bold;");
        flightInfo.add(depLabel, 0, 2);
        flightInfo.add(new Text(booking.getFlight().getDepartureTime().toLocalTime().format(timeFormatter)), 0, 3);
        
        // Row 2 - Arrival Time
        Text arrLabel = new Text("ARRIVAL");
        arrLabel.setStyle("-fx-font-weight: bold;");
        flightInfo.add(arrLabel, 1, 2);
        flightInfo.add(new Text(booking.getFlight().getArrivalTime().toLocalTime().format(timeFormatter)), 1, 3);
        
        // Row 2 - Passenger
        Text passengerLabel = new Text("PASSENGER");
        passengerLabel.setStyle("-fx-font-weight: bold;");
        flightInfo.add(passengerLabel, 2, 2);
        flightInfo.add(new Text(customer.getUsername()), 2, 3);
        
        // Row 2 - Status
        Text statusLabel = new Text("STATUS");
        statusLabel.setStyle("-fx-font-weight: bold;");
        flightInfo.add(statusLabel, 3, 2);
        flightInfo.add(new Text(booking.getStatus().toString()), 3, 3);
        
        // Seat information
        HBox seatInfo = new HBox(50);
        seatInfo.setPadding(new Insets(10, 0, 10, 0));
        
        VBox seatBox = new VBox(5);
        Text seatLabel = new Text("SEAT");
        seatLabel.setStyle("-fx-font-weight: bold;");
        
        String seatText = "N/A";
        if (booking.getSeats() != null && !booking.getSeats().isEmpty()) {
            seatText = booking.getSeats().get(0).getSeatNumber();
        }
        
        Text seatNumber = new Text(seatText);
        seatNumber.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        seatBox.getChildren().addAll(seatLabel, seatNumber);
        
        VBox gateBox = new VBox(5);
        Text gateLabel = new Text("GATE");
        gateLabel.setStyle("-fx-font-weight: bold;");
        Text gateNumber = new Text("TBD");
        gateNumber.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gateBox.getChildren().addAll(gateLabel, gateNumber);
        
        VBox boardingBox = new VBox(5);
        Text boardingLabel = new Text("BOARDING");
        boardingLabel.setStyle("-fx-font-weight: bold;");
        Text boardingTime = new Text("30 MIN BEFORE DEPARTURE");
        boardingBox.getChildren().addAll(boardingLabel, boardingTime);
        
        seatInfo.getChildren().addAll(seatBox, gateBox, boardingBox);
        
        // Barcode placeholder
        Rectangle barcodePlaceholder = new Rectangle();
        barcodePlaceholder.setWidth(300);
        barcodePlaceholder.setHeight(60);
        barcodePlaceholder.setStyle("-fx-fill: linear-gradient(to right, black 0%, black 10%, white 10%, white 15%, black 15%, black 20%, white 20%, white 25%, black 25%, black 30%, white 30%, white 35%, black 35%, black 40%, white 40%, white 45%, black 45%, black 50%, white 50%, white 55%, black 55%, black 60%, white 60%, white 65%, black 65%, black 70%, white 70%, white 75%, black 75%, black 80%, white 80%, white 85%, black 85%, black 90%, white 90%, white 95%, black 95%, black 100%);");
        
        HBox barcodeBox = new HBox();
        barcodeBox.setAlignment(Pos.CENTER);
        barcodeBox.setPadding(new Insets(10, 0, 0, 0));
        barcodeBox.getChildren().add(barcodePlaceholder);
        
        ticketContainer.getChildren().addAll(
            airlineHeader,
            new Separator(),
            flightInfo,
            new Separator(),
            seatInfo,
            barcodeBox
        );
        
        // Action buttons
        HBox actionButtons = new HBox(20);
        actionButtons.setAlignment(Pos.CENTER);
        
        Button printButton = new Button("Print Ticket");
        printButton.setOnAction(e -> {
            // In a real app, this would use JavaFX printing APIs
            showAlert("Print Ticket", "Printing functionality would be implemented here.", Alert.AlertType.INFORMATION);
        });
        
        Button emailButton = new Button("Email Ticket");
        emailButton.setOnAction(e -> {
            showAlert("Email Ticket", "Email functionality would be implemented here.", Alert.AlertType.INFORMATION);
        });
        
        Button cancelButton = new Button("Cancel Booking");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> {
            // Confirmation dialog
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Cancel Booking");
            confirmDialog.setHeaderText("Are you sure you want to cancel this booking?");
            confirmDialog.setContentText("This action cannot be undone.");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    BookingController bookingController = BookingController.getInstance();
                    boolean cancelled = bookingController.cancelBooking(booking.getBookingId());
                    
                    if (cancelled) {
                        showAlert("Booking Cancelled", "Your booking has been cancelled successfully.", Alert.AlertType.INFORMATION);
                        
                        // Return to dashboard
                        CustomerDashboardView dashboardView = new CustomerDashboardView(customer);
                        view.getScene().setRoot(dashboardView.getView());
                    } else {
                        showAlert("Error", "Could not cancel booking. Please try again.");
                    }
                }
            });
        });
        
        actionButtons.getChildren().addAll(printButton, emailButton, cancelButton);
        
        centerContent.getChildren().addAll(ticketContainer, actionButtons);
        view.setCenter(centerContent);
    }
    
    private void showAlert(String title, String message) {
        showAlert(title, message, Alert.AlertType.ERROR);
    }
    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public Parent getView() {
        return view;
    }
}