package umg.proyecto.parcial1.Leetcode;

import java.util.Arrays;

public class Ejercicio2 {

    public static void main(String[] args) {

        int[] datos = {7, 2, 9, 4, 1, 8};
        
        int[] resultado = secondMinMax(datos);
        
        System.out.println("Entrada: [7,2,9,4,1,8]");
        System.out.println("Resultado: " + Arrays.toString(resultado));
        
    }
    public static int[] secondMinMax(int[] numbers) {
        int max = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int secondMin = Integer.MAX_VALUE;
}
}