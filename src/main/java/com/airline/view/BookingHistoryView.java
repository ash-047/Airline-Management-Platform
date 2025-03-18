package com.airline.view;

import com.airline.model.BookingStatus;
import com.airline.controller.BookingController;
import com.airline.model.Booking;
import com.airline.model.Customer;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingHistoryView {
    private BorderPane view;
    private Customer customer;
    
    public BookingHistoryView(Customer customer) {
        this.customer = customer;
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        
        // Top section
        HBox topSection = new HBox(20);
        topSection.setAlignment(Pos.CENTER_LEFT);
        
        Text title = new Text("My Bookings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            CustomerDashboardView dashboardView = new CustomerDashboardView(customer);
            view.getScene().setRoot(dashboardView.getView());
        });
        
        topSection.getChildren().addAll(backButton, title);
        view.setTop(topSection);
        
        // Center content - Bookings table
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(10));
        
        TableView<Booking> bookingsTable = new TableView<>();
        
        TableColumn<Booking, String> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        
        TableColumn<Booking, String> flightNumberCol = new TableColumn<>("Flight");
        flightNumberCol.setCellValueFactory(data -> {
            if (data.getValue().getFlight() != null) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getFlight().getFlightNumber());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        
        TableColumn<Booking, String> routeCol = new TableColumn<>("Route");
        routeCol.setCellValueFactory(data -> {
            if (data.getValue().getFlight() != null && 
                data.getValue().getFlight().getSource() != null && 
                data.getValue().getFlight().getDestination() != null) {
                
                return new javafx.beans.property.SimpleStringProperty(
                    data.getValue().getFlight().getSource().getCode() + " → " +
                    data.getValue().getFlight().getDestination().getCode()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        
        TableColumn<Booking, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> {
            if (data.getValue().getFlight() != null && data.getValue().getFlight().getDepartureTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                return new javafx.beans.property.SimpleStringProperty(
                    data.getValue().getFlight().getDepartureTime().format(formatter)
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        
        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> {
            if (data.getValue().getStatus() != null) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus().toString());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        
        TableColumn<Booking, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(data -> {
            return new javafx.beans.property.SimpleStringProperty("$" + data.getValue().getTotalAmount());
        });
        
        bookingsTable.getColumns().addAll(bookingIdCol, flightNumberCol, routeCol, dateCol, statusCol, amountCol);
        
        // Load booking data
        BookingController bookingController = BookingController.getInstance();
        List<Booking> bookings = bookingController.getBookingHistory(customer);
        
        if (bookings.isEmpty()) {
            bookingsTable.setPlaceholder(new Label("You have no bookings yet. Try searching for flights!"));
        } else {
            bookingsTable.setItems(FXCollections.observableArrayList(bookings));
        }
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
        
        Button viewButton = new Button("View Ticket");
        viewButton.setDisable(true);
        
        Button cancelButton = new Button("Cancel Booking");
        cancelButton.setDisable(true);
        
        // Enable buttons when a booking is selected
        bookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean enableButtons = newSelection != null;
            viewButton.setDisable(!enableButtons);
            
            // Only enable cancel button if booking is not already cancelled
            if (enableButtons && newSelection.getStatus() != BookingStatus.CANCELLED) {
                cancelButton.setDisable(false);
            } else {
                cancelButton.setDisable(true);
            }
        });
        
        viewButton.setOnAction(e -> {
            Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
            if (selectedBooking != null) {
                TicketView ticketView = new TicketView(customer, selectedBooking);
                view.getScene().setRoot(ticketView.getView());
            }
        });
        
        cancelButton.setOnAction(e -> {
            Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
            if (selectedBooking != null) {
                // Confirmation dialog
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Cancel Booking");
                confirmDialog.setHeaderText("Are you sure you want to cancel this booking?");
                confirmDialog.setContentText("This action cannot be undone.");
                
                confirmDialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        boolean cancelled = bookingController.cancelBooking(selectedBooking.getBookingId());
                        
                        if (cancelled) {
                            showAlert("Booking Cancelled", "Your booking has been cancelled successfully.", Alert.AlertType.INFORMATION);
                            
                            // Refresh the view
                            BookingHistoryView refreshedView = new BookingHistoryView(customer);
                            view.getScene().setRoot(refreshedView.getView());
                        } else {
                            showAlert("Error", "Could not cancel booking. Please try again.");
                        }
                    }
                });
            }
        });
        
        actionButtons.getChildren().addAll(viewButton, cancelButton);
        
        centerContent.getChildren().addAll(bookingsTable, actionButtons);
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