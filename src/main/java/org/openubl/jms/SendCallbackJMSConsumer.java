package org.openubl.jms;

import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.openubl.factories.ModelFactory;
import org.openubl.providers.CallbackRSProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;
import javax.xml.soap.Text;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class SendCallbackJMSConsumer implements Runnable {

    private static final Logger LOG = Logger.getLogger(SendCallbackJMSConsumer.class);

    @Inject
    CallbackRSProvider callbackRSProvider;

    @Inject
    ConnectionFactory connectionFactory;

    @ConfigProperty(name = "openubl.callbackQueue")
    String callbackQueue;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.scheduleWithFixedDelay(this, 0L, 5L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(Session.CLIENT_ACKNOWLEDGE)) {
            JMSConsumer jmsConsumer = context.createConsumer(context.createQueue(callbackQueue));
            while (true) {
                Message message = jmsConsumer.receive();
                if (message == null) {
                    return;
                }

                BillServiceModel billServiceModel = ModelFactory.getBillServiceModel(message);
                if (message instanceof BytesMessage) {
                    billServiceModel.setCdr(message.getBody(byte[].class));
                } else {
                    billServiceModel.setCdr(null);
                }

                boolean result = callbackRSProvider.sendCallback(billServiceModel);
                if (result) {
                    message.acknowledge();
                }
            }
        } catch (JMSException e) {
            LOG.error(e);
        } catch (Throwable e) {
            LOG.error("Unexpected exception", e);
        }
    }

}
