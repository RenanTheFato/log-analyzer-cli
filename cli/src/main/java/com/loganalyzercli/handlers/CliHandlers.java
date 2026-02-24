package com.loganalyzercli.handlers;

import picocli.CommandLine;

public class CliHandlers {
  private final CommandLine cmd;

  public CliHandlers(CommandLine cmd) {
    this.cmd = cmd;
  }

  private void clearScreen() {
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
  }

  public boolean handle(String line) {
    if (line.equalsIgnoreCase("exit")) {
      System.out.println("");
      return false;
    }

    if (line.equalsIgnoreCase("help")) {
      cmd.usage(System.out);
      return true;
    }

    if (line.equalsIgnoreCase("clear") || line.equalsIgnoreCase("cls")) {
      clearScreen();
      return true;
    }

    String[] tokens = line.split("\\s+");
    cmd.execute(tokens);
    return true;
  }
}