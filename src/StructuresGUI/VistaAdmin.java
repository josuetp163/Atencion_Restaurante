    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StructuresGUI;

import LecturaEscritura.LEMesas;
import LecturaEscritura.LEVentas;
import Main.FastEat;
import Sistema.SistemaFE;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import modelos.Categoria;
import modelos.Estado;
import modelos.Mesa;
import modelos.Mesero;
import modelos.Plato;
import modelos.Venta;

/**
 * Clase para crear a vista del administrador
 * @author josue
 * @author michael
 * @author edwin
 */
public class VistaAdmin {
    private VistaReporte vistareporte;
    actualizarMonitoreo actMonitoreo;
    contadorVentas conVentas;
    private static double valorVentas;
    public static TabPane tabs;
    private VBox edicion;
    private VBox vistaEdicion;
    private Tab monitoreo = new Tab("Monitoreo");
    private Tab plano = new Tab("Diseño Plano");
    private Tab menu = new Tab("Gestion Menu");
    private Tab reporte = new Tab("Reporte Ventas");
    private BorderPane root2;
    private Pane areaMesas;
    private BorderPane areaInformacion;
    private VBox infoMesas;
    private FlowPane vistaMenu;
    private BorderPane menuAdmin;
    private BorderPane monitoreoAdmin;
    private Label valorFacturado = new Label("Valor Facturado: " + valorVentas);
    double origenX; double finalX;
    double origenY; double finalY;
    
    
    /**
     * Constructor de la vista admin que crea la estructura principal de la ventana
     * @param stage Recibe el stage principal para hacer cambios en el
     */
    public VistaAdmin(Stage stage){
        actMonitoreo = new actualizarMonitoreo();
        Thread hiloActMon = new Thread(actMonitoreo);
        conVentas = new contadorVentas();
        Thread hiloVentas = new Thread(conVentas);
        tabs =  new TabPane();
        tabs.getTabs().addAll(monitoreo,plano,menu,reporte);
        monitoreoAdmin = new BorderPane();
        root2 = new BorderPane();
        areaMesas = new Pane();
        areaInformacion = new BorderPane();
        infoMesas = new VBox();
        disenioPlanoRestaurante(stage);
        crearTabMenu();
        verReporteVentas(stage);      
        crearTabMonitoreo();
        hiloActMon.start();
        hiloVentas.start();
    }
    
    /**
     * Metodo que crea el diseño del plano del restaurante para que el administrador pueda hacer cambios
     * @param primaryStage Recibe el primaryStage para hacer cambios
     */
    private void disenioPlanoRestaurante(Stage primaryStage){
        root2.setCenter(areaMesas);
        cargarMesas();
        root2.setRight(areaInformacion);
        areaInformacion.setCenter(infoMesas);
        areaInformacion.setTop(new Label("Detalles------"));
        areaMesas.setOnMouseClicked(e1 -> {
            areaMesas.setMouseTransparent(true);
            infoMesas.getChildren().clear();
            double posX = e1.getSceneX();
            double posY = e1.getSceneY();
            TextField t1 = new TextField("Ingrese el no de mesa");
            TextField t2 = new TextField("Ingrese la capacidad de la mesa");
            Button t3 = new Button("Aceptar");
            t3.setOnAction(e2 -> {
                Mesa mesa = new Mesa(Integer.parseInt(t1.getText()),Integer.parseInt(t2.getText()),Estado.DISPONIBLE,posX,posY);
                FastEat.sistema.getMesas().add(mesa);
                crearMesa(mesa);
                crearTabMonitoreo();
                try {
                    LecturaEscritura.LEMesas.escribirMesas();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            infoMesas.getChildren().addAll(t1,t2,t3);
            areaInformacion.setCenter(infoMesas);                
        });
        
        areaMesas.setOnMouseExited(e8 -> {
            areaMesas.setMouseTransparent(false);
        });
        
        plano.setContent(root2);
        FastEat.scene.setRoot(tabs);
        primaryStage.setScene(FastEat.scene);
        primaryStage.setTitle("Administrador");
        primaryStage.show();
    }
    
    /**
     * Metodo para crear una mesa con todos sus atributos para que el administrador pueda agregar y editar una mesa
     * @param mesa Recibe como parametro una mesa del sistema y a partir de ella hace el circulo visual de la mesa
     */
    private void crearMesa(Mesa mesa){
        try{
            infoMesas.getChildren().clear();
            StackPane circuloMesa = new StackPane();
            Circle circulo = new Circle(mesa.getCapacidad()*10);
            if(mesa.getEstado()==Estado.DISPONIBLE){
                circulo.setFill(Color.GREEN);
            }else if(mesa.getEstado()==Estado.ATENDIENDO){
                circulo.setFill(Color.CRIMSON);
            }else if(mesa.getEstado()==Estado.OCUPADO){
                circulo.setFill(Color.CRIMSON);
            }else if(mesa.getEstado()==Estado.RESERVADO){
                circulo.setFill(Color.CHOCOLATE);
            }
            Label nMesa = new Label(String.valueOf(mesa.getnMesa()));
            circuloMesa.getChildren().addAll(circulo,nMesa);
            areaMesas.getChildren().add(circuloMesa);
            circuloMesa.setLayoutX(mesa.getX());
            circuloMesa.setLayoutY(mesa.getY());

            circuloMesa.setOnMousePressed(e3 -> {
                circuloMesa.setMouseTransparent(true);
                origenX = e3.getSceneX();
                origenY = e3.getSceneY();
                finalX = ((StackPane)(e3.getSource())).getTranslateX();
                finalY = ((StackPane)(e3.getSource())).getTranslateY();
            });
            
            circuloMesa.setOnMouseClicked(e5->{
                if(e5.getButton()==MouseButton.SECONDARY){
                    areaMesas.getChildren().remove(circuloMesa);
                    SistemaFE.getMesas().remove(mesa);
                    try {
                    LecturaEscritura.LEMesas.escribirMesas();
                    }
                    catch (ClassNotFoundException ex) {
                        Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (IOException ex) {
                        Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            circuloMesa.setOnMouseDragged(e4 -> {
                double algoX = e4.getSceneX() - origenX;
                double algoY = e4.getSceneY() - origenY;
                double trasladoX = finalX + algoX;
                double trasladoY = finalY + algoY;
                ((StackPane)(e4.getSource())).setTranslateX(trasladoX);
                ((StackPane)(e4.getSource())).setTranslateY(trasladoY);
                mesa.setX(e4.getSceneX());
                mesa.setY(e4.getSceneY());
            });
            circuloMesa.setOnMouseReleased(e7 -> {
                circuloMesa.setMouseTransparent(false);
                crearTabMonitoreo();
            });

            circuloMesa.setOnMouseEntered(e5 -> {                                
                    circuloMesa.setMouseTransparent(true);
                    infoMesas.getChildren().clear();
                    VBox infoMesa = new VBox();
                    Label no = new Label("El numero de la mesa es: " + mesa.getnMesa());
                    Label capa = new Label("La capacidad de la mesa es: " + mesa.getCapacidad());
                    infoMesa.getChildren().addAll(no,capa);
                    infoMesas.getChildren().add(infoMesa);
            });

            circuloMesa.setOnMouseExited(e6 -> {                                
                    circuloMesa.setMouseTransparent(false);                                
            });
        }
        catch(Exception ex) {
        }
    }
    
    /**
     * Metodo para crear la imagen y la informacion del plato en la gestion del menu
     * @param plato Recibe como parametro un plato del sistema del cual se sacara la informacion
     */
    public void crearPlato(Plato plato){
        VBox imagenPlato = new VBox();
        Label nombreP = new Label(plato.getNombre());
        Label precioP = new Label(plato.getPrecio() + "");
        try {
            Image ima = new Image("Imagenes/"+plato.getRutaImagen()+".jpg");
            ImageView imaPlato = new ImageView(ima);
            imaPlato.setFitHeight(100);imaPlato.setPreserveRatio(true);
            imagenPlato.getChildren().addAll(imaPlato,nombreP,precioP);
            vistaMenu.getChildren().add(imagenPlato);
        }
        catch(Exception ex) {
        }
        
        
        imagenPlato.setOnMouseClicked(e2 -> {
            vistaEdicion.getChildren().clear();
            Button editar = new Button("Editar");
            Button eliminar = new Button("Eliminar");    
            vistaEdicion.getChildren().addAll(editar,eliminar);
            editar.setOnAction(e3 -> {
                vistaEdicion.getChildren().clear();
                Label a1 = new Label("Ingrese el nuevo nombre:");
                TextField a2 = new TextField();
                Label b1 = new Label("Ingrese el nuevo precio:");
                TextField b2 = new TextField();
                Button aceptarCambio = new Button("Aceptar cambio");
                vistaEdicion.getChildren().addAll(a1,a2,b1,b2,aceptarCambio);
                aceptarCambio.setOnAction(e4 -> {
                    if (a2.getText() != null && b2.getText() != null){
                        try{
                            plato.setNombre(a2.getText());
                            plato.setPrecio(Double.parseDouble(b2.getText()));
                            vistaEdicion.getChildren().clear();
                            Label exito = new Label("Edición relizada con éxito.");
                            vistaEdicion.getChildren().add(exito);
                        }catch(Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                });
            });
            eliminar.setOnAction(e5 -> {
                vistaEdicion.getChildren().clear();
                Label seguro = new Label("¿Seguro que desea eliminar el plato?");
                Button si = new Button("Si");
                Button no = new Button("No");
                vistaEdicion.getChildren().addAll(seguro,si,no);
                
                si.setOnAction(e6 -> {
                    vistaEdicion.getChildren().clear();
                    vistaMenu.getChildren().remove(imagenPlato);
                    SistemaFE.menu.remove(plato);
                    Label eliminacion = new Label("Plato elimnado con éxito.");
                    vistaEdicion.getChildren().add(eliminacion);
                    try {
                        LecturaEscritura.LEPlatos.escribirPlatos();
                    } catch (IOException ex) {
                        Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                no.setOnAction(e7 -> {
                    vistaEdicion.getChildren().clear();
                    Label negacion = new Label("Continúe editando.");
                    vistaEdicion.getChildren().add(negacion);
                });
            });
        });
    }
    
    /**
     * Metodo que crea la parte del moniterio para el administrador
     */
    public void crearTabMonitoreo() {
        monitoreoAdmin.getChildren().clear();
        Pane p = new Pane();
        VBox vb = new VBox(valorFacturado);
        VBox vInfo = new VBox();
        monitoreoAdmin.setCenter(p);
        monitoreoAdmin.setRight(vb);
        vb.getChildren().add(vInfo);
        for (Mesa m : SistemaFE.mesas) {
            StackPane circuloMesa = new StackPane();
            Circle circulo = new Circle(m.getCapacidad()*10);
            Label l1 = new Label(m.getnMesa()+"");
            Mesero mes = m.getMesero();
            if (mes == null) {
                circulo.setFill(Color.GREEN);
            }
            else if (mes != null) {
                circulo.setFill(Color.CRIMSON);
            }
            if(m.getEstado()==Estado.RESERVADO){
                circulo.setFill(Color.CHOCOLATE);
            
            }
            
            
            
            circuloMesa.getChildren().addAll(circulo,l1);
            p.getChildren().add(circuloMesa);
            circuloMesa.setLayoutX(m.getX());
            circuloMesa.setLayoutY(m.getY());
            circuloMesa.setOnMouseEntered(e -> {
                if (m.getEstado() == Estado.DISPONIBLE) {
                    vInfo.getChildren().clear();
                    Label dis = new Label("DISPONIBLE");
                    vInfo.getChildren().add(dis);
                }
                else if (m.getEstado() == Estado.OCUPADO) {
                    vInfo.getChildren().clear();
                    Label ocu = new Label("OCUPADO");
                    Label nom = new Label("Nombre del mesero: " + mes.getNombre());
                    vInfo.getChildren().addAll(ocu,nom);
                }
                else if (m.getEstado() == Estado.RESERVADO) {
                    vInfo.getChildren().clear();
                    Label ocu = new Label("RESERVADO");
                    Label nom = new Label("Nombre del cliente: "+m.getCliente());
                    vInfo.getChildren().addAll(ocu,nom);
                }
            });
            
            //Sustentacion
            circuloMesa.setOnMouseClicked(e->{
                if(m.getEstado() == Estado.DISPONIBLE){
                    Stage st = new Stage();
                    VBox root = new VBox();
                    TextField tf = new TextField("Nombre del Cliente");
                    Button b = new Button("Reservar");
                    root.getChildren().addAll(tf,b);
                    
                    Scene sc = new Scene(root,200,200);
                    st.setScene(sc);
                    st.show();
                    
                    b.setOnAction(e7->{
                        Venta v = new Venta(tf.getText(),m.getnMesa());
                        m.setVenta(v);
                        m.setCliente(tf.getText());
                        SistemaFE.getVentas().add(v);
                        m.setEstado(Estado.RESERVADO);
                        try {
                            LecturaEscritura.LEMesas.escribirMesas();
                            SistemaFE.mesas=LEMesas.cargarMesas();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        st.close();
                    });
                }
            });
            monitoreo.setContent(monitoreoAdmin);
        }
    }
    
    /**
     * Metodo que crea la informacion del tab para la gestion del menu
     */
    public void crearTabMenu(){
        vistaMenu = new FlowPane();
        menuAdmin = new BorderPane();
        menuAdmin.setCenter(vistaMenu);
        edicion = new VBox();
        vistaEdicion = new VBox();
        menuAdmin.setRight(edicion);
        VBox vistaDetalles = new VBox();
        VBox detalles = new VBox();
        menuAdmin.setLeft(vistaDetalles);
        Label edi = new Label("Edicion------------------");
        edicion.getChildren().addAll(edi,vistaEdicion);
        
        Label encabezadoDetalles = new Label("Añadir nuevos platos-----");
        vistaDetalles.getChildren().add(encabezadoDetalles);
        
        
        TextField t1 = new TextField("Ingrese el nombre del plato");
        TextField t2 = new TextField("Ingrese el precio del plato");
        TextField t3 = new TextField("Ingrese la imagen del plato");
        ComboBox<Categoria> cb = new ComboBox();
        cb.setItems(FXCollections.observableArrayList(Arrays.asList(Categoria.BEBIDA,Categoria.ENTRADA,Categoria.PLATOFUERTE,Categoria.POSTRES)));
        Button aceptar = new Button("Aceptar");
        detalles.getChildren().addAll(t1,t2,t3,cb,aceptar);
        vistaDetalles.getChildren().add(detalles);
        
        cargarMenu();
        
        aceptar.setOnAction(e1 -> {
            Plato plato = new Plato(t1.getText(),Double.parseDouble(t2.getText()),t3.getText(),cb.getValue());
            FastEat.sistema.getMenu().add(plato);
            crearPlato(plato);
            try {
                LecturaEscritura.LEPlatos.escribirPlatos();
            } catch (IOException ex) {
                Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            });
        menu.setContent(menuAdmin);
    }
    
    /**
     * Metodo que crea el tab del reporte de la ventas
     * @param primaryStage Recibe el primaryStage para hacer modificaciones
     */
    public void verReporteVentas(Stage primaryStage){
        vistareporte = new VistaReporte(primaryStage);        
        reporte.setContent(vistareporte.getRoot());
    }
    
    public BorderPane getRoot() {
        return root2;
    }
    
    /**
     * Metodo para cargar las mesas desde el sistema y crea el plano con todas ellas
     */
    public void cargarMesas(){
        for(Mesa m : SistemaFE.getMesas()){
            crearMesa(m);
        }
    }
    
    /**
     * Metodo que carga los platos del menu y crea la informacion del menu con todos los platos
     */
    public void cargarMenu(){
        for(Plato p: SistemaFE.getMenu()){
            crearPlato(p);
        }
    }
    
    /**
     * Metodo que carga las ventas y saca el valor total de las ventas realizadas
     */
    public void cargarVentas() {
        for (Venta v : SistemaFE.getVentas()) {
            valorVentas += v.calcularTotal();
        }
    }
    
    
    /**
     * Inner Class como hilo para poder contar las ventas
     */
    private class contadorVentas implements Runnable {
        
        private boolean detener;
        
        /**
         * Metodo sobreescrito run donde se calcula el valor facturado
         */
        @Override
        public void run() {
            System.out.println("Se ejecuta hilo 1");
            while(!detener) {
                try {
                    sleep(120000);
                    LEVentas.escribirArchivoVentas();
                    LEMesas.escribirMesas();
                    SistemaFE.ventas = LEVentas.LeerArchivoVentas();
                    SistemaFE.mesas = LEMesas.cargarMesas();
                    cargarVentas();
                    Platform.runLater(() -> {
                        valorFacturado.setText("Valor Facturado: " + valorVentas);
                    });
                }
                catch (InterruptedException ex) {
                    System.out.println("Ha ocurrido un problema");
                    System.out.println(ex);
                }
                catch (NullPointerException ex) {
                    System.out.println("No hay ventas registradas");
                } catch (IOException ex) {
                    Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public void parar() {
            detener = true;
        }
    }
    
    /**
     * Metodo que detiene el hilo creado
     */
    public void detenerHilo() {
        conVentas.parar();
        actMonitoreo.parar();
        vistareporte.detenerHilo();
        
    }
    
    /**
     * Inner class para el hilo que actualiza el monitereo del admin
     */
    private class actualizarMonitoreo implements Runnable {
        
        private boolean detener;
       
        /**
         * Metodo run sobreescrito que ejectuta y actuliza la parte del monitoreo
         */
        @Override
        public void run() {
            System.out.println("Se ejecuta hilo 2");
            while(!detener) {
                try {
                    sleep(10000);
                    Platform.runLater(() -> {
                        try {
                            LEMesas.escribirMesas();
                            System.out.println("dow hilo");
                            SistemaFE.mesas = LecturaEscritura.LEMesas.cargarMesas();
                            crearTabMonitoreo();
                        } catch (IOException ex) {
                            Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(VistaAdmin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    });
                }
                catch (InterruptedException ex) {
                    System.out.println("No se ha podido actualizar el monitoreo");
                }
            }
        }
        
        /**
         * Metodo que detiene el hilo creado
         */
        public void parar() {
            detener = true;
        }
    }
}
