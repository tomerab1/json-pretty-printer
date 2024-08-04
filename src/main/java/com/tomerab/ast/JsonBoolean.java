package com.tomerab.ast;

import com.tomerab.visitor.JsonVisitor;

public class JsonBoolean extends JsonObject {
    private boolean bool;

    public JsonBoolean(boolean bool) {
        this.bool = bool;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Boolean getValue() {
        return bool;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }
}
