package com.airline.view;

import com.airline.Main;
import com.airline.controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class RegistrationView {
    private VBox view;
    
    public RegistrationView() {
        createView();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20, 20, 20, 20));
        view.setAlignment(Pos.CENTER);
        
        // Title
        Text title = new Text("Register New Account");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Registration Form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        
        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 0);
        
        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 0);
        
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);
        
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);
        
        Label confirmPasswordLabel = new Label("Confirm Password:");
        grid.add(confirmPasswordLabel, 0, 2);
        
        PasswordField confirmPasswordField = new PasswordField();
        grid.add(confirmPasswordField, 1, 2);
        
        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 3);
        
        TextField emailField = new TextField();
        grid.add(emailField, 1, 3);
        
        Label phoneLabel = new Label("Phone Number:");
        grid.add(phoneLabel, 0, 4);
        
        TextField phoneField = new TextField();
        grid.add(phoneField, 1, 4);
        
        // Register button
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                showAlert("Registration Error", "Please fill in all fields.");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                showAlert("Registration Error", "Passwords do not match.");
                return;
            }
            
            // Create user data map
            Map<String, String> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("password", password);
            userData.put("email", email);
            userData.put("phoneNumber", phone);
            
            // Register user
            UserController userController = UserController.getInstance();
            boolean success = userController.registerUser(userData);
            
            if (success) {
                showAlert("Registration Successful", "Your account has been created. Please login.", Alert.AlertType.INFORMATION);
                // Return to login screen
                Main.showLoginScreen();
            } else {
                showAlert("Registration Failed", "Unable to register user. Please try again.");
            }
        });
        
        Button backButton = new Button("Back to Login");
        backButton.setOnAction(e -> Main.showLoginScreen());
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(registerButton, backButton);
        
        grid.add(buttons, 1, 5);
        
        view.getChildren().addAll(title, grid);
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