package TDAPila;

import Excepciones.EmptyStackException;

public class PilaSimplementeEnlazada <E> implements Stack <E>{
	protected Nodo<E> head;
	protected int tama�o;
	
	public PilaSimplementeEnlazada() {
		head = null;
		tama�o = 0;
	}
	
	/**
	 * Consulta la cantidad de elementos de la pila.
	 * @return Cantidad de elementos de la pila.
	 */
	public int size() {
		return tama�o;
	}
	
	/**
	 * Consulta si la pila est� vac�a.
	 * @return Verdadero si la pila est� vac�a, falso en caso contrario.
	 */
	public boolean isEmpty() {
		return tama�o == 0;
	}
	
	/**
	 * Examina el elemento que se encuentra en el tope de la pila.
	 * @return Elemento que se encuentra en el tope de la pila.
	 * @throws EmptyStackException si la pila est� vac�a. 
	 */
	public E top() throws EmptyStackException{
		if (tama�o == 0) {
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
		tama�o++;
	}
	
	/**
	 * Remueve el elemento que se encuentra en el tope de la pila.
	 * @return Elemento removido.
	 * @throws EmptyStackException si la pila est� vac�a. 
	 */
	public E pop() throws EmptyStackException {
		if (isEmpty()) {
			throw new EmptyStackException("La pila esta vacia");
		}
		E aux = head.getElemento();
		head = head.getSiguiente();
		tama�o--;
		return aux;
	}	
}
