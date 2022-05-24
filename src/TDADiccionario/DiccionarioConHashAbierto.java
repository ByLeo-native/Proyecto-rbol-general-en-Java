package TDADiccionario;

import Excepciones.InvalidEntryException;
import Excepciones.InvalidKeyException;
import Excepciones.InvalidPositionException;
import TDALista.ListaDoblementeEnlazada;
import TDALista.Position;
import TDALista.PositionList;
import TDAMapeo.Entrada;
import TDAMapeo.Entry;

public class DiccionarioConHashAbierto <K,V> implements Dictionary <K,V>{
	private int tamaño;
	protected PositionList<Entrada<K,V>> [] arregloOfBuckets;
	protected int N;
	protected static final float factor= 0.9f;
	
	@SuppressWarnings("unchecked")
	public DiccionarioConHashAbierto () {
		N = 223;
		this.arregloOfBuckets = (PositionList<Entrada<K,V>>[]) new ListaDoblementeEnlazada[N];
		for( int i = 0; i < N; i++) {
			arregloOfBuckets[i] = new ListaDoblementeEnlazada<Entrada<K,V>>();
		}
		tamaño = 0;
	}
	
	public int size() {
		return tamaño;
	}
	
	public boolean isEmpty() {
		return tamaño==0;
	}
	
	public Entry<K,V> find(K key) throws InvalidKeyException {
		int hashCode;
		try {
			hashCode = this.hashCode(key);
			for(Entrada<K,V> entrada : this.arregloOfBuckets[hashCode]) {
				if(entrada.getKey().equals(key)) {
					return entrada;
				}
			}
		} catch (InvalidKeyException e) {
			throw new InvalidKeyException(e.getMessage());
		}
		return null;
		
	}

	public Iterable<Entry<K,V>> findAll(K key) throws InvalidKeyException {
		int hashCode;
		PositionList<Entry<K,V>> list = new ListaDoblementeEnlazada<Entry<K,V>>();
		try {
			hashCode = this.hashCode(key);
			for(Entrada<K,V> entrada : this.arregloOfBuckets[hashCode]) {
				if(entrada.getKey().equals(key)) {
					list.addLast(entrada);
				}
			}
		} catch (InvalidKeyException e) {
			throw new InvalidKeyException(e.getMessage());
		}
		return list;
	}
	
	public Entry<K,V> insert(K key, V value) throws InvalidKeyException {
		int hashCode;
		try {
			hashCode = this.hashCode(key);
		} catch (InvalidKeyException e) {
			throw new InvalidKeyException(e.getMessage());
		}
		Entrada<K,V> nuevaEntrada = new Entrada<K,V>( key, value);
		this.arregloOfBuckets[hashCode].addLast(nuevaEntrada);
		this.tamaño++;
		if(!(tamaño/N < factor)) {
			reHash();
		}
		return nuevaEntrada;
	}

	public Entry<K,V> remove (Entry<K,V> e) throws InvalidEntryException {
		Entrada<K,V> entrada;
		try {
			entrada = checkEntry(e);
			int hashCode = this.hashCode(entrada.getKey());
			PositionList<Entrada<K,V>> list = this.arregloOfBuckets[hashCode];
			//Buscar la posicion con la entrada buscada
			for( Position<Entrada<K,V>> pos : list.positions()) {
				if( pos.element().equals(entrada)) {
					this.tamaño--;
					//Remueve a la posicion y retornar el elemento que sera la entrada
					return list.remove(pos);
				}
			}
		} catch ( InvalidEntryException error) {
			throw new InvalidEntryException (error.getMessage());
		} catch ( InvalidKeyException | InvalidPositionException error1 ) {
			
		}
		//Si no encuentra la posicion con la entrada, entonces lanzar excepcion
		throw new InvalidEntryException("Clave invalida");
		
	}
	
	public Iterable<Entry<K,V>> entries() {
		PositionList<Entry<K,V>> it = new ListaDoblementeEnlazada<Entry<K,V>>();
		for( int i = 0; i < N; i++ ) {
			for(Entrada<K,V> entrada : this.arregloOfBuckets[i] ) {
				it.addLast(entrada);
			}
		}
		return it;
	}

	private int hashCode(K key) throws InvalidKeyException {
		if (key == null) {
			throw new InvalidKeyException("Clave invalida");
		} else {
			return key.hashCode() % N;
		}
	}
	
	private Entrada<K,V> checkEntry(Entry<K,V> e) throws InvalidEntryException {
		if( e == null) {
			throw new InvalidEntryException("Entrada invalida");
		} else {
			try {
				Entrada<K,V> entrada = (Entrada<K,V>) e;
				return entrada;
			} catch ( ClassCastException error ) {
				throw new InvalidEntryException("Entrada invalida");
			}
		}
	}

	private int nextPrimo(int m) {
		boolean esPrimo = false;
		int puedeSerPrimo = m;
		while(!esPrimo) {
			int i=2;
			puedeSerPrimo++;
			while( i <= Math.sqrt(puedeSerPrimo)) {
				esPrimo= puedeSerPrimo%i!=0;
				i++;
			}
		}
		int sigPrimo = puedeSerPrimo;
		return 	sigPrimo;
	}
	
	@SuppressWarnings("unchecked")
	private void reHash() {
		Iterable<Entry<K,V>> entradas= entries();
		N= N*2;
		N= nextPrimo(N); tamaño=0;
		arregloOfBuckets= new PositionList[N];
			for(int i=0;i<N;i++)
				arregloOfBuckets[i]= new ListaDoblementeEnlazada<Entrada<K,V>>();
			for(Entry<K,V> e: entradas) {
				try {
					this.insert(e.getKey(), e.getValue());
				} catch (InvalidKeyException e1) {
					System.out.println(e1.getMessage());
				}
			}
	}
}
