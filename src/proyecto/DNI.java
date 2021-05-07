package proyecto;

import java.util.Random;

public class DNI {
	private int numero;
	private char letra;
    private final String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

    public DNI(int numero) {
        this.numero = numero;
        this.letra = (generarLetra(numero));
    }

    public DNI(int numero, char letra) {
        this.numero = numero;
        this.letra = letra;
    }

    public DNI(String dni) throws DNIInvalidException {
        //Aquí compruebo que esté bien el numero y la letra del DNI
        char letraDni;
        letraDni = ' ';
        String numerosDni = "";

        String numeroDNI = dni.substring(0, dni.length() - 1);
        letraDni = dni.charAt(dni.length() - 1);
    }
    

    //Constructor por defecto que genere un DNI correcto
    public DNI() {
        //Generamos número aleatoriamente
        Random rand = new Random();
        this.numero = rand.nextInt(100000000);
        this.letra = (generarLetra(numero));

    }

    public char generarLetra(int numero) {
        //Ahora sacamos la letras
        int resto = numero % letras.length();
        return letras.charAt(resto);
    }

    @Override
    public String toString() {
        return "DNI: " + numero + "-" + letra;
    }

}
