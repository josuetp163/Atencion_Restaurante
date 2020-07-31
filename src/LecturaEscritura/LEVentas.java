 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LecturaEscritura;

import Sistema.SistemaFE;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import modelos.Venta;


/**
 * Clase para leer y escribir archivos de Ventas
 * @author josue
 * @author michael
 * @author edwin
 */
public class LEVentas implements Serializable{
    
    /**
     * Carga las ventas desde un archivo binario
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Venta> LeerArchivoVentas() throws FileNotFoundException, IOException, ClassNotFoundException{
        Path p = Paths.get("src/Archivos/ventas.dat");
        try(ObjectInputStream obj = new ObjectInputStream(new FileInputStream(p.toFile()))){
            System.out.println("Se leen ventas");
            return (ArrayList<Venta>)obj.readObject();
            
        }
    }
    
    /**
     * Escribe las ventas que estan registradas hasta ese momento en el sistema
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void escribirArchivoVentas() throws FileNotFoundException, IOException{
        Path p = Paths.get("src/Archivos/ventas.dat");
        try(ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(p.toFile()))){            
            if(SistemaFE.getVentas()==null){
                obj.writeObject(new ArrayList<>());
            }else{
                obj.writeObject(SistemaFE.ventas);
            }
            System.out.println("Se escriben ventas");
        }
    }    
}
