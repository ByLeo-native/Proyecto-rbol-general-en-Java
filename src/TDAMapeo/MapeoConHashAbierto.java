package TDAMapeo;

import java.util.Iterator;

import Excepciones.InvalidKeyException;
import Excepciones.InvalidPositionException;
import TDALista.ListaDoblementeEnlazada;
import TDALista.Position;
import TDALista.PositionList;

public class MapeoConHashAbierto <K,V> implements Map <K,V>{
	
	private int N = 7;
	
	protected PositionList<Entrada<K,V>> [] arregloB;
	protected int tamaño;
	protected final float factor= 0.9f;
	
	/**
	 * Crea un mapeo con hash vacio con N igual a 11
	 */
	@SuppressWarnings("unchecked")
	public MapeoConHashAbierto() {
		this.tamaño = 0;
		this.N = 11;
		this.arregloB = (PositionList<Entrada<K,V>> []) new ListaDoblementeEnlazada[N];
		for(int i = 0; i < N; i++) {
			this.arregloB[i] = new ListaDoblementeEnlazada<Entrada<K,V>>();
		}
	}
	
	/**
	 * Consulta el número de entradas del mapeo.
	 * @return Número de entradas del mapeo.
	 */
	public int size() {
		return this.tamaño;
	}

	/**
	 * Consulta si el mapeo está vacío.
	 * @return Verdadero si el mapeo está vacío, falso en caso contrario.
	 */
	public boolean isEmpty() {
		return this.tamaño == 0;
	}
	
	/**
	 * Busca una entrada con clave igual a una clave dada y devuelve el valor asociado, si no existe retorna nulo.
	 * @param key Clave a buscar.
	 * @return Valor de la entrada encontrada.
	 * @throws InvalidKeyException si la clave pasada por parámetro es inválida.
	 */
	public V get(K key) throws InvalidKeyException {
		//Valido que la clave sea valida
		if (key == null) {
			throw new InvalidKeyException("Clave invalida");
		}
		
		V valorARetornar = null;
		int claveHash = this.funcionHash(key);
		boolean encontre = false;
		
		Iterator<Entrada<K,V>> it = this.arregloB[claveHash].iterator();
		Entrada<K,V> entradaActual = it.hasNext() ? it.next() : null;
		
		while( !encontre && entradaActual != null ) {
			if( entradaActual.getKey().equals(key)) {
				valorARetornar = entradaActual.getValue();
				encontre = true;
			} else {
				entradaActual = it.hasNext() ? it.next() : null;
			}
		}
		//Retorno el valor hallado
		return valorARetornar;
	}
	
	/**
	 * Si el mapeo no tiene una entrada con clave key, inserta una entrada con clave key y valor value en el mapeo y devuelve null. 
	 * Si el mapeo ya tiene una entrada con clave key, entonces remplaza su valor y retorna el viejo valor.
	 * @param key Clave de la entrada a crear.
	 * @param value Valor de la entrada a crear. 
	 * @return Valor de la vieja entrada.
	 * @throws InvalidKeyException si la clave pasada por parámetro es inválida.
	 */
	public V put (K key, V value ) throws InvalidKeyException {
		if ( key == null ) {
			throw new InvalidKeyException("Clave invalida");
		}
		
		V valorARetornar = null;
		int claveHash = this.funcionHash(key);
		boolean encontre = false;
		Iterator<Entrada<K,V>> it = this.arregloB[claveHash].iterator();
		Entrada<K,V> entradaActual = it.hasNext() ? it.next() : null;
		
		while( !encontre && entradaActual != null ) {
			if(entradaActual.getKey().equals(key)) {
				encontre = true;
				valorARetornar = entradaActual.getValue();
				entradaActual.setValue(value);
			} else {
				entradaActual = it.hasNext() ? it.next() : null;
			}
		}
	
		if(!encontre) {
			Entrada<K,V> nueva = new Entrada<K,V>(key, value);
			arregloB[claveHash].addLast(nueva);
			tamaño++;
		}
		
		if( this.tamaño / this.N > this.factor) {
			this.reHash();
		}
		
		return valorARetornar;
	}

	/**
	 * Remueve la entrada con la clave dada en el mapeo y devuelve su valor, o nulo si no fue encontrada.
	 * @param e Entrada a remover.
	 * @return Valor de la entrada removida.
	 * @throws InvalidKeyException si la clave pasada por parámetro es inválida.
	 */
	public V remove(K key) throws InvalidKeyException {
		if ( key == null) {
			throw new InvalidKeyException("Clave invalida");
		}
		
		V valorARetornar = null;
		boolean encontre = false;
		int claveHash = this.funcionHash(key);
		Iterator<Position<Entrada<K,V>>> it = this.arregloB[claveHash].positions().iterator();
		Position<Entrada<K,V>> pos = it.hasNext() ? it.next() : null;
		
		try {
			while( !encontre && pos != null ) {
				if( pos.element().getKey().equals(key)) {
					valorARetornar = pos.element().getValue();
					encontre = true;
					this.arregloB[claveHash].remove(pos);
					tamaño--;
				} else {
					pos = it.hasNext() ? it.next() : null;
				}
			}
		} catch (InvalidPositionException e) {
			System.out.println(e.getMessage());
		}
		
		return valorARetornar;
	}

	/**
	 * Retorna una colección iterable con todas las claves del mapeo.
	 * @return Colección iterable con todas las claves del mapeo.
	 */
	public Iterable<K> keys() {
		PositionList<K> list= new ListaDoblementeEnlazada<K>();
		
		for(int i=0; i < N; i++) {
			
			for(Entrada<K,V> e: arregloB[i]) {
				list.addLast( e.getKey());
			}
		}
		return list;
	}

	/**
	 * Retorna una colección iterable con todas los valores del mapeo.
	 * @return Colección iterable con todas los valores del mapeo.
	 */
	public Iterable<V> values() {
		PositionList<V> list= new ListaDoblementeEnlazada<V>();
		
			for(int i=0; i < N; i++) {
			
				for(Entrada<K,V> e: arregloB[i]) {
				list.addLast( e.getValue());
			}
		}
		return list;
	}

	/**
	 * Retorna una colección iterable con todas las entradas del mapeo.
	 * @return Colección iterable con todas las entradas del mapeo.
	 */
	public Iterable<Entry<K, V>> entries() {
		PositionList<Entry<K,V>> list= new ListaDoblementeEnlazada<Entry<K,V>>();
		
			for(int i=0; i < N; i++) {
			
				for(Entrada<K,V> p: arregloB[i]) {
					list.addLast(p);
			}
		}
		return list;
	}
	
	/**
	 * Retorna un clave hash
	 * @param key Clave a operar
	 * @return clave hash valida 
	 */
	protected int funcionHash(K key) {
		return Math.abs(key.hashCode()%N);
	}
	
	/**
	 * Reajusta la cantidad de buckets del mapeo
	 */
	@SuppressWarnings("unchecked")
	private void reHash() {
		Iterable<Entry<K,V>> entradas= entries();
		N= N*2;
		N= nextPrimo(N); tamaño=0;
		arregloB= new PositionList[N];
			for(int i=0;i<N;i++)
				arregloB[i]= new ListaDoblementeEnlazada<Entrada<K,V>>();
			for(Entry<K,V> e: entradas) {
				try {
					this.put(e.getKey(), e.getValue());
				} catch (InvalidKeyException e1) {
					System.out.println(e1.getMessage());
				}
			}
	}
	
	/**
	 * Busca un siguiente primo a un numero dado y devuelve el valor del primo encontrado
	 * @param m Primo desde el cual se emperazara a buscar el siguiente
	 * @return Valor del primo encontrado.
	 */
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
}

