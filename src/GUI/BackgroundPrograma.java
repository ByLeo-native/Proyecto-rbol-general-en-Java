package GUI;

import java.util.Iterator;

import Excepciones.GInvalidOperationException;

import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;
import TDAArbol.ArbolGeneral;
import TDAArbol.TNodo;
import TDALista.ListaDoblementeEnlazada;
import TDALista.Position;
import TDALista.PositionList;
import TDAMapeo.Entrada;
import TDAMapeo.Entry;

public class BackgroundPrograma {
	private boolean seCreoArbol;
	private boolean seCreoRaiz;
	private ArbolGeneral<Entry<String, Integer>> arbolGeneral;
	//Lista para guardar las referencias de las entradas añadidas al árbol
	private PositionList<Entry<String,Integer>> listDeNodosDefinidos;
	
	public BackgroundPrograma () {
		this.listDeNodosDefinidos = new ListaDoblementeEnlazada<Entry<String,Integer>>();
		this.arbolGeneral = null;
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
			this.arbolGeneral.createRoot(entrada);	
		} catch (InvalidOperationException e) {
			throw new InvalidOperationException(e.getMessage());
		}
		
		this.seCreoRaiz = true;
		seEjecutoCompleto = true;
		this.listDeNodosDefinidos.addLast(entrada);
		return seEjecutoCompleto ;
	}
	
	public boolean agregarNodo( String rotuloDeNuevoNodo, int valorDeNuevoNodo, String rotuloDelNodoAncestro, int valorDelNodoAncestro) throws InvalidPositionException, GInvalidOperationException {
		boolean seCompleto = false;
		if(!seCreoArbol || !seCreoRaiz) {
			throw new GInvalidOperationException("Error en ejecucion");
		} else {
//			Entry<String,Integer> entradaNueva = new Entrada<String,Integer>( rotuloDeNuevoNodo, valorDeNuevoNodo);
//			Position<Entry<String,Integer>> posAncestro = this.buscarEnLaLista(rotuloDelNodoAncestro, valorDelNodoAncestro);
//			Position<Entry<String,Integer>> posEntradaNueva = null;
//			
//			if(posAncestro == null) {
//				throw new InvalidPositionException("No se encontro el nodo definido en el árbol");
//			} else {
//				try {
//					posEntradaNueva = this.arbolGeneral.addLastChild( posAncestro, entradaNueva);
//				} catch (InvalidPositionException e) {
//					throw new InvalidPositionException( e.getMessage());
//				}
//				seCompleto = true;
//				this.listDeNodosDefinidos.addLast(posEntradaNueva.element());
//			}
			
			Iterable<Position<Entry<String, Integer>>> list =this.arbolGeneral.positions();
			Iterator<Position<Entry<String, Integer>>> it = list.iterator();
			Position<Entry<String,Integer>> pos = null;
			while(it.hasNext() && !seCompleto) {
				Position<Entry<String, Integer>> aux = it.next();
				if(aux.element().getKey().equals(rotuloDelNodoAncestro)) {
					seCompleto = true;
					pos = aux;
				}
			}
			
			if(seCompleto) {
				Entry<String, Integer> entradaNueva = new Entrada<String, Integer>(rotuloDeNuevoNodo, valorDeNuevoNodo);
				this.arbolGeneral.addLastChild(pos, entradaNueva);
			}
			
			return seCompleto;
		}
	}
	
	public boolean eliminarNodo( String rotuloDelNodo, Integer valorDelNodo) throws InvalidPositionException {
		boolean seCompleto = false;
		
		Position<Entry<String,Integer>> posDelNodoAEliminar = this.buscarEnLaLista(rotuloDelNodo, valorDelNodo);
		
		if(posDelNodoAEliminar == null ) {
			throw new InvalidPositionException("No se encontro el nodo definido en el árbol");
		} else {
			this.arbolGeneral.removeNode(posDelNodoAEliminar);
		}
		
		seCompleto = true;
		this.listDeNodosDefinidos.remove(posDelNodoAEliminar);
		return seCompleto;
	}
	
	public boolean obtenerGrados() {
		boolean seCompleto = false;
		
		
		
		return seCompleto;
	}
	
	public int obtenerGradoDelArbol() {
		int gradoDelArbol = 0;
		
		return gradoDelArbol;
	}
	
	public int obtenerTamañoDelArbol() {
		return this.arbolGeneral.size();
	}
	
	private Position<Entry<String,Integer>> buscarEnLaLista(String rotulo, Integer valor) {
		
		boolean seEncontro = false;
		Iterator<Position<Entry<String,Integer>>> it = this.listDeNodosDefinidos.positions().iterator();
		Position<Entry<String,Integer>> posActual = it.hasNext() ? it.next() : null;
		
		TNodo<Entry<String,Integer>> nodoRetornar = null;
		
		while( posActual != null && !seEncontro ) {
			if(posActual.element().getKey().equals(rotulo) && posActual.element().getValue().equals(valor)) {
				seEncontro = true;
			} else {
				posActual = it.hasNext() ? it.next() : null;
			}
		}
		
		if(seEncontro) {
			nodoRetornar = new TNodo<Entry<String,Integer>>( posActual.element());
		}
		
		return nodoRetornar;
	}
	
	private int height(Position<Entry<String, Integer>> v) throws InvalidPositionException {
//		int altura = 0;
//		for(Position<Entry<String,Integer>> v: this.arbolGeneral.positions()) {
//			try {
//				if( this.arbolGeneral.isExternal(v)) {
//					altura = Math.max( altura, this.arbolGeneral.depth(v));
//				}	
//			} catch (InvalidPositionException e) {
//				throw new InvalidPositionException (e.getMessage());
//			}
//			
//		}
//		return altura;
		int altura = 0;
		if(this.arbolGeneral.isExternal(v)) {
			return 0;
		} else {
			for(Position<Entry<String,Integer>> w: this.arbolGeneral.children(v)) {
				altura = Math.max(altura, this.height(w));
			}
			return 1+altura;
		}
	}
	
	
	
}

	
