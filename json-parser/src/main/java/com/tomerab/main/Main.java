package com.tomerab.main;

import com.tomerab.ast.JsonObject;
import com.tomerab.lexer.JsonLexer;
import com.tomerab.parser.JsonParser;
import com.tomerab.visitor.JsonPrettyPrintVisitor;
import com.tomerab.visitor.JsonVisitor;

public class Main {
  public static void main(String[] args) {

    String str = """
                         {
          "name": "John Doe",
          "age": 30,
          "address": {
            "street": "1234 Elm Street",
            "city": "Springfield",
            "state": "IL",
            "postalCode": "62701"
          },
          "phoneNumbers": [
            {
              "type": "home",
              "number": "555-555-5555"
            },
            {
              "type": "work",
              "number": "555-555-5556"
            }
          ],
          "emails": [
            "john.doe@example.com",
            "john.doe@workplace.com"
          ],
          "spouse": {
            "name": "Jane Doe",
            "age": 28,
            "address": {
              "street": "5678 Oak Avenue",
              "city": "Springfield",
              "state": "IL",
              "postalCode": "62702"
            }
          },
          "children": [
            {
              "name": "Alice Doe",
              "age": 5
            },
            {
              "name": "Bob Doe",
              "age": 3
            }
          ],
          "employment": {
            "company": "Tech Corp",
            "position": "Software Engineer",
            "yearsEmployed": 5,
            "projects": [
              {
                "name": "Project A",
                "status": "completed"
              },
              {
                "name": "Project B",
                "status": "in-progress"
              }
            ]
          },
          "hobbies": ["reading", "traveling", "swimming"],
          "isActive": true
        }

                        """;

    JsonLexer jsonLexer = new JsonLexer(str);
    JsonParser jsonParser = new JsonParser(jsonLexer);
    JsonObject obj = jsonParser.parse();

    JsonVisitor visitor = new JsonPrettyPrintVisitor();

    obj.accept(visitor);
    System.out.println();
  }
}