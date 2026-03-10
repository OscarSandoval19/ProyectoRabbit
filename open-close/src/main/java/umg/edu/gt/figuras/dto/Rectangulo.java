package umg.edu.gt.figuras.dto;

import umg.edu.gt.figuras.IFigura;

public class Rectangulo implements IFigura {
	
	private double base;
	private double altura;

	public Rectangulo(double base, double altura) {
		super();
		this.base = base;
		this.altura = altura;
	}

	@Override
	public double calcularArea() {
		// TODO Auto-generated method stub
		return base * altura;
	}

}
