package lexer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.tomerab.lexer.JsonLexer;
import com.tomerab.lexer.JsonToken;
import com.tomerab.lexer.JsonToken.JsonType;

class JsonLexerTest {
    @Test
    public void test_empty_object() {
        String json = "{}";

        JsonLexer lexer = new JsonLexer(json);
        JsonToken[] expected = {
                new JsonToken(JsonType.OBJ_OPEN),
                new JsonToken(JsonType.OBJ_CLOSE),
        };

        int i = 0;
        while (lexer.hasNext()) {
            JsonToken token = lexer.next();
            assertTrue(testEquality(token, expected[i++]));
        }
    }

    @Test
    public void test_nested_objects() {
        String json = """
                  {
                      "name": "John",
                      "age": 30,
                      "address": {
                          "street": "123 Main St",
                          "city": "New York"
                      }
                  }
                """;

        JsonLexer lexer = new JsonLexer(json);
        JsonToken[] expected = {
                new JsonToken(JsonType.OBJ_OPEN),
                new JsonToken("name"),
                new JsonToken(JsonType.COLON),
                new JsonToken("John"),
                new JsonToken(JsonType.COMMA),
                new JsonToken("age"),
                new JsonToken(JsonType.COLON),
                new JsonToken(30),
                new JsonToken(JsonType.COMMA),
                new JsonToken("address"),
                new JsonToken(JsonType.COLON),
                new JsonToken(JsonType.OBJ_OPEN),
                new JsonToken("street"),
                new JsonToken(JsonType.COLON),
                new JsonToken("123 Main St"),
                new JsonToken(JsonType.COMMA),
                new JsonToken("city"),
                new JsonToken(JsonType.COLON),
                new JsonToken("New York"),
                new JsonToken(JsonType.OBJ_CLOSE),
                new JsonToken(JsonType.OBJ_CLOSE),
        };

        int i = 0;
        while (lexer.hasNext()) {
            JsonToken token = lexer.next();
            assertTrue(testEquality(token, expected[i++]));
        }
    }

    @Test
    public void test_array() {
        String json = """
                  [1, 2, 3, 4, 5]
                """;

        JsonLexer lexer = new JsonLexer(json);
        JsonToken[] expected = {
                new JsonToken(JsonType.ARR_OPEN),
                new JsonToken(1),
                new JsonToken(JsonType.COMMA),
                new JsonToken(2),
                new JsonToken(JsonType.COMMA),
                new JsonToken(3),
                new JsonToken(JsonType.COMMA),
                new JsonToken(4),
                new JsonToken(JsonType.COMMA),
                new JsonToken(5),
                new JsonToken(JsonType.ARR_CLOSE),
        };

        int i = 0;
        while (lexer.hasNext()) {
            JsonToken token = lexer.next();
            assertTrue(testEquality(token, expected[i++]));
        }
    }

    private boolean testEquality(JsonToken t1, JsonToken t2) {
        if (t1.getType() != t2.getType()) {
            return false;
        }

        switch (t1.getType()) {
            case BOOLEAN:
                return t1.getBool() == t2.getBool();
            case NUMBER:
                return t1.getNum() == t2.getNum();
            case STRING:
                return t1.getString().equals(t2.getString());
            default:
                return true;
        }
    }

    @Test
    public void test_array_of_objects() {
        String json = """
                  [
                      {"name": "Alice", "age": 25},
                      {"name": "Bob", "age": 30}
                  ]
                """;

        JsonLexer lexer = new JsonLexer(json);
        JsonToken[] expected = {
                new JsonToken(JsonType.ARR_OPEN),
                new JsonToken(JsonType.OBJ_OPEN),
                new JsonToken("name"),
                new JsonToken(JsonType.COLON),
                new JsonToken("Alice"),
                new JsonToken(JsonType.COMMA),
                new JsonToken("age"),
                new JsonToken(JsonType.COLON),
                new JsonToken(25),
                new JsonToken(JsonType.OBJ_CLOSE),
                new JsonToken(JsonType.COMMA),
                new JsonToken(JsonType.OBJ_OPEN),
                new JsonToken("name"),
                new JsonToken(JsonType.COLON),
                new JsonToken("Bob"),
                new JsonToken(JsonType.COMMA),
                new JsonToken("age"),
                new JsonToken(JsonType.COLON),
                new JsonToken(30),
                new JsonToken(JsonType.OBJ_CLOSE),
                new JsonToken(JsonType.ARR_CLOSE),
        };

        int i = 0;
        while (lexer.hasNext()) {
            JsonToken token = lexer.next();
            assertTrue(testEquality(token, expected[i++]));
        }
    }

    @Test
    public void test_special_characters_in_strings() {
        String json = """
                  {
                      "escapedQuotes": "He said, \\"Hello, World!\\"",
                      "unicode": "\\u0041\\u0042\\u0043",
                      "backslashes": "C:\\\\Users\\\\Example"
                  }
                """;

        JsonLexer lexer = new JsonLexer(json);
        JsonToken[] expected = {
                new JsonToken(JsonType.OBJ_OPEN),
                new JsonToken("escapedQuotes"),
                new JsonToken(JsonType.COLON),
                new JsonToken("He said, \"Hello, World!\""),
                new JsonToken(JsonType.COMMA),
                new JsonToken("unicode"),
                new JsonToken(JsonType.COLON),
                new JsonToken("ABC"),
                new JsonToken(JsonType.COMMA),
                new JsonToken("backslashes"),
                new JsonToken(JsonType.COLON),
                new JsonToken("C:\\Users\\Example"),
                new JsonToken(JsonType.OBJ_CLOSE),
        };

        int i = 0;
        while (lexer.hasNext()) {
            JsonToken token = lexer.next();
            assertTrue(testEquality(token, expected[i++]));
        }
    }

    @Test
    public void test_various_data_types() {
        String json = """
                  {
                      "string": "Hello",
                      "number": 1234,
                      "booleanTrue": true,
                      "booleanFalse": false,
                      "nullValue": null
                  }
                """;

        JsonLexer lexer = new JsonLexer(json);
        JsonToken[] expected = {
                new JsonToken(JsonType.OBJ_OPEN),
                new JsonToken("string"),
                new JsonToken(JsonType.COLON),
                new JsonToken("Hello"),
                new JsonToken(JsonType.COMMA),
                new JsonToken("number"),
                new JsonToken(JsonType.COLON),
                new JsonToken(1234),
                new JsonToken(JsonType.COMMA),
                new JsonToken("booleanTrue"),
                new JsonToken(JsonType.COLON),
                new JsonToken(true),
                new JsonToken(JsonType.COMMA),
                new JsonToken("booleanFalse"),
                new JsonToken(JsonType.COLON),
                new JsonToken(false),
                new JsonToken(JsonType.COMMA),
                new JsonToken("nullValue"),
                new JsonToken(JsonType.COLON),
                new JsonToken(JsonType.NULL),
                new JsonToken(JsonType.OBJ_CLOSE),
        };

        int i = 0;
        while (lexer.hasNext()) {
            JsonToken token = lexer.next();
            assertTrue(testEquality(token, expected[i++]));
        }
    }

    @Test
    public void test_invalid_boolean() {
        String json = "fals";

        JsonLexer lexer = new JsonLexer(json);
        assertThrows(IllegalArgumentException.class, lexer::next, "Expected boolean got: 'fals'");
    }

    @Test
    public void test_invalid_null() {
        String json = "nul";

        JsonLexer lexer = new JsonLexer(json);
        assertThrows(IllegalArgumentException.class, lexer::next, "Expected null got: 'nul'");
    }

    @Test
    public void test_unterminated_string() {
        String json = "\"Hello";

        JsonLexer lexer = new JsonLexer(json);
        assertThrows(IllegalArgumentException.class, lexer::next, "Unterminated string: Hello");
    }

    @Test
    public void test_unexpected_character() {
        String json = "#unexpected";

        JsonLexer lexer = new JsonLexer(json);
        assertThrows(IllegalArgumentException.class, lexer::next, "Unexpected character: #");
    }
}
