/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 *
 * @author ADMIN
 */
public class Venta implements Serializable{    
    private String fecha;
    private int noMesa;
    private String noMesero;      
    private int noVenta; 
    private String cliente;
    private double total;
    
    private Mesero mesero;
    private ArrayList<Plato> platos;
    private LocalDateTime date;
    private Random rd = new Random();
    
    
    public Venta(String cliente, Mesero mesero, int noMesa){
        this.mesero = mesero;
        noMesero = mesero.getNombre();
        noVenta = rd.nextInt(1000);
        this.noMesa = noMesa;System.out.println(noVenta);
        date = LocalDateTime.now();
        DateTimeFormatter df= DateTimeFormatter.ofPattern("dd-MM-YYYY hh:mm");
        fecha = date.format(df);
        this.cliente = cliente;
        platos = new ArrayList<>();
    }    
    
    public Venta(String cliente, int noMesa){
        this.mesero = null;
        noVenta = rd.nextInt(1000);
        this.noMesa = noMesa;System.out.println(noVenta);
        date = LocalDateTime.now();
        DateTimeFormatter df= DateTimeFormatter.ofPattern("dd-MM-YYYY hh:mm");
        fecha = date.format(df);
        this.cliente = cliente;
        platos = new ArrayList<>();
    }
    
    public double calcularTotal() {        
        double suma=0;
        if (platos.size()==1){
            suma = platos.get(0).getPrecio();
        }
        if (platos.size()>1){            
            for (int i = 0; i < platos.size(); i++) {                
                suma+= platos.get(i).getPrecio();
            }                        
        }
        return suma;
    }
    
    public boolean equals(Object ob){
        if (ob != null && ob instanceof Venta){
            Venta venta = (Venta)ob;
            if(noVenta ==venta.getNoVenta()){
                return true;
            }
        }
        return false;
    }  
    
    public void setTotal(double total) {
        this.total = total;
    }
                 
    public int getNoVenta() {
        return noVenta;
    }

    public LocalDateTime getDate() {
        return date;
    }
    
    public String getFecha() {
        return fecha;
    }

    public int getNoMesa() {
        return noMesa;
    }   

    public Mesero getNameMesero() {
        return mesero;
    }
    
    public ArrayList<Plato> getPlatos() {
        return platos;
    }

    
    public String getCliente(){
        return cliente;
    }
   
    public double getTotal(){
        return total;
    }

    public void setNoVenta(int noVenta) {
        this.noVenta = noVenta;
    }  

    public String getNoMesero() {
        return noMesero;
    }

    public void setNoMesero(String noMesero) {
        this.noMesero = noMesero;
    }

    public Mesero getMesero() {
        return mesero;
    }

    public void setMesero(Mesero mesero) {
        this.mesero = mesero;
    }

    @Override
    public String toString() {
        return "Venta{" + "fecha=" + fecha + ", noMesa=" + noMesa + ", noMesero=" + noMesero + ", noVenta=" + noVenta + ", cliente=" + cliente + ", total=" + total + ", mesero=" + mesero + ", platos=" + platos.size() + '}';
    }
    
    

}
