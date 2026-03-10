package umg.edu.gt.figuras.dto;

import umg.edu.gt.figuras.IFigura;

public class Circulo implements IFigura {
	
	private int radio;
	private final double PI = 3.1416;

	public Circulo(int radio) {
		super();
		this.radio = radio;
	}

	@Override
	public double calcularArea() {
		// TODO Auto-generated method stub
		return radio * radio * PI;
	}

}
