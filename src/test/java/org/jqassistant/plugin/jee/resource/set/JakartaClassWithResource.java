package org.jqassistant.plugin.jee.resource.set;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.jms.Queue;

@ApplicationScoped
public class JakartaClassWithResource {

    @Resource
    Queue queue;

}
