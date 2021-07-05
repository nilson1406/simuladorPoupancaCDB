package investimento.model;

public class Poupanca extends Investimento {

	public Poupanca(double valorInicial, double aporte, double taxa, int periodo, String dtAplicacao, String dtResgate) {
		this.valorInicial = valorInicial;
		this.aporte = aporte;
		this.taxa = taxa;
		this.periodo = periodo;
		this.dtAplicacao = dtAplicacao;
		this.dtResgate = dtResgate;
	}

	public double getValorLiquido() {
		double taxa;
		double valorLiquido = 0;

		if (getTaxa() > 0.085d) {
			taxa = 0.005d;
			valorLiquido = getValorInicial() * Math.pow((1d + taxa), getPeriodo());
		} else {
			taxa = (getTaxa() / 100d) * 70d;
			taxa = Math.pow((1d + taxa), (1d / 12d)) - 1d;
			valorLiquido = Math.pow((1d + taxa), getPeriodo()) * getValorInicial();
		}

		valorLiquido += getAporte() * (1d + taxa) * ((Math.pow((1d + taxa), getPeriodo()) - 1d) / taxa);
		return valorLiquido;
	}

}
