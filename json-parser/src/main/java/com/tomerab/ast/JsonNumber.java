package com.tomerab.ast;

import com.tomerab.visitor.JsonVisitor;

public class JsonNumber extends JsonObject {
    private double number;

    public JsonNumber(double number) {
        this.number = number;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double getValue() {
        return number;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }
}
