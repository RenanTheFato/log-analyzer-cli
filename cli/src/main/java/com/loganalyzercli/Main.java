package com.loganalyzercli;

import java.util.Scanner;

import com.loganalyzercli.commands.RootCommand;
import com.loganalyzercli.handlers.CliHandlers;

import picocli.CommandLine;

public class Main {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		CommandLine cmd = new CommandLine(new RootCommand());
		CliHandlers handlers = new CliHandlers(cmd);
		boolean firstRun = true;

		while (true) {
			if (firstRun) {
				handlers.handle("cls");
				firstRun = false;
			}
			System.out.print("\nloganalyzer@0.0.1>");
			String line = scanner.nextLine().trim();

			if (line.isEmpty()) {
				continue;
			}

			if (!handlers.handle(line)) {
				break;
			}
		}
		scanner.close();
	}
}