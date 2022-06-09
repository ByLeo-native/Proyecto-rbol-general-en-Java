package Auxiliar;

import Excepciones.EmptyQueueException;
import TDAPila.Nodo;

public class QueueEnlazada <E> implements Queue <E>{
	protected int tamaño;
	protected Nodo<E> head, tail;
	
	//Constructor
	public QueueEnlazada () {
		head = tail = null;
		tamaño = 0;
	}
	
	public void enqueue (E e) {
		Nodo <E> nodo = new Nodo<E>(e);
		if (size() == 0) {
			head = nodo;
		} else {
			tail.setSiguiente(nodo);
		}
		tail = nodo;
		tamaño++;
	}
	
	public E dequeue() throws EmptyQueueException {
		if(isEmpty()) {
			throw new EmptyQueueException("Cola vacia");
		} else {
			E nodo = head.getElemento();
			head = head.getSiguiente();
			tamaño--;
			//En caso de que no halla elementos, el head.getSiguiente es null y ya por ende queda head = null
			//Solo falta dejar en null la cola (tail)
			if (size()==0) {
				tail = null;
			}
			return nodo;
		}
	}
	
	public E front() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException("Cola vacia");
		} else {
			return head.getElemento();
		}
	}
	
	public boolean isEmpty() {
		return tamaño == 0;
	}
	
	public int size() {
		return tamaño;
	}
}
