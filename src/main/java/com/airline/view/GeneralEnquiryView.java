package com.airline.view;

import java.util.List;

import com.airline.controller.EnquiryController;
import com.airline.model.Customer;
import com.airline.model.Enquiry;
import com.airline.model.EnquiryStatus;

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

public class GeneralEnquiryView {
    private BorderPane view;
    private Customer customer;
    
    public GeneralEnquiryView(Customer customer) {
        this.customer = customer;
        createView();
    }
    
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        
        // Top section
        HBox topSection = new HBox(20);
        topSection.setAlignment(Pos.CENTER_LEFT);
        
        Text title = new Text("General Enquiries");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            CustomerDashboardView dashboardView = new CustomerDashboardView(customer);
            view.getScene().setRoot(dashboardView.getView());
        });
        
        topSection.getChildren().addAll(backButton, title);
        view.setTop(topSection);
        
        // Center content - Enquiry form
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        
        // FAQs section
        TitledPane faqsPane = new TitledPane();
        faqsPane.setText("Frequently Asked Questions");
        
        VBox faqsContent = new VBox(10);
        faqsContent.setPadding(new Insets(10));
        
        addFAQ(faqsContent, "How do I check in online?", 
               "You can check in online starting 24 hours before your flight and up to 1 hour before departure. " +
               "Just log in to your account, go to My Bookings, select your flight, and click the Check-in button.");
        
        addFAQ(faqsContent, "What is the baggage allowance?", 
               "Economy class passengers are allowed one checked bag up to 23kg and one carry-on bag up to 7kg. " +
               "Business class passengers are allowed two checked bags up to 32kg each and two carry-on bags up to 7kg each.");
        
        addFAQ(faqsContent, "How can I change or cancel my booking?", 
               "You can change or cancel your booking through My Bookings section. Select your booking and click on " +
               "the appropriate option. Please note that fees may apply depending on your fare type and timing.");
        
        addFAQ(faqsContent, "What if my flight is delayed or cancelled?", 
               "In case of delays or cancellations, we will notify you via email and SMS. You can also check the status " +
               "of your flight on our website. Our team will assist you with rebooking options or refunds as applicable.");
        
        faqsPane.setContent(faqsContent);
        faqsPane.setExpanded(true);
        
        // Contact form
        GridPane contactForm = new GridPane();
        contactForm.setHgap(10);
        contactForm.setVgap(10);
        contactForm.setPadding(new Insets(20));
        
        Text contactTitle = new Text("Contact Us");
        contactTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        contactForm.add(contactTitle, 0, 0, 2, 1);
        
        Label nameLabel = new Label("Name:");
        contactForm.add(nameLabel, 0, 1);
        
        TextField nameField = new TextField();
        nameField.setText(customer.getUsername());
        contactForm.add(nameField, 1, 1);
        
        Label emailLabel = new Label("Email:");
        contactForm.add(emailLabel, 0, 2);
        
        TextField emailField = new TextField();
        emailField.setText(customer.getEmail());
        contactForm.add(emailField, 1, 2);
        
        Label phoneLabel = new Label("Phone:");
        contactForm.add(phoneLabel, 0, 3);
        
        TextField phoneField = new TextField();
        phoneField.setText(customer.getPhoneNumber());
        contactForm.add(phoneField, 1, 3);
        
        Label subjectLabel = new Label("Subject:");
        contactForm.add(subjectLabel, 0, 4);
        
        ComboBox<String> subjectCombo = new ComboBox<>();
        subjectCombo.getItems().addAll(
            "Booking Enquiry",
            "Flight Status",
            "Refund Request",
            "Baggage Enquiry",
            "Other"
        );
        subjectCombo.setValue("Booking Enquiry");
        contactForm.add(subjectCombo, 1, 4);
        
        Label messageLabel = new Label("Message:");
        contactForm.add(messageLabel, 0, 5);
        
        TextArea messageArea = new TextArea();
        messageArea.setPrefRowCount(5);
        contactForm.add(messageArea, 1, 5);
        
        Button submitButton = new Button("Submit Enquiry");
        submitButton.setOnAction(e -> {
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || messageArea.getText().isEmpty()) {
                showAlert("Input Error", "Please fill in all required fields.");
                return;
            }
            
            // Submit the enquiry using the controller
            EnquiryController enquiryController = EnquiryController.getInstance();
            boolean success = enquiryController.submitEnquiry(
                customer, 
                subjectCombo.getValue(), 
                messageArea.getText()
            );
            
            if (success) {
                showAlert("Enquiry Submitted", 
                        "Your enquiry has been submitted successfully. Our team will respond within 24 hours.", 
                        Alert.AlertType.INFORMATION);
                
                // Clear the form
                messageArea.clear();
                
                // Show the user's enquiries including this new one
                showUserEnquiries();
            } else {
                showAlert("Submission Error", "Failed to submit your enquiry. Please try again.");
            }
        });
        
        contactForm.add(submitButton, 1, 6);
        
        // Contact information
        VBox contactInfo = new VBox(10);
        contactInfo.setPadding(new Insets(20));
        contactInfo.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 5px;");
        
        Text contactInfoTitle = new Text("Contact Information");
        contactInfoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Text phone = new Text("Phone: +1-800-123-4567");
        Text email = new Text("Email: support@skywardairlines.com");
        Text hours = new Text("Hours: 24/7 Customer Support");
        
        contactInfo.getChildren().addAll(contactInfoTitle, phone, email, hours);
        
        // Add a section to view previous enquiries and their responses
        TitledPane previousEnquiriesPane = new TitledPane();
        previousEnquiriesPane.setText("Your Previous Enquiries");
        
        VBox enquiriesBox = new VBox(10);
        enquiriesBox.setPadding(new Insets(10));
        
        // This will load and display the user's previous enquiries
        showUserEnquiriesInBox(enquiriesBox);
        
        previousEnquiriesPane.setContent(enquiriesBox);
        previousEnquiriesPane.setExpanded(true);
        
        // Add all components to centerContent once
        centerContent.getChildren().addAll(faqsPane, contactForm, contactInfo, previousEnquiriesPane);
        view.setCenter(new ScrollPane(centerContent));
    }

    private void showUserEnquiriesInBox(VBox container) {
    EnquiryController enquiryController = EnquiryController.getInstance();
    List<Enquiry> userEnquiries = enquiryController.getCustomerEnquiries(customer);
    
    if (userEnquiries.isEmpty()) {
        container.getChildren().add(new Label("You have no previous enquiries."));
        return;
    }
    
    for (Enquiry enquiry : userEnquiries) {
        TitledPane enquiryPane = new TitledPane();
        enquiryPane.setText(enquiry.getSubject() + " (" + enquiry.getStatus() + ")");
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        content.getChildren().add(new Label("Submitted on: " + enquiry.getSubmissionDate().toString()));
        content.getChildren().add(new Label("Message:"));
        
        TextArea messageArea = new TextArea(enquiry.getMessage());
        messageArea.setEditable(false);
        messageArea.setPrefRowCount(3);
        messageArea.setWrapText(true);
        content.getChildren().add(messageArea);
        
        if (enquiry.getStatus() == EnquiryStatus.RESOLVED && enquiry.getResponse() != null) {
            content.getChildren().add(new Label("Response:"));
            
            TextArea responseArea = new TextArea(enquiry.getResponse());
            responseArea.setEditable(false);
            responseArea.setPrefRowCount(3);
            responseArea.setWrapText(true);
            content.getChildren().add(responseArea);
            
            content.getChildren().add(new Label("Responded on: " + enquiry.getResponseDate().toString()));
        } else {
            content.getChildren().add(new Label("Status: Awaiting Response"));
        }
        
        enquiryPane.setContent(content);
        container.getChildren().add(enquiryPane);
    }
}

private void showUserEnquiries() {
    VBox centerContent = (VBox) ((ScrollPane) view.getCenter()).getContent();
    
    // Find the previous enquiries pane
    for (int i = 0; i < centerContent.getChildren().size(); i++) {
        if (centerContent.getChildren().get(i) instanceof TitledPane &&
            ((TitledPane) centerContent.getChildren().get(i)).getText().equals("Your Previous Enquiries")) {
            
            TitledPane enquiriesPane = (TitledPane) centerContent.getChildren().get(i);
            VBox enquiriesBox = new VBox(10);
            enquiriesBox.setPadding(new Insets(10));
            
            showUserEnquiriesInBox(enquiriesBox);
            
            enquiriesPane.setContent(enquiriesBox);
            break;
        }
    }
}
    
    private void addFAQ(VBox container, String question, String answer) {
        Accordion faqItem = new Accordion();
        
        TitledPane faqPane = new TitledPane();
        faqPane.setText(question);
        
        Label answerLabel = new Label(answer);
        answerLabel.setWrapText(true);
        answerLabel.setPadding(new Insets(10));
        
        faqPane.setContent(answerLabel);
        faqItem.getPanes().add(faqPane);
        
        container.getChildren().add(faqItem);
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