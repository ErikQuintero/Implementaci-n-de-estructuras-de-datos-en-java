package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }
    }

    /* Vertices para gráficas; implementan la interfaz ComparableIndexable y
     * VerticeGrafica */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* El diccionario de vecinos del vértice. */
        public Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
	    color = Color.NINGUNO;
	    vecinos = new Diccionario<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if (distancia > vertice.distancia){
                return 1;
            }else if (distancia < vertice.distancia){
                return -1;
	    }else{
		return 0;
	    }
	}
    }

    /* Vecinos para gráficas; un vecino es un vértice y el peso de la arista que
     * los une. Implementan VerticeGrafica. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
             this.vecino = vecino;
	     this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.color;
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Diccionario<T, Vertice>();
        aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null || contiene(elemento)){
		throw new IllegalArgumentException();	
	}else{
		Vertice v = new Vertice(elemento);
		vertices.agrega(elemento, v);
	}
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
		conecta(a, b, 1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        if (!contiene(a) || !contiene(b)){
		throw new NoSuchElementException();
	}else if (sonVecinos(a,b) || a == b || peso <= 0){
		throw new IllegalArgumentException();
	}else{
		Vertice v1 = busca(a);
		Vertice v2 = busca(b);		
		v1.vecinos.agrega(v2.elemento, new Vecino(v2, peso));
		v2.vecinos.agrega(v1.elemento, new Vecino(v1, peso));
		aristas = aristas + 1;
	}
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        if (!contiene(a) || !contiene(b)){
		throw new NoSuchElementException();
	}else if (!sonVecinos(a,b)){
		throw new IllegalArgumentException();
	}else{
		Vertice v1 = busca(a);
		Vertice v2 = busca(b);
		Vecino va = getVecino(v2, v1);
		Vecino vb = getVecino(v1, v2);
		v1.vecinos.elimina(vb.vecino.elemento);
		v2.vecinos.elimina(va.vecino.elemento);	
		aristas = aristas - 1;	
	}
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
	boolean vfd = false;
        for (Vertice v : vertices){
		if (elemento == null){
			vfd = false;
		}
		if (v.elemento.equals(elemento)){
                	vfd = true;
		}
	}
	return vfd;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
         if (!contiene(elemento)){
		throw new NoSuchElementException();
	 }else{
		Vertice v1 = busca(elemento);
		for (Vecino v : v1.vecinos){
			desconecta(v1.elemento, v.vecino.elemento);
		}
			vertices.elimina(v1.elemento); 
	}
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
	Vertice v1 = busca(a);
	Vertice v2 = busca(b); 
	if (!contiene(a) || !contiene(b)){
		throw new NoSuchElementException();
	}
	for (Vecino vAux : v1.vecinos){
		if (vAux.vecino.equals(v2)){
			return true;
		}
	}
		return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        if (!contiene(a) || !contiene(b)){
            throw new NoSuchElementException();
	}
	if (!sonVecinos(a,b)){
		throw new IllegalArgumentException();
	}
		Vertice v1 = busca(a);
        	Vertice v2 = busca(b);
		Vecino v3 = getVecino(v1, v2);
		return v3.peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        if (!contiene(a) || !contiene(b)){
            throw new NoSuchElementException();
	}
	if (!sonVecinos(a,b)){
		throw new IllegalArgumentException();
	}
		Vertice v1 = busca(a);
        	Vertice v2 = busca(b);
		Vecino v3 = getVecino(v1, v2);
		for (Vecino v : v1.vecinos){
			if (v.vecino.equals(v2)){
				v.peso = peso;
			}
		}
		for (Vecino vx : v2.vecinos){
			if (vx.vecino.equals(v1)){
				vx.peso = peso;
			}
		}
		peso = -1;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        return busca(elemento);
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null){
		throw new IllegalArgumentException();
	}
	if (vertice.getClass() == Vecino.class){
		Vecino v1 = (Vecino) vertice;
		v1.vecino.color = color;
	}else if(vertice.getClass() == Vertice.class){
		Vertice v1 = (Vertice) vertice;
		v1.color = color;		
	}else{
		throw new IllegalArgumentException();
	}
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
	if (aristas < vertices.getElementos() - 1){
		return false;
	}else{
        Cola<Vertice> colita = new Cola<>();
        Vertice v1 = null;
	for(Vertice chale : vertices){
		v1 = chale;
		break;
	}
        colita.mete(v1);
        	while(!colita.esVacia()){
          		Vertice aux = colita.saca();
          		setColor(aux,Color.ROJO);
          		for(Vecino vx : aux.vecinos){
            			if(vx.vecino.color != Color.ROJO){
              				setColor(vx.vecino, Color.ROJO);
              				colita.mete(vx.vecino);
            			}
          		}
        	}
        	for(Vertice vx : vertices){
          		if(vx.color != Color.ROJO){
            			return false;
          		}
          	vx.color = Color.NINGUNO;
        	}
        return true;
	}
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for (Vertice v : vertices){
		accion.actua(v);
	}
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
      Cola<Vertice> colita = new Cola<>();
      auxiliarBFSyDFS(elemento, accion, colita);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
      Pila<Vertice> pilita = new Pila<>();
      auxiliarBFSyDFS(elemento, accion, pilita);
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        if (vertices.esVacia() && aristas == 0){
		return true;
	}else{
		return false;
	}
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
	aristas = 0;
	vertices.limpia();
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
      String x = "";
      for(Vertice v1 : vertices)
      	   for(Vecino vx : v1.vecinos)
                if(x.contains("(" + vx.vecino.elemento + ", " + v1.elemento)){ 
	  }else
		x = x + "(" + v1.elemento.toString() + ", " + vx.vecino.elemento.toString() + "), ";
      return x;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la gráfica es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)o;
        	for(Vertice v1 : vertices){
        		if(!grafica.contiene(v1.elemento)){
            			return false;
          	}
          		for(Vecino vx : v1.vecinos){
            			if(!grafica.sonVecinos(v1.elemento, vx.vecino.elemento)){
              				return false;
            			}
          		}
        	}
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        if (!contiene(origen) || !contiene(destino)){
            throw new NoSuchElementException();
	}
	Lista<VerticeGrafica<T>> listini = new Lista <VerticeGrafica<T>>();
	Vertice o = busca(origen);
	Vertice d = busca(destino);
	if (o.equals(d)){
		listini.agrega(d);
		return listini;
	}else{
		for(Vertice v : vertices){
			v.distancia = Double.POSITIVE_INFINITY;
		}
		o.distancia = 0;
		MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<Vertice>(vertices, vertices.getElementos());
			while(!monticulo.esVacia()){
				Vertice aux = monticulo.elimina();
				for (Vecino v1 : aux.vecinos){
					if (v1.vecino.distancia == Double.POSITIVE_INFINITY || aux.distancia + v1.peso < v1.vecino.distancia){
						v1.vecino.distancia = aux.distancia + 1;
						monticulo.reordena(v1.vecino);
					}
				}
			}
		if (d.distancia != Double.POSITIVE_INFINITY){
			Vertice vAux = d;
			while(!vAux.equals(o)){
				for(Vecino v2 : vAux.vecinos){
					if (vAux.distancia -1 == v2.vecino.distancia){
						listini.agregaInicio(vAux);
						vAux = v2.vecino;
						break;
					}
				}
				if(vAux.equals(o)){
					listini.agregaInicio(o);
				}
			}
		}
		return listini;	  
	}	
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
	if (!contiene(origen) || !contiene(destino)){
            throw new NoSuchElementException();
	}
	Lista<VerticeGrafica<T>> listini = new Lista <VerticeGrafica<T>>();
	Vertice o = busca(origen);
	Vertice d = busca(destino);
	if (o.equals(d)){
		listini.agrega(d);
		return listini;
	}else{
		for(Vertice v : vertices){
			v.distancia = Double.POSITIVE_INFINITY;
		}
		o.distancia = 0;
		MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<Vertice>(vertices, getElementos());
			while(!monticulo.esVacia()){
				Vertice aux = monticulo.elimina();
				for (Vecino v1 : aux.vecinos){
					if (v1.vecino.distancia == Double.POSITIVE_INFINITY || aux.distancia + v1.peso < v1.vecino.distancia){
						v1.vecino.distancia = aux.distancia + v1.peso;
						monticulo.reordena(v1.vecino);
					}
				}
			}
		if (d.distancia != Double.POSITIVE_INFINITY){
			Vertice vAux = d;
			while(!vAux.equals(o)){
				for(Vecino v2 : vAux.vecinos){
					if (vAux.distancia -v2.peso == v2.vecino.distancia){
						listini.agregaInicio(vAux);
						vAux = v2.vecino;
						break;
					}
				}
				if(vAux.equals(o)){
					listini.agregaInicio(o);
				}
			}
		}
		return listini;	  
	}	
    }

    private Vertice busca (T elemento) {
        for (Vertice v : vertices){
            if (v.elemento.equals(elemento))
                return v;
	}
        throw new NoSuchElementException(); 
     }


     private void auxiliarBFSyDFS(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> estructura){
      	if (contiene(elemento)){		
               Vertice vertice = (Vertice) vertice(elemento);
	       estructura.mete(vertice);
	       	 while (!estructura.esVacia()) {
	         	Vertice v1 = estructura.saca();
	         	setColor(v1, Color.NEGRO);
	         	accion.actua(v1);
		        	for (Vecino vx : v1.vecinos){
				           if (vx.vecino.color != Color.NEGRO) {
		        	                setColor(vx, Color.NEGRO);
	            			   	estructura.mete(vx.vecino);
           			   	   }
         		 	}
       	       }
       	       paraCadaVertice(vy -> setColor(vy, Color.NINGUNO));
        }else{
	 	throw new NoSuchElementException();
        }
     }

     private Vecino getVecino(Vertice x, Vertice y){
     	for (Vecino vAux : x.vecinos){
		if (vAux.vecino.equals(y)){
			return vAux;
		}
	}
		return null;
     }	
}
