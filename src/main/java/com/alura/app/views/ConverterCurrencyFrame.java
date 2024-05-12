package com.alura.app.views;

import com.alura.app.exceptions.NotFoundResourceFile;
import com.alura.app.models.Currency;
import com.alura.app.models.Transaction;
import com.alura.app.services.CurrencyServiceApi;
import com.alura.app.services.HistoryTransactionService;
import com.alura.app.utils.CoreHelpers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ConverterCurrencyFrame extends JFrame {
	private final JComboBox<Currency> cmbBaseCurrency = new JComboBox<>();
	private final JComboBox<Currency> cmbTargetCurrency = new JComboBox<>();
	private final JTextField txtAmount = new JTextField("0.00");
	private final JTextField txtOutput = new JTextField("0.00");
	private final JButton btnConvert = new JButton("Convertir");

	private final JLabel lblBaseCurrency = new JLabel("Moneda base: ");
	private final JLabel lblTargetCurrency = new JLabel("Moneda objetivo: ");
	private final JLabel lblAmount = new JLabel("Monto a convertir: ");
	private final JLabel lblOutput = new JLabel("Resultado de conversión: ");

	private boolean shouldBeAnimated = false;

	private final CurrencyServiceApi currencyServiceApi;
	private final HistoryPanel historyPanel;

	public ConverterCurrencyFrame(
		List<Currency> currencies,
		CurrencyServiceApi currencyServiceApi,
		HistoryTransactionService transactionService
	) {
		super("Conversor de monedas - Alura Latam");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());

		this.currencyServiceApi = currencyServiceApi;
		currencies.forEach(cmbBaseCurrency::addItem);
		currencies.forEach(cmbTargetCurrency::addItem);
		txtAmount.setPreferredSize(new Dimension(200, 20));
		txtOutput.setPreferredSize(new Dimension(200, 20));
		txtOutput.setEditable(false);
		btnConvert.addActionListener(this::onClickConvert);

		JPanel baseCurrencyPanel = new JPanel();
		JPanel targetCurrencyPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JPanel outputPanel = new JPanel();
		this.historyPanel = new HistoryPanel(transactionService);

		baseCurrencyPanel.add(lblBaseCurrency);
		baseCurrencyPanel.add(cmbBaseCurrency);

		targetCurrencyPanel.add(lblTargetCurrency);
		targetCurrencyPanel.add(cmbTargetCurrency);

		inputPanel.add(lblAmount);
		inputPanel.add(txtAmount);
		inputPanel.add(btnConvert);

		outputPanel.add(lblOutput);
		outputPanel.add(txtOutput);

		add(baseCurrencyPanel);
		add(targetCurrencyPanel);
		add(inputPanel);
		add(outputPanel);
		add(historyPanel);

		setSize(990, 650);
		setLocationRelativeTo(null); // position center screen
	}

	public void showWindow() throws NotFoundResourceFile, IOException, FontFormatException {
		// important!, set default values for codes
		cmbBaseCurrency.setSelectedIndex(0);
		cmbTargetCurrency.setSelectedIndex(1);
		// load fonts
		Font poppins = CoreHelpers.loadFont("Poppins.ttf", 18);
		cmbBaseCurrency.setFont(poppins);
		cmbTargetCurrency.setFont(poppins);
		txtAmount.setFont(poppins);
		txtOutput.setFont(poppins);
		btnConvert.setFont(poppins);
		lblBaseCurrency.setFont(poppins);
		lblTargetCurrency.setFont(poppins);
		lblAmount.setFont(poppins);
		lblOutput.setFont(poppins);
		setVisible(true);
	}

	private void onClickConvert(ActionEvent actionEvent) {
		shouldBeAnimated = true;
		btnConvert.setEnabled(false);
		txtAmount.requestFocus();
		// start animation :)
		CompletableFuture.runAsync(() -> {
			while (shouldBeAnimated) {
				try {
					var text = txtOutput.getText();
					if (text.length() == 1) {
						txtOutput.setText("...");
					} else if (text.length() == 3) {
						txtOutput.setText(".....");
					} else {
						txtOutput.setText(".");
					}
					TimeUnit.MILLISECONDS.sleep(300);
				} catch (InterruptedException e) {
					shouldBeAnimated = false;
					throw new RuntimeException(e);
				}
			}
		});
		// start request to ExchangeRateApi
		CompletableFuture.runAsync(() -> {
			try {
				double amount = CoreHelpers.currencyFormat(txtAmount.getText());
				Optional<Currency> baseCurrency = Optional.ofNullable(cmbBaseCurrency.getItemAt(cmbBaseCurrency.getSelectedIndex()));
				Optional<Currency> targetCurrency = Optional.ofNullable(cmbTargetCurrency.getItemAt(cmbTargetCurrency.getSelectedIndex()));
				if (baseCurrency.isEmpty()) throw new Exception("You must select a base currency");
				if (targetCurrency.isEmpty()) throw new Exception("You must select a target currency");
				this.currencyServiceApi.convert(amount, baseCurrency.get().code(), targetCurrency.get().code())
					.thenApply(result -> {
						shouldBeAnimated = false;
						btnConvert.setEnabled(true);
						return result;
					})
					.thenAccept(conversionResult -> {
						txtOutput.setText(conversionResult.toString());
						try {
							this.historyPanel.addTransaction(Transaction.create(
								baseCurrency.get().code(),
								targetCurrency.get().code(),
								CoreHelpers.currencyFormat(amount),
								conversionResult.toString()
							));
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					})
					.exceptionally(throwable -> {
						txtOutput.setText("-.--");
						JOptionPane.showMessageDialog(null, throwable.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						return null;
					});
			} catch (ParseException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Invalid amount", JOptionPane.ERROR_MESSAGE);
				txtOutput.setText("0.00");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				txtOutput.setText("0.00");
			} finally {
				shouldBeAnimated = false;
				btnConvert.setEnabled(true);
			}
		});
	}

}
