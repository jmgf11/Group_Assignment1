package tarea01;

import java.util.Scanner;

public class menu{

	public static void main (String[] args ){
	//user choice
	int choice;
	compresor co = new compresor();
	Descomponser de = new Descomponser();
	//display menu


	System.out.println("Menu");
	System.out.println("1.Comprimir");
	System.out.println("2.Descomprimir");
	System.out.println("0.Salir");
	System.out.print("Introduzca una opcion : ");

	//setup scanner
	Scanner in = new Scanner(System.in);
	
	//Get choice from user
	choice = in.nextInt();
	
	//menu loop
	while (choice != 0)
		{
		if (choice == 1)
		{
		co.main_compresion();
		
		}
	else if (choice == 2)
		{
		de.main_decompresor();
		
		}
		
	else {
		
		System.out.println("Vuelva a intentarlo");
		}

	//display menu

	System.out.println("Menu");
	System.out.println("1.Comprimir");
	System.out.println("2.Descomprimir");
	System.out.println("0.Salir");
	System.out.print("Introduzca una opcion : ");
	
	//Get choice from user
	choice = in.nextInt();
	
            }
	System.out.println("Saliendo..");
        
	
	}
		
	
      }