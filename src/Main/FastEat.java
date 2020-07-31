    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import static LecturaEscritura.LECuentas.escribirCuentas;
import static LecturaEscritura.LEMesas.escribirMesas;
import static LecturaEscritura.LEPlatos.escribirPlatos;
import static LecturaEscritura.LEVentas.escribirArchivoVentas;
import Sistema.SistemaFE;
import StructuresGUI.VistaLogin;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main del Programa
 * @author josue
 * @author michael
 * @author edwin
 */
public class FastEat extends Application{
    public static Scene scene;
    public static SistemaFE sistema;
    private VistaLogin login;
        
    /**
     * Sobreescritura del metodo start
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage){
        sistema = new SistemaFE();
        scene = new Scene(new Pane(),1000,600);
        login = new VistaLogin(primaryStage);
    }
    
    /**
     * Sobreescritura del metodo stop para guardar todo el sistema
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    @Override
    public void stop() throws ClassNotFoundException, IOException{
        escribirCuentas();
        escribirMesas();
        escribirPlatos();
        escribirArchivoVentas();
        login.detenerHilos();
        
    }
    
    /**
     * Main
     * @param args 
     */
    public static void main(String[] args){
        launch(args);
    }
       
}
