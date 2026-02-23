package com.loganalyzercli.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
  name = "LogAnalyzer",
  subcommandsRepeatable = true,
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