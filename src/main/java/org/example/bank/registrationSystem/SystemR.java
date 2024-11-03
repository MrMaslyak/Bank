package org.example.bank.registrationSystem;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import org.example.bank.registrationSystem.database.DatabaseR;

import java.util.regex.Pattern;

public class SystemR {

    private boolean isEmailValid, isLoginValid, isPasswordValid;
    public Circle indicatorEmail, indicatorLogin, indicatorPassword;
    public PasswordField passwordS;
    public TextField loginS, emailS;
    public Label  errorMsg;

    public SystemR(Circle indicatorEmail, Circle indicatorLogin, Circle indicatorPassword, PasswordField passwordS, TextField loginS, TextField emailS, Label errorMsg) {
        this.indicatorEmail = indicatorEmail;
        this.indicatorLogin = indicatorLogin;
        this.indicatorPassword = indicatorPassword;
        this.passwordS = passwordS;
        this.loginS = loginS;
        this.emailS = emailS;
        this.errorMsg = errorMsg;
    }


    public void onLoginChanged() {
        String login = loginS.getText();

        if (isValidLogin(login) && !DatabaseR.getInstance().availableLogin(login)) {
            indicatorLogin.setStyle("-fx-fill: green");
            errorMsg.setVisible(false);
            setLoginValid(true);
        } else {
            indicatorLogin.setStyle("-fx-fill: red");
            setLoginValid(false);
        }

    }

    private boolean isValidLogin(String login) {
        String loginRegex = "^[a-zA-Z0-9_]{3,20}$";
        Pattern pat = Pattern.compile(loginRegex);
        if (login == null && DatabaseR.getInstance().availableLogin(login))
            return false;
        return pat.matcher(login).matches();
    }




    public void onPasswordChanged() {
        String password = passwordS.getText();
        if (isValidPassword(password) && password.length() >= 8) {
            indicatorPassword.setStyle("-fx-fill: green");
            setPasswordValid(true);
        } else {
            indicatorPassword.setStyle("-fx-fill: red");
            setPasswordValid(false);
        }

    }

    private boolean isValidPassword(String password) {

        String passwordRegex = "^[a-zA-Z0-9]{8,20}$";

        Pattern pat = Pattern.compile(passwordRegex);
        if (password == null)
            return false;
        return pat.matcher(password).matches();
    }

    public void onEmailChanged() {
        String email = emailS.getText();

        if (isValidEmail(email) && !DatabaseR.getInstance().availableEmail(email)) {
            indicatorEmail.setStyle("-fx-fill: green");
            errorMsg.setVisible(false);
            setEmailValid(true);
        } else {
            indicatorEmail.setStyle("-fx-fill: red");
            setEmailValid(false);
        }

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null && DatabaseR.getInstance().availableEmail(email))
            return false;
        return pat.matcher(email).matches();
    }

    public void setEmailValid(boolean emailValid) {
        isEmailValid = emailValid;
    }

    public void setLoginValid(boolean loginValid) {
        isLoginValid = loginValid;
    }

    public void setPasswordValid(boolean passwordValid) {
        isPasswordValid = passwordValid;
    }

    public boolean isEmailValid() {
        return isEmailValid;
    }

    public boolean isLoginValid() {
        return isLoginValid;
    }

    public boolean isPasswordValid() {
        return isPasswordValid;
    }
}
