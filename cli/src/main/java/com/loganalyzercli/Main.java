package com.loganalyzercli;

import java.util.Scanner;

import com.loganalyzercli.commands.RootCommand;

import picocli.CommandLine;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		CommandLine cmd = new CommandLine(new RootCommand());
	}
}