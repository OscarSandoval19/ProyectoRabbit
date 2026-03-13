package umg.edu.gt.figuras;

import java.util.List;

public class FiguraImpl {
	
	public double calcularAreaTotal(List<IFigura> figuras) {
		double total = 0;
		for(IFigura figura: figuras) {
			total = total + figura.calcularArea();
		}
		return total;
	}

}
