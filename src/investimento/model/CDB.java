package investimento.model;

import java.util.List;

public class CDB extends Investimento {

	private double percentualDI;
	private List<Integer> diasCorridos;
	private List<Integer> diasUteis;

	public CDB(double valorInicial, double aporte, double taxa, double percentualDI, List<Integer> diasCorridos,
			List<Integer> diasUteis, int periodo, String dtAplicacao, String dtResgate) {
		this.valorInicial = valorInicial;
		this.aporte = aporte;
		this.taxa = taxa;
		this.percentualDI = percentualDI * 100d;
		this.diasCorridos = diasCorridos;
		this.diasUteis = diasUteis;
		this.periodo = periodo;
		this.dtAplicacao = dtAplicacao;
		this.dtResgate = dtResgate;
	}

	public List<Integer> getDiasCorridos() {
		return diasCorridos;
	}

	public void setDiasCorridos(List<Integer> diasCorridos) {
		this.diasCorridos = diasCorridos;
	}

	public List<Integer> getDiasUteis() {
		return diasUteis;
	}

	public void setDiasUteis(List<Integer> diasUteis) {
		this.diasUteis = diasUteis;
	}

	public int getTotalDiasUteis() {
		return diasUteis.get(diasUteis.size() - 1);
	}

	public int getTotalDiasCorridos() {
		return diasCorridos.get(diasCorridos.size() - 1);
	}

	public double getPercentualDI() {
		return percentualDI;
	}

	public void setPercentualDI(double percentualDI) {
		this.percentualDI = percentualDI * 100;
	}

	public double getValorLiquido() {
		double DI = getTaxa();
		double IR = 0;
		double DIDia = Math.pow((1d + DI), (1d / 252d)) - 1d;
		double renBrutaDia = (DIDia * getPercentualDI()) / 100;
		double renBrutaPeriodo = (Math.pow((1d + renBrutaDia), diasUteis.get(getPeriodo() - 1)) - 1d);

		if (diasCorridos.get(getPeriodo() - 1) > 720) {
			IR = 0.15d;
		} else if (diasCorridos.get(getPeriodo() - 1) > 360) {
			IR = 0.175d;
		} else if (diasCorridos.get(getPeriodo() - 1) > 180) {
			IR = 0.2d;
		} else {
			IR = 0.225d;
		}

		double renLiquidaPeriodo = renBrutaPeriodo * (1d - IR);
		double renLiquidaMes = Math.pow((1d + renLiquidaPeriodo), (21d / diasUteis.get(getPeriodo() - 1))) - 1d;
		double valorAtualizado = (valorInicial * (1d + renBrutaPeriodo));
		double valorIR = (valorAtualizado - valorInicial) * IR;
		double valorLiquido = (valorAtualizado - valorIR);
		double aporteLiquido = getAporte() * ((Math.pow((1d + renLiquidaMes), getPeriodo()) - 1d) / renLiquidaMes);

		valorLiquido += aporteLiquido;

		return valorLiquido;
	}

}
