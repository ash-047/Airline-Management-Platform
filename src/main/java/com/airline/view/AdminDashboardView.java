package com.airline.view;

import com.airline.Main;
import com.airline.controller.BookingController;
import com.airline.controller.FlightController;
// Add EnquiryController import
import com.airline.controller.EnquiryController;
import com.airline.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class AdminDashboardView {
    private BorderPane view;
    private Admin admin;
    private TableView<Flight> flightsTable;
    private TableView<Booking> bookingsTable;
    
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
        
        Button viewBookingsBtn = new Button("View Bookings");
        viewBookingsBtn.setPrefWidth(160);
        viewBookingsBtn.setOnAction(e -> showBookingsManagement());
        
        Button viewEnquiriesBtn = new Button("View Enquiries");
        viewEnquiriesBtn.setPrefWidth(160);
        viewEnquiriesBtn.setOnAction(e -> showEnquiriesManagement());
        
        sideMenu.getChildren().addAll(
            new Label("Admin Panel"),
            manageFlightsBtn,
            viewBookingsBtn,
            viewEnquiriesBtn
        );
        
        view.setLeft(sideMenu);
        
        // Default center content - Flights management
        showFlightsManagement();
    }

    private void showEnquiriesManagement() {
        VBox enquiriesContent = new VBox(20);
        enquiriesContent.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Customer Enquiries");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Enquiries table
        @SuppressWarnings({"unchecked", "rawtypes"})
        TableView<Enquiry> enquiriesTable = new TableView<>();
        
        @SuppressWarnings({"unchecked", "rawtypes"})
        TableColumn<Enquiry, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("enquiryId"));
        
        TableColumn<Enquiry, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCustomer() != null ? 
                data.getValue().getCustomer().getUsername() : "N/A"));
        
        TableColumn<Enquiry, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        
        TableColumn<Enquiry, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getStatus().toString()));
        
        TableColumn<Enquiry, LocalDateTime> dateCol = new TableColumn<>("Submission Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("submissionDate"));
        
        enquiriesTable.getColumns().addAll(idCol, customerCol, subjectCol, statusCol, dateCol);
        
        // Load all enquiries
        EnquiryController enquiryController = EnquiryController.getInstance();
        List<Enquiry> allEnquiries = enquiryController.getAllEnquiries();
        enquiriesTable.setItems(FXCollections.observableArrayList(allEnquiries));
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        
        Button viewDetailsBtn = new Button("View Details");
        Button respondBtn = new Button("Respond");
        
        viewDetailsBtn.setDisable(true);
        respondBtn.setDisable(true);
        
        // Enable buttons when an enquiry is selected
        enquiriesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean enableButtons = newSelection != null;
            viewDetailsBtn.setDisable(!enableButtons);
            respondBtn.setDisable(!enableButtons || newSelection.getStatus() == EnquiryStatus.RESOLVED);
        });
        
        viewDetailsBtn.setOnAction(e -> {
            Enquiry selectedEnquiry = enquiriesTable.getSelectionModel().getSelectedItem();
            if (selectedEnquiry != null) {
                showEnquiryDetails(selectedEnquiry);
            }
        });
        
        respondBtn.setOnAction(e -> {
            Enquiry selectedEnquiry = enquiriesTable.getSelectionModel().getSelectedItem();
            if (selectedEnquiry != null) {
                respondToEnquiry(selectedEnquiry, enquiriesTable);
            }
        });
        
        actionButtons.getChildren().addAll(viewDetailsBtn, respondBtn);
        
        enquiriesContent.getChildren().addAll(titleLabel, enquiriesTable, actionButtons);
        view.setCenter(enquiriesContent);
    }

    private void showEnquiryDetails(Enquiry enquiry) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Enquiry Details");
        dialog.setHeaderText("Enquiry from " + enquiry.getCustomer().getUsername());
        
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        grid.add(new Label("ID:"), 0, 0);
        grid.add(new Label(enquiry.getEnquiryId()), 1, 0);
        
        grid.add(new Label("From:"), 0, 1);
        grid.add(new Label(enquiry.getCustomer().getUsername() + " (" + enquiry.getCustomer().getEmail() + ")"), 1, 1);
        
        grid.add(new Label("Subject:"), 0, 2);
        grid.add(new Label(enquiry.getSubject()), 1, 2);
        
        grid.add(new Label("Date:"), 0, 3);
        grid.add(new Label(enquiry.getSubmissionDate().toString()), 1, 3);
        
        grid.add(new Label("Status:"), 0, 4);
        grid.add(new Label(enquiry.getStatus().toString()), 1, 4);
        
        grid.add(new Label("Message:"), 0, 5);
        
        TextArea messageArea = new TextArea(enquiry.getMessage());
        messageArea.setEditable(false);
        messageArea.setPrefRowCount(5);
        messageArea.setPrefColumnCount(30);
        messageArea.setWrapText(true);
        grid.add(messageArea, 1, 5);
        
        // Show response if available
        if (enquiry.getResponse() != null && !enquiry.getResponse().isEmpty()) {
            grid.add(new Label("Response:"), 0, 6);
            
            TextArea responseArea = new TextArea(enquiry.getResponse());
            responseArea.setEditable(false);
            responseArea.setPrefRowCount(5);
            responseArea.setPrefColumnCount(30);
            responseArea.setWrapText(true);
            grid.add(responseArea, 1, 6);
        }
        
        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    }
    
    private void respondToEnquiry(Enquiry enquiry, TableView<Enquiry> tableView) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Respond to Enquiry");
        dialog.setHeaderText("Enter your response to " + enquiry.getCustomer().getUsername());
        
        ButtonType respondButton = new ButtonType("Send Response", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(respondButton, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        grid.add(new Label("Original Enquiry:"), 0, 0);
        
        TextArea originalArea = new TextArea(enquiry.getMessage());
        originalArea.setEditable(false);
        originalArea.setPrefRowCount(5);
        originalArea.setWrapText(true);
        grid.add(originalArea, 0, 1);
        
        grid.add(new Label("Your Response:"), 0, 2);
        
        TextArea responseArea = new TextArea();
        responseArea.setPrefRowCount(5);
        responseArea.setWrapText(true);
        grid.add(responseArea, 0, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        Platform.runLater(() -> responseArea.requestFocus());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == respondButton) {
                return responseArea.getText();
            }
            return null;
        });
        
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(response -> {
            EnquiryController enquiryController = EnquiryController.getInstance();
            boolean success = enquiryController.respondToEnquiry(enquiry.getEnquiryId(), response);
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Response Sent", 
                         "Your response has been sent successfully.");
                
                // Refresh the table
                tableView.setItems(FXCollections.observableArrayList(
                    enquiryController.getAllEnquiries()));
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", 
                         "Failed to send response.");
            }
        });
    }

    private void showBookingsManagement() {
        VBox bookingsContent = new VBox(20);
        bookingsContent.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Bookings Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Bookings table
        bookingsTable = new TableView<>();
        setupBookingsTable();
        
        // Load all bookings
        BookingController bookingController = BookingController.getInstance();
        List<Booking> allBookings = bookingController.getAllBookings();
        bookingsTable.setItems(FXCollections.observableArrayList(allBookings));
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        Button viewDetailsBtn = new Button("View Details");
        Button updateStatusBtn = new Button("Update Status");
        actionButtons.getChildren().addAll(viewDetailsBtn, updateStatusBtn);
        
        bookingsContent.getChildren().addAll(titleLabel, bookingsTable, actionButtons);
        view.setCenter(bookingsContent);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setupBookingsTable() {
        TableColumn<Booking, String> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        
        TableColumn<Booking, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCustomer() != null ? 
                data.getValue().getCustomer().getUsername() : "N/A"));
        
        TableColumn<Booking, String> flightCol = new TableColumn<>("Flight");
        flightCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFlight() != null ? 
                data.getValue().getFlight().getFlightNumber() : "N/A"));
        
        TableColumn<Booking, String> routeCol = new TableColumn<>("Route");
        routeCol.setCellValueFactory(data -> {
            Flight flight = data.getValue().getFlight();
            if (flight != null && flight.getSource() != null && flight.getDestination() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    flight.getSource().getCode() + " → " + flight.getDestination().getCode());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        
        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getStatus() != null ? 
                data.getValue().getStatus().toString() : "N/A"));
        
        TableColumn<Booking, String> paymentCol = new TableColumn<>("Payment Status");
        paymentCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getPaymentStatus() != null ? 
                data.getValue().getPaymentStatus().toString() : "N/A"));
        
        bookingsTable.getColumns().addAll(bookingIdCol, customerCol, flightCol, 
                                         routeCol, statusCol, paymentCol);
    }
    
    private void showFlightsManagement() {
        VBox flightsContent = new VBox(20);
        flightsContent.setPadding(new Insets(20));
        
        Label titleLabel = new Label("Flights Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Flights table
        flightsTable = new TableView<>();
        setupFlightsTable();
        
        // Load flight data
        FlightController flightController = FlightController.getInstance();
        List<Flight> flights = flightController.getAllFlights();
        flightsTable.setItems(FXCollections.observableArrayList(flights));
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        Button updateStatusBtn = new Button("Update Flight Status");
        updateStatusBtn.setOnAction(e -> updateFlightStatus());
        
        Button addFlightBtn = new Button("Add New Flight");
        addFlightBtn.setOnAction(e -> addNewFlight());
        
        actionButtons.getChildren().addAll(updateStatusBtn, addFlightBtn);
        flightsContent.getChildren().addAll(titleLabel, flightsTable, actionButtons);
        view.setCenter(flightsContent);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setupFlightsTable() {
        TableColumn<Flight, String> flightIdCol = new TableColumn<>("Flight ID");
        flightIdCol.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        
        TableColumn<Flight, String> flightNumberCol = new TableColumn<>("Flight Number");
        flightNumberCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        
        TableColumn<Flight, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getSource() != null ? 
                data.getValue().getSource().getCode() : ""));
        
        TableColumn<Flight, String> destinationCol = new TableColumn<>("Destination");
        destinationCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDestination() != null ? 
                data.getValue().getDestination().getCode() : ""));
        
        TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getStatus() != null ? 
                data.getValue().getStatus().toString() : ""));
        
        flightsTable.getColumns().addAll(flightIdCol, flightNumberCol, sourceCol, destinationCol, statusCol);
    }
    
    private void updateFlightStatus() {
        Flight selectedFlight = flightsTable.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a flight to update.");
            return;
        }
        
        Dialog<FlightStatus> dialog = new Dialog<>();
        dialog.setTitle("Update Flight Status");
        dialog.setHeaderText("Select new status for flight " + selectedFlight.getFlightNumber());
        
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
        
        ComboBox<FlightStatus> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(FlightStatus.values());
        statusComboBox.setValue(selectedFlight.getStatus());
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Flight Status:"), 0, 0);
        grid.add(statusComboBox, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> statusComboBox.requestFocus());
        
        dialog.setResultConverter(dialogButton -> 
            dialogButton == updateButtonType ? statusComboBox.getValue() : null);
        
        Optional<FlightStatus> result = dialog.showAndWait();
        result.ifPresent(status -> {
            boolean success = FlightController.getInstance().updateFlightStatus(
                selectedFlight.getFlightId(), status);
            
            if (success) {
                refreshFlightsTable();
                showAlert(Alert.AlertType.INFORMATION, "Status Updated", 
                         "Flight status updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update flight status.");
            }
        });
    }
    
    private void addNewFlight() {
        Dialog<Flight> dialog = new Dialog<>();
        dialog.setTitle("Add New Flight");
        dialog.setHeaderText("Enter flight details");
        
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField flightNumberField = new TextField();
        flightNumberField.setPromptText("Flight Number");
        
        TextField sourceField = new TextField();
        sourceField.setPromptText("Source Airport (e.g., JFK)");
        
        TextField destField = new TextField();
        destField.setPromptText("Destination Airport (e.g., LAX)");
        
        DatePicker departureDatePicker = new DatePicker();
        departureDatePicker.setValue(LocalDate.now());
        
        Spinner<Integer> departureHourSpinner = new Spinner<>(0, 23, 9);
        Spinner<Integer> departureMinuteSpinner = new Spinner<>(0, 59, 0);
        
        Spinner<Integer> arrivalHourSpinner = new Spinner<>(0, 23, 11);
        Spinner<Integer> arrivalMinuteSpinner = new Spinner<>(0, 59, 0);
        
        // Build the grid
        grid.add(new Label("Flight Number:"), 0, 0);
        grid.add(flightNumberField, 1, 0);
        grid.add(new Label("Source Airport:"), 0, 1);
        grid.add(sourceField, 1, 1);
        grid.add(new Label("Destination Airport:"), 0, 2);
        grid.add(destField, 1, 2);
        grid.add(new Label("Departure Date:"), 0, 3);
        grid.add(departureDatePicker, 1, 3);
        grid.add(new Label("Departure Time (HH:MM):"), 0, 4);
        
        HBox departureTimeBox = new HBox(5);
        departureTimeBox.getChildren().addAll(departureHourSpinner, new Label(":"), departureMinuteSpinner);
        grid.add(departureTimeBox, 1, 4);
        
        grid.add(new Label("Arrival Time (HH:MM):"), 0, 5);
        
        HBox arrivalTimeBox = new HBox(5);
        arrivalTimeBox.getChildren().addAll(arrivalHourSpinner, new Label(":"), arrivalMinuteSpinner);
        grid.add(arrivalTimeBox, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> flightNumberField.requestFocus());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    Airport source = new Airport();
                    source.setCode(sourceField.getText());
                    source.setName(sourceField.getText());
                    
                    Airport destination = new Airport();
                    destination.setCode(destField.getText());
                    destination.setName(destField.getText());
                    
                    LocalDateTime departureTime = departureDatePicker.getValue().atTime(
                        departureHourSpinner.getValue(), departureMinuteSpinner.getValue());
                    
                    LocalDateTime arrivalTime = departureDatePicker.getValue().atTime(
                        arrivalHourSpinner.getValue(), arrivalMinuteSpinner.getValue());
                    
                    if (arrivalTime.isBefore(departureTime)) {
                        arrivalTime = arrivalTime.plusDays(1);
                    }
                    
                    String flightId = "F" + (100 + new Random().nextInt(900));
                    return new Flight(flightId, flightNumberField.getText(), 
                                    source, destination, departureTime, arrivalTime);
                } catch (Exception ex) {
                    return null;
                }
            }
            return null;
        });
        
        Optional<Flight> result = dialog.showAndWait();
        result.ifPresent(this::setupAndAddNewFlight);
    }
    
    private void setupAndAddNewFlight(Flight newFlight) {
        FlightController flightController = FlightController.getInstance();
        
        // Set default values for new flight
        Map<SeatCategory, Integer> seats = new HashMap<>();
        seats.put(SeatCategory.ECONOMY, 100);
        seats.put(SeatCategory.BUSINESS, 20);
        seats.put(SeatCategory.FIRST_CLASS, 10);
        newFlight.setAvailableSeats(seats);
        
        Map<SeatCategory, Double> prices = new HashMap<>();
        prices.put(SeatCategory.ECONOMY, 300.0);
        prices.put(SeatCategory.BUSINESS, 800.0);
        prices.put(SeatCategory.FIRST_CLASS, 1500.0);
        newFlight.setPrice(prices);
        
        boolean success = flightController.addFlight(newFlight);
        
        if (success) {
            refreshFlightsTable();
            showAlert(Alert.AlertType.INFORMATION, "Flight Added", "New flight added successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add new flight.");
        }
    }
    
    private void refreshFlightsTable() {
        flightsTable.setItems(FXCollections.observableArrayList(
            FlightController.getInstance().getAllFlights()));
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public Parent getView() {
        return view;
    }
}