package com.tomerab.visitor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.tomerab.ast.JsonArray;
import com.tomerab.ast.JsonBoolean;
import com.tomerab.ast.JsonDecimal;
import com.tomerab.ast.JsonInteger;
import com.tomerab.ast.JsonMap;
import com.tomerab.ast.JsonNull;
import com.tomerab.ast.JsonObject;
import com.tomerab.ast.JsonString;

/**
 * The JsonPrettyPrintVisitor class is responsible for visiting different types
 * of JSON objects and printing them in a pretty format.
 * It implements the JsonVisitor interface.
 * 
 * The class provides methods to visit JSON maps, arrays, strings, booleans,
 * numbers, and null values.
 * When visiting a JSON map, the class prints the map in a formatted way, with
 * indentation and color coding.
 * When visiting a JSON array, the class prints the array in a formatted way,
 * with indentation.
 * When visiting a JSON string, the class prints the string in green color.
 * When visiting a JSON boolean, the class prints the boolean value in yellow
 * color.
 * When visiting a JSON number, the class prints the number in magenta color.
 * When visiting a JSON null value, the class prints "null" in yellow color.
 * 
 * The class also provides a constructor that accepts a spacing level parameter,
 * which determines the indentation level for the pretty printing.
 * 
 * 
 * <pre>
 * JsonPrettyPrintVisitor visitor = new JsonPrettyPrintVisitor(2);
 * JsonObject json = ...; // create or obtain a JSON object
 * json.accept(visitor); // print the JSON object in a pretty format
 * </pre>
 */
public class JsonPrettyPrintVisitor implements JsonVisitor {
    private static int indentLevel = 0;
    private static String INDENT = " ";

    // ANSI escape codes for colors
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[0;32m";
    private static final String CYAN = "\033[0;36m";
    private static final String YELLOW = "\033[0;33m";
    private static final String MAGENTA = "\033[0;35m";

    public JsonPrettyPrintVisitor(int spacingLvl) {
        INDENT = INDENT.repeat(spacingLvl);
    }

    @Override
    public void visit(JsonMap map) {
        Map<String, JsonObject> entries = map.getValue();

        System.out.print("{\n");
        indentLevel++;
        boolean first = true;
        for (Map.Entry<String, JsonObject> entry : entries.entrySet()) {
            if (!first) {
                System.out.print(",\n");
            }
            printIndent();
            System.out.print(CYAN + "\"" + entry.getKey() + "\":" + RESET + " ");
            entry.getValue().accept(this);
            first = false;
        }
        indentLevel--;
        System.out.print("\n");
        printIndent();
        System.out.print("}");
    }

    @Override
    public void visit(JsonArray array) {
        List<JsonObject> elements = array.getValue();

        System.out.print("[\n");
        indentLevel++;
        boolean first = true;
        for (JsonObject element : elements) {
            if (!first) {
                System.out.print(",\n");
            }
            printIndent();
            element.accept(this);
            first = false;
        }
        indentLevel--;
        System.out.print("\n");
        printIndent();
        System.out.print("]");
    }

    @Override
    public void visit(JsonString string) {
        String str = string.getValue();
        System.out.print(GREEN + "\"" + str + "\"" + RESET);
    }

    @Override
    public void visit(JsonBoolean bool) {
        boolean boolVal = bool.getValue();
        System.out.print(YELLOW + boolVal + RESET);
    }

    @Override
    public void visit(JsonDecimal decimal) {
        BigDecimal decimalVal = decimal.getValue();
        System.out.print(MAGENTA + decimalVal + RESET);
    }

    @Override
    public void visit(JsonInteger integer) {
        BigInteger integerVal = integer.getValue();
        System.out.print(MAGENTA + integerVal + RESET);
    }

    @Override
    public void visit(JsonNull jsonNull) {
        System.out.print(YELLOW + "null" + RESET);
    }

    private void printIndent() {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print(INDENT);
        }
    }
}
