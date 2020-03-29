package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista, 
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
	}

        /* Nos da el elemento siguiente. */
        @Override public T next() throws NoSuchElementException{
            if (siguiente == null){
		throw new NoSuchElementException();
	    }else{
		anterior = siguiente;
		siguiente = siguiente.siguiente;
		return anterior.elemento;
	    }
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() throws NoSuchElementException{
             if (anterior == null){
		throw new NoSuchElementException();
	    }else{
		siguiente = anterior;
		anterior = anterior.anterior;
		return siguiente.elemento;
	    }
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
		anterior = null;
		siguiente = cabeza;            
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
           	 anterior = rabo;
	   	 siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        if ((cabeza == null && rabo == null) && longitud == 0){
		return true;
	}else{
		return false;
	}
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        agregaFinal (elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) throws IllegalArgumentException{
        if (elemento == null){
		throw new IllegalArgumentException();
	}else if(longitud == 0){
		Nodo nodo = new Nodo(elemento);
		cabeza = nodo;
		rabo = nodo;
	}else if (longitud >= 1){
		Nodo nodo = new Nodo(elemento);
		rabo.siguiente = nodo;
		nodo.anterior = rabo;
		rabo = nodo;
		rabo.siguiente = null;
	}
	longitud = longitud + 1;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null){
		throw new IllegalArgumentException();
	}else if(longitud == 0){
		 Nodo nodo = new Nodo(elemento);
		cabeza = nodo;
		rabo = nodo;
	}else if (longitud >= 1){
		 Nodo nodo = new Nodo(elemento);
		cabeza.anterior = nodo;
		nodo.siguiente = cabeza;
		cabeza = nodo;
		cabeza.anterior = null;
	}
	longitud = longitud + 1;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) throws IllegalArgumentException{
        if (elemento == null){
		throw new IllegalArgumentException();
	}else if (i <= 0){
		agregaInicio(elemento);
	}else if(i >= getLongitud()){
		agregaFinal(elemento);
	}else{
		Nodo n1 = getNodo(i);
		Nodo n2 = new Nodo(elemento);
		n2.anterior = n1.anterior;
		n1.anterior.siguiente = n2;
		n2.siguiente = n1;
		n1.anterior = n2;
		longitud = longitud + 1; 
	}
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
	if (elemento != null){
		Nodo n = buscaNodo(elemento);
		if(n == null){
		}else if (n.anterior == null){
			eliminaPrimero();		
		}else if (n.siguiente == null){
			eliminaUltimo();
		}else{
			n.siguiente.anterior = n.anterior;
			n.anterior.siguiente = n.siguiente;
			longitud = longitud -1;
		}
	}
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() throws NoSuchElementException{        	
	if(longitud == 0){
		throw new NoSuchElementException();
	}else if(longitud == 1){
		T aux = cabeza.elemento;
		cabeza = null;
		rabo = null;
		longitud = longitud -1;
		return aux;
	}else if(longitud > 1){
		T aux = cabeza.elemento;
		cabeza = cabeza.siguiente;
		cabeza.anterior = null;
		longitud = longitud -1;
		return aux;
	}
		return null;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(longitud == 0){
		throw new NoSuchElementException();
	}else if(longitud == 1){
		T aux = rabo.elemento;
		cabeza = null;
		rabo = null;
		longitud = longitud -1;
		return aux;
	}else if(longitud > 1){
		T aux = rabo.elemento;
		rabo = rabo.anterior;
		rabo.siguiente = null;
		longitud = longitud -1;
		return aux;
	}
		return null;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento){
	return buscaNodo(elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
	Lista<T> l = new Lista<>();
	Nodo n = rabo;
	while (n !=null){
		l.agrega(n.elemento);
		n = n.anterior;
	}
	return l;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> l = new Lista<>();
	Nodo n = cabeza;
	while (n !=null){
		l.agrega(n.elemento);
		n = n.siguiente;
	}
	return l;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
       cabeza = null;
       rabo = null;
       longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() throws NoSuchElementException{
	if (longitud == 0){
		throw new NoSuchElementException();
	}else{
        	return cabeza.elemento;
	}
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() throws NoSuchElementException{
        if (longitud == 0){
		throw new NoSuchElementException();
	}else{
        	return rabo.elemento;
	}
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) throws ExcepcionIndiceInvalido{
        if (i < 0 || i >= getLongitud()){
		throw new ExcepcionIndiceInvalido();
	}else{
		Nodo n = cabeza;
		int c = 0;
		while (n !=null){
			if (c == i){
				return n.elemento;
			}
		n = n.siguiente;
		c = c + 1;
		}
		return null;		
	}

    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        Nodo n = cabeza;
	int c = 0;
	while (n !=null){
		if (n.elemento.equals(elemento))
			return c;
		n = n.siguiente;
		c = c + 1;
	}
	return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
	if (getLongitud()==0){        
		return "[]";
	}else{
		Nodo n = cabeza;
		String x = "[";
		while (n !=null){
			x = x + n.elemento;
			if (n.siguiente != null)
				x = x + ", ";
			n = n.siguiente;
		}
		x = x +"]";
		return x;
	}
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
        if (getLongitud() != lista.getLongitud()){
		return false;
	}else{
		Nodo n1 = cabeza;
		Nodo n2 = lista.cabeza;
		while (n1 !=null){
			if (!n1.elemento.equals(n2.elemento))
				return false;
			n1 = n1.siguiente;
			n2 = n2.siguiente;
		}
		return true;
	}
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {       
		if (longitud < 2){
			return copia();
		}else{
			Lista <T> li, ld;
			li = new Lista<T>();
			ld = new Lista<T>();
			int i = 0;
			for (T e: this){
				Lista<T> l = (i++ < getLongitud()/2) ? li :ld;
				l.agregaFinal(e); 
			}
			li = li.mergeSort(comparador);
			ld = ld.mergeSort(comparador);
				return mezcla(li,ld,comparador);  
		}
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort ((a,b) -> a. compareTo (b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <tt>true</tt> si elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo t = cabeza;
	while (t !=null){
		if (comparador.compare(t.elemento,elemento) == 0)
			return true;
		t = t.siguiente;
	}
	return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }

    private Nodo buscaNodo(T e){
    	Nodo t = cabeza;
	while (t !=null){
		if (t.elemento.equals(e))
			return t;
		t = t.siguiente;
	}
	return null;
    }
    private Nodo getNodo(int i) throws ExcepcionIndiceInvalido{
        if (i < 0 || i >= getLongitud()){
		throw new ExcepcionIndiceInvalido();
	}else{
		Nodo n = cabeza;
		int c = 0;
		while (n !=null){
			if (c == i){
				return n;
			}
		n = n.siguiente;
		c = c + 1;
		}
		return null;		
	}

    } 
    private Lista <T> mezcla (Lista <T> li, Lista<T> ld, Comparator<T> comparador){
    	Lista <T> l = new Lista<T>();
	Nodo nd = ld.cabeza;
	Nodo ni = li.cabeza; 
	while (nd !=null && ni !=null){
		if (comparador.compare(ni.elemento,nd.elemento) <= 0){
			l.agregaFinal (ni.elemento);
			ni = ni.siguiente;
		}else{
			l.agregaFinal (nd.elemento);
			nd = nd.siguiente;
		}
	}
	Nodo nf = null;
	if (ni != null){
		nf = ni;
	}else{
		nf = nd;
	}
	while (nf != null){
		l.agregaFinal(nf.elemento);
		nf = nf.siguiente;
	}
	return l;
    }
}
