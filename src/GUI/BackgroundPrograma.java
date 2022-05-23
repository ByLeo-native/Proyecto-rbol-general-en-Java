package GUI;

import java.util.Iterator;

import Excepciones.GInvalidOperationException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;
import TDAArbol.ArbolGeneral;
import TDAArbol.TNodo;
import TDALista.Position;
import TDAMapeo.Entrada;
import TDAMapeo.Entry;

public class BackgroundPrograma {
	private boolean seCreoArbol;
	private boolean seCreoRaiz;
	private ArbolGeneral<Entry<String, Integer>> arbolGeneral;
	
	public BackgroundPrograma () {
		arbolGeneral = null;
		this.seCreoArbol = false;
	}
	
	public boolean crearArbol() {
		this.arbolGeneral = new ArbolGeneral<Entry<String,Integer>>();
		this.seCreoArbol = true;
		return true;
	}
	
	public boolean crearRaiz(String rotuloDeLaRaiz, int valorDeLaRaiz)  throws InvalidOperationException {
		boolean seEjecutoCompleto = false;
		Entry<String, Integer> entrada = new Entrada<String,Integer>( rotuloDeLaRaiz, valorDeLaRaiz );
		try {
			arbolGeneral.createRoot(entrada);	
		} catch (InvalidOperationException e) {
			throw new InvalidOperationException(e.getMessage());
		}
		this.seCreoRaiz = true;
		seEjecutoCompleto = true;
		return seEjecutoCompleto ;
	}
	
	public boolean agregarNodo( String rotuloDeNuevoNodo, int valorDeNuevoNodo, String rotuloDelNodoAncestro, int valorDelNodoAncestro) throws InvalidPositionException, GInvalidOperationException {
		boolean seCompleto = false;
		if(!seCreoArbol || !seCreoRaiz) {
			throw new GInvalidOperationException("Error en ejecucion");
		} else {
			Entry<String,Integer> entrada = new Entrada<String,Integer>( rotuloDeNuevoNodo, valorDeNuevoNodo);
			Entry<String,Integer> entradaAncestro = new Entrada<String, Integer>( rotuloDelNodoAncestro, valorDelNodoAncestro);
			Position<Entry<String, Integer>> posAncestro = new TNodo<Entry<String, Integer>>( entradaAncestro );
			try {
				this.arbolGeneral.addLastChild( posAncestro, entrada);
			} catch (InvalidPositionException e) {
				throw new InvalidPositionException( e.getMessage());
			}
			seCompleto = true;
			return seCompleto;
		}
	}
	
	
}

	
