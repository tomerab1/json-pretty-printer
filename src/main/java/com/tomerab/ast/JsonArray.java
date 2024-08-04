package com.tomerab.ast;

import java.util.List;

import com.tomerab.visitor.JsonVisitor;

public class JsonArray extends JsonObject {
    private List<JsonObject> list;

    public JsonArray(List<JsonObject> list) {
        this.list = list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<JsonObject> getValue() {
        return list;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }
}
