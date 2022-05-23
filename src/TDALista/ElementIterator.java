 package TDALista;

import java.util.NoSuchElementException;

import Excepciones.*;

public class ElementIterator <E> implements java.util.Iterator<E> {
	protected PositionList<E> list;
	protected Position<E> cursor;
	
	public ElementIterator(PositionList<E> list) throws EmptyListException {
		this.list = list;
		if (list.isEmpty()) {
			cursor = null;
		}  else {
			try {
				cursor = list.first();
			} catch (EmptyListException e) {
				throw new EmptyListException(e.getMessage());
			}
		}
	}
	
	//Hay siguiente si el cursor no esta mas alla de la ultima posicion
	public boolean hasNext() {
		return cursor != null;
	}
	
	public E next() throws NoSuchElementException {
		if (cursor == null) {
			throw new NoSuchElementException("Error: No hay siguiente");
		} else {
			E toReturn = cursor.element();
			try {
				cursor = (cursor == list.last()) ? null : list.next(cursor);
			} catch (EmptyListException | InvalidPositionException | BoundaryViolationException e) {
				e.printStackTrace();
			}
			return toReturn;
		}
	}
	
	public void remove() {
		//No implementacion hecha
	}
}
