package TDAPila;

public class Nodo <E>{
	private E elemento;
	private Nodo<E> siguiente;
	
	//constructores
	public Nodo(E item, Nodo <E> sig) {
		elemento = item;
		siguiente = sig;
	}
	
	public Nodo (E item) {
		this(item, null);
	}
	
	public void setElemento (E item) {
		elemento = item;
	}
	
	public void setSiguiente ( Nodo<E> sig) {
		siguiente = sig;
	}
	
	public E getElemento() {
		 return elemento;
	}
	
	public Nodo<E> getSiguiente() {
		return siguiente;
	}
}
