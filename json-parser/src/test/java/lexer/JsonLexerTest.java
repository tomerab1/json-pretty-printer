package lexer;

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
}
