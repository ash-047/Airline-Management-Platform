package com.airline.view;

import com.airline.Main;
import com.airline.controller.FlightController;
import com.airline.model.Admin;
import com.airline.model.Flight;
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

import java.util.List;

public class AdminDashboardView {
    private BorderPane view;
    private Admin admin;
    
    public AdminDashboardView(Admin admin) {
        this.admin = admin;
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        
        // Top section with welcome message and logout button
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label welcomeLabel = new Label("Welcome, " + admin.getUsername() + " (Admin)");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> Main.showLoginScreen());
        
        topBar.getChildren().addAll(welcomeLabel, logoutButton);
        view.setTop(topBar);
        
        // Side menu
        VBox sideMenu = new VBox(10);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setPrefWidth(200);
        sideMenu.setStyle("-fx-background-color: #f0f0f0;");
        
        Button manageFlightsBtn = new Button("Manage Flights");
        manageFlightsBtn.setPrefWidth(160);
        manageFlightsBtn.setOnAction(e -> showFlightsManagement());
        
        Button generateReportsBtn = new Button("Generate Reports");
        generateReportsBtn.setPrefWidth(160);
        
        Button viewBookingsBtn = new Button("View Bookings");
        viewBookingsBtn.setPrefWidth(160);
        
        sideMenu.getChildren().addAll(
            new Label("Admin Panel"),
            manageFlightsBtn,
            generateReportsBtn,
            viewBookingsBtn
        );
        
        view.setLeft(sideMenu);
        
        // Default center content - Flights management
        showFlightsManagement();
    }
    
    private void showFlightsManagement() {
        VBox flightsContent = new VBox(20);
        flightsContent.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Flights Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Flights table
        TableView<Flight> flightsTable = new TableView<>();
        
        TableColumn<Flight, String> flightIdCol = new TableColumn<>("Flight ID");
        flightIdCol.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        
        TableColumn<Flight, String> flightNumberCol = new TableColumn<>("Flight Number");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        
        TableColumn<Flight, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(data -> {
            if (data.getValue().getSource() != null) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getSource().getCode());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        TableColumn<Flight, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(data -> {
            if (data.getValue().getDestination() != null) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getDestination().getCode());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> {
            if (data.getValue().getStatus() != null) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus().toString());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        flightsTable.getColumns().addAll(flightIdCol, flightNumberCol, sourceCol, destinationCol, statusCol);
        
        // Load flight data
        FlightController flightController = FlightController.getInstance();
        List<Flight> flights = flightController.getAllFlights();
        flightsTable.setItems(FXCollections.observableArrayList(flights));
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        
        Button updateStatusBtn = new Button("Update Flight Status");
        updateStatusBtn.setOnAction(e -> {
            Flight selectedFlight = flightsTable.getSelectionModel().getSelectedItem();
            if (selectedFlight != null) {
                // Show dialog to update flight status
                // For brevity, implementation details are omitted
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Flight Status");
                alert.setHeaderText(null);
                alert.setContentText("Status update functionality would be implemented here.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText(null);
                alert.setContentText("Please select a flight to update.");
                alert.showAndWait();
            }
        });
        
        Button addFlightBtn = new Button("Add New Flight");
        
        actionButtons.getChildren().addAll(updateStatusBtn, addFlightBtn);
        
        flightsContent.getChildren().addAll(titleLabel, flightsTable, actionButtons);
        view.setCenter(flightsContent);
    }
    
    public Parent getView() {
        return view;
    }
}