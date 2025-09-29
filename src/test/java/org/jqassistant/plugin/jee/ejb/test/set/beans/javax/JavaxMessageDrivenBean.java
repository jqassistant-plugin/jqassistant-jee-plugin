package org.jqassistant.plugin.jee.ejb.test.set.beans.javax;

import javax.ejb.Local;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * A message driven EJB.
 */
@MessageDriven
@Local
public class JavaxMessageDrivenBean implements MessageListener {

    @Override
    public void onMessage(Message message) {
    }

}
