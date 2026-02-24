package com.loganalyzercli.utils;

import java.io.File;
import java.util.List;

public class LogPropsTable {

  private File file;
  private long lineCount;
  private long charCount;
  private String formattedDate;
  private String level;
  private List<String> wordList;

  public LogPropsTable(File file, long lineCount, long charCount, String formattedDate, String level, List<String> wordList) {
    this.file = file;
    this.lineCount = lineCount;
    this.charCount = charCount;
    this.formattedDate = formattedDate;
    this.level = level;
    this.wordList = wordList;
  }

  public void show() {
    System.out.println("┌─────────────────────┬──────────────────────────────┐");
    System.out.printf("│ %-19s │ %-28s │%n", "File", file.getName());
    System.out.println("├─────────────────────┼──────────────────────────────┤");
    System.out.printf("│ %-19s │ %-28d │%n", "Lines", lineCount);
    System.out.printf("│ %-19s │ %-28d │%n", "Characters", charCount);
    System.out.printf("│ %-19s │ %-28s │%n", "Last Modified", formattedDate);
    System.out.printf("│ %-19s │ %-28s │%n", "Log Level", level);
    System.out.printf("│ %-19s │ %-28s │%n", "Word List", wordList);
    System.out.println("└─────────────────────┴──────────────────────────────┘");
  }
}
