package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
		altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString(){
            int balanceAux = alturaQueSiSirve(this.izquierdo) - alturaQueSiSirve(this.derecho);
            return elemento.toString() + " " + altura + "/" + balanceAux; 
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;            
	return (altura == vertice.altura && super.equals (o));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() {
        super();
    }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalanceo((VerticeAVL)(ultimoAgregado));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL vAVL = (VerticeAVL)(buscaQueSiSirve(raiz, elemento));
	if (vAVL != null){
		if (vAVL.hayIzquierdo()){
		VerticeAVL aux = vAVL;
		aux = vAVL;
		vAVL = (VerticeAVL) maxEnSubarbol(vAVL.izquierdo);
		aux.elemento = vAVL.elemento;
		}
			if (!vAVL.hayIzquierdo() && !vAVL.hayDerecho()){
				eliminaHoja(vAVL);
			}else if (!vAVL.hayIzquierdo()){
				eliminaSinHijoIzquierdo(vAVL);
			}else{
				eliminaSinHijoDerecho(vAVL);
			}
			rebalanceo((VerticeAVL)(vAVL.padre));
	}else{
		return;
	}	
    }
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
    
    private int balance (VerticeAVL v){
     	return alturaQueSiSirve(v.izquierdo) - alturaQueSiSirve(v.derecho);
    }

    private void rebalanceo(VerticeAVL v) {
        if (v == null)
            return;
        v.altura = nuevaAltura(v);
        if (balance(v) == -2) {
            if (balance((VerticeAVL) (v.derecho)) == 1)
                giraDerechaQueSiPuedoUsarParaAVL((VerticeAVL)(v.derecho));
            giraIzquierdaQueSiPuedoUsarParaAVL(v);
        }else if (balance(v) == 2) {
            if (balance((VerticeAVL)(v.izquierdo)) == -1)
                giraIzquierdaQueSiPuedoUsarParaAVL((VerticeAVL)(v.izquierdo));
            giraDerechaQueSiPuedoUsarParaAVL(v);
        }
        rebalanceo((VerticeAVL)(v.padre));
    }

    private void giraDerechaQueSiPuedoUsarParaAVL(VerticeAVL v){
	super.giraDerecha(v);        
	v.altura = nuevaAltura(v);
        ((VerticeAVL)(v.padre)).altura = nuevaAltura((VerticeAVL)(v.padre));
    }

    private void giraIzquierdaQueSiPuedoUsarParaAVL(VerticeAVL v){
	super.giraIzquierda(v);        
	v.altura = nuevaAltura(v);
        ((VerticeAVL)(v.padre)).altura = nuevaAltura((VerticeAVL)(v.padre));
    }

    private int nuevaAltura(VerticeAVL v) {
        return 1 + Math.max(alturaQueSiSirve(v.izquierdo), alturaQueSiSirve(v.derecho));
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

    private int numeroDeHijos(Vertice v){
	if (!v.hayIzquierdo() && !v.hayDerecho()){
		return 0;
	}else if (!v.hayIzquierdo() && v.hayDerecho()){
		return 1;
        }else if (v.hayIzquierdo() && !v.hayDerecho()){
		return 1;
	}else{
		return 2;
	}
    }
}
