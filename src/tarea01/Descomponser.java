package tarea01;

import java.io.*;
import java.util.*;


public class Descomponser {
	
	static public String[] ArregloChar;
    static public String Ruta="",Ext="";
    static public int TamanoD = 256, cp, pp;
    static public byte[] Buffer = new byte[3];
    public static boolean limite_bits = true;
    
    public static void main_decompresor() {
        System.out.println("Introduzca la ruta del archivo que desea descomprimir ");
        Scanner obj = new Scanner(System.in);
        Ruta = obj.next();
        System.out.println("Introduzca la extension que desea en la imagen descomprimida (ejemplo: imagen.jpg, imagen.png)");
        Ext = obj.next();
        try {
            File archivo = new File(Ruta);
            System.out.println(Ruta);
            descompresion();
            System.out.println("Descompresion completada revise su archivo."+Ext);      
        }
        catch(IOException ie) {
            System.out.println("Ruta no encontrada");
        }
    }
    
    public static void descompresion() throws IOException {
        ArregloChar = new String[4096];
        int i;
        
        for (i=0;i<256;i++) ArregloChar[i] = Character.toString((char)i);
        
        
        RandomAccessFile archivo_de_entrada = new RandomAccessFile(Ruta,"r");
        String[] narchivo = Ruta.split("\\.");
        RandomAccessFile archivo_de_salida = new RandomAccessFile(narchivo[0].concat("." + Ext),"rw");
        
        try {
            Buffer[0] = archivo_de_entrada.readByte();
            Buffer[1] = archivo_de_entrada.readByte();
            pp = getIntValue(Buffer[0], Buffer[1], limite_bits);
            limite_bits = !limite_bits;
            archivo_de_salida.writeBytes(ArregloChar[pp]);

          
            while (true) {
            
                if (limite_bits) {
                    Buffer[0] = archivo_de_entrada.readByte();
                    Buffer[1] = archivo_de_entrada.readByte();
                    cp = getIntValue(Buffer[0], Buffer[1], limite_bits);
                } 
                else {
                    Buffer[2] = archivo_de_entrada.readByte();
                    cp = getIntValue(Buffer[1], Buffer[2], limite_bits);
                }
                limite_bits = !limite_bits;

               
                if (cp >= TamanoD) {
                    if (TamanoD < 4096) {
                        ArregloChar[TamanoD] = ArregloChar[pp] + ArregloChar[pp].charAt(0);
                    }
                    TamanoD++;
                    archivo_de_salida.writeBytes(ArregloChar[pp] + ArregloChar[pp].charAt(0));
                } 
                
                
                else {
                    if (TamanoD < 4096) {
                        ArregloChar[TamanoD] = ArregloChar[pp] + ArregloChar[cp].charAt(0);
                    }
                    TamanoD++;
                    archivo_de_salida.writeBytes(ArregloChar[cp]);
                }
                pp = cp;
            }
        } 
        catch (EOFException e) {
            archivo_de_entrada.close();
            archivo_de_salida.close();
        }
    }
    
   
    public static int getIntValue(byte b1, byte b2, boolean limite_bits) {
        String t1 = Integer.toBinaryString(b1);
        String t2 = Integer.toBinaryString(b2);

        while (t1.length() < 8) t1 = "0" + t1;
        if (t1.length() == 32) t1 = t1.substring(24, 32);
        
        while (t2.length() < 8) t2 = "0" + t2;
        if (t2.length() == 32) t2 = t2.substring(24, 32);

        if (limite_bits) return Integer.parseInt(t1 + t2.substring(0, 4), 2);
        else return Integer.parseInt(t1.substring(4, 8) + t2, 2);
        
    }

}
