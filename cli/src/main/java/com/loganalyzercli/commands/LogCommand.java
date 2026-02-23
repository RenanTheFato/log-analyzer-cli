package com.loganalyzercli.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    Path path = file.toPath();
    List<String> lines = Files.readAllLines(path);

    long lineCount = lines.size();
    long charCount = lines.stream().mapToLong(String::length).sum();

    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
    LocalDateTime lastModified = attrs.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

    System.out.println("┌─────────────────────┬──────────────────────────────┐");
    System.out.printf("│ %-19s │ %-28s │%n", "File", file.getName());
    System.out.println("├─────────────────────┼──────────────────────────────┤");
    System.out.printf("│ %-19s │ %-28d │%n", "Lines", lineCount);
    System.out.printf("│ %-19s │ %-28d │%n", "Characters", charCount);
    System.out.printf("│ %-19s │ %-28s │%n", "Last Modified", formattedDate);
    System.out.println("└─────────────────────┴──────────────────────────────┘");

    return 0;
  }

}
