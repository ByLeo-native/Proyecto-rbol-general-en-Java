package TDALista;

public class DNodo <E> implements Position <E> {
	private E elemento;
	private DNodo<E> previo, siguiente;
	
	public DNodo(E item, DNodo<E> p, DNodo<E> n) {
		elemento = item;
		previo = p;
		siguiente = n;
	}
	
	public DNodo(E item) {
		this(item, null, null);
	}
	
	public void setElement(E e) {
		elemento = e;
	}
	
	public void setPrev(DNodo<E> d) {
		previo = d;
	}
	
	public void setNext(DNodo<E> d) {
		siguiente = d;
	}
	
	public E element() {
		return elemento;
	}
	
	public DNodo<E> getPrev() {
		return previo;
	}
	
	public DNodo<E> getNext() {
		return siguiente;
	}
}
