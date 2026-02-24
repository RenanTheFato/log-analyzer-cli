package com.loganalyzercli.commands;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "log", mixinStandardHelpOptions = true, version = "ALPHA 0.0.1")
public class LogCommand implements Callable<Integer> {

  @Option(names = { "-f", "--file" }, paramLabel = "FILE", description = "Path to the log file to be analyzed.", required = true)
  private File file;

  @Option(names = { "-l", "--level" }, paramLabel = "STRING", description = "Filters rows by log level.")
  private String level;

  @Option(names = { "-wl", "--wordlist" }, paramLabel = "LIST", split = ",", description = "Keywords to filter rows. Can be entered multiple times.")
  private List<String> wordList;

  @Option(names = { "-af", "--after" }, paramLabel = "DATE", description = "Filters log content from specific date")
  private Date date;

  private String outputPath = "cli/logs";

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
    System.out.printf("│ %-19s │ %-28s │%n", "Log Level", level);
    System.out.printf("│ %-19s │ %-28s │%n", "Word List", wordList);
    System.out.println("└─────────────────────┴──────────────────────────────┘");

    if (level != null) {
      List<String> filteredLines = lines.stream().filter(line -> line.toUpperCase().contains(level.toUpperCase())).toList();
      System.out.println("\n─────────────────────── Level Filter: " + level + " ─────────────────────");
      System.out.println("Matched lines: " + filteredLines.size() + " of " + lineCount);
      System.out.println("────────────────────────────────────────────────────────────────");

      if (filteredLines.isEmpty()) {
        System.out.println("No lines matched the level: " + level);
      }

      try {
        Path outputDir = Paths.get(outputPath);
        Files.createDirectories(outputDir);
        String localDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));

        Path logAnalyzed = outputDir.resolve(String.format("log-analyzed%s.txt", localDateTime));
        Files.write(logAnalyzed, filteredLines.toString().getBytes(StandardCharsets.UTF_8));
        System.out.println("Text file with log analyzed created at: " + logAnalyzed.toAbsolutePath());
      } catch (Exception exception) {
        System.err.println(exception.getMessage());
        exception.printStackTrace();
      }

      filteredLines.forEach(System.out::println);
    }

    return 0;
  }

}
