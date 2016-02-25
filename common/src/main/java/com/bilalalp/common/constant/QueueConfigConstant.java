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

    public static final String AMQP_LINK_SEARCHER_QUEUE_NAME = "amqp.link.searcher.queue.name";
    public static final String AMQP_LINK_SEARCHER_QUEUE_KEY = "amqp.link.searcher.queue.key";

    public static final String AMQP_COLLECTOR_QUEUE_NAME = "amqp.collector.queue.name";
    public static final String AMQP_COLLECTOR_QUEUE_KEY = "amqp.collector.queue.key";

    public static final String AMQP_FOUND_LINK_QUEUE_NAME = "amqp.found.link.queue.name";
    public static final String AMQP_FOUND_LINK_QUEUE_KEY = "amqp.found.link.queue.key";

    public static final String AMQP_EXTRACTOR_QUEUE_NAME = "amqp.extractor.queue.name";
    public static final String AMQP_EXTRACTOR_QUEUE_KEY = "amqp.extractor.queue.key";

    public static final String AMQP_SELECTOR_QUEUE_NAME = "amqp.selector.queue.name";
    public static final String AMQP_SELECTOR_QUEUE_KEY = "amqp.selector.queue.key";

    public static final String AMQP_TF_IDF_QUEUE_NAME = "amqp.tfidf.queue.name";
    public static final String AMQP_TF_IDF_QUEUE_KEY = "amqp.tfidf.queue.key";

    public static final String AMQP_TF_IDF_PROCESS_QUEUE_NAME = "amqp.tfidf.process.queue.name";
    public static final String AMQP_TF_IDF_PROCESS_QUEUE_KEY = "amqp.tfidf.process.queue.key";

    private QueueConfigConstant() {
        // Constant Class
    }
}