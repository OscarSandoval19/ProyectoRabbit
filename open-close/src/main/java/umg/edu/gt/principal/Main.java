package umg.edu.gt.principal;

import java.util.Arrays;
import java.util.List;

import umg.edu.gt.figuras.FiguraImpl;
import umg.edu.gt.figuras.IFigura;
import umg.edu.gt.figuras.dto.Circulo;
import umg.edu.gt.figuras.dto.Cuadrado;
import umg.edu.gt.figuras.dto.Rectangulo;
import umg.edu.gt.figuras.dto.Triangulo;
import umg.edu.gt.figuras.dto.TrianguloIsosceles;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		IFigura cuadrado = new Cuadrado(5);
		IFigura triangulo = new Triangulo(3, 2);
		IFigura circulo = new Circulo(4);
		IFigura rectangulo = new Rectangulo(5, 6);
		IFigura trianguloIsosceles = new TrianguloIsosceles(10, 12);
		
		List<IFigura> figuras = Arrays.asList(cuadrado, triangulo, circulo, rectangulo, trianguloIsosceles);
		FiguraImpl impl = new FiguraImpl();
		double total = impl.calcularAreaTotal(figuras);
		System.out.println("total: " + total);

	}

}
