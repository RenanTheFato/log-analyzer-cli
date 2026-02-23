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

			if (line.equalsIgnoreCase("help")) {
				cmd.usage(System.out);
				continue;
			}

			if (line.equalsIgnoreCase("clear") || line.equalsIgnoreCase("cls")) {
				try {
					new ProcessBuilder(System.getProperty("os.name").contains("Windows")
							? new String[] { "cmd", "/c", "cls" }
							: new String[] { "clear" })
							.inheritIO()
							.start()
							.waitFor();
				} catch (Exception e) {
					System.out.print("\033[H\033[2J");
					System.out.flush();
				}
				continue;
			}

			String[] tokens = line.split("\\s+");
			cmd.execute(tokens);

		}
		scanner.close();
	}
}