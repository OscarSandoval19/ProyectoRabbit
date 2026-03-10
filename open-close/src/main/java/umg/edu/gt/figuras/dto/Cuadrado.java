package umg.edu.gt.figuras.dto;

import umg.edu.gt.figuras.IFigura;

public class Cuadrado implements IFigura {
	
	private int lado;

	@Override
	public double calcularArea() {
		// TODO Auto-generated method stub
		return lado * 4;
	}

	public Cuadrado(int lado) {
		super();
		this.lado = lado;
	}
	
	
	
	
	


}
