package com.tomerab.lexer;

public class JsonLexer {
    private String jsonStr;

    public JsonLexer(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public boolean hasNext() {
        return !jsonStr.isEmpty();
    }

    public JsonToken next() {
        // Consume whitespace
        jsonStr = jsonStr.replaceAll("(^[\\r\\n\\s]+|[\\r\\n\\s]+$)", "");

        if (jsonStr.isEmpty()) {
            return null;
        }

        char currentChar = jsonStr.charAt(0);
        switch (currentChar) {
            case '{' -> {
                jsonStr = jsonStr.substring(1);
                return new JsonToken(JsonToken.JsonType.OBJ_OPEN);
            }
            case '}' -> {
                jsonStr = jsonStr.substring(1);
                return new JsonToken(JsonToken.JsonType.OBJ_CLOSE);
            }
            case ',' -> {
                jsonStr = jsonStr.substring(1);
                return new JsonToken(JsonToken.JsonType.COMMA);
            }
            case ':' -> {
                jsonStr = jsonStr.substring(1);
                return new JsonToken(JsonToken.JsonType.COLON);
            }
            case '"' -> {
                jsonStr = jsonStr.substring(1);
                return parseString();
            }
            case '[' -> {
                jsonStr = jsonStr.substring(1);
                return new JsonToken(JsonToken.JsonType.ARR_OPEN);
            }
            case ']' -> {
                jsonStr = jsonStr.substring(1);
                return new JsonToken(JsonToken.JsonType.ARR_CLOSE);
            }
            default -> {
                if (Character.isDigit(currentChar) || currentChar == '-') {
                    return parseNumber();
                }
                if (isBooleanPrefix(currentChar)) {
                    return parseBoolean();
                }
                if (isNullPrefix(currentChar)) {
                    return parseNull();
                }
                throw new IllegalArgumentException("Unexpected character: " + currentChar);
            }
        }
    }

    private JsonToken parseBoolean() {
        StringBuilder result = new StringBuilder();
        int boolLen = jsonStr.charAt(0) == 't' ? 4 : 5; // len(false) = 5, len(true) = 4.

        while (!jsonStr.isEmpty() && boolLen-- > 0) {
            result.append(jsonStr.charAt(0));
            jsonStr = jsonStr.substring(1);
        }

        String boolStr = result.toString();
        if ("true".equals(boolStr) || "false".equals(boolStr)) {
            return new JsonToken(Boolean.parseBoolean(boolStr));
        }

        throw new IllegalArgumentException("Expected boolean got: '" + boolStr + "'");
    }

    private JsonToken parseNull() {
        StringBuilder result = new StringBuilder();
        int nullLen = 4;

        while (!jsonStr.isEmpty() && nullLen-- > 0) {
            result.append(jsonStr.charAt(0));
            jsonStr = jsonStr.substring(1);
        }

        String nullStr = result.toString();
        if ("null".equals(nullStr)) {
            return new JsonToken(JsonToken.JsonType.NULL);
        }

        throw new IllegalArgumentException("Expected null got: '" + nullStr + "'");
    }

    private boolean isNullPrefix(char ch) {
        return ch == 'n';
    }

    private boolean isBooleanPrefix(char ch) {
        return ch == 't' || ch == 'f';
    }

    private JsonToken parseString() {
        StringBuilder result = new StringBuilder();
        boolean escaping = false;

        while (!jsonStr.isEmpty()) {
            char currentChar = jsonStr.charAt(0);
            jsonStr = jsonStr.substring(1);

            if (escaping) {
                // Handle escape sequences
                switch (currentChar) {
                    case '"':
                    case '\\':
                    case '/':
                        result.append(currentChar);
                        break;
                    case 'b':
                        result.append('\b');
                        break;
                    case 'f':
                        result.append('\f');
                        break;
                    case 'n':
                        result.append('\n');
                        break;
                    case 'r':
                        result.append('\r');
                        break;
                    case 't':
                        result.append('\t');
                        break;
                    case 'u': // Unicode escape
                        if (jsonStr.length() >= 4) {
                            String hex = jsonStr.substring(0, 4);
                            result.append((char) Integer.parseInt(hex, 16));
                            jsonStr = jsonStr.substring(4);
                        } else {
                            throw new IllegalArgumentException("Invalid Unicode escape sequence");
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid escape sequence: \\" + currentChar);
                }
                escaping = false;
            } else {
                if (currentChar == '\\') {
                    escaping = true;
                } else if (currentChar == '"') {
                    return new JsonToken(result.toString());
                } else {
                    result.append(currentChar);
                }
            }
        }

        throw new IllegalArgumentException("Unterminated string: " + result);
    }

    private JsonToken parseNumber() {
        StringBuilder result = new StringBuilder();
        boolean hasDot = false;
        boolean hasExponent = false;

        while (!jsonStr.isEmpty()) {
            char ch = jsonStr.charAt(0);
            if (Character.isDigit(ch) || ch == '-' || ch == '+') {
                result.append(ch);
            } else if (ch == '.' && !hasDot) {
                result.append(ch);
                hasDot = true;
            } else if ((ch == 'e' || ch == 'E') && !hasExponent) {
                result.append(ch);
                hasExponent = true;
                hasDot = true; // 'e' or 'E' implies an exponent, thus disabling further dot usage.
            } else {
                break;
            }
            jsonStr = jsonStr.substring(1);
        }

        String numberStr = result.toString();
        if (numberStr.contains(".") || numberStr.contains("e") || numberStr.contains("E")) {
            return new JsonToken(Double.parseDouble(numberStr));
        } else {
            return new JsonToken(Integer.parseInt(numberStr));
        }
    }
}
