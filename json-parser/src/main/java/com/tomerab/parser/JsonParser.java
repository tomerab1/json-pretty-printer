package com.tomerab.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tomerab.ast.JsonArray;
import com.tomerab.ast.JsonBoolean;
import com.tomerab.ast.JsonMap;
import com.tomerab.ast.JsonNull;
import com.tomerab.ast.JsonNumber;
import com.tomerab.ast.JsonObject;
import com.tomerab.ast.JsonString;
import com.tomerab.exceptions.JsonSyntaxError;
import com.tomerab.lexer.JsonLexer;
import com.tomerab.lexer.JsonToken;
import com.tomerab.lexer.JsonToken.JsonType;

public class JsonParser {
    private JsonLexer lexer;

    public JsonParser(JsonLexer lexer) {
        this.lexer = lexer;
    }

    public JsonObject parse() {
        if (!lexer.hasNext()) {
            throw new JsonSyntaxError("Unexpected end of input");
        }

        JsonToken token = lexer.next();

        switch (token.getType()) {
            case OBJ_OPEN:
                return parseObject(new LinkedHashMap<>());
            case ARR_OPEN:
                return parseArray(new LinkedList<>());
            default:
                throw new JsonSyntaxError("Expected object or array at the beginning");
        }
    }

    private JsonObject parseObject(Map<String, JsonObject> map) {
        boolean shouldExitLoop = false;
        Set<JsonType> expectedTypes = new HashSet<>();

        while (!shouldExitLoop && lexer.hasNext()) {
            JsonToken token = lexer.next();

            if (!expectedTypes.isEmpty() && !expectedTypes.contains(token.getType())) {
                throw new JsonSyntaxError(
                        "Expected ',' or '}' after property value in object, at: " + lexer.getCursor());
            }

            switch (token.getType()) {
                case OBJ_CLOSE:
                    shouldExitLoop = true;
                    break;
                case STRING:
                    String key = token.getString();

                    if (!isNextMatching(JsonType.COLON)) {
                        throw new JsonSyntaxError("Expected ':' after property name, at: " + lexer.getCursor());
                    }

                    addExpectedTypesObject(expectedTypes);
                    JsonObject value = parseValue();
                    map.put(key, value);

                    break;
                case COMMA:
                    expectedTypes.clear();
                    break;
                default:
                    throw new JsonSyntaxError("Unexpected token in object, at: " + lexer.getCursor());
            }
        }

        if (!shouldExitLoop) {
            throw new JsonSyntaxError("Unterminated object");
        }

        return new JsonMap(map);
    }

    private JsonObject parseValue() {
        if (!lexer.hasNext()) {
            throw new JsonSyntaxError("Unexpected end of input while parsing value");
        }

        JsonToken token = lexer.next();

        switch (token.getType()) {
            case OBJ_OPEN:
                return parseObject(new LinkedHashMap<>());
            case ARR_OPEN:
                return parseArray(new ArrayList<>());
            case STRING:
                return new JsonString(token.getString());
            case NUMBER:
                return new JsonNumber(token.getNum());
            case BOOLEAN:
                return new JsonBoolean(token.getBool());
            case NULL:
                return new JsonNull();
            default:
                throw new JsonSyntaxError("Unexpected token while parsing value, at: " + lexer.getCursor());
        }
    }

    private boolean isNextMatching(JsonType type) {
        if (!lexer.hasNext()) {
            throw new JsonSyntaxError("Unexpected end of input when expecting token: " + type);
        }

        return lexer.next().getType() == type;
    }

    private JsonObject parseArray(List<JsonObject> arr) {
        boolean shouldExitLoop = false;
        Set<JsonType> expectedTypes = new HashSet<>();

        while (!shouldExitLoop && lexer.hasNext()) {
            JsonToken token = lexer.next();

            if (!expectedTypes.isEmpty() && !expectedTypes.contains(token.getType())) {
                throw new JsonSyntaxError("Expected ',' or ']' after array element, at: " + lexer.getCursor());
            }

            switch (token.getType()) {
                case OBJ_OPEN:
                    arr.add(parseObject(new LinkedHashMap<>()));
                    break;
                case ARR_OPEN:
                    arr.add(parseArray(new LinkedList<>()));
                    break;
                case ARR_CLOSE:
                    shouldExitLoop = true;
                    break;
                case STRING:
                    addExpectedTypesArray(expectedTypes);
                    arr.add(new JsonString(token.getString()));
                    break;
                case NUMBER:
                    addExpectedTypesArray(expectedTypes);
                    arr.add(new JsonNumber(token.getNum()));
                    break;
                case BOOLEAN:
                    addExpectedTypesArray(expectedTypes);
                    arr.add(new JsonBoolean(token.getBool()));
                    break;
                case NULL:
                    addExpectedTypesArray(expectedTypes);
                    arr.add(new JsonNull());
                    break;
                case COMMA:
                    expectedTypes.clear();
                    break;
                default:
                    throw new JsonSyntaxError("Unexpected token in array, at: " + lexer.getCursor());
            }
        }

        if (!shouldExitLoop) {
            throw new JsonSyntaxError("Unterminated array");
        }

        return new JsonArray(arr);
    }

    private void addExpectedTypesArray(Set<JsonType> set) {
        set.add(JsonType.COMMA);
        set.add(JsonType.ARR_CLOSE);
    }

    private void addExpectedTypesObject(Set<JsonType> set) {
        set.add(JsonType.COMMA);
        set.add(JsonType.OBJ_CLOSE);
    }
}
