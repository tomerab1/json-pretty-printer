package com.tomerab.ast;

import com.tomerab.visitor.JsonVisitor;

public abstract class JsonObject {
    public abstract <T> T getValue();

    public abstract void accept(JsonVisitor visitor);
}
