package com.tomerab.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.tomerab.ast.JsonObject;
import com.tomerab.exceptions.JsonSyntaxErrorException;
import com.tomerab.lexer.JsonLexer;
import com.tomerab.parser.JsonParser;
import com.tomerab.visitor.JsonPrettyPrintVisitor;
import com.tomerab.visitor.JsonVisitor;

import java.io.IOException;

public class Main {
  // Todo(tomer): Add more tests, also refine the cli interface, maybe add a
  // Todo(tomer): visitor for outputing the
  // Todo(tomer): result to a file... Also print usage etc.
  // Todo(tomer): Add support for Nan.
  // Todo(tomer): Add more descriptive error messages for unterminated strings
  // Todo(tomer): etc.

  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      StringBuilder inputBuilder = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        inputBuilder.append(line).append("\n");
      }

      String jsonInput = inputBuilder.toString();

      JsonLexer jsonLexer = new JsonLexer(jsonInput);
      JsonParser jsonParser = new JsonParser(jsonLexer);
      JsonObject obj = jsonParser.parse();
      JsonVisitor visitor = new JsonPrettyPrintVisitor(4);

      obj.accept(visitor);

      System.out.println();
    } catch (IllegalArgumentException e) {
      System.out.println("IllegalArguemntException: " + e.getMessage());
    } catch (JsonSyntaxErrorException e) {
      System.out.println("JsonSyntaxError: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
