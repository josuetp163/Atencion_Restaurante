/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import Sistema.SistemaFE;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josue
 */
public class Mesero extends Usuario implements Serializable{ 
    private ArrayList<Venta> ventas = new ArrayList<>();
    private ArrayList<Mesa> mesas = new ArrayList<>();
    
    public Mesero(String n){
        super(n);      
        ventas = new ArrayList<>();
    }
    
    public void aniadirMesa(Mesa m) {
        if(!mesas.contains(m)) {
            mesas.add(m);
            System.out.println("siu");
        }
    }

    public ArrayList<Mesa> getMesas() {
        return mesas;
    }
    
    public void abrirCuenta( Mesa mesa) {
        if(SistemaFE.getMesas().contains(mesa)){
            for (Mesa mesita: SistemaFE.getMesas()){
                if(mesita.equals(mesa)){
                    Venta cuentita  = new Venta(mesa.getCliente(), this, mesa.getnMesa());
                    ventas.add(cuentita);
                    mesa.setVenta(cuentita);
                    SistemaFE.getVentas().add(cuentita);
                    System.out.println("Se abre la cuenta");
                }
            }
        }else{
            System.out.println("Problemas al abrir cuenta");
        }
    }
    
    public void registrarPedido( Venta venti, Plato plato){
        if(ventas != null && SistemaFE.getMenu().contains(plato)){
            if(ventas.contains(venti) && SistemaFE.getVentas().contains(venti)){                    
                venti.getPlatos().add(plato);
                venti.setTotal(venti.calcularTotal());                        
                System.out.println("Se registra el pedido");
                try {
                    LecturaEscritura.LEVentas.escribirArchivoVentas();
                } catch (IOException ex) {
                    Logger.getLogger(Mesero.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            System.out.println("Problema al registrar pedido");
        }      
    }
        
    public void finalPedido(Venta vent){
        if(ventas != null && SistemaFE.getVentas().contains(vent)){            
            if(ventas.contains(vent)){
                vent.setTotal(vent.calcularTotal());                
                System.out.println("Gracias por visitarnos, vuelva pronto"); }
                System.out.println(vent.toString());
                SistemaFE.getMesas().get(vent.getNoMesa()-1).setEstado(Estado.DISPONIBLE);
                try {
                    LecturaEscritura.LEVentas.escribirArchivoVentas();
                    LecturaEscritura.LEMesas.escribirMesas();
                }catch (IOException ex) {
                    Logger.getLogger(Mesero.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Mesero.class.getName()).log(Level.SEVERE, null, ex);
                }                                    
            
                        
        }else{
            System.out.println("El registro de ventas esta vacio");  
        }
    }
    
    public ArrayList<Venta> getVentas() {
        return ventas;
    }
    
    public String getNombre() {
        return super.getNombre();
    }
        
}