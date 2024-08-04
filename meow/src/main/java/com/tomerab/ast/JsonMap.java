package com.tomerab.ast;

import java.util.Map;

import com.tomerab.visitor.JsonVisitor;

public class JsonMap extends JsonObject {
    Map<String, JsonObject> object;

    public JsonMap(Map<String, JsonObject> object) {
        this.object = object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, JsonObject> getValue() {
        return object;
    }

    @Override
    public void accept(JsonVisitor visitor) {
        visitor.visit(this);
    }
}
