package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.GenericNonTransactionalClass;
import javax.transaction.Transactional;

@Transactional
public class JavaxOverridingSubClassOfGenericNonTransactionalClass extends GenericNonTransactionalClass<Long> {

    @Override
    public void method(Long l) {
        super.method(l);
    }

}
