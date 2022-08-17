package ec;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.io.*;

public class ClsParametros {
	static File parametro = null;
	static FileReader fr = null;
	static BufferedReader br = null;
	static ArrayList<String> parametroList=new ArrayList();
	public static void main(String[] args) {
		//parametrosInicio();
	}

	//// lectura de parámetros para la transmisión de datos ////

	public ArrayList<String> parametrosInicio()
	{
		try 
		{
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			//parametro = new File ("parametros.txt");
			parametro = new File ("C:\\maquinaBT\\utilitarios\\parametros.txt");
			fr = new FileReader (parametro);
			br = new BufferedReader(fr);
			// Lectura del fichero
			String linea;
			while((linea=br.readLine())!=null)
			{					
				//System.out.println("linea "+linea);
				parametroList.add(linea);
			}
			/*for (String contParametros : parametroList) {
				System.out.println(contParametros);
			}*/			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try
			{                    
				if( null != fr )
				{   
					fr.close();     
				}                  
			}
			catch (Exception e2)
			{ 
				e2.printStackTrace();
			}
		}
		return parametroList;
	}
	public void SetImagenLabel(JLabel lblImagen, String ruta)
	{
		ImageIcon igmImagen = new ImageIcon(ruta);
		Icon icon = new ImageIcon(igmImagen.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), 1));
		lblImagen.setIcon(icon);
		//repaint();
	}
}
