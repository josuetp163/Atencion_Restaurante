/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StructuresGUI;

import LecturaEscritura.LEVentas;
import Sistema.SistemaFE;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.time.LocalDateTime;
import modelos.Venta;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author ADMIN
 */
public class VistaReporte {       
    private BorderPane root;
    private TableView tabla;
    ActualizarReporte ar;
    ArrayList<Venta> filtrao;
             
    public VistaReporte(Stage stage){
        filtrao = new ArrayList<>();
        root = new BorderPane();        
        crearReporte(stage);
        ar = new ActualizarReporte();
        Thread hilo = new Thread(ar);
    }
    
    public void crearReporte(Stage primaryStage){        
        agregarContenido();                
        primaryStage.show();
    }
    
    public void agregarContenido(){
        root.getChildren().clear();
        HBox up = new HBox();
        Label fechini = new Label("F Inicio");
        TextField izq = new TextField();
        Label fechfin = new Label("F Fin");        
        TextField der = new TextField();
        Button buscar = new Button("Buscar");
        filtrao = SistemaFE.getVentas();
        crearTabla(filtrao);
        buscar.setOnMouseClicked(e->{
            try{
                int anio = Integer.parseInt(izq.getText().split("-")[2].split(" ")[0]);
                int mes = Integer.parseInt(izq.getText().split("-")[1]);
                int dia = Integer.parseInt(izq.getText().split("-")[0]);
                int h = Integer.parseInt(izq.getText().split(" ")[1].split(":")[0]);
                int m = Integer.parseInt(izq.getText().split(" ")[1].split(":")[1]);
                LocalDateTime fechaInicial = LocalDateTime.of(anio, mes, dia, h, m);
                System.out.println(fechaInicial);

                int anio2 = Integer.parseInt(der.getText().split("-")[2].split(" ")[0]);
                int mes2 = Integer.parseInt(der.getText().split("-")[1]);
                int dia2 = Integer.parseInt(der.getText().split("-")[0]);
                int h2 = Integer.parseInt(der.getText().split(" ")[1].split(":")[0]);
                int m2 = Integer.parseInt(der.getText().split(" ")[1].split(":")[1]);
                LocalDateTime fechaFinal = LocalDateTime.of(anio2, mes2, dia2, h2, m2);
                System.out.println(fechaFinal);
                filtrao = SistemaFE.filtrarVentas(fechaInicial, fechaFinal);          
                crearTabla(filtrao);
            }catch(IndexOutOfBoundsException ex){
                System.out.println("Error al filtrar las ventas\nNo se han comlpletado los 5 campos para la fecha");
                root.setCenter(new Label("La fecha debe ser ingresada de la siguiente manera: \ndd-MM-YYYY hh:mm"));
                System.out.println(ex);
            }catch(NumberFormatException nfex){
                System.out.println("Error al filtrar las ventas\nEl valor ingresado no son numeros enteros");
                root.setCenter(new Label("La fecha debe ser ingresada de la siguiente manera: \ndd-MM-YYYY hh:mm"));
                System.out.println(nfex);
            }
        });
        up.setSpacing(10);
        up.getChildren().addAll(fechini, izq, fechfin, der, buscar);
        root.setTop(up);
    }
        
    public void crearTabla(ArrayList<Venta> filtrado){
        if (filtrado != null){
            tabla = new TableView();
            ArrayList<String> campos = new ArrayList<>();
            campos.add("fecha");campos.add("noMesa");campos.add("noMesero");campos.add("noVenta");campos.add("cliente");campos.add("total");

            for (String campo: campos){
                TableColumn<String, Venta> colum = new TableColumn<>(campo);
                colum.setCellValueFactory(new PropertyValueFactory<>(campo.toLowerCase()));
                tabla.getColumns().add(colum);
            }        
            tabla.getItems().clear();                
            for(Venta vent: filtrado){
                tabla.getItems().add(vent);
            }                
            root.setCenter(tabla);
        }else{
            System.out.println("No se crean tablas \nFiltros vacios");
        }
    }
    
    public BorderPane getRoot(){
        return root;
    }            
    
    private class ActualizarReporte implements Runnable {
        boolean detener;               
        

        @Override
        public void run() {
            try {
                while(!detener){
                    sleep(10000);
                    LEVentas.escribirArchivoVentas();
                    SistemaFE.setVentas(LecturaEscritura.LEVentas.LeerArchivoVentas());
                    Platform.runLater(() -> {
                        crearTabla(filtrao);
                    });
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(VistaReporte.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VistaReporte.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VistaReporte.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void parar(){
            detener = true;
        }                
    }
    
    public void detenerHilo() {
        ar.parar();
        System.out.println("Se detiene el hilo de reportes ADMIN");
    }
}
