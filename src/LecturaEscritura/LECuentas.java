/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LecturaEscritura;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import modelos.Administrador;
import modelos.Cuenta;
import modelos.Mesero;

/**
 * Clase para leer y escribir archivos de cuentas
 * @author josue
 * @author michael
 * @author edwin
 */
public class LECuentas implements Serializable{
    private static String file = "src/Archivos/cuentas.dat";
     
    /**
     * Carga las cuentas desde un archivo binario
     * @return El arrayList de las cuentas cargadas
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Cuenta> cargarCuentas() throws IOException, ClassNotFoundException{ 
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))){
            ArrayList<Cuenta> cuentas = (ArrayList<Cuenta>)input.readObject();
            System.out.println("Se leen cuentas");
            return cuentas;
        }catch(IOException e){
            throw e;
        }
    }
    
    /**
     * Escribe las cuentas que estan registradas hasta ese momento en el sistema
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public static void escribirCuentas() throws ClassNotFoundException, IOException{
        try{
           ArrayList<Cuenta> cuentas = new ArrayList<>();
           cuentas.add(new Cuenta("admin@admin.com","admin",new Administrador("Elena Nito")));
           cuentas.add(new Cuenta("mes@mes.com","mesero",new Mesero("Alba Bosa")));
           cuentas.add(new Cuenta("mes1@mes.com","mesero1",new Mesero("Elpa panatas")));
           ObjectOutputStream ou = new ObjectOutputStream(new FileOutputStream(file));
           System.out.println("Se escriben cuentas");
           ou.writeObject(cuentas);
        }catch(IOException ex){
            throw ex;
        }
    }
}
