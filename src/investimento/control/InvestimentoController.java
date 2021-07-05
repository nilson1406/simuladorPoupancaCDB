package investimento.control;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import investimento.MainApp;
import investimento.MainApp.OnChangeScreen;
import investimento.model.CDB;
import investimento.model.Investimento;
import investimento.model.Poupanca;
import investimento.util.Data;
import investimento.util.Mask;
import investimento.util.Money;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class InvestimentoController {

	@FXML
	private TextField txtValorInicial;

	@FXML
	private TextField txtJuros;

	@FXML
	private TextField txtAporte;

	@FXML
	protected StackPane pane;

	@FXML
	private LineChart<String, Number> lineChart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private Label lblTaxa;

	@FXML
	private Label lbl1;

	@FXML
	private TextField txt1;

	@FXML
	private Label lbl2;

	@FXML
	private TextField txt2;

	@FXML
	private Label lblTotalInvestido;

	@FXML
	private Label lblAux3;

	@FXML
	private Label lblAux3Desc;

	@FXML
	private Label lblAux4;

	@FXML
	private Label lblAux4Desc;

	@FXML
	private Label lblAux1;

	@FXML
	private Label lblAux1Desc;

	@FXML
	private Label lblAux2;

	@FXML
	private Label lblAux2Desc;

	@FXML
	private Label lblPercentualCDB;

	@FXML
	private Label lblPeriodo;

	@FXML
	private TextField txtPercentualCDB;

	@FXML
	private DatePicker dataInicial;

	@FXML
	private DatePicker dataFinal;

	private String screen;

	private CDB cdb() {

		// Total de dias
		int dias = (int) ChronoUnit.DAYS.between(dataInicial.getValue(), dataFinal.getValue());

		// Lista com os feriados carregados de um XML
		List<String> feriados = Data.getFeriados();

		// Objeto utilizado para tranformar a data obtida do sistema no formato
		// brasileiro
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yy");

		// Obtém a data inicial informada para calcular os dias uteis e corridos
		LocalDate dt = dataInicial.getValue();

		List<Integer> listDiasUteis = new ArrayList<Integer>();
		List<Integer> listDiasCorridos = new ArrayList<Integer>();

		// Variaveis que iram armazenar a quantidade de dias corridos e uteis,
		// informações necessárias para calcular a rentabilidade
		int diasCorridos = 0;
		int diasUteis = 0;

		// Percorre todos os dias no intervalo informado
		for (int i = 0; i < dias; i++) {
			// Verifica se o dia é feriado ou fim de semana
			if (!(feriados.contains(dt.plusDays(i).format(formatador))
					|| dt.plusDays(i).getDayOfWeek().toString().equals("SUNDAY")
					|| dt.plusDays(i).getDayOfWeek().toString().equals("SATURDAY"))) {
				diasUteis++;
			}
			diasCorridos++;
			if (dt.plusDays(i).getMonthValue() != dt.plusDays(i + 1).getMonthValue()) {
				listDiasUteis.add(diasUteis);
				listDiasCorridos.add(diasCorridos);
			}
		}

		double percentualDI = 0;

		// Alimentando a aplicação com valores
		switch (screen) {
		case "Simulador CDB":

			percentualDI = Money.porcentagemToDouble(txt1.getText());
			break;

		case "Comparativo Poupança vs CDB":

			percentualDI = Money.porcentagemToDouble(txt2.getText());
			break;

		}

		// Capturando e convertendo as informações da tela
		double valorInicial = Money.moneyToDouble(txtValorInicial.getText());
		double aporte = Money.moneyToDouble(txtAporte.getText());
		double taxa = Money.porcentagemToDouble(txtJuros.getText());
		int periodo = (int) ChronoUnit.MONTHS.between(dataInicial.getValue(), dataFinal.getValue());
		String dtAplicacao = dataInicial.getValue().toString();
		String dtResgate = dataFinal.getValue().toString();

		// Criando o objeto CDB
		CDB cdb = new CDB(valorInicial, aporte, taxa, percentualDI, listDiasCorridos, listDiasUteis, periodo,
				dtAplicacao, dtResgate);

		return cdb;
	}

	private Poupanca poupanca() {

		// Capturando e convertendo as informações da tela
		double valorInicial = Money.moneyToDouble(txtValorInicial.getText());
		double aporte = Money.moneyToDouble(txtAporte.getText());
		double taxa = Money.porcentagemToDouble(txtJuros.getText());
		int periodo = (int) ChronoUnit.MONTHS.between(dataInicial.getValue(), dataFinal.getValue());
		String dtAplicacao = dataInicial.getValue().toString();
		String dtResgate = dataFinal.getValue().toString();

		// Criando o objeto Poupança
		Poupanca p = new Poupanca(valorInicial, aporte, taxa, periodo, dtAplicacao, dtResgate);

		return p;

	}

	@SuppressWarnings("unchecked")
	private void carregaGrafico(Investimento inv, int periodo) {

		// Desativando a obrigação que o ponto 0 seja exibido no gráfico
		yAxis.setForceZeroInRange(false);

		// Definindo titulo ao gráfico, criando pontos
		lineChart.setCreateSymbols(true);
		lineChart.setAlternativeRowFillVisible(true);

		// Lista de meses para a linha do tempo
		List<String> categoryMeses = Data.getCategoryMeses(periodo, dataInicial.getValue().getMonthValue());

		// Criando as linhas do gráfico
		XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> series3 = new XYChart.Series<String, Number>();

		// Variavel para iterar os periodos
		int i = 0;

		// Adicionando dados as linhas, o primeiro ponto é o inicial
		series1.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), inv.getValorInicial()));
		series2.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), 0));
		series3.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), inv.getValorInicial()));
		/*
		 * Rotina para ajustar a quantidade de meses que serão exibidos no
		 * gráfico
		 */
		while (i < periodo) {

			if (periodo > 768 && (i + 96) <= periodo) {
				i = i + 96;
			} else if (periodo > 384 && (i + 48) <= periodo) {
				i = i + 48;
			} else if (periodo > 192 && (i + 24) <= periodo) {
				i = i + 24;
			} else if (periodo > 96 && (i + 12) <= periodo) {
				i = i + 12;
			} else if (periodo > 72 && (i + 4) <= periodo) {
				i = i + 4;
			} else if (periodo > 48 && (i + 3) <= periodo) {
				i = i + 3;
			} else if (periodo > 24 && (i + 2) <= periodo) {
				i = i + 2;
			} else {
				i++;
			}

			inv.setPeriodo(i);

			// Adicionando informações as linhas
			series1.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), inv.getValorLiquido()));
			series2.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), inv.getJuros()));
			series3.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), inv.getTotalInvestido()));

		}

		// Nomeando as linhas
		series1.setName("Total");
		series2.setName("Juros");
		series3.setName("Total investido");

		// Adicionando as linhas ao gráfico
		lineChart.getData().addAll(series1, series2, series3);

		// Exibindo os resultados finais nas Labels
		lblPeriodo.setText(String.valueOf(periodo));
		lblTotalInvestido.setText(Money.doubleToMoney(inv.getTotalInvestido()));
		lblAux2Desc.setText(Money.doubleToMoney(inv.getJuros()));
		lblAux1Desc.setText(Money.doubleToMoney(inv.getValorLiquido()));

	}

	@SuppressWarnings("unchecked")
	private void carregaGrafico(CDB cdb, Poupanca p, int periodo) {

		// Desativando a obrigação que o ponto 0 seja exibido no gráfico
		yAxis.setForceZeroInRange(false);

		// Definindo titulo ao gráfico, criando pontos
		lineChart.setCreateSymbols(true);
		lineChart.setAlternativeRowFillVisible(true);

		// Lista de meses para a linha do tempo
		List<String> categoryMeses = Data.getCategoryMeses(periodo, dataInicial.getValue().getMonthValue());

		// Criando as linhas do gráfico
		XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();

		// Variavel para iterar os periodos
		int i = 0;

		series1.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), cdb.getValorInicial()));
		series2.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), p.getValorInicial()));

		/*
		 * Rotina para ajustar a quantidade de meses que serão exibidos no
		 * gráfico
		 */
		while (i < periodo) {

			if (periodo > 768 && (i + 96) <= periodo) {
				i = i + 96;
			} else if (periodo > 384 && (i + 48) <= periodo) {
				i = i + 48;
			} else if (periodo > 192 && (i + 24) <= periodo) {
				i = i + 24;
			} else if (periodo > 96 && (i + 12) <= periodo) {
				i = i + 12;
			} else if (periodo > 72 && (i + 4) <= periodo) {
				i = i + 4;
			} else if (periodo > 48 && (i + 3) <= periodo) {
				i = i + 3;
			} else if (periodo > 24 && (i + 2) <= periodo) {
				i = i + 2;
			} else {
				i++;
			}

			cdb.setPeriodo(i);
			p.setPeriodo(i);

			// Adicionando informações as linhas
			series1.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), cdb.getValorLiquido()));
			series2.getData().add(new XYChart.Data<String, Number>(categoryMeses.get(i), p.getValorLiquido()));

		}

		// Nomeando as linhas
		series1.setName("CDB");
		series2.setName("Poupança");

		// Adicionando as linhas ao gráfico
		lineChart.getData().addAll(series1, series2);

		// Exibindo os resultados finais nas Labels
		lblPeriodo.setText(String.valueOf(periodo));
		lblTotalInvestido.setText(Money.doubleToMoney(cdb.getTotalInvestido()));
		lblAux3Desc.setText(Money.doubleToMoney(cdb.getJuros()));
		lblAux4Desc.setText(Money.doubleToMoney(p.getJuros()));
		lblAux1Desc.setText(Money.doubleToMoney(cdb.getValorLiquido()));
		lblAux2Desc.setText(Money.doubleToMoney(p.getValorLiquido()));

	}

	/*
	 * Metodo executado ao abrir a janela, carrega as informações passadas via
	 * Observer
	 */
	@FXML
	protected void initialize() {

		txtValorInicial.setTooltip(new Tooltip("Valor que pretende investir inicialmente"));
		txtAporte.setTooltip(
				new Tooltip("Valor que pretende investir mensalmente\n Igual para todos os meses do período"));
		dataInicial.setTooltip(new Tooltip("Data em que deseja realizar a aplicação"));
		dataFinal.setTooltip(new Tooltip("Data em que deseja realizar o resgate"));

		lblTotalInvestido.setTooltip(new Tooltip("Valor total investido sem o rendimento da aplicação"));

		MainApp.addOnChangeScreenListener(new OnChangeScreen() {

			@Override
			public void onScreenChanged(String newScreen, Object userData) {

				// Recebe o nome da janela a ser aberta
				screen = newScreen;

				// Inicializa os campos com informçãoes padrão
				txtValorInicial.setText(Money.doubleToMoney(10000));
				txt1.setText("100,00");
				txtAporte.setText(Money.doubleToMoney(100));
				dataInicial.setValue(LocalDate.of(LocalDate.now().getYear(), 1, 1));
				dataFinal.setValue(LocalDate.of(LocalDate.now().getYear() + 1, 1, 1));

				int periodo = (int) ChronoUnit.MONTHS.between(dataInicial.getValue(), dataFinal.getValue());

				// Limpa o grafico
				lineChart.getData().clear();

				// Atribuia as máscaras aos campos
				Mask.monetaryField(txtValorInicial);
				Mask.monetaryField(txtAporte);
				Mask.porcentageField(txtJuros);
				Mask.porcentageField(txt1);
				Mask.porcentageField(txt2);

				// Verifica o nome da janela solicita e a abre
				switch (newScreen) {

				case "Simulador Poupança":

					// Caso seja poupança não exibe Percentual CDB
					txtJuros.setTooltip(new Tooltip(
							"A taxa de juros da poupança é a Selic, que pode ser consultada no site do banco central \n(http://www.bcb.gov.br/)"));
					txt1.setVisible(false);
					lbl1.setVisible(false);
					txt2.setVisible(false);
					lbl2.setVisible(false);
					lblAux3.setVisible(false);
					lblAux3Desc.setVisible(false);
					lblAux4.setVisible(false);
					lblAux4Desc.setVisible(false);
					lblTaxa.setText("Taxa de Juros %a.a (Poupança):");
					lblAux1.setText("Total (Poupança):");
					lblAux2.setText("Total ganho em juros (Poupança):");
					lblAux1Desc.setTooltip(new Tooltip("Valor total já com o rendimento da aplicação"));
					lblAux2Desc.setTooltip(new Tooltip("Valor do rendimento da aplicação (Total - Total investido)"));
					txtJuros.setText("7,50");
					lineChart.setTitle("Poupança");
					carregaGrafico(poupanca(), periodo);
					break;

				case "Simulador CDB":

					// Caso seja CDB exibe Percentual CDB
					txtJuros.setTooltip(new Tooltip(
							"A taxa de juros de aplicações CDB é a taxa DI, pode ser consultada no site da CETIP \n(https://www.cetip.com.br/)"));
					txt1.setTooltip(new Tooltip(
							"Determina qual a proporção da taxa DI que será aplicada no rendimento da aplicação"));
					txt1.setVisible(true);
					lbl1.setVisible(true);
					txt2.setVisible(false);
					lbl2.setVisible(false);
					lblAux3.setVisible(false);
					lblAux3Desc.setVisible(false);
					lblAux4.setVisible(false);
					lblAux4Desc.setVisible(false);
					lblTaxa.setText("Taxa de Juros %a.a (CDB):");
					lblAux1.setText("Total (CDB):");
					lblAux2.setText("Total ganho em juros (CDB):");
					lblAux1Desc.setTooltip(new Tooltip("Valor total já com o rendimento da aplicação"));
					lblAux2Desc.setTooltip(new Tooltip("Valor do rendimento da aplicação (Total - Total investido)"));
					txtJuros.setText("7,39");
					lineChart.setTitle("Certificado de Depósito Bancário (CDB)");
					carregaGrafico(cdb(), periodo);
					break;

				case "Comparativo Poupança vs CDB":

					lblAux1.setText("Total (CDB):");
					lblAux2.setText("Total (Poupança):");
					lblTaxa.setText("Taxa de Juros %a.a (Selic):");
					lbl1.setText("Taxa de Juros %a.a (CDB):");
					txtJuros.setTooltip(new Tooltip(
							"A taxa de juros da poupança é a Selic, que pode ser consultada no site do banco central \n(http://www.bcb.gov.br/)"));
					txt1.setTooltip(new Tooltip(
							"A taxa de juros de aplicações CDB é a taxa DI, pode ser consultada no site da CETIP \n(https://www.cetip.com.br/)"));
					txt2.setTooltip(new Tooltip(
							"Determina qual a proporção da taxa DI que será aplicada no rendimento da aplicação"));
					lblAux1Desc.setTooltip(new Tooltip("Valor total já com o rendimento do CDB"));
					lblAux2Desc.setTooltip(new Tooltip("Valor total já com o rendimento da poupança"));
					lblAux3Desc.setTooltip(new Tooltip("Valor do rendimento do CDB (Total - Total investido)"));
					lblAux4Desc.setTooltip(new Tooltip("Valor do rendimento da poupança (Total - Total investido)"));
					txt1.setVisible(true);
					lbl1.setVisible(true);
					txt2.setVisible(true);
					lbl2.setVisible(true);
					lblAux3.setVisible(true);
					lblAux3Desc.setVisible(true);
					lblAux4.setVisible(true);
					lblAux4Desc.setVisible(true);
					txtJuros.setText("7,50");
					txt1.setText("7,39");
					txt2.setText("100,00");
					lineChart.setTitle("Poupança vs CDB");
					carregaGrafico(cdb(), poupanca(), periodo);
					break;

				}

			}
		});
	}

	// Evento responsavel pela aplicação da mascara de dinheiro
	@FXML
	private void monetaryField(KeyEvent e) {
		Mask.monetaryField((TextField) (e.getSource()));
	}

	// Evento responsavel pela aplicação da mascara de porcentagem
	@FXML
	private void porcentageField(KeyEvent e) {
		Mask.porcentageField((TextField) (e.getSource()));
	}

	// Evento disparado ao pressionar o botão simular
	@FXML
	private void simular() {

		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Atenção");

		if (txtValorInicial.getText().isEmpty() || txtAporte.getText().isEmpty() || txtJuros.getText().isEmpty()
				|| ((screen.equals("Simulador CDB") || screen.equals("Comparativo Poupança vs CDB"))
						&& txt1.getText().isEmpty())
				|| (screen.equals("Comparativo Poupança vs CDB") && txt2.getText().isEmpty())) {
			alert.setHeaderText("Preencha todos os campos");
			alert.showAndWait();
		} else {

			int periodo = (int) ChronoUnit.MONTHS.between(dataInicial.getValue(), dataFinal.getValue());

			if (periodo <= 0) {

				alert.setHeaderText("Escolha um intervalo maior que um mês");
				alert.showAndWait();

			} else {

				// Limpa o grafico
				lineChart.getData().clear();

				// Verifica o nome da janela solicita e a abre
				switch (screen) {
				case "Simulador Poupança":

					lineChart.setTitle("Poupança");
					carregaGrafico(poupanca(), periodo);
					break;

				case "Simulador CDB":

					lineChart.setTitle("Certificado de Depósito Bancário (CDB)");
					carregaGrafico(cdb(), periodo);
					break;

				case "Comparativo Poupança vs CDB":

					lineChart.setTitle("Comparativo Poupança vs CDB");
					carregaGrafico(cdb(), poupanca(), periodo);
					break;

				}
			}
		}
	}
}
