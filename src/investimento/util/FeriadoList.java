package investimento.util;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

//Classe que monta a lista de feriados a partir do xml
@XmlRootElement(namespace = "investimento.util")
public class FeriadoList {

	private ArrayList<Feriado> feriadoList;

	public void setFeriadoList(ArrayList<Feriado> feriadoList) {
		this.feriadoList = feriadoList;
	}

	public ArrayList<Feriado> getFeriadoList() {
		return feriadoList;
	}

}