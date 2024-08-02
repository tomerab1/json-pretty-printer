package com.tomerab.ast;

import com.tomerab.visitor.JsonVisitor;

public class JsonNull extends JsonObject {
    public Object getNull() {
        return null;
    }

    @Override
    public <T> T getValue() {
        throw new UnsupportedOperationException("Unimplemented method 'getValue'");
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }
}
