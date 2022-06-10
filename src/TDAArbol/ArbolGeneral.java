package TDAArbol;

//Funciona
import java.util.Iterator;

import Excepciones.BoundaryViolationException;
import Excepciones.EmptyListException;
import Excepciones.EmptyTreeException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;
import TDALista.ListaDoblementeEnlazada;
import TDALista.Position;
import TDALista.PositionList;


public class ArbolGeneral <E> implements Tree <E> {
	private int tamaño;
	private TNodo<E> raiz;
	
	/**
	 * Crea un arbol vacio
	 */
	public ArbolGeneral() {
		this.raiz = null;
		tamaño = 0;
	}
	
	/**
	 * Consulta la cantidad de nodos en el árbol.
	 * @return Cantidad de nodos en el árbol.
	 */
	public int size() {
		return this.tamaño;
	}
	
	/**
	 * Consulta si el árbol está vacío.
	 * @return Verdadero si el árbol está vacío, falso en caso contrario.
	 */
	public boolean isEmpty() {
		return this.tamaño == 0;
	}
	
	/**
	 * Devuelve un iterador de los elementos almacenados en el árbol en preorden.
	 * @return Iterador de los elementos almacenados en el árbol.
	 */
	public Iterator<E> iterator() {
		PositionList<E> list = new ListaDoblementeEnlazada<E>();
		if( !this.isEmpty() ) {
			this.recPreordenPorElementos(raiz, list);
		}
		return list.iterator();
	}
	
	/**
	 * Devuelve una colección iterable de las posiciones de los nodos del árbol.
	 * @return Colección iterable de las posiciones de los nodos del árbol.
	 */
	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> list = new ListaDoblementeEnlazada<Position<E>>();
		if( !this.isEmpty() ) {
			this.recPreorden(raiz, list);
		}
		return list;
	}
	
	/**
	 * Reemplaza el elemento almacenado en la posición dada por el elemento pasado por parámetro. Devuelve el elemento reemplazado.
	 * @param v Posición de un nodo.
	 * @param e Elemento a reemplazar en la posición pasada por parámetro.
	 * @return Elemento reemplazado.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida.
	 */
	public E replace (Position<E> v, E e) throws InvalidPositionException {
		if( this.raiz == null) {
			throw new InvalidPositionException("El arbol esta vacio");
		}
		
		TNodo<E> nodo = this.checkPosition(v);
		
		boolean pertenece = this.perteneceAlArbol(nodo);
		if(!pertenece) {
			throw new InvalidPositionException("La posicion v no fue encontrada en el árbol");
		} else {
			E elementToReturn = nodo.element();
			nodo.setElement(e);
			return elementToReturn;
		}
	}
	
	/**
	 * Devuelve la posición de la raíz del árbol.
	 * @return Posición de la raíz del árbol.
	 * @throws EmptyTreeException si el árbol está vacío.
	 */
	public Position<E> root() throws EmptyTreeException {
		if( this.raiz == null ) {
			throw new EmptyTreeException("Arbol vacio");
		} else {
			return raiz;
		}
	}
	
	/**
	 * Devuelve la posición del nodo padre del nodo correspondiente a una posición dada.
	 * @param v Posición de un nodo.
	 * @return Posición del nodo padre del nodo correspondiente a la posición dada.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida.
	 * @throws BoundaryViolationException si la posición pasada por parámetro corresponde a la raíz del árbol.
	 */
	public Position<E> parent (Position<E> v) throws InvalidPositionException, BoundaryViolationException {
		TNodo<E> nodo = this.checkPosition(v);
		
		if ( this.raiz == nodo ) {
			throw new BoundaryViolationException("La raiz no tiene ancestro");
		} else {
			return nodo.getPadre();
		}
	}
	
	/**
	 * Devuelve una colección iterable de los hijos del nodo correspondiente a una posición dada.
	 * @param v Posición de un nodo.
	 * @return Colección iterable de los hijos del nodo correspondiente a la posición pasada por parámetro.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida.
	 */
	public Iterable<Position<E>> children(Position<E> v) throws InvalidPositionException {
		TNodo<E> nodo = this.checkPosition(v);
		PositionList<TNodo<E>> descentans = nodo.getHijos();
		PositionList<Position<E>> list = new ListaDoblementeEnlazada<Position<E>>();
		for (TNodo<E> tnodo : descentans ) {
			list.addLast(tnodo);
		}
		return list;
	}
	
	/**
	 * Consulta si una posición corresponde a un nodo interno.
	 * @param v Posición de un nodo.
	 * @return Verdadero si la posición pasada por parámetro corresponde a un nodo interno, falso en caso contrario.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida.
	 */
	public boolean isInternal(Position<E> v) throws InvalidPositionException {
		TNodo<E> nodo = this.checkPosition(v);
		return !nodo.getHijos().isEmpty();
	}
	
	/**
	 * Consulta si una posición dada corresponde a un nodo externo.
	 * @param v Posición de un nodo.
	 * @return Verdadero si la posición pasada por parámetro corresponde a un nodo externo, falso en caso contrario.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida.
	 */
	public boolean isExternal(Position<E> v) throws InvalidPositionException {
		TNodo<E> nodo = this.checkPosition(v);
		return nodo.getHijos().isEmpty();
	}
	
	/**
	 * Consulta si una posición dada corresponde a la raíz del árbol.
	 * @param v Posición de un nodo.
	 * @return Verdadero, si la posición pasada por parámetro corresponde a la raíz del árbol,falso en caso contrario.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida.
	 */
	public boolean isRoot(Position<E> v) throws InvalidPositionException {
		try {
			TNodo<E> tNodo = checkPosition(v);
			return tNodo.equals(raiz);
		} catch ( InvalidPositionException e) {
			throw new InvalidPositionException(e.getMessage());
		}
	}
	
	/**
	 * Crea un nodo con rótulo e como raíz del árbol.
	 * @param E Rótulo que se asignará a la raíz del árbol.
	 * @throws InvalidOperationException si el árbol ya tiene un nodo raíz.
	 */
	public void createRoot(E e) throws InvalidOperationException {
		if ( this.raiz != null) {
			throw new InvalidOperationException("Operacion invalida");
		} else {
			this.raiz = new TNodo<E>( e );
			this.tamaño++;
		}
	}
	
	/**
	 * Agrega un nodo con rótulo e como primer hijo de un nodo dado.
	 * @param e Rótulo del nuevo nodo.
	 * @param padre Posición del nodo padre.
	 * @return La posición del nuevo nodo creado.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida o el árbol está vacío.
	 */
	public Position<E> addFirstChild(Position<E> p, E e) throws InvalidPositionException {
		if( this.raiz == null ) {
			throw new InvalidPositionException("Arbol vacio");
		}
		
		TNodo<E> ancester = this.checkPosition(p);
		
		boolean perteneceAlArbol = this.perteneceAlArbol(ancester);
		
		//Verifica si el nodo creado a partir de p pertenece al arbol
		if (!perteneceAlArbol) {
			throw new InvalidPositionException("p no pertenece al árbol");
		} else {
			TNodo<E> nuevo = new TNodo<E>( e, ancester);
			ancester.getHijos().addFirst(nuevo);
			this.tamaño++;
			return nuevo;
		}
	}
	
	/**
	 * Agrega un nodo con rótulo e como útimo hijo de un nodo dado.
	 * @param e Rótulo del nuevo nodo.
	 * @param p Posición del nodo padre.
	 * @return La posición del nuevo nodo creado.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida o el árbol está vacío.
	 */
	public Position<E> addLastChild(Position<E> p, E e) throws InvalidPositionException {
		if( this.raiz == null ) {
			throw new InvalidPositionException("Arbol vacio");
		}
		
		TNodo<E> ancester = this.checkPosition(p);
		
		boolean perteneceAlArbol = this.perteneceAlArbol(ancester);
		
		
		if (!perteneceAlArbol) {
			throw new InvalidPositionException("p no pertenece al árbol");
		} else {
			TNodo<E> nuevo = new TNodo<E>( e, ancester);
			ancester.getHijos().addLast(nuevo);
			this.tamaño++;
			return nuevo;
		}
	}
	
	/**
	 * Agrega un nodo con rótulo e como hijo de un nodo padre dado. El nuevo nodo se agregará delante de otro nodo también dado.
	 * @param e Rótulo del nuevo nodo.
	 * @param p Posición del nodo padre.
	 * @param rb Posición del nodo que será el hermano derecho del nuevo nodo.
	 * @return La posición del nuevo nodo creado.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida, o el árbol está vacío, o la posición rb no corresponde a un nodo hijo del nodo referenciado por p.
	 */
	public Position<E> addBefore(Position<E> p, Position<E> rb, E e) throws InvalidPositionException {
		
		if( this.raiz == null ) {
			throw new InvalidPositionException("Arbol vacio");
		}
		
		TNodo<E> ancester = this.checkPosition(p);
		
		boolean pertenece = this.perteneceAlArbol(ancester);
		
		if(!pertenece) {
			throw new InvalidPositionException("p no pertenece al arbol");
		} else {
			TNodo<E> hermanoDerecho = this.checkPosition(rb);
			
			boolean encontre = false;
			Iterator<Position<TNodo<E>>> it = ancester.getHijos().positions().iterator();
			Position<TNodo<E>> posActual = it.hasNext() ? it.next() : null;
			
			while( posActual != null && !encontre ) {
				if( posActual.element() == hermanoDerecho ) {
					encontre = true;
				} else {
					posActual = it.hasNext() ? it.next() : null;
				}
			}
			
			if (!encontre) {
				throw new InvalidPositionException("lb no es hijo de p");
			} else {
				Position<TNodo<E>> posOfRb = posActual;
				TNodo<E> nuevo = new TNodo<E>( e, ancester);
				ancester.getHijos().addBefore(posOfRb, nuevo);
				this.tamaño++;
				return nuevo;
			}
		}
	}
	
	/**
	 * Agrega un nodo con rótulo e como hijo de un nodo padre dado. El nuevo nodo se agregará a continuación de otro nodo también dado.
	 * @param e Rótulo del nuevo nodo.
	 * @param p Posición del nodo padre.
	 * @param lb Posición del nodo que será el hermano izquierdo del nuevo nodo.
	 * @return La posición del nuevo nodo creado.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida, o el árbol está vacío, o la posición lb no corresponde a un nodo hijo del nodo referenciado por p.
	 */
	public Position<E> addAfter(Position<E> p, Position<E> lb, E e) throws InvalidPositionException {
		
		if( this.raiz == null ) {
			throw new InvalidPositionException("Arbol vacio");
		}
		
		TNodo<E> ancester = this.checkPosition(p);
		
		boolean perteneceAlArbol = this.perteneceAlArbol(ancester);
		if(!perteneceAlArbol) {
			throw new InvalidPositionException("No se encontro p en el arbol");
		} else {
			TNodo<E> hermanoDerecho = this.checkPosition(lb);
			
			boolean encontre = false;
			Iterator<Position<TNodo<E>>> it = ancester.getHijos().positions().iterator();
			Position<TNodo<E>> posActual = it.hasNext() ? it.next() : null;
			
			while( posActual != null && !encontre ) {
				if( posActual.element() == hermanoDerecho ) {
					encontre = true;
				} else {
					posActual = it.hasNext() ? it.next() : null;
				}
			}
			
			if (!encontre) {
				throw new InvalidPositionException("lb no es hijo de p");
			} else {
				Position<TNodo<E>> posOfLb = posActual;
				TNodo<E> nuevo = new TNodo<E>( e, ancester);
				ancester.getHijos().addAfter(posOfLb, nuevo);
				this.tamaño++;
				return nuevo;
			}
		}
	}

	/**
	 * Elimina el nodo referenciado por una posición dada, si se trata de un nodo externo. 
	 * @param n Posición del nodo a eliminar.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida o no corresponde a un nodo externo, o el árbol está vacío.
	 */
	public void removeExternalNode(Position<E> p) throws InvalidPositionException {
		//Lanzo excepcion en caso de que el arbol este vacio
		if( this.isEmpty() ) {
			throw new InvalidPositionException("El arbol esta vacio");
		}
		
		//Lanzo excepcion en caso de que no sea un nodo externo
		if( !this.isExternal(p)) {
			throw new InvalidPositionException("El nodo no es un nodo externo");
		}
		
		TNodo<E> nodo = this.checkPosition(p);
		
		if( this.raiz == nodo ) {
			this.raiz = null;
			this.tamaño--;
		} else {
			TNodo<E> ancester = nodo.getPadre();
			PositionList<TNodo<E>> descentansOfAncester = ancester.getHijos();
			
			boolean encontre = false;
			
			Position<TNodo<E>> pos = null;
			Iterable<Position<TNodo<E>>> positions = descentansOfAncester.positions();
			Iterator<Position<TNodo<E>>> it = positions.iterator();
			
			while( it.hasNext() && !encontre ) {
				pos = it.next();
				if ( pos.element() == nodo ) {
					encontre = true;
				}
			}
			
			if ( !encontre ) {
				throw new InvalidPositionException("p no aparece en la lista de hijos de su padre: ¡no elimine!");
			} else {
				descentansOfAncester.remove(pos);
				this.tamaño--;
			}
		}
		
	}
	
	/**
	 * Elimina el nodo referenciado por una posición dada, si se trata de un nodo interno. Los hijos del nodo eliminado lo reemplazan en el mismo orden en el que aparecen. 
	 * Si el nodo a eliminar es la raíz del árbol,  únicamente podrá ser eliminado si tiene un solo hijo, el cual lo reemplazará.
	 * @param n Posición del nodo a eliminar.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida o no corresponde a un nodo interno o corresponde a la raíz (con más de un hijo), o el árbol está vacío.
	 */
	public void removeInternalNode (Position<E> p) throws InvalidPositionException {
		TNodo<E> nodo = this.checkPosition(p);
		
		if( this.raiz == null ) {
			throw new InvalidPositionException("Arbol vacio");
		}
		
		if( !this.isInternal(p)) {
			throw new InvalidPositionException("No es un nodo interno");
		}
		//Si el nodo es la raiz
		if( this.raiz == nodo ) {
			//Si la raiz tiene solo un descendiente
			if( this.raiz.getHijos().size() == 1) {
				try {
					TNodo<E> nuevaRaiz = this.raiz.getHijos().remove(this.raiz.getHijos().first());
					nuevaRaiz.setPadre(null);
					this.raiz = nuevaRaiz;
					this.raiz.setPadre(null);
				} catch (EmptyListException e) {
					System.out.println("Algo malo paso");
				}
				
			} else {
				throw new InvalidPositionException("La raiz no tiene un unico descendiete");
			}
		} else {
			
			//Si el nodo no es la raiz
			Position<TNodo<E>> posOfNodo;
			boolean encontre = false;
			TNodo<E> ancester = nodo.getPadre();
			PositionList<TNodo<E>> listDescentansOfAncester = ancester.getHijos();
			Iterator<Position<TNodo<E>>> it = listDescentansOfAncester.positions().iterator();
			Position<TNodo<E>> posActual = it.next();
			//Buscar en la lista de descendientes del ancestro de nodo la posicion de nodo
			while ( posActual != null && !encontre ) {
				if( posActual.element() == nodo ) {
					encontre = true;
				} else {
					posActual = it.hasNext() ? it.next() : null;
				}
			}
			
			if( !encontre ) {
				throw new InvalidPositionException("No encontre al descendiete, me asusto");
			} else {
				//Teniendo la posicion de nodo en la lista de descendientes del ancestro de nodo
				posOfNodo = posActual;
				//Paso los descendiente del nodo a eliminar al al ancestro del nodo a eliminar
				for(TNodo<E> tNodo : nodo.getHijos()) {
					tNodo.setPadre(ancester);
					listDescentansOfAncester.addBefore(posOfNodo, tNodo);
				}
				
				//Reseteo los valores del nodo a eliminar
				nodo.setElement(null);
				nodo.setPadre(null);
				//Vacio la lista de descendientes del nodo a eliminar
				while(!nodo.getHijos().isEmpty()) {
					try {
						nodo.getHijos().remove( nodo.getHijos().first() );
					} catch (InvalidPositionException | EmptyListException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				listDescentansOfAncester.remove(posOfNodo);
			}
			
		}
		this.tamaño--;
	}
	
	/**
	 * Elimina el nodo referenciado por una posición dada. Si se trata de un nodo interno. Los hijos del nodo eliminado lo reemplazan en el mismo orden en el que aparecen. 
	 * Si el nodo a eliminar es la raíz del árbol,  únicamente podrá ser eliminado si tiene un solo hijo, el cual lo reemplazará.
	 * @param n Posición del nodo a eliminar.
	 * @throws InvalidPositionException si la posición pasada por parámetro es inválida o corresponde a la raíz (con más de un hijo), o el árbol está vacío.
	 */
	public void removeNode (Position<E> p) throws InvalidPositionException {
		TNodo<E> nodo = this.checkPosition(p);
		// Si no hay raiz, lanza una excepción.
		if( this.raiz == null ) {
			throw new InvalidPositionException("Arbol vacio");
		}
		//Si el nodo a eliminar es la raiz, ...
		if( this.raiz == nodo ) {
			//... entonces si la raiz no tiene hijos, ...
			if( this.raiz.getHijos().isEmpty()) {
				//... entonces borro la raiz
				this.raiz = null; 
			} else if( this.raiz.getHijos().size() == 1) {// ... de lo contrario, si la raiz tiene un hijo, ...
				//... entonces hacer que el hijo pase a ser la raiz del arbol
				try {
					TNodo<E> nuevaRaiz = this.raiz.getHijos().remove(this.raiz.getHijos().first());
					nuevaRaiz.setPadre(null);
					this.raiz = nuevaRaiz;
				} catch (EmptyListException e) {System.out.println("Algo malo paso");}
				
			} else {
				//... de lo contrario, lanza una excepcion
				throw new InvalidPositionException("La raiz no tiene un unico descendiente");
			}
		} else {
			//... de lo contrario
			TNodo<E> ancester = nodo.getPadre();
			PositionList<TNodo<E>> listDescentansOfAncester = ancester.getHijos();
			Position<TNodo<E>> posOfNodo;
			boolean encontre = false;
			Iterator<Position<TNodo<E>>> it = listDescentansOfAncester.positions().iterator();
			Position<TNodo<E>> posActual = it.next();
			
			while ( posActual != null && !encontre) {
				if( posActual.element() == nodo ) {
					encontre = true;
				} else {
					posActual = it.hasNext() ? it.next() : null;
				}
			}
			
			if( !encontre ) {
				throw new InvalidPositionException("No encontre al descendiete, me asusto");
			} else {
				posOfNodo = posActual;
				
				//Si es un nodo externo
				if(this.isExternal(p)) {
					listDescentansOfAncester.remove(posOfNodo);
				} else {
					//Si es un nodo interno
					//Paso los descendiente del nodo a eliminar al al ancestro del nodo a eliminar
					for(TNodo<E> tNodo : nodo.getHijos()) {
						tNodo.setPadre(ancester);
						listDescentansOfAncester.addBefore(posOfNodo, tNodo);
					}
					
					//Reseteo los valores del nodo a eliminar
					nodo.setElement(null);
					nodo.setPadre(null);
					//Vacio la lista de descendientes del nodo a eliminar
					while(!nodo.getHijos().isEmpty()) {
						try {
							nodo.getHijos().remove( nodo.getHijos().first() );
						} catch (InvalidPositionException | EmptyListException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					listDescentansOfAncester.remove(posOfNodo);
				}
				
			}
			
		}
		this.tamaño--;
	}
	
	/**
	 * Valida si una posicion es valida para un arbol y retorna casteo de la posicion verificada a un nodo
	 * @param v Posicion a chequear
	 * @return Casteo de la posicion verificada a un nodo
	 * @throws InvalidPositionException si la posicion es nula o no es valida
	 */
	private TNodo<E> checkPosition(Position<E> v) throws InvalidPositionException {
		if( v == null ) {
			throw new InvalidPositionException("Posicion invalida (1)");
		} else {
			try {
				TNodo<E> tNodo = (TNodo<E>) v;
				return tNodo;
			} catch (ClassCastException e) {
				throw new InvalidPositionException("Posicion invalida (2)");
			}
			
		}
	}
	
	/**
	 * Realiza un recorrido de pre-orden y agrega la posicion de cada elemento del arbol en una lista.
	 * @param v Nodo desde que se comienza a recorrer.
	 * @param list
	 */
	private void recPreorden( TNodo<E> v, PositionList<Position<E>> list) {
		list.addLast(v);
		for( TNodo<E> h : v.getHijos() ) {
			recPreorden( h, list );
		}
	}
	
	private void recPreordenPorElementos( TNodo<E> v, PositionList<E> list) {
		list.addLast(v.element());
		for( TNodo<E> h : v.getHijos() ) {
			recPreordenPorElementos( h, list);
		}
	}
	
	/**
	 * Verifica si un nodo pertenece al arbol
	 * @param p Nodo a verificar
	 * @return verdadero si el nodo que pasa por parametro pertenece al arbol, false en caso contrario.
	 */
	private boolean perteneceAlArbol(TNodo<E> p) {
		boolean pertenece = false;
				
		if( this.raiz == null) {
			pertenece = false;
		} else {
			
			Iterator<E> it = this.iterator();
			E elementActual = it.hasNext() ? it.next() : null;
			
			while( elementActual != null && !pertenece ) {
				if( elementActual.equals(p.element())) {
					pertenece = true;
				} else {
					elementActual = it.hasNext() ? it.next() : null;
				}
			}
		}
		
		return pertenece;
	}
}
