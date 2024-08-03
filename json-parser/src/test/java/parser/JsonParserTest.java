package parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.tomerab.ast.JsonArray;
import com.tomerab.ast.JsonBoolean;
import com.tomerab.ast.JsonMap;
import com.tomerab.ast.JsonNull;
import com.tomerab.ast.JsonNumber;
import com.tomerab.ast.JsonObject;
import com.tomerab.ast.JsonString;
import com.tomerab.lexer.JsonLexer;
import com.tomerab.parser.JsonParser;

public class JsonParserTest {
    // Todo(tomer): Add test for error handling

    @Test
    public void testSimpleJson() {
        String json = "{\"name\":\"John\", \"age\":30, \"car\":null}";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        Map<String, JsonObject> expected = new LinkedHashMap<>();
        expected.put("name", new JsonString("John"));
        expected.put("age", new JsonNumber(30));
        expected.put("car", new JsonNull());

        assertTrue(testEquality(object, new JsonMap(expected)));
    }

    @Test
    public void testEmptyJsonObject() {
        String json = "{}";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        assertTrue(testEquality(object, new JsonMap(new LinkedHashMap<>())));
    }

    @Test
    public void testEmptyJsonArray() {
        String json = "[]";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        assertTrue(testEquality(object, new JsonArray(List.of())));
    }

    @Test
    public void testNestedJsonObjects() {
        String json = "{\"person\":{\"name\":\"John\",\"age\":30},\"car\":null}";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        Map<String, JsonObject> personMap = new LinkedHashMap<>();
        personMap.put("name", new JsonString("John"));
        personMap.put("age", new JsonNumber(30));

        Map<String, JsonObject> expected = new LinkedHashMap<>();
        expected.put("person", new JsonMap(personMap));
        expected.put("car", new JsonNull());

        assertTrue(testEquality(object, new JsonMap(expected)));
    }

    @Test
    public void testNestedJsonArrays() {
        String json = "[[1,2],[3,4]]";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        List<JsonObject> innerList1 = List.of(new JsonNumber(1), new JsonNumber(2));
        List<JsonObject> innerList2 = List.of(new JsonNumber(3), new JsonNumber(4));
        List<JsonObject> outerList = List.of(new JsonArray(innerList1), new JsonArray(innerList2));

        assertTrue(testEquality(object, new JsonArray(outerList)));
    }

    @Test
    public void testJsonBoolean() {
        String json = "{\"flag\":true}";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        Map<String, JsonObject> expected = new LinkedHashMap<>();
        expected.put("flag", new JsonBoolean(true));

        assertTrue(testEquality(object, new JsonMap(expected)));
    }

    @Test
    public void testJsonStringSpecialChars() {
        String json = "{\"text\":\"Hello\\nWorld!\\t\\\"Quotes\\\"\"}";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        Map<String, JsonObject> expected = new LinkedHashMap<>();
        expected.put("text", new JsonString("Hello\\nWorld!\\t\"Quotes\""));

        assertTrue(testEquality(object, new JsonMap(expected)));
    }

    @Test
    public void testJsonBooleanEquality() {
        String json = "{\"flag\":true}";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        Map<String, JsonObject> expected = new LinkedHashMap<>();
        expected.put("flag", new JsonBoolean(true));

        assertTrue(testEquality(object, new JsonMap(expected)));
    }

    @Test
    public void test() {
        String json = "{\"name\":\"John\", \"age\":30, \"car\":null}";
        JsonLexer lexer = new JsonLexer(json);
        JsonParser parser = new JsonParser(lexer);
        JsonObject object = parser.parse();

        Map<String, JsonObject> expected = new LinkedHashMap<>();
        expected.put("name", new JsonString("John"));
        expected.put("age", new JsonNumber(30));
        expected.put("car", new JsonNull());

        assertTrue(testEquality(object, new JsonMap(expected)));
    }

    private boolean testEquality(JsonObject obj1, JsonObject obj2) {
        if (obj1.getClass() != obj2.getClass()) {
            return false;
        }

        // At this point, we know that the runtime classes of obj1 and obj2 are equal.
        if (obj1 instanceof JsonMap) {
            Map<String, JsonObject> obj1Map = ((JsonMap) obj1).getValue();
            Map<String, JsonObject> obj2Map = ((JsonMap) obj2).getValue();

            if (obj1Map.size() != obj2Map.size()) {
                return false;
            }

            for (Map.Entry<String, JsonObject> entry : obj1Map.entrySet()) {
                String key = entry.getKey();
                JsonObject value = entry.getValue();

                if (!obj2Map.containsKey(key)) {
                    return false;
                }

                if (!testEquality(value, obj2Map.get(key))) {
                    return false;
                }
            }

            return true;
        }
        if (obj1 instanceof JsonArray) {
            List<JsonObject> l1 = ((JsonArray) obj1).getValue();
            List<JsonObject> l2 = ((JsonArray) obj2).getValue();

            if (l1.size() != l2.size()) {
                return false;
            }

            for (int i = 0; i < l1.size(); i++) {
                if (!testEquality(l1.get(i), l2.get(i))) {
                    return false;
                }
            }

            return true;
        }
        if (obj1 instanceof JsonString) {
            String s1 = ((JsonString) obj1).getValue();
            String s2 = ((JsonString) obj2).getValue();
            return s1 != null && s2 != null && s1.equals(s2);
        }
        if (obj1 instanceof JsonBoolean) {
            boolean b1 = ((JsonBoolean) obj1).getValue();
            boolean b2 = ((JsonBoolean) obj2).getValue();
            return b1 == b2;
        }
        if (obj1 instanceof JsonNumber) {
            double d1 = ((JsonNumber) obj1).getValue();
            double d2 = ((JsonNumber) obj2).getValue();
            return Double.compare(d1, d2) == 0;
        }

        return true;
    }

}
