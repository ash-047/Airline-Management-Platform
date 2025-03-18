package com.airline.view;

import com.airline.controller.PaymentController;
import com.airline.model.*;
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

public class PaymentView {
    private BorderPane view;
    private Customer customer;
    private Booking booking;
    
    public PaymentView(Customer customer, Booking booking) {
        this.customer = customer;
        this.booking = booking;
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        
        // Top section
        VBox topSection = new VBox(10);
        
        Text title = new Text("Payment");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Button backButton = new Button("Back to Booking");
        backButton.setOnAction(e -> {
            BookingView bookingView = new BookingView(customer, booking.getFlight());
            view.getScene().setRoot(bookingView.getView());
        });
        
        HBox backBox = new HBox(backButton);
        backBox.setAlignment(Pos.CENTER_LEFT);
        
        topSection.getChildren().addAll(backBox, title);
        view.setTop(topSection);
        
        // Center content - Payment form
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(10));
        
        // Booking summary
        VBox summaryBox = new VBox(5);
        summaryBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");
        
        Text summaryTitle = new Text("Booking Summary");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Text flightInfo = new Text(String.format("Flight: %s - %s to %s", 
            booking.getFlight().getFlightNumber(),
            booking.getFlight().getSource().getCode(), 
            booking.getFlight().getDestination().getCode()));
        
        Text departureInfo = new Text("Departure: " + booking.getFlight().getDepartureTime().toString());
        Text seatsInfo = new Text("Seats: " + (booking.getSeats() != null ? booking.getSeats().size() : 0));
        Text totalInfo = new Text("Total Amount: $" + booking.getTotalAmount());
        
        summaryBox.getChildren().addAll(summaryTitle, flightInfo, departureInfo, seatsInfo, totalInfo);
        
        // Payment method selection
        VBox paymentMethodBox = new VBox(10);
        
        Text methodTitle = new Text("Select Payment Method");
        methodTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        ToggleGroup paymentToggle = new ToggleGroup();
        
        RadioButton creditCardBtn = new RadioButton("Credit Card");
        creditCardBtn.setToggleGroup(paymentToggle);
        creditCardBtn.setSelected(true); // Default
        
        RadioButton debitCardBtn = new RadioButton("Debit Card");
        debitCardBtn.setToggleGroup(paymentToggle);
        
        RadioButton netBankingBtn = new RadioButton("Net Banking");
        netBankingBtn.setToggleGroup(paymentToggle);
        
        paymentMethodBox.getChildren().addAll(methodTitle, creditCardBtn, debitCardBtn, netBankingBtn);
        
        // Card details form (shown when credit/debit card selected)
        GridPane cardForm = new GridPane();
        cardForm.setHgap(10);
        cardForm.setVgap(10);
        cardForm.setPadding(new Insets(10));
        
        Label cardNumberLabel = new Label("Card Number:");
        cardForm.add(cardNumberLabel, 0, 0);
        
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("XXXX-XXXX-XXXX-XXXX");
        cardForm.add(cardNumberField, 1, 0);
        
        Label nameOnCardLabel = new Label("Name on Card:");
        cardForm.add(nameOnCardLabel, 0, 1);
        
        TextField nameOnCardField = new TextField();
        cardForm.add(nameOnCardField, 1, 1);
        
        Label expiryLabel = new Label("Expiry Date:");
        cardForm.add(expiryLabel, 0, 2);
        
        HBox expiryBox = new HBox(5);
        ComboBox<String> monthCombo = new ComboBox<>();
        for (int i = 1; i <= 12; i++) {
            monthCombo.getItems().add(String.format("%02d", i));
        }
        
        ComboBox<String> yearCombo = new ComboBox<>();
        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 10; i++) {
            yearCombo.getItems().add(String.valueOf(currentYear + i));
        }
        
        monthCombo.setValue("01");
        yearCombo.setValue(String.valueOf(currentYear));
        
        expiryBox.getChildren().addAll(new Label("Month:"), monthCombo, new Label("Year:"), yearCombo);
        cardForm.add(expiryBox, 1, 2);
        
        Label cvvLabel = new Label("CVV:");
        cardForm.add(cvvLabel, 0, 3);
        
        PasswordField cvvField = new PasswordField();
        cvvField.setPromptText("XXX");
        cardForm.add(cvvField, 1, 3);
        
        // Payment button
        Button payButton = new Button("Pay Now");
        payButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        payButton.setPrefSize(200, 40);
        
        payButton.setOnAction(e -> {
            // Validate inputs
            if (creditCardBtn.isSelected() || debitCardBtn.isSelected()) {
                if (cardNumberField.getText().isEmpty() || nameOnCardField.getText().isEmpty() || cvvField.getText().isEmpty()) {
                    showAlert("Input Error", "Please fill in all card details.");
                    return;
                }
            }
            
            // Determine payment method
            PaymentMethod paymentMethod;
            if (creditCardBtn.isSelected()) {
                paymentMethod = PaymentMethod.CREDIT_CARD;
            } else if (debitCardBtn.isSelected()) {
                paymentMethod = PaymentMethod.DEBIT_CARD;
            } else {
                paymentMethod = PaymentMethod.NET_BANKING;
            }
            
            // Process payment
            PaymentController paymentController = PaymentController.getInstance();
            boolean success = paymentController.processPayment(booking.getBookingId(), paymentMethod);
            
            if (success) {
                // Show confirmation and navigate to ticket view
                showAlert("Payment Successful", "Your payment has been processed successfully.", Alert.AlertType.INFORMATION);
                
                // Generate ticket and show ticket view
                TicketView ticketView = new TicketView(customer, booking);
                view.getScene().setRoot(ticketView.getView());
            } else {
                showAlert("Payment Failed", "There was an error processing your payment. Please try again.");
            }
        });
        
        centerContent.getChildren().addAll(summaryBox, paymentMethodBox, cardForm, payButton);
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