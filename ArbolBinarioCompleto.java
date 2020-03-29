package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
        	cola = new Cola<Vertice>();
		if (raiz != null){
			cola.mete(raiz);
		}
		
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
	    Vertice v = cola.saca();
            T e = v.elemento;
            if (v.izquierdo != null){
		cola.mete(v.izquierdo);
	    }
		if (v.derecho != null){
			cola.mete(v.derecho);
		}
	return e;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice x = new Vertice(elemento);
        Vertice vi = this.ultimoAgregado;
        if (this.esVacia()) {
            raiz = this.ultimoAgregado = x;
            elementos = elementos +1;
            return;
        }
        if (esHijoIzquierdo(vi)) {
            vi.padre.derecho = ultimoAgregado = x;
            x.padre = vi.padre;
            elementos = elementos +1;
            return;
        }
        while (esHijoDerecho(vi)) {
            vi = vi.padre;
        }
        if (vi != raiz) {
            vi = vi.padre.derecho;
        }
	agregaIzquierda(vi, x);
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if (elemento != null){
		Vertice v  = (Vertice)busca(elemento);
		if (v != null){
			elementos = elementos -1;
			if (elementos == 0){
				raiz = null;
			}else{
				Cola <Vertice> colita = new Cola<>();
				colita.mete(raiz);
				Vertice x = null;
			while (!colita.esVacia()){
				x = colita.saca();
				if (x.izquierdo != null){
					colita.mete(x.izquierdo);
				}
				if (x.derecho != null){
					colita.mete(x.derecho);
				}
			}
			v.elemento = x.elemento;
			x.elemento = elemento;
			if (esHijoIzquierdo(x)){
				x.padre.izquierdo = null;
			}else{
				x.padre.derecho = null;
			}
			} 
		}
	}
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
	if (raiz == null){
		return -1;
	}
        int h = (int)(Math.floor(Math.log(elementos) / Math.log(2)));
	return h;
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if (!esVacia()){
		Cola<Vertice> c = new Cola<>();
		c.mete(raiz);
		while (!c.esVacia()){
			Vertice v = c.saca();
			accion.actua(v);
				if (v.izquierdo != null)
					c.mete(v.izquierdo);
					if (v.derecho != null){
						c.mete(v.derecho);
					}
		}
	}
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
  
    private boolean esHijoIzquierdo(Vertice v) {
        if (!v.hayPadre()) {
            return false;
        }
        return v.padre.izquierdo == v;
    }

    private boolean esHijoDerecho(Vertice v) {
        if (!v.hayPadre()) {
            return false;
        }
        return v.padre.derecho == v;
    }

    private void agregaIzquierda(Vertice vi, Vertice n) {
        while (vi.izquierdo != null){
            vi = vi.izquierdo;
        }
        vi.izquierdo = ultimoAgregado = n;
        n.padre = vi;
        elementos = elementos +1;
    }	

}
