package sfs2x.client.examples;

import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter you username: ");
        String username = scanner.next();

        new SFS2XConnector(username);
    }
}
