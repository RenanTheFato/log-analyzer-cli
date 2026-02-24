package com.loganalyzercli.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
  name = "LogAnalyzer",
  subcommandsRepeatable = true,
  subcommands = {
    LogCommand.class,
    HelloCommand.class
  },
  synopsisHeading = "",
  commandListHeading = "Commands Available: %n",
  optionListHeading = "Options: %n"
)

public class RootCommand implements Runnable {
  @Override
  public void run(){
    new CommandLine(this).usage(System.out);
  }
}