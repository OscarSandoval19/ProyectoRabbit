package umg.edu.gt.figuras.dto;

import umg.edu.gt.figuras.IFigura;

public class Triangulo implements IFigura {
	
	private int base;
	private int altura;

	public Triangulo(int base, int altura) {
		super();
		this.base = base;
		this.altura = altura;
	}

	@Override
	public double calcularArea() {
		// TODO Auto-generated method stub
		return (base * altura) / 2;
	}

}
