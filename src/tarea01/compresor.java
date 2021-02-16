package tarea01;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;



public class compresor {
	
    public static String Ruta = "",nruta="",BP=""; //Ruta del archivo a comprimir
    public static HashMap<String, Integer> diccionario = new HashMap<>(); //Creacion de la tabla del diccionario
    public static int TamanoD = 256; //Tamano del diccionario 
    public static byte EntradaB; 
    public static byte[] Buffer = new byte[3]; //Buffer para la conversion a 12 bits
    public static boolean limite_bits = true;  //Define el limite de los codewords
    
    public static void main_compresion () {
        System.out.println("Introduzca la ruta del archivo que desea comprimir ");
        System.out.println("Ejemplo: C:\\Users\\jmgf11\\Desktop\\prueba\\prueba.jpg");
        Scanner objt = new Scanner(System.in);
        nruta = objt.next();
        try {
            File archivo = new File(nruta); //Lee ruta del archivo
            compresion(); //Llama el metodo de compress
            String[] narchivo = nruta.split("\\.");//Divide la cadena de la direccion de entrada en diferentes posiciones del arreglo.
           
            System.out.println("Compresion Satisfactoria, su archivo se encuentra en :  "+narchivo[0].concat(".lzw")); // Imprime el nombre del archivo nuevo con al extension de la compresion        
        }
        catch(IOException ie) {System.out.println("Ruta no encontrada"); }
    }
    
    public static void compresion() throws IOException {   //metodo de compresión
        
        int i,conversion_int;
        char caracter;
        
        for(i=0;i<256;i++) {
            diccionario.put(Character.toString((char)i),i);  /*Se llenan los primeros 256 espacios de la tabla con los caracteres de la tabla ASCII*/}
        
        RandomAccessFile archivo_de_entrada = new RandomAccessFile(nruta,"r"); //Crea un archivo temporal para ir llenandolo con la informacion de la compresión
        
        String[] narhivo = nruta.split("\\.");
        
        RandomAccessFile archivo_de_salida = new RandomAccessFile(narhivo[0].concat(".lzw"),"rw"); //Nombra el archivo de salida junto con su extension
        
        try {
        
            EntradaB = archivo_de_entrada.readByte(); //Asigna a inputByte la primera entrada del archivo
            
            conversion_int = new Byte(EntradaB).intValue(); //convertir byte a int y guardandolo en una variable
            
            if(conversion_int < 0) conversion_int += 256; //Le da el codeword caracter que se evaluando
            caracter = (char) conversion_int; // convierte int a char y lo guarda en caracter
            Ruta = ""+caracter; //Modifica la direccion de entrada
            
            while(true) {
                EntradaB = archivo_de_entrada.readByte(); // Retorna el peso del archivo en bytes
                conversion_int = new Byte(EntradaB).intValue(); //Convierte tipo byte a int
            
                if(conversion_int < 0) conversion_int += 256; //Le da el codeword al caracter que se esta evaluando
                caracter = (char) conversion_int; // convierte int a char y lo guarda en caracter
                
                
                if(diccionario.containsKey(Ruta+caracter)) {
                    Ruta = Ruta+caracter;}//Se guarda una cadena de caracter nueva
                                          
                else {
                    BP = conversion(diccionario.get(Ruta));
                    if(limite_bits) {
                        Buffer[0] = (byte) Integer.parseInt(BP.substring(0,8),2);  
                        Buffer[1] = (byte) Integer.parseInt(BP.substring(8,12)+"0000",2);                   
                    }
                    else {
                        Buffer[1] += (byte) Integer.parseInt(BP.substring(0,4),2); 
                        Buffer[2] = (byte) Integer.parseInt(BP.substring(4,12),2);
                        for(i=0;i<Buffer.length;i++) {
                            archivo_de_salida.writeByte(Buffer[i]);
                            Buffer[i]=0;
                        }
                    }   
                    
                    limite_bits = !limite_bits;
                    if(TamanoD < 4096) diccionario.put(Ruta+caracter,TamanoD++);
                    Ruta=""+caracter;}                                                 
                }
            }
              
        catch(IOException ie) {
            BP = conversion(diccionario.get(Ruta));
            if(limite_bits) {
                Buffer[0] = (byte) Integer.parseInt(BP.substring(0,8),2);  
                Buffer[1] = (byte) Integer.parseInt(BP.substring(8,12)+"0000",2);
                archivo_de_salida.writeByte(Buffer[0]);  
                archivo_de_salida.writeByte(Buffer[1]);                
            }
            else {
                Buffer[1] += (byte) Integer.parseInt(BP.substring(0,4),2); 
                Buffer[2] = (byte) Integer.parseInt(BP.substring(4,12),2);
                for(i=0;i<Buffer.length;i++) {
                     archivo_de_salida.writeByte(Buffer[i]);
                     Buffer[i]=0;
                }}
            archivo_de_entrada.close();
            archivo_de_salida.close();  
            }}
    
    public static String conversion(int i) {
        String conversion_a_12bits = Integer.toBinaryString(i);
        while (conversion_a_12bits.length() < 12) conversion_a_12bits = "0" + conversion_a_12bits;
        return conversion_a_12bits;
    }}