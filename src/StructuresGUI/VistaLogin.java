/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StructuresGUI;

import Main.FastEat;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import modelos.Administrador;
import modelos.Cuenta;
import modelos.Mesero;
import modelos.Usuario;

/**
 * Clase para crear a vista del logeo
 * @author josue
 * @author michael
 * @author edwin
 */
public class VistaLogin {
    private Stage stageLogin;
    private Scene escenaLogin;
    private VBox login;
    private VistaAdmin admin;
    private VistaMesero mesero;
    
    /**
     * Constructor que crea la estructura de la ventana
     * @param stage Recibe el stage principal del programa
     */
    public VistaLogin(Stage stage){
        stageLogin = new Stage();
        crearEstructura(stage);
    }
    
    /**
     * Metodo que crea la estructura basica para la ventana del logeo
     * @param stage Recibe el stage principal para modificarlo
     */
    public void crearEstructura(Stage stage){
        login = new VBox();
        escenaLogin = new Scene(login,400,700);
        Label usrLabel = new Label("Correo");
        Label pasLabel = new Label("Contraseña");
        TextField usrTField = new TextField();
        TextField pasTField = new TextField();
        HBox usrBox = new HBox(usrLabel,usrTField);
        usrBox.setSpacing(20);
        usrBox.setAlignment(Pos.CENTER);
        HBox pasBox = new HBox(pasLabel,pasTField);
        pasBox.setSpacing(10);
        pasBox.setAlignment(Pos.CENTER);
        Button ingresar = new Button("Ingresar");
        login.getChildren().addAll(usrBox,pasBox,ingresar);
        login.setAlignment(Pos.CENTER);
        stageLogin.setTitle("Inicio de sesión");
        stageLogin.setScene(escenaLogin);
        stageLogin.show();
        
        ingresar.setOnMouseClicked((event)->{
            String corr = usrTField.getText();
            String pass = pasTField.getText();
            Cuenta cuenta = FastEat.sistema.buscarCuenta(corr, pass);
            Usuario u = cuenta.getUser();
            if(cuenta!=null){
                if(u instanceof Administrador){
                    stageLogin.close();
                    admin = new VistaAdmin(stage);
                }else if(u instanceof Mesero){
                    stageLogin.close();
                    mesero = new VistaMesero(stage,cuenta);
                }
                
            }
        });
     
    }

    public VBox getRoot() {
        return login;
    }
    
    /**
     * Metodo que detiene los hilos del admin y del mesero
     */
    public void detenerHilos() {
        if (admin != null) {
            admin.detenerHilo();
        }
        else if(mesero != null) {
            mesero.detenerHilo();    
        }
        
    }
    
}
