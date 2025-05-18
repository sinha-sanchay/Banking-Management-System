import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);
             Connection con = Database.getConnection()) {

            if (con == null) {
                System.out.println("Exiting program due to DB connection failure.");
                return;
            }
            CustomerService service = new CustomerService(con, sc);
            while (true) {
                System.out.println("\n***** Banking Management System *****");
                System.out.println("1. Show Customer Records");
                System.out.println("2. Add Customer Record");
                System.out.println("3. Delete Cu1stomer Record");
                System.out.println("4. Update Customer Information");
                System.out.println("5. Show Account Details of a Customer");
                System.out.println("6. Show Loan Details of a Customer");
                System.out.println("7. Deposit Money to an Account");
                System.out.println("8. Withdraw Money from an Account");
                System.out.println("9. Exit");
                System.out.print("Enter your choice (1-9): ");

                String choice = sc.nextLine();
                switch (choice) {
                    case "1" -> service.showCustomers();
                    case "2" -> {
                        service.addCustomer();
                        service.showCustomers();
                    }
                    case "3" -> {
                        service.deleteCustomer();
                        service.showCustomers();
                    }
                    case "4" -> {
                        service.updateCustomer();
                        service.showCustomers();
                    }
                    case "5" -> service.showAccountDetails();
                    case "6" -> service.showLoanDetails();
                    case "7" -> service.depositMoney();
                    case "8" -> service.withdrawMoney();
                    case "9" -> {
                        System.out.println("Exiting program!!");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
