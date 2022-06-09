package TDACola;

import Excepciones.EmptyQueueException;
import Excepciones.FullQueueException;

public class QueueCircular <E> implements Queue <E> {
	private int f, r;
	private E [] datos;
	
	/**
	 * Constructor de una cola circular de n componentes
	 * @param n Cantidad de componentes de la cola
	 */
	@SuppressWarnings("unchecked")
	public QueueCircular( int n) {
		datos = (E[]) new Object[n];
		f = r = 0;
	}
	
	/**
	 * Constructor de una cola circular de 1001 componentes
	 */
	public QueueCircular() {
		this(1001);
	}
	
	/**
	 * Consulta si la cola esta vacia.
	 * @return Cantidad de componentes ocupadas
	 */
	public int size() {
		return (datos.length - f + r)% (datos.length);
	}
	
	/**
	 * Consulta si la cola esta vacia
	 * @return Verdadero si la cola esta vacia, falso si la cola no esta vacia
	 */
	public boolean isEmpty() {
		return (f == r);
	}
	
	/**
	 * Inspecciona el elemento que  se encuentra en el frente de la cola.
	 * @return Elemento que se encuentra en el frente de la cola.
	 * @throws EmptyQueueException si la cola esta vacia.
	 */
	public E front() throws EmptyQueueException {
		if (this.isEmpty()) {
			throw new EmptyQueueException("Cola vacia");
		} else {
			return datos[f];
		}
	}
	
	/**
	 * Inserta un elemento en el fondo de la cola.
	 * @param element Nuevo elemento a insertar.
	 */
	public void enqueue(E element) throws FullQueueException {
		if ( this.size() == (datos.length - 1)) {
			throw new FullQueueException("Cola llena");
		} else {
			datos[r] = element;
			r = (r+1) % datos.length;
		}
		
		if((datos.length - f + r)% (datos.length) == datos.length) {
			this.agrandarArreglo();
		}
	}
	
	/**
	 * Remueve el elemento en el frente de la cola.
	 * @return Elemento removido.
	 * @throws FullQueueException si la cola esta llena.
	 */
	public E dequeue() throws EmptyQueueException {
		if (this.isEmpty()) {
			throw new EmptyQueueException("Cola vacia");
		} else {
			E temp = datos[f];
			datos[f] = null;
			f = (f+1) % datos.length;
			return temp;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void agrandarArreglo() {
		E [] nuevoArreglo = (E[]) new Object [this.datos.length + 23 ];
		for(int i = 0; i < this.datos.length; i++) {
			nuevoArreglo [i] = this.datos [i];
		}
		this.datos = nuevoArreglo;
	}
}
