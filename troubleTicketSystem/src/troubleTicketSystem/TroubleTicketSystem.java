/*
 * Program for trouble ticketing system.
 * Trouble ticket (sometimes called a trouble report) is a mechanism used in an organization to create, track, report, and resolute technical and operational issues.
 * JFrame JAVA Swing.
 * File name : TroubleTicketSystem.java
 */

package troubleTicketSystem;

/**
 * @author Abhijeet
 */

// package imports for the program
import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import net.proteanit.sql.DbUtils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TroubleTicketSystem {

	// declare and initialize the global variables
	private JFrame frame;
	private JTextField textFieldId;
	private JTextField textFieldName;
	private JTextField textFieldEmail;
	private JTextField textFieldIssue;
	private JTextField textFieldStatus;
	private JTable table;
	private JTextField textFieldSearch;
	private JComboBox<String> comboBoxSearch;

	// sqlite connection
	Connection connection = null;

	/**
	 * Launch the application. Main method.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TroubleTicketSystem window = new TroubleTicketSystem();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application. Construct method.
	 */
	public TroubleTicketSystem() {
		// initialize application method
		initialize();
		// creating the sqlite connection
		connection = SqliteConnection.dbConnector();
		// refreshing table method
		refreshTable();
		// setting fields to empty method
		fieldsEmpty();
	}

	/**
	 * Setting values to empty after every use.
	 */
	public void fieldsEmpty() {
		textFieldId.setText("");
		textFieldName.setText("");
		textFieldEmail.setText("");
		textFieldIssue.setText("");
		textFieldStatus.setText("");
	}

	/**
	 * Refreshing table after every users action.
	 */
	public void refreshTable() {
		try {
			String query = "select * from TTSdb";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			ps.close();
			rs.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 798, 476);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// drop down combo box for selecting the search index key
		comboBoxSearch = new JComboBox<>();
		comboBoxSearch.setModel(new DefaultComboBoxModel<>(new String[] { "Id", "Name", "Email", "Issue", "Status" }));
		comboBoxSearch.setBounds(10, 17, 165, 35);
		frame.getContentPane().add(comboBoxSearch);

		// search field
		textFieldSearch = new JTextField();
		textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					String search = (String) comboBoxSearch.getSelectedItem();
					String query = "select * from TTSdb where " + search + " = ?";
					PreparedStatement ps = connection.prepareStatement(query);
					ps.setString(1, textFieldSearch.getText());
					ResultSet rs = ps.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					ps.close();
					rs.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
		textFieldSearch.setBounds(185, 18, 270, 34);
		textFieldSearch.setFont(new Font("Tahoma", Font.BOLD, 12));
		frame.getContentPane().add(textFieldSearch);
		textFieldSearch.setColumns(10);

		// outer panel to scroll the table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 63, 612, 261);
		frame.getContentPane().add(scrollPane);

		// table
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					int row = table.getSelectedRow();
					String selected = (table.getModel().getValueAt(row, 0)).toString();
					String query = "select * from TTSdb where id = '" + selected + "'";
					PreparedStatement ps = connection.prepareStatement(query);
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						textFieldId.setText(rs.getString("Id"));
						textFieldName.setText(rs.getString("Name"));
						textFieldEmail.setText(rs.getString("Email"));
						textFieldIssue.setText(rs.getString("Issue"));
						textFieldStatus.setText(rs.getString("Status"));
					}
					ps.close();
					rs.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
		scrollPane.setViewportView(table);

		// the action buttons header
		JLabel lblActionHeaders = new JLabel("Actions");
		lblActionHeaders.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblActionHeaders.setBounds(682, 17, 69, 35);
		frame.getContentPane().add(lblActionHeaders);

		// load table action button
		// this will refresh the table and load the table
		JButton btnLoad = new JButton("Load Table");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String query = "select * from TTSdb";
					PreparedStatement ps = connection.prepareStatement(query);
					ResultSet rs = ps.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					JOptionPane.showMessageDialog(null, "Table loaded ! ! !");
					ps.close();
					rs.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
				fieldsEmpty();
			}
		});
		btnLoad.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLoad.setBounds(635, 63, 137, 35);
		frame.getContentPane().add(btnLoad);

		// view ticket action button
		JButton btnView = new JButton("View ticket");
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int row = table.getSelectedRow();
					String selected = (table.getModel().getValueAt(row, 0)).toString();
					String query = "select * from TTSdb where id = '" + selected + "'";
					PreparedStatement ps = connection.prepareStatement(query);
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						if (textFieldId.getText().equals("") || textFieldName.getText().equals("")
								|| textFieldEmail.getText().equals("") || textFieldIssue.getText().equals("")
								|| textFieldStatus.getText().equals("")) {
							JOptionPane.showMessageDialog(null,
									"No ticket selected ?" + "\n" + "Select a ticket from the table ! ! !");
							return;
						} else {
							JOptionPane.showMessageDialog(null,
									"Id:            " + textFieldId.getText() + "\n" + "Name:    "
											+ textFieldName.getText() + "\n" + "Email:    " + textFieldEmail.getText()
											+ "\n" + "Issue:    " + textFieldIssue.getText() + "\n" + "Status:  "
											+ textFieldStatus.getText(),
									"View Ticket", JOptionPane.CLOSED_OPTION);
						}
						ps.close();
						rs.close();
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"No ticket selected ?" + "\n" + "Select a ticket from the table ! ! !");
				}
				fieldsEmpty();
			}
		});
		btnView.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnView.setBounds(635, 118, 137, 35);
		frame.getContentPane().add(btnView);

		// save ticket action button
		JButton btnSave = new JButton("Save ticket");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String query = "insert into TTSdb (id, name, email, issue, status) values (?, ?, ?, ?, ?)";
					PreparedStatement ps = connection.prepareStatement(query);
					ps.setString(1, textFieldId.getText());
					ps.setString(2, textFieldName.getText());
					ps.setString(3, textFieldEmail.getText());
					ps.setString(4, textFieldIssue.getText());
					ps.setString(5, textFieldStatus.getText());
					if (textFieldId.getText().equals("") || textFieldName.getText().equals("")
							|| textFieldEmail.getText().equals("") || textFieldIssue.getText().equals("")
							|| textFieldStatus.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "All Fields required");
						return;
					}
					ps.execute();
					JOptionPane.showMessageDialog(null,
							"Ticket saved" + "\n" + "New ticket generated with Id: " + textFieldId.getText());
					ps.close();
				} catch (Exception e) {
					boolean sameId = Boolean.parseBoolean(textFieldId.getText());
					if (sameId == false) {
						JOptionPane.showMessageDialog(null, "Check Id" + "\n" + "Duplicate or bad data");
						return;
					}
					JOptionPane.showMessageDialog(null, e);
				}
				refreshTable();
				fieldsEmpty();
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSave.setBounds(635, 175, 137, 35);
		frame.getContentPane().add(btnSave);

		// update ticket action button
		JButton btnUpdate = new JButton("Update ticket");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String query = "update TTSdb set id = '" + textFieldId.getText() + "', name = '"
							+ textFieldName.getText() + "', email = '" + textFieldEmail.getText() + "', issue = '"
							+ textFieldIssue.getText() + "', status = '" + textFieldStatus.getText() + "' where id = '"
							+ textFieldId.getText() + "' ";
					PreparedStatement ps = connection.prepareStatement(query);
					if (textFieldId.getText().equals("") || textFieldName.getText().equals("")
							|| textFieldEmail.getText().equals("") || textFieldIssue.getText().equals("")
							|| textFieldStatus.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"No ticket selected ?" + "\n" + "Select a ticket from the table ! ! !");
						return;
					}
					ps.execute();
					JOptionPane.showMessageDialog(null, "Ticket updated");
					ps.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
				refreshTable();
				fieldsEmpty();
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnUpdate.setBounds(635, 232, 137, 35);
		frame.getContentPane().add(btnUpdate);

		// delete ticket action button
		JButton btnDelete = new JButton("Delete ticket");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textFieldId.getText().equals("") || textFieldName.getText().equals("")
						|| textFieldEmail.getText().equals("") || textFieldIssue.getText().equals("")
						|| textFieldStatus.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"No ticket selected ?" + "\n" + "Select a ticket from the table ! ! !");
					return;
				}
				int action = JOptionPane.showConfirmDialog(null,
						"Do you really want to delete the ticket with Id: " + textFieldId.getText() + " ", "Delete",
						JOptionPane.YES_NO_OPTION);
				if (action == 0) {
					try {
						String query = "delete from TTSdb where id = '" + textFieldId.getText() + "'";
						PreparedStatement ps = connection.prepareStatement(query);
						ps.execute();
						JOptionPane.showMessageDialog(null, "Ticket deleted");
						ps.close();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e);
					}
					refreshTable();
					fieldsEmpty();
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDelete.setBounds(635, 289, 137, 35);
		frame.getContentPane().add(btnDelete);

		// id label
		JLabel lblId = new JLabel("Id:");
		lblId.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblId.setBounds(31, 348, 76, 28);
		frame.getContentPane().add(lblId);

		// id field
		textFieldId = new JTextField();
		textFieldId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldId.setBounds(59, 350, 116, 28);
		frame.getContentPane().add(textFieldId);
		textFieldId.setColumns(10);

		// name label
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblName.setBounds(185, 348, 76, 28);
		frame.getContentPane().add(lblName);

		// name field
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldName.setBounds(239, 350, 216, 28);
		frame.getContentPane().add(textFieldName);
		textFieldName.setColumns(10);

		// email label
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmail.setBounds(475, 348, 76, 28);
		frame.getContentPane().add(lblEmail);

		// email field
		textFieldEmail = new JTextField();
		textFieldEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldEmail.setBounds(522, 350, 216, 28);
		frame.getContentPane().add(textFieldEmail);
		textFieldEmail.setColumns(10);

		// issue label
		JLabel lblIssue = new JLabel("Issue:");
		lblIssue.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIssue.setBounds(10, 387, 76, 28);
		frame.getContentPane().add(lblIssue);

		// issue field
		textFieldIssue = new JTextField();
		textFieldIssue.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldIssue.setBounds(59, 387, 385, 28);
		frame.getContentPane().add(textFieldIssue);
		textFieldIssue.setColumns(10);

		// status label
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStatus.setBounds(464, 387, 76, 28);
		frame.getContentPane().add(lblStatus);

		// status field
		textFieldStatus = new JTextField();
		textFieldStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFieldStatus.setBounds(522, 389, 216, 28);
		frame.getContentPane().add(textFieldStatus);
		textFieldStatus.setColumns(10);

	}
}