/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LecturaEscritura;

import Sistema.SistemaFE;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import modelos.Mesa;

/**
 * Clase para leer y escribir archivos de Mesas
 * @author josue
 * @author michael
 * @author edwin
 */
public class LEMesas {
    private static final String file = "src/Archivos/mesas.dat";
    
    /**
     * Carga las mesas desde un archivo binario
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static ArrayList<Mesa> cargarMesas() throws IOException,ClassNotFoundException{
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))){
            ArrayList<Mesa> mesas = (ArrayList<Mesa>) input.readObject();
            return mesas;
        }catch(IOException e){
            throw e;
        }
    }
    
    /**
     * Escribe las mesas que estan registradas hasta ese momento en el sistema
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public static void escribirMesas() throws ClassNotFoundException ,IOException{
        try{
            ObjectOutputStream ou = new ObjectOutputStream(new FileOutputStream(file));
            if(SistemaFE.getMesas()==null){
                ou.writeObject(new ArrayList<>());
            }else{
                ou.writeObject(SistemaFE.mesas);
            }
        }catch(IOException ex){
            throw ex;
        }
    }
}
