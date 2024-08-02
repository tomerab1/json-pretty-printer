package com.tomerab.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.tomerab.ast.JsonArray;
import com.tomerab.ast.JsonBoolean;
import com.tomerab.ast.JsonMap;
import com.tomerab.ast.JsonNull;
import com.tomerab.ast.JsonNumber;
import com.tomerab.ast.JsonObject;
import com.tomerab.ast.JsonString;
import com.tomerab.lexer.JsonLexer;
import com.tomerab.lexer.JsonToken;
import com.tomerab.lexer.JsonToken.JsonType;

public class JsonParser {
    private JsonLexer lexer;
    // For checking braces balancing.
    private Stack<Character> stack;

    public JsonParser(JsonLexer lexer) {
        this.lexer = lexer;
        stack = new Stack<>();
    }

    public JsonObject parse() {
        while (lexer.hasNext()) {
            JsonToken token = lexer.next();

            switch (token.getType()) {
                case OBJ_OPEN -> {
                    stack.push('{');
                    return parseObject(new LinkedHashMap<>());
                }
                case ARR_OPEN -> {
                    stack.push('[');
                    return parseArray(new LinkedList<>());
                }
                default -> {
                    System.out.println("Error");
                }
            }
        }

        return null;
    }

    private JsonObject parseObject(Map<String, JsonObject> map) {
        boolean shouldExitLoop = false;

        while (!shouldExitLoop && lexer.hasNext()) {
            JsonToken token = lexer.next();

            switch (token.getType()) {
                case OBJ_CLOSE -> {
                    shouldExitLoop = true;
                }
                case STRING -> {
                    String key = token.getString();

                    if (!isNextMatching(JsonType.COLON)) {
                        System.out.println("Error: Expected a ':'");
                    }

                    JsonObject value = parseValue();
                    map.put(key, value);
                }
                default -> {
                    System.out.println("Error");
                }
            }
        }

        return new JsonMap(map);
    }

    private JsonObject parseValue() {
        while (lexer.hasNext()) {
            JsonToken token = lexer.next();

            switch (token.getType()) {
                case OBJ_OPEN -> {
                    return parseObject(new LinkedHashMap<>());
                }
                case ARR_OPEN -> {
                    return parseArray(new ArrayList<>());
                }
                case STRING -> {
                    return new JsonString(token.getString());
                }
                case NUMBER -> {
                    return new JsonNumber(token.getNum());
                }
                case BOOLEAN -> {
                    return new JsonBoolean(token.getBool());
                }
                case NULL -> {
                    return new JsonNull();
                }
                default -> {
                    System.out.println("Error");
                }
            }
        }

        return null;
    }

    private boolean isNextMatching(JsonType type) {
        if (lexer.hasNext()) {
            return lexer.next().getType() == type;
        }

        return false;
    }

    private JsonObject parseArray(List<JsonObject> arr) {
        boolean shouldExitLoop = false;

        while (!shouldExitLoop && lexer.hasNext()) {
            JsonToken token = lexer.next();

            switch (token.getType()) {
                case OBJ_OPEN -> {
                    arr.add(parseObject(new LinkedHashMap<>()));
                }
                case ARR_OPEN -> {
                    arr.add(parseArray(new LinkedList<>()));
                }
                case ARR_CLOSE -> {
                    shouldExitLoop = true;
                }
                case STRING -> {
                    arr.add(new JsonString(token.getString()));
                }
                case NUMBER -> {
                    arr.add(new JsonNumber(token.getNum()));
                }
                case BOOLEAN -> {
                    arr.add(new JsonBoolean(token.getBool()));
                }
                case NULL -> {
                    arr.add(new JsonNull());
                }
                default -> {
                    System.out.println("Error");
                }
            }
        }

        return new JsonArray(arr);
    }
}
