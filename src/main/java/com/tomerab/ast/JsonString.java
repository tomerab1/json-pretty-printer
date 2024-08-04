package com.tomerab.ast;

import com.tomerab.visitor.JsonVisitor;

public class JsonString extends JsonObject {
    private String string;

    public JsonString(String string) {
        this.string = string;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getValue() {
        return string;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }
}
