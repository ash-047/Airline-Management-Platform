package com.airline.view;

import com.airline.Main;
import com.airline.controller.BookingController;
import com.airline.model.Booking;
import com.airline.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class CustomerDashboardView {
    private BorderPane view;
    private Customer customer;
    
    public CustomerDashboardView(Customer customer) {
        this.customer = customer;
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        
        // Top section with welcome message and logout button
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label welcomeLabel = new Label("Welcome, " + customer.getUsername() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> Main.showLoginScreen());
        
        topBar.getChildren().addAll(welcomeLabel, logoutButton);
        view.setTop(topBar);
        
        // Center content with menu options
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        
        Label dashboardLabel = new Label("Customer Dashboard");
        dashboardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        // Menu buttons
        Button searchFlightsButton = new Button("Search Flights");
        searchFlightsButton.setPrefSize(200, 40);
        searchFlightsButton.setOnAction(e -> {
            FlightSearchView searchView = new FlightSearchView(customer);
            Main.getPrimaryStage().getScene().setRoot(searchView.getView());
        });
        
        Button viewBookingsButton = new Button("View My Bookings");
        viewBookingsButton.setPrefSize(200, 40);
        viewBookingsButton.setOnAction(e -> {
            BookingHistoryView historyView = new BookingHistoryView(customer);
            Main.getPrimaryStage().getScene().setRoot(historyView.getView());
        });
        
        Button enquiryButton = new Button("General Enquiries");
        enquiryButton.setPrefSize(200, 40);
        enquiryButton.setOnAction(e -> {
            GeneralEnquiryView enquiryView = new GeneralEnquiryView(customer);
            Main.getPrimaryStage().getScene().setRoot(enquiryView.getView());
        });
        
        // Most recent bookings summary
        VBox recentBookings = new VBox(10);
        recentBookings.setPadding(new Insets(20));
        
        Label recentLabel = new Label("Recent Bookings");
        recentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        recentBookings.getChildren().add(recentLabel);
        
        BookingController bookingController = BookingController.getInstance();
        List<Booking> bookings = bookingController.getBookingHistory(customer);
        
        if (bookings.isEmpty()) {
            recentBookings.getChildren().add(new Label("No bookings found."));
        } else {
            // Display up to 3 most recent bookings
            int count = Math.min(3, bookings.size());
            for (int i = 0; i < count; i++) {
                Booking booking = bookings.get(i);
                HBox bookingBox = new HBox(10);
                bookingBox.setAlignment(Pos.CENTER_LEFT);
                
                String flightInfo = booking.getFlight().getFlightNumber() + ": " + 
                                    booking.getFlight().getSource().getCode() + " to " + 
                                    booking.getFlight().getDestination().getCode();
                
                bookingBox.getChildren().add(new Label(flightInfo + " - " + booking.getStatus()));
                
                recentBookings.getChildren().add(bookingBox);
            }
        }
        
        centerContent.getChildren().addAll(
            dashboardLabel, 
            searchFlightsButton, 
            viewBookingsButton, 
            enquiryButton,
            recentBookings
        );
        
        view.setCenter(centerContent);
    }
    
    public Parent getView() {
        return view;
    }
}