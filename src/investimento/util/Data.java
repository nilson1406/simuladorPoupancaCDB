package investimento.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Data {

	// metodo para obter o ano
	public static int getAno(Calendar c) {
		Date data = new Date();
		c.setTime(data);
		Format format = new SimpleDateFormat("yyyy");
		return Integer.parseInt(format.format(c.getTime()).toString());
	}

	// monta os meses no formato MES/ANO para a exibição no grafico
	public static List<String> getCategoryMeses(int periodo, int mes) {
		String meses[] = { "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Aug", "Set", "Out", "Nov", "Dez" };
		Calendar c = Calendar.getInstance();
		int ano = Data.getAno(c);
		List<String> categoryMeses = new ArrayList<>();
		for (int i = 0; i <= periodo; i++) {
			categoryMeses.add(meses[mes - 1] + "/" + ano);
			if (mes == 12) {
				mes = 1;
				ano++;
			} else {
				mes++;
			}
		}
		return categoryMeses;
	}

	// Lê os feriados do arquivo xml
	public static List<String> getFeriados() {
		List<String> feriados = new ArrayList<String>();
		String FERIADOS_XML = "./feriados-jaxb.xml";
		JAXBContext context;

		try {

			context = JAXBContext.newInstance(FeriadoList.class);
			Unmarshaller um = context.createUnmarshaller();
			FeriadoList feriadoList = (FeriadoList) um.unmarshal(new FileReader(FERIADOS_XML));

			List<Feriado> aux = feriadoList.getFeriadoList();

			for (int i = 0; i < aux.size(); i++) {
				feriados.add(aux.get(i).getData());
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return feriados;
	}

}
