package com.tomerab.lexer;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents a token in a JSON document.
 */
public class JsonToken {
    private boolean boolVal;
    private String stringVal;
    private BigDecimal decimal;
    private BigInteger integer;
    private final JsonType type;

    public enum JsonType {
        OBJ_OPEN,
        OBJ_CLOSE,
        ARR_OPEN,
        ARR_CLOSE,
        COMMA,
        COLON,
        STRING,
        NUMBER_DECIMAL,
        NUMBER_INTEGER,
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

    public JsonToken(BigDecimal decimal) {
        this.decimal = decimal;
        type = JsonType.NUMBER_DECIMAL;
    }

    public JsonToken(BigInteger integer) {
        this.integer = integer;
        type = JsonType.NUMBER_INTEGER;
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

    public BigDecimal getDecimal() {
        return decimal;
    }

    public BigInteger getInteger() {
        return integer;
    }

    @Override
    public String toString() {
        String res = "[" + type.name() + "]";

        switch (type) {
            case STRING -> {
                return res + ": " + stringVal;
            }
            case NUMBER_DECIMAL -> {
                return res + ": " + decimal;
            }
            case NUMBER_INTEGER -> {
                return res + ": " + integer;
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
