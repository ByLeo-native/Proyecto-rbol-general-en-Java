package TDADiccionario;

public class Entrada<K,V> implements Entry<K,V> {
	private K clave;
	private V valor;
	// Constructor
	public Entrada(K clave, V valor) { 
		this.clave = clave; this.valor = valor; 
	}
	
	// Getters
	public K getKey() { 
		return clave; 
	} 
	public V getValue() { 
		return valor; 
	}
	
	// Setters
	public void setKey( K clave ) { 
		this.clave = clave; 
	} 
	
	public void setValue(V valor) { 
		this.valor = valor; 
	}
	
	public String toString() { // Para mostrar entradas
		return "(" + getKey() + "," + getValue() + ")" ;
	}
}
