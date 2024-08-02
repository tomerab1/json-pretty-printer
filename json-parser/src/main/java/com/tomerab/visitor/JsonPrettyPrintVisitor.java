package com.tomerab.visitor;

import java.util.List;
import java.util.Map;

import com.tomerab.ast.JsonArray;
import com.tomerab.ast.JsonBoolean;
import com.tomerab.ast.JsonMap;
import com.tomerab.ast.JsonNull;
import com.tomerab.ast.JsonNumber;
import com.tomerab.ast.JsonObject;
import com.tomerab.ast.JsonString;

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
    public void visit(JsonNumber number) {
        double num = number.getValue();
        System.out.print(MAGENTA + num + RESET);
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
