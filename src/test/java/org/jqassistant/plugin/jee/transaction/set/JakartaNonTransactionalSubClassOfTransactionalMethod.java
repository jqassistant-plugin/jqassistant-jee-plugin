package org.jqassistant.plugin.jee.transaction.set;

public class JakartaNonTransactionalSubClassOfTransactionalMethod extends JakartaTransactionalMethod {

    // Method is not transactional, since class-level annotations are not inherited.
    @Override
    public void transactionalMethod() {}
}
