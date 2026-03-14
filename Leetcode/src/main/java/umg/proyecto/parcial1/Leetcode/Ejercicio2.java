package umg.proyecto.parcial1.Leetcode;

import java.util.Arrays;

public class Ejercicio2 {

    public static void main(String[] args) {
        int[] datos = {4, 3, 7, 9, 2, 6};
        
        int[] resultado = secondMinMax(datos);
        
        
        System.out.println("Entrada: [4, 3, 7, 9, 2, 6]");
        System.out.println("Resultado: " + Arrays.toString(resultado));
    }

  
    public static int[] secondMinMax(int[] numbers) {
        
        int max = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int secondMin = Integer.MAX_VALUE;

        for (int num : numbers) {
           
            if (num > max) {
                secondMax = max;
                max = num;
            } else if (num > secondMax && num != max) {
                secondMax = num;
            }

         
            if (num < min) {
                secondMin = min;
                min = num;
            } else if (num < secondMin && num != min) {
                secondMin = num;
            }
        }

        return new int[]{secondMin, secondMax};
    }
}