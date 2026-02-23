package com.loganalyzercli.commands;

import java.io.File;
import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "log", mixinStandardHelpOptions = true)
public class LogCommand implements Callable<Integer> {

  @Option(names = { "-f", "--file" }, paramLabel = "FILE", description = "Path where log file is", required = true)
  private File file;

  @Override
  public Integer call() throws Exception {
    if (!file.exists()) {
      System.err.println("File not found" + file.getAbsolutePath());
      return 1;
    }

    if (!file.isFile()) {
      System.err.println("Path isn't a file " + file.getAbsolutePath());
      return 1;
    }

    System.out.println("Analyzing: " + file.getName());

    return 0;
  }

}
