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

/**
 * The JsonParser class is responsible for parsing JSON strings and converting
 * them into JsonObject representations.
 * It uses a JsonLexer to tokenize the input string and then recursively parses
 * the tokens to build the JsonObject.
 * The parser supports parsing JSON objects and arrays, as well as string,
 * number, boolean, and null values.
 * 
 * Example usage:
 * 
 * <pre>
 * JsonLexer lexer = new JsonLexer(jsonString);
 * JsonParser parser = new JsonParser(lexer);
 * JsonObject jsonObject = parser.parse();
 * </pre>
 * 
 * @param lexer The JsonLexer used to tokenize the input JSON string.
 */
public class JsonParser {
    private JsonLexer lexer;

    public JsonParser(JsonLexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Parses the JSON input and returns a JsonObject.
     * 
     * @return The parsed JsonObject.
     * @throws JsonSyntaxError if there is an unexpected end of input or if the
     *                         input does not start with an object or array.
     */
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

    /**
     * Parses a JSON object from the input stream.
     *
     * @param map The map to store the parsed key-value pairs of the JSON object.
     * @return The parsed JSON object.
     * @throws JsonSyntaxError If there is a syntax error in the JSON object.
     */
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

    /**
     * Parses a JSON value from the input stream.
     *
     * @return The parsed JSON value.
     * @throws JsonSyntaxError If an unexpected end of input or token is encountered
     *                         while parsing the value.
     */
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

    /**
     * Parses an array of JSON objects.
     *
     * @param arr The list of JSON objects to parse.
     * @return The parsed JSON array.
     * @throws JsonSyntaxError If there is a syntax error in the JSON array.
     */
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
                    addExpectedTypesArray(expectedTypes);
                    arr.add(parseObject(new LinkedHashMap<>()));
                    break;
                case ARR_OPEN:
                    addExpectedTypesArray(expectedTypes);
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
