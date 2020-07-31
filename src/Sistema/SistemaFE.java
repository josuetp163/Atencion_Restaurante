/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistema;

import static LecturaEscritura.LECuentas.cargarCuentas;
import static LecturaEscritura.LECuentas.escribirCuentas;
import static LecturaEscritura.LEMesas.cargarMesas;
import static LecturaEscritura.LEMesas.escribirMesas;
import static LecturaEscritura.LEPlatos.escribirPlatos;
import static LecturaEscritura.LEPlatos.leerArchivoPlato;
import static LecturaEscritura.LEVentas.LeerArchivoVentas;
import static LecturaEscritura.LEVentas.escribirArchivoVentas;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import modelos.*;

/**
 * Clase donde se guarda toda la informacion del sistema del restaurante
 * @author josue
 * @author michael
 * @author edwin
 */
public class SistemaFE implements Serializable{
    
    public static ArrayList<Plato> menu;
    private static ArrayList<Cuenta> cuentas;
    public static ArrayList<Mesa> mesas;
    public static ArrayList<Venta> ventas;
    
    /**
     * Constructor del sistema donde se carga toda la informacion desde los archivos, y si no encuentra nada se crea desde cero.
     */
    public SistemaFE(){
        try{
            cuentas = cargarCuentas();
        }catch(IOException|ClassNotFoundException e){
            try{
                escribirCuentas();
                cuentas = cargarCuentas();
            }catch(IOException|ClassNotFoundException ex){
                System.out.println(ex.getMessage());
            }
        }
        try{
            mesas = cargarMesas();
        }catch(IOException|ClassNotFoundException e){
            try{
                escribirMesas();
                mesas = cargarMesas();
            }catch(IOException|ClassNotFoundException ex){
                System.out.println(ex.getMessage());
            }
        }
        try{
            menu = leerArchivoPlato();
        }catch(IOException|ClassNotFoundException e){
            try{
                escribirPlatos();
                menu = leerArchivoPlato();
            }catch(IOException|ClassNotFoundException ex){
                System.out.println(ex.getMessage());
            }
        }        
        try{
            ventas = LeerArchivoVentas();
        }catch(IOException|ClassNotFoundException e) {
            try{
                escribirArchivoVentas();
                ventas = LeerArchivoVentas();
            }catch(IOException|ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }
    }
    
    /**
     * Metodo para buscar una cuenta a partir del correo y la contraseña
     * @param corr Correo del usuario que esta ingresando
     * @param con Contraseña del usuario que esta ingresando
     * @return Retorna la cuenta que esta asociada a ese correo y esa contraseña
     */
    public Cuenta buscarCuenta(String corr,String con){
        for(Cuenta c : cuentas){
            System.out.println(c.getCorreo());
            if(c.getCorreo().equals(corr) && c.getContrasena().equals(con)){
                return c;
            }
        }
        return null;
    }
    
    /**
     * Metodo para filtrar las ventas por fechas
     * @param fechini Fecha de inicio de periodo a buscar
     * @param fechfin Fecha de fin de periodo a buscar
     * @return Retorna un arrayList de ventas que sera usado luego para ser mostrado
     */
    public static ArrayList<Venta> filtrarVentas(LocalDateTime fechini, LocalDateTime fechfin){
        ArrayList<Venta> filtro = new ArrayList<>();
        for (Venta venta: ventas) {
            System.out.println(venta.getDate().isAfter(fechini));
            System.out.println( venta.getDate().isBefore(fechfin));
            if (venta.getDate().isAfter(fechini) && venta.getDate().isBefore(fechfin)){
                System.out.println("Fechas ingresadas correctamente");
                filtro.add(venta);
            }
        }
        return filtro;
    }
    
    public static ArrayList<Plato> getMenu() {
        return menu;
    }

    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }

    public static ArrayList<Mesa> getMesas() {
        return mesas;
    }

    public static ArrayList<Venta> getVentas() {
        return ventas;
    }

    public static void setMenu(ArrayList<Plato> menu) {
        SistemaFE.menu = menu;
    }

    public void setCuentas(ArrayList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public static void setMesas(ArrayList<Mesa> mesas) {
        SistemaFE.mesas = mesas;
    }

    public static void setVentas(ArrayList<Venta> ventas) {
        SistemaFE.ventas = ventas;
    }
}
