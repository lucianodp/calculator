import parser.Expression;
import parser.Parser;
import scanner.Scanner;
import scanner.Token;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the calculator!");
        System.out.println("Enter you expressions below to be evaluated. Press Ctrl + C to quit.");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (;;) {
            System.out.print(">>> ");
            try {
                Scanner scanner = new Scanner(reader.readLine());

                List<Token> scannedTokens = scanner.scanTokens();

//                for (Token token : scannedTokens)
//                    System.out.println(token);

                Expression expr = new Parser(scannedTokens).parse();
//                System.out.println("parsed expr: " + expr);
                System.out.println("Result: " + expr.evaluate());
            } catch (Exception ex) {
                System.out.println(ex.toString().split(":", 2)[1].trim());
            }
        }
    }
}
