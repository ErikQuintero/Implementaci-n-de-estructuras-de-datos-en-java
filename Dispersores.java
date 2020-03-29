package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave){
      int x = 0;
      int y = 0;
      int k = llave.length;
      	while (k >= 4) {
      		x = x ^ (cascaHuevosGrande(llave[y], llave[y+1], llave[y+2], llave[y+3]));
      		y = y + 4;
        	k = k - 4;
      	}
      int w = 0;
      switch (k) {
   	     case 3: w = w | ((llave[y+2] & 0xFF) << 8);
   	     case 2: w = w | ((llave[y+1] & 0xFF) << 16);
   	     case 1: w = w | ((llave[y] & 0xFF)   << 24);
      }
   	   x = x ^ w;
   	  return x;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave){
	int k = llave.length;
	int x = 0x9e3779b9; 
	int y = 0x9e3779b9;
	int z = 0xffffffff;
	int i = 0;
	int[] d;
	while(k >= 12){
		x = x + cascaHuevosChico(llave[i], llave[i+1], llave[i+2], llave[i+3]);
		y = y + cascaHuevosChico(llave[i+4], llave[i+5], llave[i+6], llave[i+7]);
		z = z + cascaHuevosChico(llave[i+8], llave[i+9], llave[i+10], llave[i+11]);
	    d = mezclaAux(x, y, z);
	    x = d[0];
	    y = d[1];
	    z = d[2];
	    k = k - 12;
	    i = i + 12;
	}
	z = z + llave.length;
	switch(k){
		case 11: z = z + ((llave[i+10] & 0xFF) << 24);
		case 10: z = z + ((llave[i+9] & 0xFF) << 16);
		case 9: z = z + ((llave[i+8] & 0xFF) << 8);
		case 8: y = y + ((llave[i+7] & 0xFF) << 24);
		case 7: y = y + ((llave[i+6] & 0xFF) << 16);
		case 6: y = y + ((llave[i+5] & 0xFF) << 8);
		case 5: y = y + (llave[i+4] & 0xFF);
		case 4: x = x + ((llave[i+3] & 0xFF) << 24);
		case 3: x = x + ((llave[i+2] & 0xFF) << 16);
		case 2: x = x + ((llave[i+1] & 0xFF) << 8);
		case 1: x = x + (llave[i] & 0xFF);
	}
		d = mezclaAux(x, y, z);
        	return d[2];

    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int x = 5381;
       	for (int i = 0; i < llave.length; i++) {
       		x = x + ((x << 5) + (llave[i] & 0xFF));
        }
        return x;
    }

    private static int cascaHuevosGrande(byte x, byte y, byte z, byte w){
    	return ((x & 0xFF) << 24) | ((y & 0xFF) << 16) | ((z & 0xFF) << 8) | ((w & 0xFF));
    }

    private static int cascaHuevosChico(byte x, byte y, byte z, byte w){
    	return ((x & 0xFF)) | ((y & 0xFF) << 8) | ((z & 0xFF) << 16) | ((w & 0xFF) << 24);
    }

    private static int [] mezclaAux(int x, int y, int z){
	x = x - y;
	x = x - z;
	x = x ^ (z >>> 13);
	y = y - z;
	y = y - x;
	y = y ^ (x << 8);
	z = z - x;
	z = z - y;
	z = z ^ (y >>> 13);
	x = x - y;
	x = x - z;
	x = x ^ (z >>> 12);
        y = y - z;
	y = y - x;
	y = y ^ (x<< 16);
	z = z - x;
	z = z - y;
	z = z ^ (y >>> 5);
        x = x - y;
	x = x - z;
	x = x ^ (z >>> 3);
        y = y - z;
	y = y - x;
	y = y ^ (x << 10);
	z = z - x;
	z = z - y;
	z = z ^ (y >>> 15);
	int[] d = {x,y,z};
	return d;
   }
    
}
