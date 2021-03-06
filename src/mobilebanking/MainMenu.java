package mobilebanking;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {

    Scanner myScanner = new Scanner(System.in);
    private User user;

    public MainMenu(User user) {
        this.user = user;
        if (user.getAccounts().size() > 0) {
            displayMenu();
        } else
            newCustomer();
    }

    private void newCustomer() {
        System.out.println("Hello, welcome " + user.getForename());
        System.out.println("We see you do not currently have any accounts with us,");
        System.out.println("Would you like to open one now? (y/n)");
        String choice = myScanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            openAccount();
        } else {
            newCustomer();
        }
    }

    public void displayMenu() {
        System.out.println("Hello, welcome back " + user.getForename());
        System.out.println("1 - Open a new Account");
        System.out.println("2 - View your Accounts");
        System.out.println("3 - Close an Account");
        System.out.println("4 - Logout");
        System.out.println("Please select an option from the menu above");
        byte choice = Byte.parseByte(myScanner.nextLine());
        processChoice(choice);


//        public static void main {
//
//            try {
//                int arr[] = {1, 2, 3, 4, 5};
//                System.out.println(arr[7]);
//            }
//            catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("The specified index does not exist " + "in array. Please correct the error.");
//            }
//        }
    }



    private void processChoice(byte choice) {

        switch (choice) {

            case 1:
                openAccount();
                System.out.println("Open account selected.");
                break;

            case 2:
                viewAccount();
                System.out.println("View accounts selected.");
                break;

            case 3:
                closeAccount();
                System.out.println("Close account selected.");
                break;

            case 4:
//                logout();
                System.out.println("Logout option selected.");
                return;

            default:
                System.out.println("Invalid option selected please choose option between 1-4.");
                displayMenu();
        }
        displayMenu();
    }

//    private void logout() {
//        System.out.println("Hello, " + user.getForename());
//        System.out.println("Are you sure you want to logout? (y/n)");
//        String choice = myScanner.nextLine();
//        if (choice.equalsIgnoreCase("y")) {
//            newCustomer();
//        } else {
//            displayMenu();
//        }
//    }

    private void openAccount() {
        System.out.println("Thanks for choosing to open a new account with us!");
        System.out.println("Please select the type of account you with to open from the list below");
        System.out.println("1 - Current Account");
        System.out.println("2 - Savings Account");
        System.out.println("3 - ISA");
        System.out.println("4 - Credit Account");
        byte choice = Byte.parseByte(myScanner.nextLine());
        String accountType = null;

        switch (choice) {

            case 1:
                accountType = "Current Account";
                break;

            case 2:
                accountType = "Savings Account";
                break;

            case 3:
                accountType = "ISA";
                break;

            case 4:
                accountType = "Credit Account";
                break;

            default:
                System.out.println("Invalid option provided, please try again");
                openAccount();
        }
        if (accountType != null) {
            Account a = new Account(0004, 0.0, accountType, new ArrayList<>());
            user.addNewAccounts(a);
            displayMenu();
        }

    }

    private void closeAccount() {
        System.out.println("Hello, " + user.getForename());
        System.out.println("Are you sure you want to close your account? (y/n)");
        String choice = myScanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            System.out.println("WARNING: any money in this account will be lost");
            System.out.println("Would you like to continue? (y/n)");
            if (choice.equalsIgnoreCase("y")) {
            } viewAccount();
        } else {
            closeAccount();
        }
    }

    private void viewAccount() {
        if (user.getAccounts().size() > 1) {
            System.out.println("Please select the account you wish to access");
            int counter = 1;
            for (Account a : user.getAccounts()) {
                System.out.println(counter + " - " + a.getAccountType() + ", Account Number: " + a.getAccountNumber());
                counter++;
            }
            byte choice = Byte.parseByte(myScanner.nextLine());
            displayAccountActionsMenu(user.getAccounts().get(choice - 1));
        } else {
            displayAccountActionsMenu(user.getAccounts().get(0));
        }
    }

    private void displayAccountActionsMenu(Account account) {
        System.out.println("Select and option from the list below");
        System.out.println("1- View Account balance");
        System.out.println("2- View Statement");
        if (user.getAccounts().size() > 1) {
            System.out.println("3- Transfer Funds");
        }
        byte choice = Byte.parseByte(myScanner.nextLine());
        if (choice == 1) {
            viewAccountBalance(account);
        } else if (choice == 2) {
            viewAccountStatement(account);
        } else if (choice == 3 && user.getAccounts().size() > 1) {
            transferFunds(account);
        } else {
            System.out.println("Invalid option please try again");
            displayAccountActionsMenu(account);
        }
    }

    private void transferFunds(Account source) {
        System.out.println("Please select the account you'd like to transfer too");
        Account destination = null;
        if (user.getAccounts().size() > 2) {
            int counter = 1;
            for (Account a : user.getAccounts()) {
                if (a != source) {
                    System.out.println(counter + "- " + a.getAccountType() + ", Account number:" + a.getAccountNumber());
                    counter++;
                }
            }
            byte choice = Byte.parseByte(myScanner.nextLine());
            if ((choice - 1) >= user.getAccounts().indexOf(source)) {
                destination = user.getAccounts().get(choice);
            } else {
                destination = user.getAccounts().get(choice - 1);
            }
        } else if (user.getAccounts().size() == 2) {
            if (user.getAccounts().get(0).getAccountNumber() == source.getAccountNumber())
                destination = user.getAccounts().get(1);
        } else {
            destination = user.getAccounts().get(0);
        }
        System.out.println("How much would you like to transfer?");
        double transferAmount = Double.parseDouble(myScanner.nextLine());
        if (source.getAccountBalance() < transferAmount) {
            System.out.println("You do not have the funds to cover this transfer");
        } else {
            source.setAccountBalance(source.getAccountBalance() - transferAmount);
            destination.setAccountBalance(destination.getAccountBalance() + transferAmount);
            source.addTransaction(new Transactions("23/08/2020", Integer.toString(destination.getAccountNumber()), transferAmount * -1));
            destination.addTransaction(new Transactions("23/08/2020", Integer.toString(destination.getAccountNumber()), transferAmount));
            System.out.println("Congratulations your transfer was successful");
        }
    }

//        System.out.println("Transfer amount");
//        double amount = Double.parseDouble(myScanner.nextLine());
//        System.out.println("Date");
//        String date = myScanner.nextLine();
//        System.out.println("Which account would you like to transfer to?");
//        int counter = 1;
//        for(Account a : user.getAccounts()) {
//            System.out.println(counter + " - " + a.getAccountType() + ". Account Number: " + a.getAccountNumber());
//            counter++;
//        }
//        byte choice = Byte.parseByte(myScanner.nextLine());
//        Account destination = user.getAccounts().get(choice -1);
//        if (account.getAccountBalance() - amount > 0) {
//            account.setAccountBalance(account.getAccountBalance() - amount);
//            destination.setAccountBalance(destination.getAccountBalance() + amount);
//            Transactions retrieve = new Transactions(date, Integer.toString(destination.getAccountNumber()), amount*-1);
//            Transactions sent = new Transactions(date, Integer.toString(account.getAccountNumber()), amount);
//            destination.addTransaction(sent);
//            account.addTransaction(sent);
//            System.out.println("Transaction Successful");
//;        }
//        else System.out.println("Not enough funds, please try again");
//}

    private void viewAccountStatement(Account account) {
        System.out.println("Statement for account " + account.getAccountNumber());
        for(Transactions t : account.getTransactions()){
            System.out.println(t.getTransactionDate() + "t" + t.getTransactionPerson() + "t" + t.getTransactionAmount());
        }
    }

    private void viewAccountBalance(Account account) {
        System.out.println("Account balance: £" + account.getAccountBalance());
    }

}