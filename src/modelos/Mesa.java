/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.Serializable;
import static modelos.Estado.DISPONIBLE;

/**
 *
 * @author josue
 */
public class Mesa implements Serializable{
    private int nMesa;
    private int capacidad;  
    private Estado estado;
    private String cliente;
    private double x;
    private double y;
    private Mesero mesero;
    private Venta venta;
    
    public Mesa(int n , int c , Estado es , double cx , double cy){
        nMesa=n;
        capacidad=c;
        estado=es;
        x=cx;
        y=cy;
        mesero = null;
        cliente = null;
    }
    
    public Mesa(int n , int c) {
        nMesa = n;
        capacidad = c;
        estado = DISPONIBLE;
        mesero = null;
        cliente = null;
    }

    public int getnMesa() {
        return nMesa;
    }

    public void setnMesa(int nMesa) {
        this.nMesa = nMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }    

    public boolean equals(Object ob){
        if (ob != null && ob instanceof Mesa){
            Mesa mesita = (Mesa)ob;
            if (this.nMesa==mesita.getnMesa()){
                return true;
            }
        }
        return false;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public String getCliente(){
        return cliente;
    }

    public Mesero getMesero() {
        return mesero;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setMesero(Mesero mesero) {
        this.mesero = mesero;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    
}
