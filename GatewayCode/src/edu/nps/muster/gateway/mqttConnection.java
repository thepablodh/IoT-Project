package edu.nps.muster.gateway;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by Micah on 8/21/16.
 *
 * This is sample code and is in the works -- not integrated into the gateway server yet.
 */

public class mqttConnection implements MqttCallback {

    // Class Variables
    private MqttClient CLIENT;
    private String BROKER_URL = "tcp://172.16.1.144:8883";
    private String CLIENT_ID = "Gateway";
    private String USERNAME = "owntracks";
    private String PASSWORD = "mosquitto";
    private static final int QOS = 2;
    private static final boolean RETAIN = false;
    private static final MemoryPersistence PERSISTENCE = new MemoryPersistence();
    private mqttCaller callBack;



    public mqttConnection(mqttCaller caller){
        callBack = caller;
        connect();
    }

    public mqttConnection(mqttCaller caller, String server, String clientID, String username, String password){
        callBack = caller;
        BROKER_URL = "tcp://"+server;
        CLIENT_ID = clientID;
        USERNAME = username;
        PASSWORD = password;
        connect();
    }

    private void connect() {

        // Setup Connection Options
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setKeepAliveInterval(30);
        connOpts.setUserName(USERNAME);
        connOpts.setPassword(PASSWORD.toCharArray());

        // Connect
        try {
            CLIENT = new MqttClient(BROKER_URL, CLIENT_ID, PERSISTENCE);
            CLIENT.setCallback(this);
            //print("Connecting to broker: "+BROKER_URL);
            CLIENT.connect(connOpts);
            //print("Connected");

        } catch(MqttException e) {
            //e.printStackTrace();
            print("Could not connect");
            //System.exit(-1);
        }
    }

    public void disconnect(){ try { CLIENT.disconnect(); } catch(Exception e) {e.printStackTrace();} }

    public void connectionLost(Throwable t) {
        print("Connection lost! reconnecting.");
        // code to reconnect to the BROKER_URL would go here if desired
        connect();

    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        /* try {
            //print("Pub complete" + new String(token.getMessage().getPayload()));

        } catch(MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        } */
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        callBack.messageReceived(topic, new String(message.getPayload()));
    }

    public void subscribe(String topic) {
        try {
            CLIENT.subscribe(topic, QOS);
            CLIENT.setCallback(this);
        } catch (Exception e) {
            print("Could not subscribe");
            //e.printStackTrace();
        }
    }

    public void subscribe(String[] topic) {
        try {
            CLIENT.subscribe(topic);
            CLIENT.setCallback(this);
        } catch (Exception e) {
            print("Could not subscribe");
            //e.printStackTrace();
        }
    }

    public void publish(String topic, String message){
        try {
            MqttMessage msg = new MqttMessage();
            msg.setQos(QOS);
            msg.setPayload(message.getBytes());
            msg.setRetained(RETAIN);
            CLIENT.publish(topic, msg);

        } catch (Exception e) {
            print("Could not publish");
            //e.printStackTrace();
        }
    }

    private static void print(Object out) { System.out.println(out.toString()); }

    private static void main(String[] args) {
        //new mqttConnection(this).test();
    }

    private void test() {

        // subscribe to topic if SUBSCRIBER
        subscribe("Test");

        // publish messages if PUBLISHER
        for (int i=1; i<=10; i++) publish("Test", "{\"pubmsg\":" + i + "}");


        // disconnect
        try {
            // wait to ensure subscribed messages are delivered
            Thread.sleep(10000);

            CLIENT.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
