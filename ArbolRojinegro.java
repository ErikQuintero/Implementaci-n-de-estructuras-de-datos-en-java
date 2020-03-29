package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
	    if (elemento == null){
		color = Color.NEGRO;
	    }else{
            	color = Color.ROJO;
	    }
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            if (color == Color.NEGRO){
		return "N{" + elemento.toString() + "}";
	    }else{
            	return "R{" + elemento.toString() + "}";
	    }
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)o;
	    return (color == vertice.color && super.equals (o));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() {
        super();
    }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        if (vertice == null){
		return Color.NEGRO;
	}
	if (!(vertice instanceof ArbolRojinegro.VerticeRojinegro)){
		throw new ClassCastException();
	}
	VerticeRojinegro v = (VerticeRojinegro) vertice;
	return v.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
	VerticeRojinegro v = (VerticeRojinegro) ultimoAgregado;
	v.color = Color.ROJO;
	balanciniAgreguini(v);
	
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro v = (VerticeRojinegro) buscaQueSiSirve(raiz, elemento);
	VerticeRojinegro gasparin = null;
	VerticeRojinegro minimi;
	if (v == null){
		return;
	}
	if (v.hayIzquierdo()){
		VerticeRojinegro aux = v;
		v = (VerticeRojinegro) maxEnSubarbol(v.izquierdo);
		aux.elemento = v.elemento;				
	}
	if (!v.hayIzquierdo() && !v.hayDerecho()){
		gasparin = (VerticeRojinegro) nuevoVertice(null);
		gasparin.color = Color.NEGRO;
		gasparin.padre = v;
		v.izquierdo = gasparin; 
	}
	if (v.hayIzquierdo()){
		minimi = (VerticeRojinegro) v.izquierdo;
	}else{
		minimi = (VerticeRojinegro) v.derecho;
	}
	subirHijo(v);
	if ((v == null || v.color == Color.NEGRO) && (minimi == null || minimi.color == Color.NEGRO)){
		balanciniElimini(minimi);	
	}else{
		minimi.color = Color.NEGRO;
	}
	if (gasparin != null){
		if (gasparin == raiz){
			raiz = ultimoAgregado = gasparin = null;
		}else{
			if (esHijoIzquierdo(gasparin)){
				gasparin.padre.izquierdo = null;
			}else{
				gasparin.padre.derecho = null;
			}
		}
	}
	elementos = elementos -1 ;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }

    private void balanciniAgreguini (VerticeRojinegro v){
	//Caso 1
    	if (v.padre == null){
		v.color = Color.NEGRO;
		return;
	}
	//Caso 2
	VerticeRojinegro padre = (VerticeRojinegro) v.padre;
	if (padre.color == Color.NEGRO){
 		return;
	}
	//Caso 3
	VerticeRojinegro aguelo = (VerticeRojinegro) v.padre.padre;
	VerticeRojinegro chavorruco = (esHijoIzquierdo(padre)) ? (VerticeRojinegro) v.padre.padre.derecho : (VerticeRojinegro) v.padre.padre.izquierdo;    
	if (chavorruco != null && chavorruco.color == Color.ROJO){
		chavorruco.color = Color.NEGRO;
		padre.color = Color.NEGRO;
		aguelo.color = Color.ROJO;
		balanciniAgreguini (aguelo);
		return;
	}
	//Caso 4
	if ((esHijoIzquierdo(padre) && esHijoDerecho(v)) || (esHijoIzquierdo(v) && esHijoDerecho(padre))){
		if (esHijoDerecho(padre)){
			super.giraDerecha(padre);
		}else{
			super.giraIzquierda(padre);
		}
		VerticeRojinegro aux = padre;
		padre = v;
		v = aux;
	}
	//Caso 5
	padre.color = Color.NEGRO;
	aguelo.color = Color.ROJO;
	if(esHijoIzquierdo(v)){
		super.giraDerecha(aguelo);
	}else{
		super.giraIzquierda(aguelo);	
	}
    }

    private void balanciniElimini(VerticeRojinegro v){
	//Caso 1
    	if (v.padre == null){
		v.color = Color.NEGRO;
		raiz = v;		
		return;
	}
	//Caso 2
	VerticeRojinegro padre = (VerticeRojinegro) v.padre;
	VerticeRojinegro carnalito = (esHijoIzquierdo(v)) ? (VerticeRojinegro) v.padre.derecho : (VerticeRojinegro) v.padre.izquierdo;
	if (carnalito.color != Color.NEGRO){
		carnalito.color = Color.NEGRO;
		padre.color = Color.ROJO;
		if (esHijoIzquierdo(v)){
			super.giraIzquierda(padre);
		}else{
			super.giraDerecha(padre);
		}
	padre = (VerticeRojinegro) v.padre;
	carnalito = (esHijoIzquierdo(v)) ? (VerticeRojinegro) v.padre.derecho : (VerticeRojinegro) v.padre.izquierdo;
	}
	//Caso 3
	VerticeRojinegro mocosoIzq = (VerticeRojinegro) carnalito.izquierdo;
	VerticeRojinegro mocosoDer = (VerticeRojinegro) carnalito.derecho;
	if (esNegro(padre) && esNegro(carnalito) && esNegro(mocosoIzq) && esNegro(mocosoDer)){
		carnalito.color = Color.ROJO;
		balanciniElimini(padre);
		return;
	}
	//Caso 4
	if (padre.color != Color.NEGRO && carnalito.color == Color.NEGRO && (mocosoIzq == null || mocosoIzq.color == Color.NEGRO) && (mocosoDer == null || mocosoDer.color == Color.NEGRO)){
		padre.color = Color.NEGRO;
		carnalito.color = Color.ROJO;
		return; 
	}
	//Caso 5
	//if (mocosos2Colores(mocosoIzq, mocosoDer) && estanCruzados(v, mocosoIzq, mocosoDer)){
	if (((mocosoIzq == null || mocosoDer == null || mocosoIzq.color == Color.NEGRO || mocosoDer.color == Color.NEGRO) && !((mocosoIzq == null || mocosoIzq.color == Color.NEGRO) && (mocosoDer.color == Color.NEGRO))) && (((mocosoIzq == null || mocosoIzq.color == Color.NEGRO) && esHijoDerecho(v)) || ((mocosoDer == null || mocosoDer.color == Color.NEGRO) && esHijoIzquierdo(v)))){
		if (mocosoIzq != null && mocosoIzq.color != Color.NEGRO){
			mocosoIzq.color = Color.NEGRO;
		}else{
			mocosoDer.color = Color.NEGRO;
		}
			carnalito.color = Color.ROJO;
		if (esHijoIzquierdo(v)){
			super.giraDerecha(carnalito);	
		}else{
			super.giraIzquierda(carnalito);
		}
		carnalito = (esHijoIzquierdo(v)) ? (VerticeRojinegro) v.padre.derecho : (VerticeRojinegro) v.padre.izquierdo;
		mocosoIzq = (VerticeRojinegro) carnalito.izquierdo;
		mocosoDer = (VerticeRojinegro) carnalito.derecho;
	}
	//Caso 6
	carnalito.color = padre.color;
	padre.color = Color.NEGRO;
	if (esHijoIzquierdo(v)){
		mocosoDer.color = Color.NEGRO;
	}else{
		mocosoIzq.color = Color.NEGRO;
	}
	if (esHijoIzquierdo(v)){
		super.giraIzquierda(padre);
	}else{
		super.giraDerecha(padre);
	}
    }

    private boolean estanCruzados (VerticeRojinegro v, VerticeRojinegro mocosoIzq, VerticeRojinegro mocosoDer){
	return esHijoDerecho(v) && mocosoIzq.color == Color.NEGRO && mocosoDer.color == Color.ROJO || esHijoIzquierdo(v) && mocosoDer.color == Color.NEGRO && mocosoIzq.color == Color.ROJO;
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

    private void subirHijo(VerticeRojinegro v){
        if (v.hayIzquierdo()) {
            if (v == raiz) {
              raiz = v.izquierdo;
              raiz.padre = null;
            } else {
                v.izquierdo.padre = v.padre;
                if (esHijoIzquierdo(v)) {
                    v.padre.izquierdo = v.izquierdo;
                } else {
                    v.padre.derecho = v.izquierdo;
                }
            }
        } else {
            if (v == raiz) {
                raiz = v.derecho;
                raiz.padre = null;
            } else {
                v.derecho.padre = v.padre;
                if (esHijoIzquierdo(v)) {
                    v.padre.izquierdo = v.derecho;
                } else {
                    v.padre.derecho = v.derecho;
                }
            }
        }

    }

    private boolean esNegro(VerticeRojinegro v){
	return v == null || v.color == Color.NEGRO;
    }

    private boolean esRojo(VerticeRojinegro v){
	return v != null || v.color == Color.ROJO;
    }

    private boolean mocosos2Colores (VerticeRojinegro v, VerticeRojinegro b){
	return esNegro(v) && esRojo(b) || esRojo (v) && esNegro(b);   
    }

    //private VerticeRojinegro verticeRojinegro (VerticeArbolBinario<T> v){
	//VerticeRojinegro vaux = (VerticeRojinegro) v;
	//return vaux; 
   // }
}
