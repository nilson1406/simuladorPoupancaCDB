package investimento.util;

import java.text.NumberFormat;
import java.util.Locale;

// Contem metodos de conversao
public class Money {

	static Locale locale = new Locale("pt", "BR");
	static NumberFormat formater = NumberFormat.getCurrencyInstance(locale);

	public static String doubleToMoney(double num) {
		return formater.format(num).substring(3);
	}

	public static String stringToMoney(String num) {
		double aux = 0;
		if (num.contains("R$")) {
			num = num.substring(3);
		}
		if (num.contains("\\.") || num.contains(",")) {
			aux = moneyToDouble(num);
		} else {
			aux = Double.parseDouble(num);
			formater.format(aux);
		}
		return formater.format(aux).substring(3);
	}

	public static double moneyToDouble(String money) {
		StringBuffer result = new StringBuffer(money);
		int index = 0;
		while ((index = result.indexOf(".")) != -1) {
			index = result.indexOf(".");
			result.replace(index, index + 1, "");
		}
		while ((index = result.indexOf(",")) != -1) {
			result.replace(index, index + 1, ".");
		}
		return Double.parseDouble(result.toString());
	}

	public static String doubleToPorcentagem(double porcentagem) {
		porcentagem = porcentagem * 100;
		StringBuffer result = new StringBuffer(String.valueOf(porcentagem));
		int index;
		while (result.indexOf(".") != -1) {
			index = result.indexOf(".");
			result.replace(index, index + 1, ",");
		}
		return result.toString();
	}

	public static String stringToPorcentagem(String porcentagem) {
		StringBuffer result = new StringBuffer(String.valueOf(porcentagem));
		int index;
		while (result.indexOf(".") != -1) {
			index = result.indexOf(".");
			result.replace(index, index + 1, ",");
		}
		return result.toString();
	}

	public static double porcentagemToDouble(String porcentagem) {
		StringBuffer result = new StringBuffer(String.valueOf(porcentagem));
		int index;
		while ((index = result.indexOf(",")) != -1) {
			result.replace(index, index + 1, ".");
		}
		return Double.parseDouble(result.toString().substring(0, result.length())) / 100;
	}
}
