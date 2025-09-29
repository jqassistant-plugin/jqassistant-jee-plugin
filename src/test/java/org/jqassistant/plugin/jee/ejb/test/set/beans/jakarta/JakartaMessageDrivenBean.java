package org.jqassistant.plugin.jee.ejb.test.set.beans.jakarta;

import jakarta.ejb.Local;
import jakarta.ejb.MessageDriven;

import jakarta.jms.Message;
import jakarta.jms.MessageListener;

/**
 * A message driven EJB.
 */
@MessageDriven
@Local
public class JakartaMessageDrivenBean implements MessageListener {

    @Override
    public void onMessage(Message message) {
    }

}
