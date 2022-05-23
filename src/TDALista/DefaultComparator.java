package TDALista;

public class DefaultComparator <E> implements java.util.Comparator<E>{
	@SuppressWarnings("unchecked")
	public int compare( E a, E b) throws ClassCastException {
		return ((Comparable<E>) a).compareTo(b);
	}
}
