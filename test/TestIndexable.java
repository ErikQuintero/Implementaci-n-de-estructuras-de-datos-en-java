package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.Indexable;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Indexable}.
 */
public class TestIndexable {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* El elemento. */
    private int elemento;
    /* El valor. */
    private double valor;
    /* El indexable. */
    private Indexable<Integer> indexable;

    /**
     * Crea un indexable para cada prueba.
     */
    public TestIndexable() {
        random = new Random();
        elemento = random.nextInt(100);
        valor = random.nextDouble() * 100.0;
        indexable = new Indexable<Integer>(elemento, valor);
    }

    /**
     * Prueba unitaria para {@link Indexable#Indexable}.
     */
    @Test public void testIndexable() {
        Assert.assertTrue(elemento == indexable.getElemento());
        Assert.assertTrue(valor == indexable.getValor());
        Assert.assertTrue(-1 == indexable.getIndice());
    }

    /**
     * Prueba unitaria para {@link Indexable#getElemento}.
     */
    @Test public void testGetElemento() {
        Assert.assertTrue(elemento == indexable.getElemento());
    }

    /**
     * Prueba unitaria para {@link Indexable#compareTo}.
     */
    @Test public void testCompareTo() {
        int e = indexable.getElemento();
        double v = indexable.getValor();
        Indexable<Integer> idx = new Indexable<Integer>(e, v);
        Assert.assertFalse(indexable.compareTo(idx) < 0);
        Assert.assertTrue(indexable.compareTo(idx) == 0);
        Assert.assertFalse(indexable.compareTo(idx) > 0);
        v = indexable.getValor() + 1.0;
        idx = new Indexable<Integer>(e, v);
        Assert.assertTrue(indexable.compareTo(idx) < 0);
        Assert.assertFalse(indexable.compareTo(idx) == 0);
        Assert.assertFalse(indexable.compareTo(idx) > 0);
        v = indexable.getValor() - 1.0;
        idx = new Indexable<Integer>(e, v);
        Assert.assertFalse(indexable.compareTo(idx) < 0);
        Assert.assertFalse(indexable.compareTo(idx) == 0);
        Assert.assertTrue(indexable.compareTo(idx) > 0);
    }

    /**
     * Prueba unitaria para {@link Indexable#setIndice}.
     */
    @Test public void testSetIndice() {
        int i = random.nextInt(100);
        Assert.assertFalse(i == indexable.getIndice());
        indexable.setIndice(i);
        Assert.assertTrue(i == indexable.getIndice());
    }

    /**
     * Prueba unitaria para {@link Indexable#getIndice}.
     */
    @Test public void testGetIndice() {
        Assert.assertTrue(indexable.getIndice() == -1);
        int i = random.nextInt(100);
        indexable.setIndice(i);
        Assert.assertTrue(i == indexable.getIndice());
    }

    /**
     * Prueba unitaria para {@link Indexable#setValor}.
     */
    @Test public void testSetValor() {
        double v = indexable.getValor() + 10.0;
        Assert.assertFalse(v == indexable.getValor());
        indexable.setValor(v);
        Assert.assertTrue(v == indexable.getValor());
    }

    /**
     * Prueba unitaria para {@link Indexable#getValor}.
     */
    @Test public void testGetValor() {
        Assert.assertTrue(indexable.getValor() == valor);
        double v = random.nextDouble() * 100.0;
        indexable.setValor(v);
        Assert.assertTrue(v == indexable.getValor());
    }

    /**
     * Prueba unitaria para {@link Indexable#equals}.
     */
    @Test public void testEquals() {
        int e = indexable.getElemento() + 1;
        double v = indexable.getValor() + 10.0;
        Indexable<Integer> otro = new Indexable<Integer>(e, v);
        Assert.assertFalse(indexable.equals(otro));
        otro = new Indexable<Integer>(elemento, valor);
        Assert.assertTrue(indexable.equals(otro));
    }

    /**
     * Prueba unitaria para {@link Indexable#toString}.
     */
    @Test public void testToString() {
        String s = String.format("%d:%2.9f", elemento, valor);
        Assert.assertTrue(s.equals(indexable.toString()));
    }
}
