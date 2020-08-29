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
    private final Map<String, WsTopic<?>> topics = new ConcurrentHashMap<>();
    
    private final WsTopicConfirmation topicConfirmation;
    private final WsTopicVote topicVote;
    private final WsTopicStoppedElection topicStoppedElection;
    private final WsTopicActiveDifficulty topicActiveDifficulty;
    private final WsTopicWork topicWork;
    private final WsTopicTelemetry topicTelemetry;
    private final WsTopicUnconfirmedBlocks topicUnconfirmed;
    private final WsTopicBootstrap topicBootstrap;
    
    
    public TopicRegistry(NanoWebSocketClient client) {
        this.client = client;
        
        register(this.topicConfirmation = new WsTopicConfirmation(client));
        register(this.topicVote = new WsTopicVote(client));
        register(this.topicStoppedElection = new WsTopicStoppedElection(client));
        register(this.topicActiveDifficulty = new WsTopicActiveDifficulty(client));
        register(this.topicWork = new WsTopicWork(client));
        register(this.topicTelemetry = new WsTopicTelemetry(client));
        register(this.topicUnconfirmed = new WsTopicUnconfirmedBlocks(client));
        register(this.topicBootstrap = new WsTopicBootstrap(client));
    }
    
    
    protected NanoWebSocketClient getClient() {
        return client;
    }
    
    
    /**
     * Registers a topic to this websocket if one isn't already registered under the same topic. Most developers and
     * app use-cases will not need to use this method.
     * @param topic the topic to register
     */
    public void register(WsTopic<?> topic) {
        this.topics.putIfAbsent(topic.getTopicName().toLowerCase(), topic);
    }
    
    /**
     * Returns a topic manager object from a given topic name.
     * @param topicName the topic name
     * @return the topic associated with the given name
     */
    public WsTopic<?> get(String topicName) {
        return this.topics.get(topicName.toLowerCase());
    }
    
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code confirmation} topic object
     */
    public WsTopicConfirmation topicConfirmedBlocks() {
        return topicConfirmation;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code vote} topic object
     */
    public WsTopicVote topicVote() {
        return topicVote;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code stopped_election} topic object
     */
    public WsTopicStoppedElection topicStoppedElection() {
        return topicStoppedElection;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code active_difficulty} topic object
     */
    public WsTopicActiveDifficulty topicActiveDifficulty() {
        return topicActiveDifficulty;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code work} topic object
     */
    public WsTopicWork topicWork() {
        return topicWork;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code telemetry} topic object
     */
    public WsTopicTelemetry topicTelemetry() {
        return topicTelemetry;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code new_unconfirmed_block} topic object
     */
    public WsTopicUnconfirmedBlocks topicUnconfirmedBlocks() {
        return topicUnconfirmed;
    }
    
    /**
     * Returns a topic object where you can register listeners, subscribe to and unsubscribe from.
     * @return the {@code bootstrap} topic object
     */
    public WsTopicBootstrap topicBootstrap() {
        return topicBootstrap;
    }
}
