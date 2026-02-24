package com.loganalyzercli.commands;

import picocli.CommandLine.Command;

@Command(name = "hello")
public class HelloCommand implements Runnable {

  @Override
  public void run(){
    System.out.println("Hello World");
  }
}