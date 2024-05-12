package com.alura.app.views;

import com.alura.app.models.Transaction;
import com.alura.app.services.HistoryTransactionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;

public class HistoryPanel extends JPanel {
	private final HistoryTransactionService historyTransactionService;
	private final DefaultTableModel tableModel;

	public HistoryPanel(HistoryTransactionService transactionService) {
		this.historyTransactionService = transactionService;
		this.tableModel = new DefaultTableModel(recoverData(), getColumnNames());
		JTable table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800, 300));
		JButton btnClear = new JButton("Borrar Historial");
		btnClear.addActionListener(this::onClickClear);
		this.add(scrollPane);
		this.add(btnClear);
	}

	public void addTransaction(Transaction transaction) throws IOException {
		historyTransactionService.save(transaction);
		this.tableModel.setDataVector(recoverData(), getColumnNames());
	}

	private void onClickClear(ActionEvent actionEvent) {
		try {
			this.tableModel.setRowCount(0);
			this.historyTransactionService.clearAll();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private Vector<Vector<String>> recoverData() {
		try {
			var transactions = historyTransactionService.getTransactions();
			var data = new Vector<Vector<String>>();
			transactions.forEach(current -> data.add(current.getRowData()));
			return data;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return new Vector<>();
		}
	}

	private Vector<String> getColumnNames() {
		var columnNames = new Vector<String>();
		columnNames.add("Fecha");
		columnNames.add("Monto");
		columnNames.add("Resultado de conversion");
		return columnNames;
	}
}
