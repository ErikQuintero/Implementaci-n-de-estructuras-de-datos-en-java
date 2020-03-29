package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
            this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <tt>true</tt> si el vértice tiene padre,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayPadre() {
            return padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <tt>true</tt> si el vértice tiene izquierdo,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return izquierdo != null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <tt>true</tt> si el vértice tiene derecho,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() throws NoSuchElementException{
		if (hayPadre()){
			return padre;
		}else{
			throw new NoSuchElementException(); 
		}            
		
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() throws NoSuchElementException{
           if (hayIzquierdo()){
			return izquierdo;
		}else{
			throw new NoSuchElementException(); 
		}
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() throws NoSuchElementException{
            if (hayDerecho()){
			return derecho;
		}else{
			throw new NoSuchElementException(); 
		}
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
        	return alturaQueSiSirve(this);
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            if (padre == null){
	    	return 0;
            }else{
            	return 1 + padre.profundidad();   
	    }
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)o;
            return equalsQueSiSirve(this, vertice); 
        }
	private boolean equalsQueSiSirve(Vertice v1, Vertice v2){
		if (v1 == null && v2 == null){
			return true;
		}		
		if ((v1 == null && v2 != null) || (v1 != null && v2 == null) || !v1.elemento.equals(v2.elemento)){
			return false;
		}else{
			return equalsQueSiSirve(v1.izquierdo, v2.izquierdo) && equalsQueSiSirve(v1.derecho, v2.derecho);
		}
	}

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            return elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
    	for(T n : coleccion){
		agrega(n);	
    	} 
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
          if (raiz == null){
	    	return -1;
	    }else{
            	return raiz.altura();
            }
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return buscaQueSiSirve(raiz, elemento) != null;
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <tt>null</tt>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <tt>null</tt> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
       return buscaQueSiSirve(raiz, elemento);
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() throws NoSuchElementException{
	if (esVacia()){
		throw new NoSuchElementException();
	}else{        
		return raiz;
	}
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        if (raiz == null && elementos == 0){
		return true;
	}else{
		return false;
	}
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        raiz = null;
	elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param o el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)o;
	    if (this.esVacia()){
		return arbol.esVacia();
	    }else{
     	    	return this.raiz.equals(arbol.raiz);
	    }
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        if (elementos == 0)
            return "";
        boolean[] b = new boolean[altura() + 1];
        for (int i = 0; i < altura() + 1 ; i++)
            b[i] = false;
        String x = toStringQueSiSirve(raiz, 0, b);
	return x.substring(0, x.length() -1) + "\n";
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
 
    private int alturaQueSiSirve(Vertice v){
	if (v == null){
		return -1;
	}else if(v.izquierdo == null && v.derecho == null){
		return 0;
	}else{
		return 1 + Math.max(alturaQueSiSirve(v.izquierdo),alturaQueSiSirve(v.derecho));
	}
    }

    private Vertice buscaQueSiSirve(Vertice v, T elemento){
	if (v == null){
		return null;
	}
	if (v.elemento.equals(elemento)){
		return v;
	}
		Vertice va = buscaQueSiSirve(v.izquierdo, elemento);
			if (va != null){
				return va;
			}else{
				return buscaQueSiSirve(v.derecho, elemento);
					 
			}
    }
	
    private String toStringQueSiSirve(Vertice v, int k, boolean[] b) {
        String x = v + "\n";
        b[k] = true;
        if (v.izquierdo != null && v.derecho != null) {
            x = x + espacios(k, b);
            x = x + "├─›";
            x = x + toStringQueSiSirve(v.izquierdo, k + 1, b);
            x = x + espacios(k, b);
            x = x + "└─»";
            b[k] = false;
            x = x + toStringQueSiSirve(v.derecho, k + 1, b);
        } else if (v.izquierdo != null) {
            x = x + espacios(k, b);
            x = x + "└─›";
            b[k] = false;
            x = x + toStringQueSiSirve(v.izquierdo, k + 1, b);
        } else if (v.derecho != null) {
            x = x + espacios(k, b);
            x = x + "└─»";
            b[k] = false;
            x = x + toStringQueSiSirve(v.derecho, k + 1, b);
        }
        return x;
    }


   private String espacios(int a, boolean[] b) {
        String x = "";
        for (int i = 0; i < a; i++)
            if (b[i])
                x += "│  ";
            else
                x += "   ";
        return x;
   }
   
}
