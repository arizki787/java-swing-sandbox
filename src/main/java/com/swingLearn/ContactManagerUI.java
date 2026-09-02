package com.swingLearn;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.sql.*;

public class ContactManagerUI extends JFrame{
	
	private final ContactDAO dao = new ContactDAO();
	private final DefaultTableModel tableModel = 
			new DefaultTableModel(new Object[] {"ID", "Name", "Phone", "Email"}, 0) {
		
				@Override
				public boolean isCellEditable(int row, int col) {
					return false;
				}
	};
	
	private final JTable table = new JTable(tableModel);
	
	private final JTextField nameField = new JTextField();
	private final JTextField phoneField = new JTextField();
	private final JTextField emailField = new JTextField();
	
	private int selectedId = -1;
	
	public ContactManagerUI() {
		super("Contact Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 420);
		setLocationRelativeTo(null); //center on screen
		
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(buildFormPanel(), BorderLayout.SOUTH);
		
		table.getSelectionModel().addListSelectionListener(e -> {
			int row = table.getSelectedRow();
			if (row == -1) return;
			selectedId = (int) tableModel.getValueAt(row, 0);
            nameField.setText((String) tableModel.getValueAt(row, 1));
            phoneField.setText((String) tableModel.getValueAt(row, 2));
            emailField.setText((String) tableModel.getValueAt(row, 3));
		});
		
		refreshTable();
	}
	
	private JPanel buildFormPanel() {
		JPanel form = new JPanel(new GridLayout(2,4,6,6));
        form.add(new JLabel("Name"));
        form.add(nameField);
        form.add(new JLabel("Phone"));
        form.add(phoneField);
        form.add(new JLabel("Email"));
        form.add(emailField);
        

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
 
        addBtn.addActionListener(e -> {
            try {
                dao.insert(new Contact(nameField.getText(), phoneField.getText(), emailField.getText()));
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
                dao.update(new Contact(selectedId, nameField.getText(), phoneField.getText(), emailField.getText()));
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
	
    private void showError(SQLException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
    }
    
    
	private void refreshTable() {
        try {
            List<Contact> contacts = dao.findAll();
            tableModel.setRowCount(0); // wipe existing rows
            for (Contact c : contacts) {
                tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail()});
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }
 
    private void clearForm() {
        selectedId = -1;
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        table.clearSelection();
    }
			
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ContactManagerUI().setVisible(true));
	}

}
