package com.loganalyzercli.commands;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;

import com.loganalyzercli.utils.LogPropsTable;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "log", mixinStandardHelpOptions = true, version = "ALPHA 0.1.1")
public class LogCommand implements Callable<Integer> {

  @Option(names = { "-f", "--file" }, paramLabel = "FILE", description = "Path to the log file to be analyzed.", required = true)
  private File file;

  @Option(names = { "-l", "--level" }, paramLabel = "STRING", description = "Filters rows by log level.")
  private String level;

  @Option(names = { "-wl", "--wordlist" }, paramLabel = "LIST", split = ",", description = "Keywords to filter rows. Can be entered multiple times.")
  private List<String> wordList;

  static class DateOptions {
    @Option(names = { "-af", "--after" }, paramLabel = "DATE", description = "Filters log content from specific date", required = true)
    private String date;

    @Option(names = { "-df", "--dateformat" }, paramLabel = "FORMAT", description = "Date format in the log file. Ex: dd-MM-yyyy HH:mm:ss", required = true)
    private String dateFormat;
  }

  @ArgGroup(exclusive = false)
  DateOptions dateOptions;

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

    if (dateOptions != null) {
      try {
        DateTimeFormatter logDateValidate = DateTimeFormatter.ofPattern(dateOptions.dateFormat);
        LocalDate.parse(dateOptions.date, logDateValidate);
      } catch (Exception e) {
        System.err.printf("Error: date '%s' does not match format '%s'", dateOptions.date, dateOptions.dateFormat);
        return 1;
      }
    }

    System.out.println("Analyzing: " + file.getName());

    Path path = file.toPath();
    List<String> lines = Files.readAllLines(path);

    long lineCount = lines.size();
    long charCount = lines.stream().mapToLong(String::length).sum();

    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
    LocalDateTime lastModified = attrs.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    String formattedDate = lastModified.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

    new LogPropsTable(file, lineCount, charCount, formattedDate, level, wordList).show();

    List<String> filteredLines = lines.stream().filter(line -> {
      boolean matchesLevel = level == null || line.toUpperCase().contains(level.toUpperCase());
      boolean matchesWordList = wordList == null || wordList.stream().anyMatch(word -> line.toLowerCase().contains(word.toLowerCase()));
      boolean matchesDate = true;

      if (dateOptions != null) {
        try {
          DateTimeFormatter logDateFormat = DateTimeFormatter.ofPattern(dateOptions.dateFormat);
          LocalDate after = LocalDate.parse(dateOptions.date, logDateFormat);
          int dateLenght = dateOptions.dateFormat.length();
          boolean found = false;
          for (int i = 0; i < line.length() - dateLenght; i++) {
            try {
              String candidate = line.substring(i, i + dateLenght);
              LocalDate lineDate = LocalDate.parse(candidate, logDateFormat);
              matchesDate = !lineDate.isBefore(after);
              found = true;
              break;
            } catch (Exception e) {}
          }
          if (!found) {
            matchesDate = true;
          }
        } catch (Exception exception) {
          matchesDate = true;
        }
      }
      return matchesLevel && matchesDate && matchesWordList;
    }).toList();

    
    if (filteredLines.isEmpty()) {
      System.out.println("No lines matched the level: " + level);
      return 0;
    }

    filteredLines.forEach(System.out::println);

    try {
      Path outputDir = Paths.get(outputPath);
      Files.createDirectories(outputDir);
      String localDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));

      Path logAnalyzed = outputDir.resolve(String.format("log-analyzed%s.txt", localDateTime));
      Files.write(logAnalyzed, String.join("\n", filteredLines).getBytes(StandardCharsets.UTF_8));
      System.out.println("Text file with log analyzed created at: " + logAnalyzed.toAbsolutePath());
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
      exception.printStackTrace();
    }

    return 0;
  }

}
