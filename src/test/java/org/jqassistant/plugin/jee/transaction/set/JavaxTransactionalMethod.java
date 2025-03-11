package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

public class JavaxTransactionalMethod {

    @Transactional
    public void transactionalMethod() {

    }

    public void nonTransactionalMethod() {
    }

    private void callingTransactional() {
        transactionalMethod();
    }

    @Transactional
    private void privateTransactionalMethod() {
    }

    private void callingPrivateTransactionalMethod() {
        privateTransactionalMethod(); // Private transactional methods may be called.
    }

}
