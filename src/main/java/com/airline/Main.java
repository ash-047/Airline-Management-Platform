package com.airline;

import com.airline.controller.UserController;
import com.airline.model.Admin;
import com.airline.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Airline Management System");
        
        // Create sample admin for demonstration
        UserController userController = UserController.getInstance();
        Admin admin = userController.createSampleAdmin();
        
        // Show login screen
        showLoginScreen();
        
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();
    }
    
    public static void showLoginScreen() {
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}