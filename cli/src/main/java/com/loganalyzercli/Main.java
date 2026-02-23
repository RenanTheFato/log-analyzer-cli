package com.loganalyzercli;

import java.util.Scanner;

import com.loganalyzercli.commands.HelloCommand;
import com.loganalyzercli.commands.RootCommand;

import picocli.CommandLine;

public class Main {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		CommandLine cmd = new CommandLine(new RootCommand()).addSubcommand("hello", new HelloCommand());

		while (true) {
			String line = scanner.nextLine().trim();

			if (line.isEmpty()) {
				continue;
			}

			if (line.equalsIgnoreCase("exit")) {
				System.out.println("");
				break;
			}

			String[] tokens = line.split("\\s+");
			cmd.execute(tokens);

		}
		scanner.close();
	}
}