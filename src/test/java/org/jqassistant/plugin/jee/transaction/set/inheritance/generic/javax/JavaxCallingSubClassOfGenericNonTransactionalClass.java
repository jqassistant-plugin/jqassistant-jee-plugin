package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.GenericNonTransactionalClass;
import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfGenericNonTransactionalClass extends GenericNonTransactionalClass<Long> {

    public void anotherMethod() {
        method(1L);
    }

}
