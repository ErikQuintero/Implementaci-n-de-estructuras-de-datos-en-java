package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase para montículos de Dijkstra con arreglos.
 */
public class MonticuloArreglo<T extends ComparableIndexable<T>>
    implements MonticuloDijkstra<T> {

    /* Número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arreglo;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor para montículo de Dijkstra con un arreglo a partir de una
     * colección.
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloArreglo(Coleccion<T> coleccion) {
	arreglo = nuevoArreglo(coleccion.getElementos());
	elementos = coleccion.getElementos();
	int i = 0;
	for(T e : coleccion){
		arreglo[i] = e;
		arreglo[i].setIndice(i);
		i = i + 1;
	}        
    }

    /**
     * Construye un nuevo para montículo de Dijkstra con arreglo a partir de un
     * iterable.
     * @param iterable el iterable a partir de la cual construir el montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloArreglo(Iterable<T> iterable, int n) {
        arreglo = nuevoArreglo(n);
	elementos = n;
	int i = 0;
	for(T e : iterable){
		arreglo[i] = e;
		arreglo[i].setIndice(i);
		i = i + 1;
	}  
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
	if (esVacia()){
		throw new IllegalStateException();
	}else{
		T v = null;	
		int m = 0;	
		for (T e : arreglo){
			if (e != null){
				v = e;
				break;
			}
			m = m + 1;
		}
		for (int j = m; j < arreglo.length; j++){
			if (arreglo[j] != null){
				if (arreglo[j].compareTo(v) < 0){
					v = arreglo[j];				
					m = j; 
				}
			}	
		}
		arreglo[m].setIndice(-1);
		arreglo[m] = null;
		elementos = elementos - 1;	
		return v;
    	}
    }        	

    /**
     * Regresa el <i>i</i>-ésimo elemento del arreglo.
     * @param i el índice del elemento que queremos.
     * @return el <i>i</i>-ésimo elemento del arreglo.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (i < 0 || i >= elementos){
		throw new NoSuchElementException();
	}else{
		return arreglo[i]; 
	}
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacia() {
        if (elementos == 0){
		return true;
	}else{
		return false;
	}
    }

    /**
     * Regresa el número de elementos en el montículo.
     * @return el número de elementos en el montículo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    private T buscaMinimo(){
	T v = null;	
	int m = 0;	
	for (T e : arreglo){
		if (e != null){
			v = e;
			break;
		}
		m = m + 1;
	}
	for (int j = m; j < elementos - 1; j++){
		if (arreglo[j] != null){
			if (arreglo[j].compareTo(v) < 0){
				v = arreglo[j];				
				m = j; 
			}
		}	
	}
	arreglo[m].setIndice(-1);
	arreglo[m] = null;	
	return v;
    }
}
