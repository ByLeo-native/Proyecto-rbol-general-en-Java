package GUI;

import java.util.Iterator;

import Excepciones.BoundaryViolationException;
import Excepciones.EmptyTreeException;
import Excepciones.GInvalidOperationException;
import Excepciones.InvalidEntryException;
import Excepciones.InvalidKeyException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;
import TDAArbol.ArbolGeneral;
import TDAArbol.Tree;
import TDADiccionario.DiccionarioConHashAbierto;
import TDADiccionario.Dictionary;
import TDALista.Position;
import TDAMapeo.Entrada;
import TDAMapeo.Entry;

public class BackgroundPrograma {
	private boolean seCreoArbol;
	private boolean seCreoRaiz;
	private Tree<Entry<String, Integer>> arbolGeneral;
	private int gradoDelArbol;
	//Diccionario que almacena la cantidad de hijos y el rotulo de un nodo
	private Dictionary<String, Integer> diccionarioDeGrados;
	private int ordenDelArbol;
	private String rotuloDelNodoConMasDescendiente;
		
	public BackgroundPrograma () {
		this.seCreoArbol = false;
		this.arbolGeneral = null;
		this.diccionarioDeGrados = new DiccionarioConHashAbierto< String, Integer>();
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
		this.ordenDelArbol = 1;
		seEjecutoCompleto = true;
		
		try {
			this.diccionarioDeGrados.insert( rotuloDeLaRaiz, 0);
			this.rotuloDelNodoConMasDescendiente = rotuloDeLaRaiz;
		} catch (InvalidKeyException e) {}
		
		return seEjecutoCompleto ;
	}
	
	public boolean agregarNodo( String rotuloDeNuevoNodo, int valorDeNuevoNodo, String rotuloDelNodoAncestro, int valorDelNodoAncestro) throws InvalidPositionException, GInvalidOperationException {
		boolean seEncontro = false;
		if(!seCreoArbol || !seCreoRaiz) {
			throw new GInvalidOperationException("Error en ejecucion");
		} else {

			Iterable<Position<Entry<String, Integer>>> list = this.arbolGeneral.positions();
			Iterator<Position<Entry<String, Integer>>> it = list.iterator();
			Position<Entry<String,Integer>> pos = null;
			while(it.hasNext() && !seEncontro) {
				Position<Entry<String, Integer>> aux = it.next();
				if(aux.element().getKey().equals(rotuloDelNodoAncestro)) {
					seEncontro = true;
					pos = aux;
				}
			}
			
			if(!seEncontro) {
				throw new InvalidPositionException("No se encontro el nodo ancestro ( "+rotuloDelNodoAncestro+", "+valorDelNodoAncestro+") en el árbol");
			} else {
				Entry<String, Integer> entradaNueva = new Entrada<String, Integer>(rotuloDeNuevoNodo, valorDeNuevoNodo);
				this.arbolGeneral.addLastChild(pos, entradaNueva);
			
				//Parte que actualiza el diccionario con la cantidad de hijos de un nodo
				
				Entry<String,Integer> entrada = null;
				try {
					//Agrego al nuevo nodo con cero descendientes
					this.diccionarioDeGrados.insert(rotuloDeNuevoNodo, 0);
					
					entrada = this.diccionarioDeGrados.find(rotuloDelNodoAncestro);
				
					//Si no encuentra la clave en el diccionario, la agrega con cantidad de hijos igual a 0
					if( entrada == null ) {
						System.out.println("Esto es raro");
					} else {
						//Si encontro la clave en el diccionario
						//Obtengo la cantidad de hijos actual de la entrada ancestro
						int cantDeHijosDelAncestro = entrada.getValue();
						
						int nuevaCantDeHijosDelAncestro = cantDeHijosDelAncestro + 1;
						//Remuevo la entrada ancestro del diccionario
						this.diccionarioDeGrados.remove(entrada);
						//Vuelvo a agregar al diccionario con el mismo rotulo pero con actualizando la cantidad de descendientes
						this.diccionarioDeGrados.insert(rotuloDelNodoAncestro, nuevaCantDeHijosDelAncestro);
						
						this.verificarGradoDelArbol( rotuloDelNodoAncestro, nuevaCantDeHijosDelAncestro);
					} 
				} catch (InvalidKeyException | InvalidEntryException e1) {
					System.out.println("Algo salio mal");
				}
			}
			return seEncontro;
		}
	}
	
	public boolean eliminarNodo( String rotuloDelNodo, Integer valorDelNodo) throws InvalidPositionException {

		boolean seEncontro = false;
		Iterable<Position<Entry<String, Integer>>> list =this.arbolGeneral.positions();
		Iterator<Position<Entry<String, Integer>>> it = list.iterator();
		Position<Entry<String,Integer>> pos = null;
		while(it.hasNext() && !seEncontro) {
			Position<Entry<String, Integer>> aux = it.next();
			if(aux.element().getKey().equals(rotuloDelNodo) && aux.element().getValue().equals(valorDelNodo)) {
				seEncontro = true;
				pos = aux;
			}
		}
		
		if(!seEncontro) {
			throw new InvalidPositionException("No se encontro el nodo ancestro ( "+rotuloDelNodo+", "+valorDelNodo+") en el árbol");
		} else {
			
			int cantidadDeHijosDelAncestro = 0;
			int cantidadDeHijosDelNodo = 0;
			String rotuloDelAncestroDelNodoRemovido = null;
			Entry<String,Integer> entradaDelAncestro = null;
			//Obtengo informacion del nodo ancestro del ARBOL antes de eliminarlo
			if(!this.arbolGeneral.isRoot(pos)) {
				try {
					entradaDelAncestro = this.arbolGeneral.parent(pos).element();
					rotuloDelAncestroDelNodoRemovido = entradaDelAncestro.getKey();
				} catch (InvalidPositionException | BoundaryViolationException e) {
					System.out.println("Algo mal salio (1)");
				}
					
			}
			
			this.arbolGeneral.removeNode(pos);
			//Los descendientes del nodo removido pasa al nodo ancestro menos uno (el nodo removido)
			//Ya eliminado, prosigo actualizando al nodo ancestro en el diccionario
			try {
				//Obtengo la entrada "del diccionario" del nodo eliminado
				Entry<String,Integer> entradaDelDiccionario = this.diccionarioDeGrados.find(rotuloDelNodo);
				//Obtengo la cantidad de descendientes que tenia
				cantidadDeHijosDelNodo = entradaDelDiccionario.getValue();
				//Remuevo de la lista al nodo eliminado
				this.diccionarioDeGrados.remove(entradaDelDiccionario);
				
				Entry<String,Integer> entradaDelAncestroDelDiccionario = this.diccionarioDeGrados.find(rotuloDelAncestroDelNodoRemovido);
				cantidadDeHijosDelAncestro = entradaDelAncestroDelDiccionario.getValue();
				//Elimino al nodo ancestro para luego volver a insertarlo con la cantidad de hijos actualizado
				this.diccionarioDeGrados.remove(entradaDelAncestroDelDiccionario);
				
				int nuevaCantidadDeDescendienteDelAncestro = cantidadDeHijosDelAncestro + cantidadDeHijosDelNodo - 1;

				//Inserto al ancestro del nodo eliminado y para los descendientes tengo: la cantidad que tenia + la cantidad de descendientes que tenia el nodo eliminado - uno por el nodo eliminado
				this.diccionarioDeGrados.insert(rotuloDelAncestroDelNodoRemovido, nuevaCantidadDeDescendienteDelAncestro );

				this.verificarGradoDelArbol( rotuloDelAncestroDelNodoRemovido, nuevaCantidadDeDescendienteDelAncestro);
			} catch (InvalidKeyException | InvalidEntryException e) {
				System.out.println("Algo salio mal (2)");
			}
		}
		
		return seEncontro;
		
	}
	
	public boolean obtenerGrados() {
		boolean seCompleto = false;
		int [] arreglo = new int [this.ordenDelArbol];
		
		
		
		return seCompleto;
	}
	
	private void verificarGradoDelArbol(String rotuloDelNodoAVerificar, int cantidadDeDescendienteDeUnNodo) {
		//Cada vez que se modifica el arbol, se utilizara el metodo para ver si hay que actualizar la variable gradoDelArbol y cual es el rotulo
		if ( cantidadDeDescendienteDeUnNodo > this.gradoDelArbol ) {
			this.gradoDelArbol = cantidadDeDescendienteDeUnNodo;
			this.rotuloDelNodoConMasDescendiente = rotuloDelNodoAVerificar;
		} else if (rotuloDelNodoAVerificar.equals(rotuloDelNodoConMasDescendiente)) {
			//Si la cantidad pasada por parametro no es mayor al grado del arbol pero es el mismo rotulo --> se modifico el nodo que tenia mayor descendientes
			this.buscarGradoDelArbol();
		}	
		System.out.println(this.gradoDelArbol);
	}
	
	private void buscarGradoDelArbol() {
		//Establezco inicialmente que el grado del arbol es cero
		int mayorGradoEncontrado = 0;
		String rotuloDelNodoDeMayorGradoEncontrado = "";
		if(this.arbolGeneral.size() == 1) {
			mayorGradoEncontrado = 1;
			try {
				rotuloDelNodoDeMayorGradoEncontrado  = this.arbolGeneral.root().element().getKey();
			} catch (EmptyTreeException e) {}
			
		} else {
			
			Iterable<Position<Entry<String,Integer>>> positions = this.arbolGeneral.positions();
			//Por cada posicion que referencia a un nodo del arbol
			for( Position<Entry<String,Integer>> pos : positions) {
				//Asumo que tiene cero o mas hijos
				int cantDeHijosDePos = 0;
				boolean esPosInternal = false;
				
				try {
					esPosInternal = this.arbolGeneral.isInternal(pos);
				} catch (InvalidPositionException e1) {}
				
				//Si el nodo que referencia pos es un nodo interno en el arbol
				if(esPosInternal) {
					Iterable<Position<Entry<String,Integer>>> hijosDePos = null;
					try {
						hijosDePos = this.arbolGeneral.children(pos);
					} catch (InvalidPositionException e) {}
					
					for(Position<Entry<String,Integer>> poshijo: hijosDePos) {
						cantDeHijosDePos++;
					}
					
					if(cantDeHijosDePos > mayorGradoEncontrado) {
						mayorGradoEncontrado = cantDeHijosDePos;
						rotuloDelNodoDeMayorGradoEncontrado = pos.element().getKey();
					}
				}
				
			}
		}
		
		//Luego de la busqueda, actualizo las variables por lo encontrado
		this.gradoDelArbol = mayorGradoEncontrado;
	}
	
	public int obtenerTamañoDelArbol() {
		return this.arbolGeneral.size();
	}
	
	public String rotuloDeLaRaiz() {
		String rotulo = null;
		try {
			rotulo = this.arbolGeneral.root().element().getKey();
		} catch (EmptyTreeException e) {
			e.fillInStackTrace();
		}
		return rotulo;
	}
	
	public int valorDeLaRaiz() {
		int valor = 0;
		try {
			valor = this.arbolGeneral.root().element().getValue();
		} catch (EmptyTreeException e) {
			e.fillInStackTrace();
		}
		return valor;
	}
	
	private int height(Position<Entry<String, Integer>> v) throws InvalidPositionException {

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

	
