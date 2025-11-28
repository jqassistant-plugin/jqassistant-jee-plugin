package org.jqassistant.plugin.jee.transaction.set;

public class JavaxNonTransactionalSubClassOfTransactionalMethod extends JavaxTransactionalMethod {

    // Method is not transactional, since class-level annotations are not inherited.
    @Override
    public void transactionalMethod() {}
}
