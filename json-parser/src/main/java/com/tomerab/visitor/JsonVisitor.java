package com.tomerab.visitor;

import com.tomerab.ast.*;

public interface JsonVisitor {
    public void visit(JsonMap map);

    public void visit(JsonArray map);

    public void visit(JsonString map);

    public void visit(JsonBoolean map);

    public void visit(JsonNumber map);

    public void visit(JsonNull map);
}
