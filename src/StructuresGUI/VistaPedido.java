/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StructuresGUI;

import Sistema.SistemaFE;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelos.Estado;
import modelos.Mesa;
import modelos.Mesero;
import modelos.Plato;
import modelos.Venta;

/**
 * Clase para crear la vista para realizar los pedidos de los platos y llevar la cuenta
 * @author josue
 * @author michael
 * @author edwin
 */
public class VistaPedido {
    private BorderPane ventana;
    private VBox left;
    private VBox cuenta;
    private VBox acciones;
    private VBox organizacion;   
    private HBox categorias;
    private FlowPane menu;
    private HBox busqueda;
    private TextField filtro;
    private Venta venta;
    private Mesa mesa;
    private Stage pedido;
    private Mesero mesero;
    private ActualizadorMenu amenu;
    
    /**
     * Constructor de la vista pedido que genera la ventna principal de la ventana para el pedido
     * @param vent Recibe la venta que se esta creando en el sistema
     * @param mes  Recibe la mesa donde se esta realizando el pedido
     */
    public VistaPedido(Venta vent,Mesa mes){
        busqueda = new HBox();
        organizacion = new VBox();
        this.venta=vent;
        this.mesa=mes;
        mesero = mes.getMesero();
        filtro = new TextField();
        ventana = new BorderPane();
        left = new VBox();
        cuenta = new VBox();
        acciones = new VBox();                
        menu = new FlowPane();        
        Button bus = new Button("Buscar");
        Button all = new Button("Todo");
        all.setOnMouseClicked(e->{
            menu.getChildren().clear();
            menu();
        });
        bus.setOnMouseClicked(e->{
            areaClasificar(filtro);
        });
        categorias = new HBox(); 
        Button entrada = new Button("ENTRADA");
        Button fuerte = new Button("PLATOFUERTE");
        Button postre = new Button("POSTRES");
        Button bebida = new Button("BEBIDA"); 
        busqueda.getChildren().addAll(filtro, bus, all);
        categorias.getChildren().addAll(entrada, fuerte, postre, bebida);        
        organizacion.getChildren().addAll(busqueda, categorias, menu);        
        menu();
        cuenta();
        crearBotones(venta);
        
        entrada.setOnMouseClicked(e->{ actionButton(entrada);});
        fuerte.setOnMouseClicked(e->{ actionButton(fuerte);});
        postre.setOnMouseClicked(e->{ actionButton(postre);});
        bebida.setOnMouseClicked(e->{ actionButton(bebida);});        
        
        left.getChildren().addAll(cuenta);
        Label info = new Label("Mesa "+mesa.getnMesa()+", Cliente: "+venta.getCliente());
        ventana.setLeft(left);
        ventana.setTop(info);
        ventana.setCenter(organizacion);
        amenu = new ActualizadorMenu();
        Thread tr = new Thread(amenu);
        tr.start();
    }   
    
    /**
     * Crea el boton para poder leer los platos del menu
     * @param boton recibe el boton para agregarle la funcionalidad
     */
    public void actionButton(Button boton){        
        try {
            SistemaFE.setMenu(LecturaEscritura.LEPlatos.leerArchivoPlato());
        } catch (IOException ex) {
            Logger.getLogger(VistaPedido.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VistaPedido.class.getName()).log(Level.SEVERE, null, ex);
        }        
        for(Plato plato: SistemaFE.getMenu()){
            if(boton.getText().equals(plato.getCategoria().toString())){
                menu.getChildren().clear();
                VBox imagenPlato = new VBox();
                Label nombreP = new Label(plato.getNombre());
                Label precioP = new Label(plato.getPrecio() + "");
                try {
                    Image ima = new Image("Imagenes/"+plato.getRutaImagen()+".jpg");
                    ImageView imaPlato = new ImageView(ima);
                    imaPlato.setFitHeight(100);imaPlato.setPreserveRatio(true);
                    imagenPlato.getChildren().addAll(imaPlato,nombreP,precioP);
                    menu.getChildren().add(imagenPlato);                    
                    imagenPlato.setOnMouseClicked(e->{
                        mesero.registrarPedido(venta, plato);
                        left.getChildren().clear();
                        cuenta();
                        crearBotones(venta);
                    });
                }
                catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        if(!organizacion.getChildren().contains(menu)){
            organizacion.getChildren().add(menu);                                                                                      
        }
    }
    
    /**
     * Crea el area para clasificar los platos por categoria
     * @param text Recibe el textField para obtener la informacion escrita
     */
    public void areaClasificar(TextField text){
        if(text.getText() != null){            
            String clave = text.getText().toLowerCase();
            for(Plato plato: SistemaFE.getMenu()){
                if(plato.getNombre().toLowerCase().startsWith(clave)){
                    menu.getChildren().clear();
                    VBox imagenPlato = new VBox();
                    Label nombreP = new Label(plato.getNombre());
                    Label precioP = new Label(plato.getPrecio() + "");
                    try {
                    Image ima = new Image("Imagenes/"+plato.getRutaImagen()+".jpg");
                    ImageView imaPlato = new ImageView(ima);
                    imaPlato.setFitHeight(100);imaPlato.setPreserveRatio(true);
                    imagenPlato.getChildren().addAll(imaPlato,nombreP,precioP);
                    menu.getChildren().add(imagenPlato);                    
                    imagenPlato.setOnMouseClicked(e->{
                        mesero.registrarPedido(venta, plato);
                        left.getChildren().clear();
                        cuenta();
                        crearBotones(venta);
                    });
                    }catch(Exception ex) {
                        System.out.println(ex);
                    }                   
                }
            }
            if(!organizacion.getChildren().contains(menu)){
                organizacion.getChildren().add(menu);                                                                                      
            }
        }   
    }
    
    /**
     * Metodo que crea el menu con la informacion de cada plato
     */
    public void menu(){
        try {
            SistemaFE.setMenu(LecturaEscritura.LEPlatos.leerArchivoPlato());
        } catch (IOException ex) {
            Logger.getLogger(VistaPedido.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VistaPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Plato plato: SistemaFE.getMenu()){
            VBox imagenPlato = new VBox();
            Label nombreP = new Label(plato.getNombre());
            Label precioP = new Label(plato.getPrecio() + "");
            try {
                Image ima = new Image("Imagenes/"+plato.getRutaImagen()+".jpg");
                ImageView imaPlato = new ImageView(ima);
                imaPlato.setFitHeight(100);imaPlato.setPreserveRatio(true);
                imagenPlato.getChildren().addAll(imaPlato,nombreP,precioP);
                menu.getChildren().add(imagenPlato);
                imagenPlato.setOnMouseClicked(e->{
                mesero.registrarPedido(venta, plato);
                left.getChildren().clear();
                cuenta();
                crearBotones(venta);
            });
            }
            catch(Exception ex) {
            }
        }
    }
    
    /**
     * Metodo que genera la informacion de la cuenta y va agregando platos y el total
     */
    public void cuenta(){
        Label linfo = new Label("Cuenta:");
        left.getChildren().add(linfo);
        for(Plato plato: venta.getPlatos()){
            Label nombre = new Label(plato.getNombre());
            Label precio = new Label(plato.getPrecio()+"");
            HBox info = new HBox(nombre,precio);
            left.getChildren().addAll(info);
        }
        Label total = new Label("Total: "+venta.getTotal()+"");
        left.getChildren().addAll(total);
    }    
    
    /**
     * Metodo que crea los botones para finalizar la orden y para regrsar al otro stage
     * @param vent Venta que se esta realizando en esa mesa
     */
    public void crearBotones(Venta vent){
        acciones.getChildren().clear();
        Button fin = new Button("Finalizar Orden");
        Button atras = new Button("Regresar");
        fin.setOnMouseClicked(e->{            
            mesero.finalPedido(vent);
            pedido.close();
            for(Mesa mesita: SistemaFE.getMesas()){
                if(mesita.getnMesa()==vent.getNoMesa()){
                    mesita.setEstado(Estado.DISPONIBLE);
                    try {
                        LecturaEscritura.LEMesas.escribirMesas();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(VistaPedido.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(VistaPedido.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        atras.setOnMouseClicked(a->{
            pedido.close();
        });
        acciones.getChildren().addAll(fin, atras);  
        left.getChildren().addAll(acciones);      
    }
    
    /**
     * Metodo que muestra la ventana del pedido
     */
    public void mostrarVentana(){
        pedido = new Stage();
        Scene spedido = new Scene(ventana,800,800);
        pedido.setScene(spedido);
        pedido.show();
    }
    
    public BorderPane getVentana() {
        return ventana;
    }
    
    /**
     * Inner Class para el hilo que actualiza el menu si hace algun cambio el admin
     */
    private class ActualizadorMenu implements Runnable {
        
        private boolean detener;
        
        /**
         * Metodo sobreescrito del run que actuliza la informacion de los plato
         */
        @Override
        public void run() {
            while(!detener) {
                try {
                    sleep(10000);
                    Platform.runLater(() -> {
                        try {
                            SistemaFE.setMenu(LecturaEscritura.LEPlatos.leerArchivoPlato());
                            menu.getChildren().clear();
                            menu();
                        } catch (IOException ex) {
                            System.out.println("Error cargando menu Mesero");
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
         * Metodo que da la orden de detener el hilo
         */
        public void detente(){
            detener = true;
            System.out.println("Detenter hilo en mesas MESERO");
        }
    }
    
    /**
     * Metodo que detiene los hilos ejectutandose
     */
    public void Parar(){
        amenu.detente();
        System.out.println("Detiene el hilo menu MESERO");
    }               
}
