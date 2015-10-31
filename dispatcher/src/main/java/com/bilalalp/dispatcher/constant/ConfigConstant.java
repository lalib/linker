package com.bilalalp.dispatcher.constant;

public final class ConfigConstant {

    private ConfigConstant() {
        // Constant Class
    }

    public static final String AMQP_PASSWORD = "amqp.password";

    public static final String AMQP_USERNAME = "amqp.username";

    public static final String AMQP_HOST = "amqp.host";

    public static final String AMQP_PORT = "amqp.port";

    public static final String AMQP_CONNECTION_TIMEOUT = "amqp.connection_timeout";

    public static final String AMQP_CHANNEL_TRANSACTED = "amqp.channel_transacted";

    public static final String AMQP_DIRECT_NAME = "amqp.direct.name";

    public static final String AMQP_DISPATCHER_REQUEST_QUEUE_NAME = "amqp.dispatcher.request.queue.name";

    public static final String AMQP_DISPATCHER_REQUEST_QUEUE_KEY = "amqp.dispatcher.request.queue.key";

    public static final String AMQP_DISPATCHER_REQUEST_ERROR_QUEUE_NAME = "amqp.dispatcher.request.error.queue.name";

    public static final String AMQP_DISPATCHER_REQUEST_ERROR_QUEUE_KEY = "amqp.dispatcher.request.error.queue.key";
}