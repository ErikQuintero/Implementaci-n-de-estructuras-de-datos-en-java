package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
	quickSort2 (arreglo, 0, arreglo.length-1, comparador);
   
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for (int i = 0; i < arreglo.length-1; i++){
		int m = i;
		for (int j = i + 1; j<arreglo.length;j++){
			if (comparador.compare(arreglo[j],arreglo[m]) < 0){
				intercambia (arreglo,m,j);
			}else{ }
		}
				intercambia (arreglo,m,i);
	}
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
	return auxBusquedaBinaria (arreglo, elemento, comparador, 0, arreglo.length-1);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
	
    private static <T> void intercambia (T[] arreglo, int x, int y){
	T temp = arreglo[x]; 
	arreglo[x] = arreglo[y];
	arreglo[y] = temp;

    } 
    
    private static <T> int auxBusquedaBinaria (T[] arreglo, T elemento, Comparator<T> comparador, int a, int b){
	if (b < a){
		return -1;
	}else{
		int m = (a+b)/2;
		if (comparador.compare(arreglo[m],elemento) < 0){
			return auxBusquedaBinaria(arreglo, elemento, comparador, m+1, b);
		}else if(comparador.compare(arreglo[m],elemento) > 0){
			return auxBusquedaBinaria(arreglo, elemento, comparador, a, m-1);
		}else{
			return m;
		}
	}
    }
    private static <T> void quickSort2(T[]arreglo, int ini, int fin, Comparator <T> comparador){
	if (ini >= fin){
		return;
	}
	int i = ini + 1;
	int j = fin ;
	while (i < j){
		if (comparador.compare (arreglo[i], arreglo[ini]) > 0 && comparador.compare (arreglo[j], arreglo[ini]) <= 0){
			intercambia (arreglo, i++, j--);
		}else if (comparador.compare(arreglo[i], arreglo[ini]) <= 0){
			i++;
		}else{
			j--;
		}	
	}
		if (comparador.compare(arreglo[i], arreglo[ini]) > 0){
			i--;
		}
		intercambia (arreglo, i, ini);
		quickSort2 (arreglo, ini, i-1,comparador);
		quickSort2 (arreglo, i + 1, fin,comparador);
    }
}
