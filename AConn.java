import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class AConn implements ActionListener {

    Connection connection;

    JButton Add, View, Search;
    JTextArea data;

    public AConn() {

        JFrame jf = new JFrame("Product Information");
        jf.setLayout(new FlowLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));

        Add = new JButton("Add Product");
        View = new JButton("View Product");
        Search = new JButton("Search Product");

        data = new JTextArea(15, 20);
        data.setEditable(false);

        JScrollPane scroll = new JScrollPane(data);

        buttonPanel.add(Add);
        buttonPanel.add(View);
        buttonPanel.add(Search);

        viewPanel.add(scroll);

        jf.add(buttonPanel);
        jf.add(viewPanel);

        Add.addActionListener(this);
        View.addActionListener(this);
        Search.addActionListener(this);

        jf.setSize(450, 400);
        jf.setVisible(true);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent AE) {

        // VIEW PRODUCT
        if (AE.getSource() == View) {

            try {

                String res = "";

                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                connection = DriverManager.getConnection(
                        "jdbc:ucanaccess://C:\\company.accdb");

                System.out.println("Connected Successfully");

                PreparedStatement preparedStatement =
                        connection.prepareStatement("select * from Product");

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    String PName = resultSet.getString("PName");
                    int Price = resultSet.getInt("PPrice");
                    int Quantity = resultSet.getInt("PQuantity");

                    res += "Name: " + PName +
                            " | Price: Rs." + Price +
                            " | Quantity: " + Quantity + "\n";
                }

                data.setText(res);

            } catch (Exception e) {
               
                System.out.println(e);
            }
        }

        // SEARCH PRODUCT
        else if (AE.getSource() == Search) {

            String pname = JOptionPane.showInputDialog(
                    null,
                    "Enter Product Name"
            );

            try {

                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

                connection = DriverManager.getConnection(
                        "jdbc:ucanaccess://C:\\company.accdb");

                PreparedStatement preparedStatement =
                        connection.prepareStatement(
                                "select * from Product where PName like ?"
                        );

                preparedStatement.setString(1, "%" + pname + "%");

                ResultSet resultSet = preparedStatement.executeQuery();

                String rs = "";

                while (resultSet.next()) {

                    String PName = resultSet.getString("PName");
                    int Price = resultSet.getInt("PPrice");
                    int Quantity = resultSet.getInt("PQuantity");

                    rs += "Name: " + PName +
                            " | Price: Rs." + Price +
                            " | Quantity: " + Quantity + "\n";
                }

                if (!rs.isBlank()) {
                    JOptionPane.showMessageDialog(null, rs);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "No Product Found"
                    );
                }

            } catch (Exception e) {
                System.out.println("Error in connection");
                e.printStackTrace();
            }
        }

        // ADD PRODUCT
        else if (AE.getSource() == Add) {

            new GuiShow();
        }
    }

    public static void main(String[] args) {

        new AConn();
    }
}