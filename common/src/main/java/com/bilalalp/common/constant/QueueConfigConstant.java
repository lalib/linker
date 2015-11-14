package com.bilalalp.common.constant;

public final class QueueConfigConstant {

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

    public static final String AMQP_LINK_SEARCHER_QUEUE_NAME = "amqp.link.searcher.queue.name";
    public static final String AMQP_LINK_SEARCHER_QUEUE_KEY = "amqp.link.searcher.queue.key";

    public static final String AMQP_LINK_SEARCHER_ERROR_QUEUE_NAME = "amqp.link.searcher.error.queue.name";
    public static final String AMQP_LINK_SEARCHER_ERROR_QUEUE_KEY = "amqp.link.searcher.error.queue.key";

    public static final String AMQP_FOUND_LINK_QUEUE_NAME = "amqp.found.link.queue.name";
    public static final String AMQP_FOUND_LINK_QUEUE_KEY = "amqp.found.link.queue.key";

    private QueueConfigConstant() {
        // Constant Class
    }
}