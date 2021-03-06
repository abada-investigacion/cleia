/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.task.service.hornetq;

/*
 * #%L
 * Cleia
 * %%
 * Copyright (C) 2013 Abada Servicios Desarrollo (investigacion@abadasoft.com)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.jbpm.task.service.BaseClientHandler;
import org.jbpm.task.service.BaseHandler;
import org.jbpm.task.service.TaskClientConnector;
import org.jbpm.task.service.hornetq.BaseHornetQTaskServer;
import org.jbpm.task.service.hornetq.HornetQTaskClientHandler;
import org.jbpm.task.service.hornetq.HornetQTaskServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sustituye {@link org.jbpm.task.service.hornetq.HornetQTaskClientConnector} jbpm 5.4.0.Final compliant
 * 
 * @author katsu
 */
public class HornetQTaskClientConnector implements TaskClientConnector {

    private static final Logger logger = LoggerFactory.getLogger(HornetQTaskClientConnector.class);
    protected ClientSession session;
    protected final BaseClientHandler handler;
    protected final String name;
    protected AtomicInteger counter;
    private String address;
    private Integer port;
    private ServerLocator serverLocator;
    private ClientProducer producer;
    private ClientConsumer consumer;
    private Thread responsesThread;

    public HornetQTaskClientConnector(String name, String address,int port,BaseClientHandler handler) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null");
        }
        this.name = name;
        this.handler = handler;        
        this.counter = new AtomicInteger();
        this.address=address;
        this.port=port;
    }

    public boolean connect(String address, int port) {
        this.port = port;
        this.address = address;
        return connect();
    }

    public boolean connect() {
        if (session != null && !session.isClosed()) {
            throw new IllegalStateException("Already connected. Disconnect first.");
        }
        try {
            Map<String, Object> connectionParams = new HashMap<String, Object>();
            if (address == null) {
                address = "127.0.0.1";
            }
            if (port == null) {
                port = 5153;
            }
            connectionParams.put(TransportConstants.PORT_PROP_NAME, port);
            connectionParams.put(TransportConstants.HOST_PROP_NAME, address);

            TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getCanonicalName(), connectionParams);
            serverLocator = HornetQClient.createServerLocatorWithoutHA(transportConfiguration);
            ClientSessionFactory factory = serverLocator.createSessionFactory(transportConfiguration);
            session = factory.createSession();
            producer = session.createProducer(HornetQTaskServer.SERVER_TASK_COMMANDS_QUEUE);

            createClientQueue();

            responsesThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        consumer = session.createConsumer(name);
                    } catch (HornetQException e) {
                        logger.error("Error creating consumer. ", e);
                        if (e.getCode() == HornetQException.OBJECT_CLOSED) {
                            logger.info(e.getMessage());
                            return;
                        }
                        throw new RuntimeException("Client Exception with class " + getClass()
                                + " using port " + port, e);
                    }


                    while (!consumer.isClosed()) {
                        try {
                            ClientMessage serverMessage = consumer.receive();
                            if (serverMessage != null) {
                                ((HornetQTaskClientHandler) handler).messageReceived(session, readMessage(serverMessage), BaseHornetQTaskServer.SERVER_TASK_COMMANDS_QUEUE);
                            }
                        } catch (HornetQException e) {
                            if (e.getCode() == HornetQException.OBJECT_CLOSED) {
                                try {
                                    logger.warn("Connection lost, trying to reconnect...");
                                    disconnect();
                                    connect();
                                } catch (Exception e1) {
                                    logger.error("Reconnecting failed, exiting...", e);
                                    return;
                                }
                            } else {
                                logger.error("HornetQ Exception with class " + getClass() + " using port " + port, e);
                            }
                        } catch (Exception e) {
                            // LOG the exception and continue receiving messages.
                            logger.error("Client Exception with class " + getClass() + " using port " + port, e);
                        }
                    }

                }
            });
            responsesThread.start();
            session.start();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    private Object readMessage(ClientMessage serverMessage) throws IOException, ClassNotFoundException {
        int bodySize = serverMessage.getBodySize();
        byte[] message = new byte[bodySize];
        serverMessage.getBodyBuffer().readBytes(message);
        ByteArrayInputStream bais = new ByteArrayInputStream(message);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

    private void createClientQueue() {
        try {
            session.createQueue(name, name, true);
        } catch (HornetQException e) {
            if (e.getCode() != HornetQException.QUEUE_EXISTS) {
                throw new RuntimeException("Client Exception with class " + getClass() + " using port " + port, e);
            }
            logger.info(e.getMessage());
        }
    }

    public void disconnect() throws Exception {
        if (session != null && !session.isClosed()) {
            session.close();
            producer.close();
            if (consumer != null) {
                consumer.close();
            }
            serverLocator.close();

            responsesThread.interrupt();
        }
    }

    public void write(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oout;
        try {
            oout = new ObjectOutputStream(baos);
            oout.writeObject(object);
            ClientMessage message = session.createMessage(true);
            message.getBodyBuffer().writeBytes(baos.toByteArray());
            message.putStringProperty("producerId", name);
            producer.send(message);
        } catch (IOException e) {
            throw new RuntimeException("Error creating message", e);
        } catch (HornetQException e) {
            if (e.getCode() == HornetQException.OBJECT_CLOSED) {
                try {
                    logger.warn("Connection lost, trying to reconnect...");
                    disconnect();
                    boolean connected = connect();
                    if (connected) {
                        // retry sending
                        write(object);
                    } else {
                        throw e;
                    }
                } catch (Exception e1) {
                    throw new RuntimeException("Error writing message (Reconnecting failed, exiting...)", e);
                }

            } else {
                throw new RuntimeException("Error writing message", e);
            }
        }
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public BaseHandler getHandler() {
        return handler;
    }

    public String getName() {
        return name;
    }
}
