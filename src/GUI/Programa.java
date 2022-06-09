package GUI;

import java.util.Iterator;

import Auxiliar.Queue;
import Auxiliar.QueueEnlazada;
import Excepciones.BoundaryViolationException;
import Excepciones.EmptyQueueException;
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

public class Programa {
	private boolean seCreoArbol;
	private boolean seCreoRaiz;
	private Tree<Entry<String, Integer>> arbolGeneral;
	private int gradoDelArbol;
	//Diccionario que almacena el rotulo de un nodo y la cantidad de descendiente que tiene
	private Dictionary<String, Integer> diccionarioDeGrados;
	private int ordenDelArbol;
	private String rotuloDelNodoConMasDescendiente;
		
	public Programa () {
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
			//Busco la posicion del nodo a agregar un nuevo nodo
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
				//Si se encontro la posicion del nodo a agregar un nuevo nodo, creo el objeto de tipo entrada que tendra el rotulo y el entero de 
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
	
	public boolean eliminarNodo( String rotuloDelNodo) throws InvalidPositionException {

		boolean seEncontro = false;
		Iterable<Position<Entry<String, Integer>>> list =this.arbolGeneral.positions();
		Iterator<Position<Entry<String, Integer>>> it = list.iterator();
		Position<Entry<String,Integer>> pos = null;
		while(it.hasNext() && !seEncontro) {
			Position<Entry<String, Integer>> aux = it.next();
			if(aux.element().getKey().equals(rotuloDelNodo)) {
				seEncontro = true;
				pos = aux;
			}
		}
		
		if(!seEncontro) {
			throw new InvalidPositionException("No se encontro el nodo ancestro ( "+rotuloDelNodo+") en el árbol");
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
			//Elimino al nodo a eliminar
			this.arbolGeneral.removeNode(pos);
			
			//Modifico al diccionario
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
	
	public String obtenerGrados() {
		/*
		 * Se crea un arreglo que tendra el tamaño del grado del arbol, cada indice del arreglo 
		 * coincidira con la cantidad de descendiente de los nodos 
		 * donde se almacenaran sus respectivos rotulos
		 */
		String textoCompleto = "";
		if( this.seCreoRaiz ) {
			
			for( int i = 0; i <= this.gradoDelArbol; i++) {
				String [] arreglo = new String [this.diccionarioDeGrados.size()];
				int cant= 0;
				textoCompleto += "Nodos con "+i+" hijos: ";
				for(Entry<String,Integer> entrada : this.diccionarioDeGrados.entries()) {
					if( entrada.getValue() == i) {
						arreglo[cant++] = entrada.getKey();
					}
				}
				boolean aunFaltaPorVer = true;
				int j = 0;
				while( j < arreglo.length && aunFaltaPorVer ) {
					if(arreglo[j] == null) {
						aunFaltaPorVer = false;
					} else {
						textoCompleto += arreglo[j]+" "; 
						j++;
					}
				}
				
				textoCompleto += "\n";
			}
		}
		
		return textoCompleto;
	}
	
	public int obtenerGradoDelArbol() {
		return this.gradoDelArbol;
	}
	
	public String obtenerCamino(String rotulo, int valor) throws InvalidPositionException {
		String camino = "";
		boolean seEncontro = false;
		Iterable<Position<Entry<String, Integer>>> list =this.arbolGeneral.positions();
		Iterator<Position<Entry<String, Integer>>> it = list.iterator();
		Position<Entry<String,Integer>> pos = null;
		while(it.hasNext() && !seEncontro) {
			Position<Entry<String, Integer>> aux = it.next();
			if(aux.element().getKey().equals(rotulo)) {
				seEncontro = true;
				pos = aux;
			}
		}
		
		if(!seEncontro) {
			throw new InvalidPositionException("No se encontro el nodo ancestro ( "+rotulo+", "+valor+") en el árbol");
		} else {
			camino = ""+pos.element().getKey();
			try {
				Position<Entry<String,Integer>> posAscendente = this.arbolGeneral.parent(pos);
				boolean seCompleto = false;
				
				while( !seCompleto ) {
					camino += "--"+pos.element().getKey();
					if( this.arbolGeneral.isRoot(posAscendente) ) {
						seCompleto = true;
					} else {
						posAscendente = this.arbolGeneral.parent(posAscendente);
					}
				}
			} catch (InvalidPositionException | BoundaryViolationException e) {}
		}
		return camino;
		
	}

	public String mostrarRecorridoPreorden() {
		String recorrido = "";
		try {
			recorrido = this.PreOrden(this.arbolGeneral.root());
		} catch (EmptyTreeException e) {}
		return recorrido;
	}
	
	public String mostrarRecorridoPostorden() {
		String recorrido = "";
		try {
			recorrido += this.PosOrden(this.arbolGeneral.root());
			
		} catch (EmptyTreeException e) {}
		return recorrido;
	}
	
	public String mostrarPorNiveles() {
		String texto = "";
		Queue<Position<Entry<String,Integer>>> cola = new QueueEnlazada<Position<Entry<String,Integer>>>();
		try {
			cola.enqueue(this.arbolGeneral.root());
			cola.enqueue(null);
			while (!cola.isEmpty()) {
				Position<Entry<String,Integer>> v = cola.dequeue();
				if (v != null) {
					texto += "-"+v.element().getKey()+"-";
					for (Position<Entry<String,Integer>> w : this.arbolGeneral.children(v))
						cola.enqueue(w);
				} else {
					texto += "\n";
					if (!cola.isEmpty())
						cola.enqueue(null);
				}
			}
		} catch (EmptyQueueException | EmptyTreeException | InvalidPositionException e) {}
		return texto;

	}
	
	public boolean eliminarNodosGradoK(int k) {
		boolean seCompleto = false;
		int cant = 0;
		
		String [] arreglo = new String [this.arbolGeneral.size()];
		
		//Almaceno los rotulos de los nodos con orden k
		for(Entry<String,Integer> e : this.diccionarioDeGrados.entries()) {
			if(e.getValue().equals(k)) {
				arreglo[cant++] = e.getKey();
			}
		}
		
		//Por cada rotulo del arreglo, voy a eliminar con la funcion eliminarNodo()
		for(int i = 0; i < arreglo.length ; i++) {
			try {
				this.eliminarNodo(arreglo[i]);
			} catch (InvalidPositionException e1) {}
		}
		seCompleto = true;
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
	
	private String PreOrden(Position<Entry<String,Integer>> v) {
		String recorrido = ""+v.element().getKey();
		try {
			for (Position<Entry<String, Integer>> hijo : this.arbolGeneral.children(v)) {
				recorrido += "-"+PreOrden(hijo);
			}
		} catch (InvalidPositionException e) {}
		return recorrido;
	}
	
	private String PosOrden( Position<Entry<String,Integer>> v) {
		String recorrido = "";
		try {
			for (Position<Entry<String,Integer>> hijo : this.arbolGeneral.children(v)) {
				recorrido += this.PosOrden(hijo);
				
			}

			recorrido += v.element().getKey();
			
			if(!this.arbolGeneral.isRoot(v)) {
				recorrido += "-";
			}
			
			
		} catch (InvalidPositionException e) {}
		return recorrido;
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

	

