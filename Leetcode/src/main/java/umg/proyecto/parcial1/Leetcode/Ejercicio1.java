package umg.proyecto.parcial1.Leetcode;

public class Ejercicio1 {

	public static void main(String[] args) {
	      int[] numeros = {1, 3, 5, 7, 9};
	        
	        
	        int resultado = score(numeros);
	        
	       
	        System.out.println("El puntaje total es: " + resultado);
	        
	       
	        
	    }

	    public static int score(int[] numbers) {
	        int totalScore = 0;

	        for (int num : numbers) {
	            if (num == 5) {
	                totalScore += 5;
	            } else if (num % 2 == 0) {
	                totalScore += 1;
	            } else {
	                totalScore += 3;
	            }
	        }

	        return totalScore;
	    }
	
}
	//Complejidad temporal: El algoritmo recorre el arreglo una sola vez. El tiempo de ejecución crece de manera lineal conforme aumenta el número de elementos (n) en el arreglo.
	//" Espacial: Solo se utiliza una variable entera (totalScore) para acumular el puntaje. No se crean estructuras de datos adicionales que dependan del tamaño de la entrada.

	//Justificacion

	//Estructura de Control (if-else if-else):
	// Se utiliza esta estructura para garantizar que cada número sea evaluado bajo una sola regla.
	//Prioridad del 5: Se evalúa primero num == 5. Aunque el 5 es técnicamente impar, la regla específica de +5 puntos tiene jerarquía sobre la regla general de impares (+3).
	//Manejo del 0: La condición num % 2 == 0 identifica correctamente al 0 como par (+1), cumpliendo con el requerimiento.

	//Eficiencia de Memoria:
	//No se crean copias del arreglo ni estructuras intermedias (como ArrayList). 
	//Solo se utiliza un acumulador de tipo int.

	


