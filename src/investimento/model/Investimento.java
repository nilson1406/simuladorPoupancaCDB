package investimento.model;

public abstract class Investimento {

	protected double valorInicial;
	protected double aporte;
	protected double taxa;
	protected double percentualTaxa;
	protected int periodo;
	protected String dtAplicacao;
	protected String dtResgate;

	public String getDtAplicacao() {
		return dtAplicacao;
	}

	public void setDtAplicacao(String dtAplicacao) {
		this.dtAplicacao = dtAplicacao;
	}

	public String getDtResgate() {
		return dtResgate;
	}

	public void setDtResgate(String dtResgate) {
		this.dtResgate = dtResgate;
	}

	public double getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(double valorInicial) {
		this.valorInicial = valorInicial;
	}

	public double getAporte() {
		return aporte;
	}

	public void setAporte(double aporte) {
		this.aporte = aporte;
	}

	public double getTaxa() {
		return taxa;
	}

	public void setTaxa(double taxa) {
		this.taxa = taxa;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public double getTotalInvestido() {
		return (getValorInicial() + (getAporte() * getPeriodo()));
	}

	public double getPercentualTaxa() {
		return percentualTaxa;
	}

	public void setPercentualTaxa(double percentualTaxa) {
		this.percentualTaxa = percentualTaxa;
	}

	public double getJuros() {
		return (getValorLiquido() - getTotalInvestido());
	}

	public abstract double getValorLiquido();

}
