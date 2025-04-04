package com.airline.view;

import com.airline.controller.FlightController;
import com.airline.model.Customer;
import com.airline.model.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;
import java.util.List;

public class FlightSearchView {
    private BorderPane view;
    private Customer customer;
    private TableView<Flight> flightsTable;
    
    public FlightSearchView(Customer customer) {
        this.customer = customer;
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        
        // Top section - Title and back button
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Text title = new Text("Search Flights");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            CustomerDashboardView dashboardView = new CustomerDashboardView(customer);
            view.getScene().setRoot(dashboardView.getView());
        });
        
        topBar.getChildren().addAll(title, backButton);
        view.setTop(topBar);
        
        // Search form
        GridPane searchForm = new GridPane();
        searchForm.setAlignment(Pos.CENTER);
        searchForm.setHgap(10);
        searchForm.setVgap(10);
        searchForm.setPadding(new Insets(20));
        
        Label sourceLabel = new Label("From:");
        searchForm.add(sourceLabel, 0, 0);
        
        // For demo purposes, using TextField instead of a proper dropdown
        ComboBox<String> sourceCombo = new ComboBox<>();
        sourceCombo.setPromptText("Select departure airport");
        sourceCombo.setEditable(true); // Allow custom input
        sourceCombo.getItems().addAll(
            "JFK", "LAX", "ORD", "ATL", "LHR", "CDG", "DXB", "SIN", "HND", "SYD"
        );
        sourceCombo.setPrefWidth(200);
        searchForm.add(sourceCombo, 1, 0);
        
        Label destLabel = new Label("To:");
        searchForm.add(destLabel, 0, 1);
        
        // For demo purposes, using TextField instead of a proper dropdown
        ComboBox<String> destCombo = new ComboBox<>();
        destCombo.setPromptText("Select arrival airport");
        destCombo.setEditable(true); // Allow custom input
        destCombo.getItems().addAll(
            "JFK", "LAX", "ORD", "ATL", "LHR", "CDG", "DXB", "SIN", "HND", "SYD"
        );
        destCombo.setPrefWidth(200);
        searchForm.add(destCombo, 1, 1);
        
        Label dateLabel = new Label("Departure Date:");
        searchForm.add(dateLabel, 0, 2);
        
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now().plusDays(1)); // Default to tomorrow
        searchForm.add(datePicker, 1, 2);
        
        Button searchButton = new Button("Search Flights");
        searchButton.setOnAction(e -> {
            String source = sourceCombo.getValue();
            String destination = destCombo.getValue();
            LocalDate date = datePicker.getValue();
            
            if (source == null || source.trim().isEmpty() || 
                destination == null || destination.trim().isEmpty() || 
                date == null) {
                showAlert("Search Error", "Please fill in all search fields.");
                return;
            }
            
            searchFlights(source.trim().toUpperCase(), destination.trim().toUpperCase(), date);
        });
        
        searchForm.add(searchButton, 1, 3);
        
        // Table for search results
        flightsTable = new TableView<>();
        flightsTable.setPlaceholder(new Label("Search for flights to see results"));
        
        TableColumn<Flight, String> flightNumberCol = new TableColumn<>("Flight");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        
        TableColumn<Flight, String> sourceCol = new TableColumn<>("From");
        sourceCol.setCellValueFactory(data -> {
            if (data.getValue().getSource() != null) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getSource().getCode());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        TableColumn<Flight, String> destCol = new TableColumn<>("To");
        destCol.setCellValueFactory(data -> {
            if (data.getValue().getDestination() != null) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getDestination().getCode());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        TableColumn<Flight, String> depTimeCol = new TableColumn<>("Departure");
        depTimeCol.setCellValueFactory(data -> {
            if (data.getValue().getDepartureTime() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    data.getValue().getDepartureTime().toLocalTime().toString()
                );
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        
        TableColumn<Flight, String> arrTimeCol = new TableColumn<>("Arrival");
        arrTimeCol.setCellValueFactory(data -> {
            if (data.getValue().getArrivalTime() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    data.getValue().getArrivalTime().toLocalTime().toString()
                );
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
        
        flightsTable.getColumns().addAll(flightNumberCol, sourceCol, destCol, depTimeCol, arrTimeCol, statusCol);
        
        Button bookButton = new Button("Book Selected Flight");
        bookButton.setDisable(true);
        
        // Enable the book button when a flight is selected
        flightsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            bookButton.setDisable(newSelection == null);
        });
        
        bookButton.setOnAction(e -> {
            Flight selectedFlight = flightsTable.getSelectionModel().getSelectedItem();
            if (selectedFlight != null) {
                BookingView bookingView = new BookingView(customer, selectedFlight);
                view.getScene().setRoot(bookingView.getView());
            }
        });
        
        VBox resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(10));
        resultsBox.getChildren().addAll(flightsTable, bookButton);
        
        // Combine form and results in the center pane
        VBox centerContent = new VBox(20);
        centerContent.getChildren().addAll(searchForm, resultsBox);
        view.setCenter(centerContent);
    }
    
    private void searchFlights(String source, String destination, LocalDate date) {
        FlightController flightController = FlightController.getInstance();
        List<Flight> results = flightController.searchFlights(source, destination, date);
        
        if (results.isEmpty()) {
            showAlert("No Flights Found", "No flights found matching your criteria.", Alert.AlertType.INFORMATION);
        } else {
            ObservableList<Flight> flightData = FXCollections.observableArrayList(results);
            flightsTable.setItems(flightData);
        }
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