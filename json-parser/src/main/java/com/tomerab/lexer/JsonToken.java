package com.tomerab.lexer;

public class JsonToken {
    private boolean boolVal;
    private String stringVal;
    private double numVal;
    private final JsonType type;

    public enum JsonType {
        OBJ_OPEN,
        OBJ_CLOSE,
        ARR_OPEN,
        ARR_CLOSE,
        COMMA,
        COLON,
        STRING,
        NUMBER,
        BOOLEAN,
        NULL
    }

    public JsonToken(JsonType type) {
        this.type = type;
    }

    public JsonToken(boolean boolVal) {
        this.boolVal = boolVal;
        type = JsonType.BOOLEAN;
    }

    public JsonToken(String stringVal) {
        this.stringVal = stringVal;
        type = JsonType.STRING;
    }

    public JsonToken(double numVal) {
        this.numVal = numVal;
        type = JsonType.NUMBER;
    }

    public JsonType getType() {
        return type;
    }

    public String getString() {
        return stringVal;
    }

    public boolean getBool() {
        return boolVal;
    }

    public double getNum() {
        return numVal;
    }

    @Override
    public String toString() {
        String res = "[" + type.name() + "]";

        switch (type) {
            case STRING -> {
                return res + ": " + stringVal;
            }
            case NUMBER -> {
                return res + ": " + numVal;
            }
            case BOOLEAN -> {
                return res + ": " + boolVal;
            }
            case NULL -> {
                return res + ": " + "null";
            }
            default -> {
            }
        }

        return res;
    }
}
