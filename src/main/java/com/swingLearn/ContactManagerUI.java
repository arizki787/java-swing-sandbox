package com.swingLearn;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.sql.*;

public class ContactManagerUI extends JFrame {

	private final ContactDAO dao = new ContactDAO();
	private final DepartmentDAO departmentDao = new DepartmentDAO();
	private final DefaultTableModel tableModel = new DefaultTableModel(
			new Object[] { "ID", "First Name", "Last Name", "Phone", "Email", "Department" }, 0) {
		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	};

	private final JTable table = new JTable(tableModel);

	private final JTextField fnameField = new JTextField();
	private final JTextField lnameField = new JTextField();
	private final JTextField phoneField = new JTextField();
	private final JTextField emailField = new JTextField();
	private final JComboBox<Department> departmentBox = new JComboBox<>();

	private int selectedId = -1;

	public ContactManagerUI() {
		super("Employee Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 480);
		setLocationRelativeTo(null);
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		JMenu analytics = new JMenu("Analytics");
		menubar.add(analytics);

		JMenuItem viewChartItem = new JMenuItem("View Salary Chart");
		analytics.add(viewChartItem);

		add(new JScrollPane(table), BorderLayout.CENTER);
		add(buildFormPanel(), BorderLayout.SOUTH);

		table.getSelectionModel().addListSelectionListener(e -> {
			if (e.getValueIsAdjusting()) {
				return;
			}
			int row = table.getSelectedRow();
			if (row == -1) {
				return;
			}
			selectedId = (int) tableModel.getValueAt(row, 0);
			fnameField.setText((String) tableModel.getValueAt(row, 1));
			lnameField.setText((String) tableModel.getValueAt(row, 2));
			phoneField.setText((String) tableModel.getValueAt(row, 3));
			emailField.setText((String) tableModel.getValueAt(row, 4));
			selectDepartmentByName((String) tableModel.getValueAt(row, 5));
		});

		loadDepartments();
		refreshTable();
		
		viewChartItem.addActionListener(e -> {
			new trial().setVisible(true);
		});
	}

	private JPanel buildFormPanel() {
		JPanel form = new JPanel(new GridLayout(3, 4, 6, 6));
		form.add(new JLabel("First Name"));
		form.add(fnameField);
		form.add(new JLabel("Last Name"));
		form.add(lnameField);
		form.add(new JLabel("Phone"));
		form.add(phoneField);
		form.add(new JLabel("Email"));
		form.add(emailField);
		form.add(new JLabel("Department"));
		form.add(departmentBox);

		JButton addBtn = new JButton("Add");
		JButton updateBtn = new JButton("Update");
		JButton deleteBtn = new JButton("Delete");
		JButton clearBtn = new JButton("Clear");

		addBtn.addActionListener(e -> {
			try {
				Department dept = (Department) departmentBox.getSelectedItem();
				if (dept == null) {
					JOptionPane.showMessageDialog(this, "Select a department first.");
					return;
				}
				dao.insert(new Contact(
						fnameField.getText(),
						lnameField.getText(),
						emailField.getText(),
						phoneField.getText(),
						dept.getId()));
				refreshTable();
				clearForm();
			} catch (SQLException ex) {
				showError(ex);
			}
		});

		updateBtn.addActionListener(e -> {
			if (selectedId == -1) {
				JOptionPane.showMessageDialog(this, "Select a row to update first.");
				return;
			}
			try {
				Department dept = (Department) departmentBox.getSelectedItem();
				if (dept == null) {
					JOptionPane.showMessageDialog(this, "Select a department first.");
					return;
				}
				dao.update(new Contact(
						selectedId,
						fnameField.getText(),
						lnameField.getText(),
						emailField.getText(),
						phoneField.getText(),
						dept.getId()));
				refreshTable();
				clearForm();
			} catch (SQLException ex) {
				showError(ex);
			}
		});

		deleteBtn.addActionListener(e -> {
			if (selectedId == -1) {
				JOptionPane.showMessageDialog(this, "Select a row to delete first.");
				return;
			}
			try {
				dao.delete(selectedId);
				refreshTable();
				clearForm();
			} catch (SQLException ex) {
				showError(ex);
			}
		});

		clearBtn.addActionListener(e -> clearForm());

		JPanel buttons = new JPanel(new GridLayout(1, 4, 6, 6));
		buttons.add(addBtn);
		buttons.add(updateBtn);
		buttons.add(deleteBtn);
		buttons.add(clearBtn);

		JPanel wrapper = new JPanel(new BorderLayout(6, 6));
		wrapper.add(form, BorderLayout.CENTER);
		wrapper.add(buttons, BorderLayout.SOUTH);
		wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		return wrapper;
	}

	private void loadDepartments() {
		try {
			departmentBox.removeAllItems();
			for (Department dept : departmentDao.findAll()) {
				departmentBox.addItem(dept);
			}
		} catch (SQLException ex) {
			showError(ex);
		}
	}

	private void selectDepartmentByName(String departmentName) {
		if (departmentName == null) {
			return;
		}
		for (int i = 0; i < departmentBox.getItemCount(); i++) {
			Department dept = departmentBox.getItemAt(i);
			if (departmentName.equals(dept.getName())) {
				departmentBox.setSelectedIndex(i);
				return;
			}
		}
	}

	private void showError(SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
	}

	private void refreshTable() {
		try {
			List<Contact> contacts = dao.findAll();
			tableModel.setRowCount(0);
			for (Contact c : contacts) {
				tableModel.addRow(new Object[] {
						c.getId(),
						c.getFname(),
						c.getLname(),
						c.getPhone(),
						c.getEmail(),
						c.getDepartmentName()
				});
			}
		} catch (SQLException ex) {
			showError(ex);
		}
	}

	private void clearForm() {
		selectedId = -1;
		fnameField.setText("");
		lnameField.setText("");
		phoneField.setText("");
		emailField.setText("");
		if (departmentBox.getItemCount() > 0) {
			departmentBox.setSelectedIndex(0);
		}
		table.clearSelection();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ContactManagerUI().setVisible(true));
	}
}
