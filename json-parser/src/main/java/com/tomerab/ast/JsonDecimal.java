package com.tomerab.ast;

import java.math.BigDecimal;

import com.tomerab.visitor.JsonVisitor;

public class JsonDecimal extends JsonNumber {
    private BigDecimal decimal;

    public JsonDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BigDecimal getValue() {
        return decimal;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }
}
