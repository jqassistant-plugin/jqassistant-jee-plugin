package org.jqassistant.plugin.jee.resource.set;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Queue;

@ApplicationScoped
public class JavaxClassWithResource {

    @Resource
    Queue queue;

}
