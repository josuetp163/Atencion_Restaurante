/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.Serializable;

/**
 *
 * @author josue
 */
public class Plato implements Serializable{
    private String nombre;
    private double precio;
    private String rutaImagen;
    private Categoria categoria;
    
    public Plato(String n,double p, String nombreImagen ,Categoria ca){
        nombre=n;
        precio=p;
        rutaImagen = nombreImagen;
        categoria=ca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    @Override
    public boolean equals(Object ob){
        if (ob != null && ob instanceof Plato){
            Plato plato = (Plato)ob;
            if (this.nombre.equals(plato.getNombre())){
                return true;
            }
        }
        return false;
    }
}
