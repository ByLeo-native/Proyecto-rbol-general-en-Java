package TDALista;

import java.util.Comparator;

public class TestComparator {
	public static void main (String [] args) {
		Comparator<Integer> comp = new DefaultComparator<Integer>();
		int r1 = comp.compare( 5, 8);
		int r2 = comp.compare(5, 5);
		int r3 = comp.compare(7, 2);
		System.out.println(r1);
		System.out.println(r2);
		System.out.println(r3);
	}
}
