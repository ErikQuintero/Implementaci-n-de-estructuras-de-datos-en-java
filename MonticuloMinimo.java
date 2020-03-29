package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice < elementos && arbol[indice] != null;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (hasNext()){
		return arbol[indice++];
	    }else{
		throw new NoSuchElementException();
	    }	
	}
    }

    /* Clase estática privada para poder implementar HeapSort. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            int i = 0;
	    if (this.elemento.compareTo(adaptador.elemento) < 0){
		return -1;
            }else if (this.elemento.compareTo(adaptador.elemento) == 0){
		return 0;
	    }else{
		return 1;
	    }
	}
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        arbol = nuevoArreglo(coleccion.getElementos());
	elementos = coleccion.getElementos();
	int i = 0;
	for(T e : coleccion){
		arbol[i] = e;
		arbol[i].setIndice(i);
		i = i + 1;
	}
	for (int j = (elementos-1)/2; j>= 0; j--){
		gipifiDaun(j);
	}
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        arbol = nuevoArreglo(n);
	elementos = n;
	int i = 0;
	for(T e : iterable){
		arbol[i] = e;
		arbol[i].setIndice(i);
		i = i + 1;
	}
	for (int j = (elementos-1)/2; j>= 0; j--){
		gipifiDaun(j);
	}
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if (elementos >= arbol.length) {
            T[] nuevoArbolito = nuevoArreglo(arbol.length * 2);
            for (int i = 0; i < arbol.length; i++) {
                nuevoArbolito[i] = arbol[i];
            }
            arbol = nuevoArbolito;
        }
        arbol[elementos] = elemento;
        arbol[elementos].setIndice(elementos);
	gipifiOp(elementos++);
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
		T eliminadoPobrecito = arbol[0];
		elimina(arbol[0]);
		return eliminadoPobrecito;
	}
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if (elemento.getIndice() < 0 || elemento.getIndice() >= elementos){
		return;
	}else{
	        int x = elemento.getIndice();
      		elementos = elementos - 1;
        	arbol[x] = arbol[elementos];
        	arbol[elementos] = elemento;
        	arbol[x].setIndice(x);
		arbol[elementos].setIndice(elementos);
		arbol[elementos].setIndice(-1);
                arbol[elementos] = null;
                reordena(arbol[x]);
	}
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if (elemento.getIndice() < 0 || elemento.getIndice() >= elementos){
		return false;
	}else if(!(arbol[elemento.getIndice()].equals(elemento))){
		return false;
	}else{
		return true;
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
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        elementos = 0;
	for (int i = 0; i < arbol.length; i++){
		arbol[i] = null;
	}
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if (elemento == null){
            return;
	}
        int i = elemento.getIndice();
	gipifiDaun(i);
        gipifiOp(i);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (i < 0 || i >= elementos){
            throw new NoSuchElementException();
	}else{
	    return arbol[i];
	}
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String x = "";
	for (int i = 0; i < arbol.length; i++){
		x = x + arbol[i] + ", ";
	}
	return x;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param o el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)o;
        if (this.elementos != monticulo.elementos){
		return false;
	}else{
		for (int i = 0; i < arbol.length; i++){
			if (!(arbol[i].equals(monticulo.arbol[i]))){
				return false;
			}
		}
	return true;
	} 
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
	Lista<Adaptador<T>> listaAdaptadores = new Lista<>();        
	Lista<T> listaDeElementos = new Lista<>();
	for(T e: coleccion){
		listaAdaptadores.agrega(new Adaptador<>(e));
	}
	MonticuloMinimo<Adaptador <T>> monticuloFinal = new MonticuloMinimo<>();
	for(Adaptador<T> adaptadorsito : listaAdaptadores){
		monticuloFinal.agrega(adaptadorsito);
	}
	while(!monticuloFinal.esVacia()){
		Adaptador<T> adaptadorsito2 = monticuloFinal.elimina();
		listaDeElementos.agrega(adaptadorsito2.elemento);
	}
	return listaDeElementos;

    }
  
    private void gipifiOp(int i){
	int papa = (i - 1) / 2;
	int hijo = i;
	if (papa >= 0 && arbol[papa].compareTo(arbol[i]) > 0){
		hijo = papa;
	}
	if (hijo != i){
		T aux = arbol[i];
		arbol[i] = arbol[papa];
		arbol[i].setIndice(i);
		arbol[papa] = aux;
		arbol[papa].setIndice(papa);
		gipifiOp(hijo);
	}else{

	}
    }

    private void gipifiDaun(int i) {
        int izquierdo = (i * 2) + 1;
        int derecho = (i * 2) + 2;
        if (izquierdo >= elementos && derecho >= elementos){
            return;
	}else{
        	int min = minAuxiliar(izquierdo, derecho);
       		min = minAuxiliar(i, min);
        	if (min != i) {
            		T aux = arbol[i];
           		arbol[i] = arbol[min];
            		arbol[i].setIndice(i);
            		arbol[min] = aux;
            		arbol[min].setIndice(min);
            		gipifiDaun(min);
		}
	}
    }

    private int minAuxiliar(int n, int m) {
        if (m >= elementos){
            return n;
        }else if (arbol[n].compareTo(arbol[m]) < 0){
            return n;
        }else{
            return m;
	}
    }
}
