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
import java.util.ArrayList;
import modelos.Plato;

/**
 * Clase para leer y escribir archivos de Platos
 * @author josue
 * @author michael
 * @author edwin
 */
public class LEPlatos implements Serializable{
    private static String file = "src/Archivos/platos.dat";
    
    /**
     * Carga los platos desde un archivo binario
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Plato> leerArchivoPlato() throws FileNotFoundException, IOException, ClassNotFoundException{        
        ObjectInputStream obj = new ObjectInputStream(new FileInputStream("src/Archivos/platos.dat"));
        ArrayList<Plato> menu = new ArrayList<>();
        
        ArrayList<Plato> platosArchivo = (ArrayList<Plato>)obj.readObject();
        for(Plato plat : platosArchivo){
            menu.add(plat);
            System.out.println("Se leen platos");
        }
        return menu;
    }
    
    /**
     * Escribe los platos del menu que estan registradas hasta ese momento en el sistema
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void escribirPlatos() throws FileNotFoundException, IOException{
        ObjectOutputStream ou = new ObjectOutputStream(new FileOutputStream(file));
        if(SistemaFE.getMenu() == null) {
            ou.writeObject(new ArrayList<>());
        }
        else {
            ou.writeObject(SistemaFE.getMenu());
        }
        System.out.println("Se escriben platos");
    }
}
