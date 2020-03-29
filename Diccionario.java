package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase para las entradas del diccionario. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
        	this.llave = llave;
		this.valor = valor;
        }
    }

    /* Clase privada para iteradores de diccionarios. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            Lista<Entrada> listini = new Lista<Entrada>();
            for (int i = 0; i < entradas.length; i++)
            	if (entradas[i] != null)
                	for (Entrada entrada : entradas[i])
                  	      listini.agrega(entrada);
			      this.iterador = listini.iterator();
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() { 
		return iterador.next();
        }
    }

    /* Clase privada para iteradores de llaves de diccionarios. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Construye un nuevo iterador de llaves del diccionario. */
        public IteradorLlaves() {
            super();
        }

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            return siguiente().llave;
        }
    }

    /* Clase privada para iteradores de valores de diccionarios. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Construye un nuevo iterador de llaves del diccionario. */
        public IteradorValores() {
            super();
        }

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            return siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K p) -> p.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K p) -> p.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
	this.dispersor = dispersor;
        if(capacidad < 64){
	    entradas = nuevoArreglo(64);
	}else{
	    capacidad = calculaTamaño(capacidad);
	    entradas = nuevoArreglo(capacidad);
	}
	elementos = 0;
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if (llave == null || valor == null){
		throw new IllegalArgumentException();
	}else{
		int i = (entradas.length-1) & (dispersor.dispersa(llave));
			if (entradas[i] == null){
				Lista<Entrada> listini = new Lista<Entrada>();
				entradas[i] = listini;
			}
			for(Entrada entrada : entradas[i]){
				if (entrada.llave.equals(llave)){
					entrada.valor = valor;
					return;
				}else{
				}
			}
		entradas[i].agrega(new Entrada(llave, valor));
		elementos = elementos + 1;
		if(carga() >= MAXIMA_CARGA){
				creceArg(); 	
		}else{
		} 
	}
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if (llave == null){
		throw new IllegalArgumentException();
	}else{	
		int i = (entradas.length-1) & (dispersor.dispersa(llave));
        	if (entradas[i] == null){
        	    throw new NoSuchElementException();
		}else{
        		for (Entrada entrada : entradas[i]) {
        	    		if (entrada.llave.equals(llave)){
        	        		return entrada.valor;
				}else{
				}
        		}
			throw new NoSuchElementException();
		}
	}
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
	if(llave == null){
	    return false;
	}else{
        	int i = (entradas.length-1) & (dispersor.dispersa(llave));
		if(entradas[i] == null){
	    		return false;
		}else{
			for(Entrada entrada : entradas[i]){
	    			if(entrada.llave.equals(llave)){
					return true;
	    			}else{
				}
			}
		}
	return false;
	}
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
	if(llave == null){
	    throw new IllegalArgumentException();
	}else{
		if(!contiene(llave)){
		    throw new NoSuchElementException();
		}else{
        	int i = (entradas.length - 1) & (dispersor.dispersa(llave));
			for(Entrada entrada : entradas[i]){
				if(entrada.llave.equals(llave)){
					entradas[i].elimina(entrada);
	 	   		}else{
				}
			}
		}
	elementos = elementos -1;
	}
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int c = 0;
        for (int i = 0 ; i < entradas.length ; i++) {
            if (entradas[i] != null){
		int e = entradas[i].getElementos() - 1;
            	c = c + e;
	    }else{
	    }
        }
	return c;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int c = 0;
        int cMax = 0;
        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null) {
		int e = entradas[i].getElementos() - 1;
                c = e;
	    }else{
            	if (c > cMax){
                    cMax = c;
		}else{
		}
            }
        }
	return cMax;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        double l = entradas.length + 0.0;
	double cargaR = elementos / l;
	return cargaR;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        if (elementos == 0){
		return true;
	}else{
		return false;
	}
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        Lista<Entrada>[] entradasLimpias = nuevoArreglo(entradas.length);
	entradas = entradasLimpias;
	elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
	if (elementos == 0){
		return "{}";
	}else{
        	String larouse = "{ ";
      		for (Lista<Entrada> listini : entradas) {
        		if (listini != null) {
        		  for (Entrada entrada : listini) {
        		    larouse = larouse + "'" + entrada.llave + "'" + ": '" + entrada.valor + "', ";
        		  }
        		}else{
			}
      		}	
      	larouse = larouse + "}";
      	return larouse;
      }
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
	if (elementos != d.getElementos()){
		return false;
	}else{
		Lista<K> llaves = llavero();
        	for (K key : llaves) {
          		if (d.contiene(key) && d.get(key).equals(get(key))) {
          		}else{
				return false;	
			}
        	}
        return true;
	}
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
	
    private int calculaTamaño(int x) {
	if (x < 64){
		x = 64; 
	}else{
		x = x;
	}
        int capacidad = 1;
        while (capacidad < x * 2){
            capacidad = capacidad * 2;
	}
        return capacidad;
    }


    private void creceArg(){
	int tamaño = calculaTamaño(entradas.length);
	elementos = 0;
	Lista<Entrada>[] larouseA = entradas;
	Lista<Entrada>[] larouseN = nuevoArreglo(tamaño);
	entradas = larouseN;
		for(int i = 0; i < larouseA.length; i++){
	    		if(larouseA[i] != null){
				for(Entrada entrada : larouseA[i]){
		    			agrega(entrada.llave, entrada.valor);
				}
	    		}else{
	    		}
		}
   }

   private Lista<K> llavero(){
      Lista<K> llavero = new Lista<K>();
      for (Lista<Entrada> listini : entradas) {
      	    if (listini != null) {
            	for (Entrada entrada : listini) {
            		llavero.agrega(entrada.llave);
          	}
            }else{
	    }
      }
      return llavero;
    }
}
