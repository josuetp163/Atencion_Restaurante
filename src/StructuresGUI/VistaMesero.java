/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StructuresGUI;

import LecturaEscritura.LEMesas;
import Main.FastEat;
import Sistema.SistemaFE;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import modelos.Cuenta;
import modelos.Estado;
import modelos.Mesa;
import modelos.Mesero;

/**
 * Clase para crear la vista del mesero
 * @author josue
 * @author michael
 * @author edwin
 */
public class VistaMesero {
    private actualizadorMesas acMesas;
    private Stage registroCliente;
    private Scene escenaRegistroCliente;
    private VBox rootRegistroCliente;
    private BorderPane root;
    private Pane areaMesas;
    private Mesero meseroLogeado;
    Label Lnombre;
    VistaPedido vistaPed;
    
    /**
     * Constructor de la vista Mesero que genera la vista principal para el mesero
     * @param stage Recibe el stage principal para modificarlo
     * @param c Recibe la cuenta que se esta logeando para poder usar su informacion
     */
    public VistaMesero(Stage stage , Cuenta c) {
        acMesas = new actualizadorMesas();
        Thread hiloMesas = new Thread(acMesas);
        meseroLogeado = (Mesero) c.getUser();
        System.out.println(meseroLogeado);
        rootRegistroCliente = new VBox();
        escenaRegistroCliente = new Scene(rootRegistroCliente,100,100);
        registroCliente = new Stage();
        registroCliente.setScene(escenaRegistroCliente);
        root = new BorderPane();
        areaMesas = new Pane();
        Lnombre = new Label(meseroLogeado.getNombre());
        Lnombre.setPadding(new Insets(15,15,15,15));
        Lnombre.setAlignment(Pos.CENTER);
        root.setTop(Lnombre);
        disenioPlanoRestaurante(stage);
        hiloMesas.start();
        
    }
    
    /**
     * Metodo que crea el diseño del plano del restaturante con todas las mesas
     * @param primaryStage Recibe el stage principal para modificarlo
     */
    public void disenioPlanoRestaurante(Stage primaryStage){
        cargarMesas();
        FastEat.scene.setRoot(root);
        primaryStage.setScene(FastEat.scene);
        primaryStage.setTitle("Mesero");
        primaryStage.show();
    }        
    
    /**
     * Metodo que carga las mesas del sistema y crea el plano del restaurante
     */
    public void cargarMesas(){
        try{
            root.getChildren().clear();
            SistemaFE.mesas = LecturaEscritura.LEMesas.cargarMesas();
            System.out.println("Se ejecuta cargarMesas");
            for(Mesa mesa : SistemaFE.mesas){
                StackPane circuloMesa = new StackPane();
                Label nMesa = new Label(String.valueOf(mesa.getnMesa()));
                Circle circulo = new Circle(mesa.getCapacidad()*10);                
                if(mesa.getEstado()==Estado.DISPONIBLE){
                    circulo.setFill(Color.LIGHTGREEN);
                }else if(mesa.getEstado() == Estado.OCUPADO){
                        if (mesa.getMesero().equals(meseroLogeado)) {
                            circulo.setFill(Color.YELLOW);        
                        }
                        else{
                           circulo.setFill(Color.CORAL);
                        }
                }else if(mesa.getEstado()==Estado.RESERVADO){
                    circulo.setFill(Color.CHOCOLATE);
                }
                circuloMesa.getChildren().addAll(circulo, nMesa);
                circuloMesa.setLayoutX(mesa.getX());
                circuloMesa.setLayoutY(mesa.getY());
                
                circuloMesa.setOnMouseClicked(e -> {
                    if(mesa.getEstado() == Estado.DISPONIBLE) {
                        stageRegistroCliente(mesa,circulo);
                        circuloMesa.getChildren().clear();
                        circuloMesa.getChildren().addAll(circulo,nMesa);
                    }
                    else if (mesa.getEstado() == Estado.OCUPADO && mesa.getMesero().equals(meseroLogeado)){
                        if(SistemaFE.ventas.contains(mesa.getVenta())){
                                vistaPed = new VistaPedido(mesa.getVenta(),mesa);
                                vistaPed.mostrarVentana();
                            }
                    }
                });
                System.out.println("Se crea el circulo");
                areaMesas.getChildren().add(circuloMesa);
                root.setCenter(areaMesas);                                                   
            }
        }catch(Exception ex) {
            System.out.println(ex);
        }        
    }
    
    /**
     * Metodo que corrobora el mesero logeado para determinar de que color poner la mesa
     * @param mesa Recibe la mesa que se esta determinando el color
     * @param circulo Recibe el circulo de la mesa que se esta creando
     */
    public void tareasMesero(Mesa mesa,Circle circulo){
        if(mesa.getEstado() == Estado.DISPONIBLE){
            meseroLogeado.abrirCuenta(mesa);
            mesa.setMesero(meseroLogeado);
            mesa.setEstado(Estado.OCUPADO);
            circulo.setFill(Color.YELLOW);
            try {
                LecturaEscritura.LEMesas.escribirMesas();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VistaMesero.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VistaMesero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }             
    }
    
    /**
     * Metodo que crea el nuevo stage para registrar el nombre del cliente para abrir una nueva cuenta
     * @param mesa Recibe la mesa en la cual se registrara la nueva cuenta
     * @param circulo Recibe el circulo donde se da el click para registrar la nueva cuenta
     */
    public void stageRegistroCliente(Mesa mesa , Circle circulo) {
        registroCliente.show();
        rootRegistroCliente.getChildren().clear();
        Label l1 = new Label("Cliente: ");
        TextField t1 = new TextField();
        mesa.setCliente(t1.getText());
        Button aceptar = new Button("Aceptar");
        rootRegistroCliente.getChildren().addAll(l1,t1,aceptar);
        
        aceptar.setOnAction(e -> {
            registroCliente.close();
            tareasMesero( mesa,circulo);
        });       
    }
    
    /**
     * Inner Class para el hilo que actualiza las mesas en el plano
     */
    private class actualizadorMesas implements Runnable {
        
        private boolean detener;
        
        /**
         * Metodo sobreescrito run donde se carga todo el plano para actualizarlo
         */
        @Override
        public void run() {
            while(!detener) {
                try {
                    sleep(10000);
                    Platform.runLater(() -> {
                        try {
                            SistemaFE.mesas = LecturaEscritura.LEMesas.cargarMesas();
                            cargarMesas();
                        } catch (IOException ex) {
                            Logger.getLogger(VistaMesero.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(VistaMesero.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(VistaMesero.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        
        /**
         * setea la orden de detener
         */
        public void parar() {
            detener = true;
        }
    }
    
    /**
     * Metodo para detener los hilos creados
     */
    public void detenerHilo() {
        acMesas.parar();
        if(vistaPed != null) {
            vistaPed.Parar();    
        }
        
    }
}