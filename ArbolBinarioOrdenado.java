package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
            pila = new Pila<Vertice>();
		Vertice v = raiz;
		while (v != null){
			pila.mete(v);
			v = v.izquierdo;
		}
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next(){
            Vertice v = pila.saca();
            T e = v.elemento;
            v = v.derecho;
            while (v != null) {
                pila.mete(v);
                v = v.izquierdo;
            }
		return e;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) throws IllegalArgumentException {
	if (elemento == null){
		throw new IllegalArgumentException();
	}else if (raiz == null){
		raiz = ultimoAgregado = nuevoVertice(elemento);
	}else{
		agregaQueSiSirve(raiz, elemento);
	}
	elementos = elementos + 1;
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice e = buscaQueSiSirve(raiz, elemento), vi;
	if (e == null){
		return;
	}
	if (e.hayIzquierdo()){
		vi = e;
		e = maxEnSubarbol(e.izquierdo);
		vi.elemento = e.elemento;
	}
	if (!e.hayIzquierdo() && !e.hayDerecho()){
		eliminaHoja(e);
	}else if (!e.hayIzquierdo()){
		eliminaSinHijoIzquierdo(e);
	}else{
		eliminaSinHijoDerecho(e);
	}
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        return null;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return buscaQueSiSirve(raiz, elemento);
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {        
  	if (vertice == null || !vertice.hayIzquierdo()){
		return;
	}else{
		Vertice v = (Vertice) vertice;
		Vertice vi = v.izquierdo;
		vi.padre = v.padre;
		if (!esRaiz(v)){
			if (esHijoIzquierdo(v)){
				v.padre.izquierdo = vi;
			}else{
				v.padre.derecho = vi;			
			}
		}else{
			raiz = vi;
		}
		v.izquierdo = vi.derecho;
		if (vi.hayDerecho()){
			vi.derecho.padre = v;
		}
		vi.derecho = v;
		v.padre = vi;
	}
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
       if (vertice == null || !vertice.hayDerecho()){
		return;
	}else{
		Vertice v = (Vertice) vertice;
		Vertice vd = v.derecho;
		vd.padre = v.padre;
		if (!esRaiz(v)){
			if (esHijoIzquierdo(v)){
				v.padre.izquierdo = vd;
			}else{
				v.padre.derecho = vd;			
			}
		}else{
			raiz = vd;
		}
		v.derecho = vd.izquierdo;
		if (vd.hayIzquierdo()){
			vd.izquierdo.padre = v;
		}
		vd.izquierdo = v;
		v.padre = vd;
	} 
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
	dfsPreOrderAux(raiz, accion);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
	dfsInOrderAux(raiz, accion);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
	dfsPostOrderAux (raiz, accion);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
    private boolean esRaiz(Vertice v){
	return v == raiz;
    }
	
    private void dfsInOrderAux(Vertice v, AccionVerticeArbolBinario<T> accion) {
	if (v == null){
        	return;
	}else{
		dfsInOrderAux(v.izquierdo, accion);
		accion.actua(v);
                dfsInOrderAux(v.derecho, accion);
        }
    }
	
    private void dfsPostOrderAux(Vertice v, AccionVerticeArbolBinario<T> accion) {        
	if (v == null){
        	return;
	}else{
		dfsPostOrderAux(v.izquierdo, accion);
                dfsPostOrderAux(v.derecho, accion);
        	accion.actua(v);
        }
    }

    private void dfsPreOrderAux(Vertice v, AccionVerticeArbolBinario<T> accion) {        
	if (v == null){
        	return;
	}else{
		accion.actua(v);
		dfsPreOrderAux(v.izquierdo, accion);
                dfsPreOrderAux(v.derecho, accion);
        }

    }

     private void agregaQueSiSirve (Vertice v, T elemento){
	if (elemento.compareTo(v.get()) < 0)
		if (!v.hayIzquierdo()){
			Vertice va = nuevoVertice(elemento);
			va.padre = v;
			v.izquierdo = ultimoAgregado = va;
		}else
			agregaQueSiSirve(v.izquierdo, elemento);
		else
			if (!v.hayDerecho()){
				Vertice va = nuevoVertice (elemento);
			va.padre = v;
			v.derecho = ultimoAgregado = va;
			
			}else
				agregaQueSiSirve(v.derecho, elemento);			
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
	

    protected boolean esHijoDerecho(Vertice v){
	if (!v.hayPadre()){
		return false;
	}else{
		return v.padre.derecho == v;
	}
	
    }

    protected boolean esHijoIzquierdo(Vertice v){
	if (!v.hayPadre()){
		return false;
	}else{
		return v.padre.izquierdo == v;
	}
	
    }
	
    protected Vertice maxEnSubarbol (Vertice vertice){
    	if (vertice.derecho == null){
		return vertice;
	}else{
		return maxEnSubarbol(vertice.derecho);
	}
    }
  
    protected void eliminaHoja (Vertice e){
	if (raiz == e){
		raiz = null;
		ultimoAgregado = null;
	}else if (esHijoIzquierdo(e)){
		e.padre.izquierdo = null;
	}else{
		e.padre.derecho = null;
	}
	elementos = elementos -1;
    }

    protected void eliminaSinHijoIzquierdo(Vertice e){
    	if (raiz == e){
		raiz = raiz.derecho;
		e.derecho.padre = null;
	}else{
		e.derecho.padre = e.padre;
		if (esHijoIzquierdo(e)){
			e.padre.izquierdo = e.derecho;
		}else{
			e.padre.derecho = e.derecho;
		}
	}
	elementos = elementos -1;
    }

    protected void eliminaSinHijoDerecho(Vertice e){
    	if (raiz == e){
		raiz = raiz.izquierdo;
		e.izquierdo.padre = null;
	}else{
		e.izquierdo.padre = e.padre;
		if (esHijoIzquierdo(e)){
			e.padre.izquierdo = e.izquierdo;
		}else{
			e.padre.derecho = e.izquierdo;
		}
	}
	elementos = elementos -1;
    }

}
