import java.sql.*;
import java.util.Scanner;

public class CustomerService {
    private Connection con;
    private Scanner sc;

    public CustomerService(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void showCustomers() {
        String query = "SELECT * FROM customer";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Cust_No | Name           | Phone No    | City");
            System.out.println("------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-8s | %-14s | %-11s | %s\n",
                        rs.getString("cust_no"),
                        rs.getString("name"),
                        rs.getString("phoneno"),
                        rs.getString("city"));
            }
        } catch (SQLException e) {
            System.out.println("Error showing customers: " + e.getMessage());
        }
    }

    public void addCustomer() {
        System.out.print("Enter cust_no: ");
        String custNo = sc.nextLine();
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter phone no: ");
        String phone = sc.nextLine();
        System.out.print("Enter city: ");
        String city = sc.nextLine();

        String query = "INSERT INTO customer (cust_no, name, phoneno, city) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, custNo);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, city);

            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    public void deleteCustomer() {
        System.out.print("Enter cust_no to delete: ");
        String custNo = sc.nextLine();

        String query = "DELETE FROM customer WHERE cust_no = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, custNo);
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                System.out.println("Customer deleted successfully.");
            else
                System.out.println("Customer not found.");
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    public void updateCustomer() {
        System.out.print("Enter cust_no to update: ");
        String custNo = sc.nextLine();

        System.out.println("Update options:\n1. Name\n2. Phone no\n3. City");
        System.out.print("Enter your choice: ");
        String choice = sc.nextLine();

        String column = null;
        switch (choice) {
            case "1" -> column = "name";
            case "2" -> column = "phoneno";
            case "3" -> column = "city";
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        System.out.print("Enter new value: ");
        String newValue = sc.nextLine();

        String query = "UPDATE customer SET " + column + " = ? WHERE cust_no = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, custNo);
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                System.out.println("Customer updated successfully.");
            else
                System.out.println("Customer not found.");
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    public void showAccountDetails() {
        System.out.print("Enter cust_no to show account details: ");
        String custNo = sc.nextLine();

        String query = """
            SELECT c.cust_no, c.name, a.account_no, a.type, a.balance,
                   b.branch_code, b.branch_name, b.branch_city
            FROM customer c
            JOIN account a ON c.cust_no = a.cust_no
            JOIN branch b ON a.branch_code = b.branch_code
            WHERE c.cust_no = ?""";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, custNo);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Customer: " + rs.getString("name") + " (" + rs.getString("cust_no") + ")");
                System.out.println("Account No: " + rs.getString("account_no"));
                System.out.println("Type: " + rs.getString("type"));
                System.out.println("Balance: " + rs.getDouble("balance"));
                System.out.println("Branch Code: " + rs.getString("branch_code"));
                System.out.println("Branch Name: " + rs.getString("branch_name"));
                System.out.println("Branch City: " + rs.getString("branch_city"));
                System.out.println("------------------------------");
            }
            if (!found) {
                System.out.println("No account details found for this customer.");
            }
        } catch (SQLException e) {
            System.out.println("Error showing account details: " + e.getMessage());
        }
    }

    public void showLoanDetails() {
        System.out.print("Enter cust_no to show loan details: ");
        String custNo = sc.nextLine();

        String query = """
            SELECT c.cust_no, c.name, l.loan_no, l.amount,
                   b.branch_code, b.branch_name, b.branch_city
            FROM customer c
            LEFT JOIN loan l ON c.cust_no = l.cust_no
            LEFT JOIN branch b ON l.branch_code = b.branch_code
            WHERE c.cust_no = ?""";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, custNo);
            ResultSet rs = pstmt.executeQuery();

            boolean hasLoan = false;
            while (rs.next()) {
                if (rs.getString("loan_no") != null) {
                    hasLoan = true;
                    System.out.println("Customer: " + rs.getString("name") + " (" + rs.getString("cust_no") + ")");
                    System.out.println("Loan No: " + rs.getString("loan_no"));
                    System.out.println("Amount: " + rs.getDouble("amount"));
                    System.out.println("Branch Code: " + rs.getString("branch_code"));
                    System.out.println("Branch Name: " + rs.getString("branch_name"));
                    System.out.println("Branch City: " + rs.getString("branch_city"));
                    System.out.println("------------------------------");
                }
            }
            if (!hasLoan) {
                System.out.println("Congratulations! This customer has no loans.");
            }
        } catch (SQLException e) {
            System.out.println("Error showing loan details: " + e.getMessage());
        }
    }

    public void depositMoney() {
        System.out.print("Enter account_no to deposit: ");
        String accountNo = sc.nextLine();
        System.out.print("Enter amount to deposit: ");
        double amount;
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }

        try {
            con.setAutoCommit(false);

            String selectQuery = "SELECT balance FROM account WHERE account_no = ?";
            double balance;

            try (PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {
                selectStmt.setString(1, accountNo);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    balance = rs.getDouble("balance");
                } else {
                    System.out.println("Account not found.");
                    con.setAutoCommit(true);
                    return;
                }
            }

            double newBalance = balance + amount;
            String updateQuery = "UPDATE account SET balance = ? WHERE account_no = ?";
            try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                updateStmt.setDouble(1, newBalance);
                updateStmt.setString(2, accountNo);
                updateStmt.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            System.out.println("Deposit successful. New balance: " + newBalance);

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { }
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    public void withdrawMoney() {
        System.out.print("Enter account_no to withdraw: ");
        String accountNo = sc.nextLine();
        System.out.print("Enter amount to withdraw: ");
        double amount;
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }

        try {
            con.setAutoCommit(false);

            String selectQuery = "SELECT balance FROM account WHERE account_no = ?";
            double balance;

            try (PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {
                selectStmt.setString(1, accountNo);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    balance = rs.getDouble("balance");
                } else {
                    System.out.println("Account not found.");
                    con.setAutoCommit(true);
                    return;
                }
            }

            if (balance < amount) {
                System.out.println("Insufficient balance.");
                con.setAutoCommit(true);
                return;
            }

            double newBalance = balance - amount;
            String updateQuery = "UPDATE account SET balance = ? WHERE account_no = ?";
            try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                updateStmt.setDouble(1, newBalance);
                updateStmt.setString(2, accountNo);
                updateStmt.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            System.out.println("Withdrawal successful. New balance: " + newBalance);

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { }
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }
}
