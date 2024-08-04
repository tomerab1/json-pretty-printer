package com.tomerab.ast;

import java.math.BigInteger;

import com.tomerab.visitor.JsonVisitor;

public class JsonInteger extends JsonNumber {
    private BigInteger integer;

    public JsonInteger(BigInteger integer) {
        this.integer = integer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BigInteger getValue() {
        return integer;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }
}
