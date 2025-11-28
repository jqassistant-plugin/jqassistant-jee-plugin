package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import javax.transaction.Transactional;

public class JavaxGenericClassWithTransactionalMethod<T> {

    @Transactional
    public void method(T parameter) {

    }

}
