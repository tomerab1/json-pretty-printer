package com.tomerab.visitor;

import com.tomerab.ast.*;

public interface JsonVisitor {
    public void visit(JsonMap map);

    public void visit(JsonArray array);

    public void visit(JsonString string);

    public void visit(JsonBoolean bool);

    public void visit(JsonNumber number);

    public void visit(JsonNull jsonNull);
}
