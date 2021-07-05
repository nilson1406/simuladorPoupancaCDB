package investimento.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//Classe necessaria para a leitura correta dos feriados no arquivo xml
@XmlRootElement(name = "feriado")
public class Feriado {

	private String data;
	private String descricao;

	@XmlElement(name = "data")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@XmlElement(name = "descricao")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
