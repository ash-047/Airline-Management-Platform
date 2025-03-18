package com.airline.view;

import com.airline.Main;
import com.airline.controller.UserController;
import com.airline.model.Admin;
import com.airline.model.Customer;
import com.airline.model.User;
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

public class LoginView {
    private VBox view;
    
    public LoginView() {
        createView();
    }
    
    private void createView() {
        view = new VBox(20);
        view.setPadding(new Insets(20, 20, 20, 20));
        view.setAlignment(Pos.CENTER);
        
        // Title
        Text title = new Text("Airline Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        // Login Form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        
        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 0);
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        grid.add(usernameField, 1, 0);
        
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        grid.add(passwordField, 1, 1);
        
        // Login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Login Error", "Please enter both username and password.");
                return;
            }
            
            // Attempt login
            UserController userController = UserController.getInstance();
            User user = userController.authenticateUser(username, password);
            
            if (user != null) {
                if (user instanceof Customer) {
                    // Navigate to Customer Dashboard
                    CustomerDashboardView customerView = new CustomerDashboardView((Customer) user);
                    Main.getPrimaryStage().getScene().setRoot(customerView.getView());
                } else if (user instanceof Admin) {
                    // Navigate to Admin Dashboard
                    AdminDashboardView adminView = new AdminDashboardView((Admin) user);
                    Main.getPrimaryStage().getScene().setRoot(adminView.getView());
                }
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        });
        
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            // Navigate to Registration View
            RegistrationView registrationView = new RegistrationView();
            Main.getPrimaryStage().getScene().setRoot(registrationView.getView());
        });
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(loginButton, registerButton);
        
        grid.add(buttons, 1, 2);
        
        // Add demo account information for testing
        Text demoInfo = new Text("Demo Accounts:\nCustomer - username: user, password: password\nAdmin - username: admin, password: admin123");
        demoInfo.setFont(Font.font("Arial", 12));
        
        view.getChildren().addAll(title, grid, demoInfo);
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