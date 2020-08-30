/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket;

import uk.oczadly.karl.jnano.websocket.topic.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TopicRegistry {
    
    private final NanoWebSocketClient client;
    private final Map<String, Topic<?>> topics = new ConcurrentHashMap<>();
    
    private final TopicConfirmation topicConfirmation;
    private final TopicVote topicVote;
    private final TopicStoppedElection topicStoppedElection;
    private final TopicActiveDifficulty topicActiveDifficulty;
    private final TopicWork topicWork;
    private final TopicTelemetry topicTelemetry;
    private final TopicUnconfirmedBlocks topicUnconfirmed;
    private final TopicBootstrap topicBootstrap;
    
    
    public TopicRegistry(NanoWebSocketClient client) {
        this.client = client;
        
        register(this.topicConfirmation = new TopicConfirmation(client));
        register(this.topicVote = new TopicVote(client));
        register(this.topicStoppedElection = new TopicStoppedElection(client));
        register(this.topicActiveDifficulty = new TopicActiveDifficulty(client));
        register(this.topicWork = new TopicWork(client));
        register(this.topicTelemetry = new TopicTelemetry(client));
        register(this.topicUnconfirmed = new TopicUnconfirmedBlocks(client));
        register(this.topicBootstrap = new TopicBootstrap(client));
    }
    
    
    protected NanoWebSocketClient getClient() {
        return client;
    }
    
    
    /**
     * Registers a topic to this websocket if one isn't already registered under the same topic. Most developers and
     * app use-cases will not need to use this method.
     * @param topic the topic to register
     */
    public void register(Topic<?> topic) {
        this.topics.putIfAbsent(topic.getTopicName().toLowerCase(), topic);
    }
    
    /**
     * Returns a topic manager object from a given topic name.
     * @param topicName the topic name
     * @return the topic associated with the given name
     */
    public Topic<?> get(String topicName) {
        return this.topics.get(topicName.toLowerCase());
    }
    
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code confirmation} topic object
     */
    public TopicConfirmation topicConfirmedBlocks() {
        return topicConfirmation;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code vote} topic object
     */
    public TopicVote topicVote() {
        return topicVote;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code stopped_election} topic object
     */
    public TopicStoppedElection topicStoppedElection() {
        return topicStoppedElection;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code active_difficulty} topic object
     */
    public TopicActiveDifficulty topicActiveDifficulty() {
        return topicActiveDifficulty;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code work} topic object
     */
    public TopicWork topicWork() {
        return topicWork;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code telemetry} topic object
     */
    public TopicTelemetry topicTelemetry() {
        return topicTelemetry;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code new_unconfirmed_block} topic object
     */
    public TopicUnconfirmedBlocks topicUnconfirmedBlocks() {
        return topicUnconfirmed;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code bootstrap} topic object
     */
    public TopicBootstrap topicBootstrap() {
        return topicBootstrap;
    }
}
