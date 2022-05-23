package TDAPila;

import Excepciones.EmptyStackException;

public class PilaSimplementeEnlazada <E> implements Stack <E>{
	protected Nodo<E> head;
	protected int tamaño;
	
	public PilaSimplementeEnlazada() {
		head = null;
		tamaño = 0;
	}
	
	/**
	 * Consulta la cantidad de elementos de la pila.
	 * @return Cantidad de elementos de la pila.
	 */
	public int size() {
		return tamaño;
	}
	
	/**
	 * Consulta si la pila está vacía.
	 * @return Verdadero si la pila está vacía, falso en caso contrario.
	 */
	public boolean isEmpty() {
		return tamaño == 0;
	}
	
	/**
	 * Examina el elemento que se encuentra en el tope de la pila.
	 * @return Elemento que se encuentra en el tope de la pila.
	 * @throws EmptyStackException si la pila está vacía. 
	 */
	public E top() throws EmptyStackException{
		if (tamaño == 0) {
			throw new EmptyStackException("Pila vacia");
		} else {
			return head.getElemento();
		}
	}
	
	/**
	 * Inserta un elemento en el tope de la pila.
	 * @param element Elemento a insertar.
	 */
	public void push(E element) {
		head = new Nodo<E>(element, head);
		tamaño++;
	}
	
	/**
	 * Remueve el elemento que se encuentra en el tope de la pila.
	 * @return Elemento removido.
	 * @throws EmptyStackException si la pila está vacía. 
	 */
	public E pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException("La pila esta vacia");
		}
		E aux = head.getElemento();
		head = head.getSiguiente();
		tamaño--;
		return aux;
	}	
}
