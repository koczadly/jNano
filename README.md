# Introduction to jNano
jNano is a Java library used to interface with the official [Nano node](https://github.com/nanocurrency/nano-node)
 via RPC requests. It also includes many native utilities and tools so that a node isn't always necessary.
 
Nano is a crypto-currency which aims to offer instantaneous feeless transactions. For more information, visit
[https://nano.org](https://nano.org).

## Features
This library provides you simple access to the following:
- Synchronous and asynchronous methods for [executing RPC calls](https://github.com/koczadly/jNano/wiki/Query-requests) (all commands up to V21 are supported).
- [WebSocket support](https://github.com/koczadly/jNano/wiki/WebSocket-communication) for receiving live data feeds
 and events from the node.
- A [server for listening to real-time block callbacks](https://github.com/koczadly/jNano/wiki/Block-callback) from
 the node.
- Various [utility classes](https://github.com/koczadly/jNano/wiki/Utilities) for validating, computing and processing
 natively, without the need of a connected node.

## Usage
### Maven
This project is hosted on [Maven Central](https://search.maven.org/artifact/uk.oczadly.karl/jnano). To import this
 library, add the following dependency into your `pom.xml`:
```xml
<dependency>
    <groupId>uk.oczadly.karl</groupId>
    <artifactId>jnano</artifactId>
    <version>2.10.1-V21.2</version>
</dependency>
```

### Documentation
The latest Javadoc pages can be [viewed online through Javadoc.io](https://www.javadoc.io/doc/uk.oczadly.karl/jnano/latest/uk.oczadly.karl.jnano-summary.html).
Additional documentation can be found on the [wiki pages](https://github.com/koczadly/jNano/wiki/).

### Configuring the node
Before using this library, you will need to configure the node correctly. Make sure to view the wiki topics for steps
 on how to do this.

### Examples
*More detailed examples can be found on the associated [wiki pages](https://github.com/koczadly/jNano/wiki/).*

#### Executing RPC queries
To make queries to an external Nano node through the RPC system, you will need to use [RpcQueryNode](https://www.javadoc.io/doc/uk.oczadly.karl/jnano/latest/uk/oczadly/karl/jnano/rpc/RpcQueryNode.html)
 class.

The following example will define a node on `localhost:7076` (default for the parameterless constructor). Other
 addresses and ports can be specified using the constructor arguments. Alternatively, the nested [Builder](https://www.javadoc.io/doc/uk.oczadly.karl/jnano/latest/uk/oczadly/karl/jnano/rpc/RpcQueryNode.Builder.html)
 class can be used to construct instances with additional control over the object.
```java
RpcQueryNode rpc = new RpcQueryNode();
```
##### Synchronous (blocking) queries
This example will print an account's balance to the console using a synchronous (blocking) call.
```java
// Construct the request (with arguments)
RequestAccountBalance request = new RequestAccountBalance(
        "nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz");

// Execute the request and obtain the result
ResponseBalance balance = rpc.processRequest(request);

// Handle the result object however you wish (eg. print the balance)
System.out.println("Account balance: " + balance.getTotal());
```
##### Asynchronous queries
This example will execute the query in a separate thread, and print the node version to the console. Alternatively,
 you can retrieve the `Future` response object returned by the method instead of using a [QueryCallback](https://www.javadoc.io/doc/uk.oczadly.karl/jnano/latest/uk/oczadly/karl/jnano/rpc/QueryCallback.html).
```java
rpc.processRequestAsync(new RequestVersion(), new QueryCallback<>() {
    @Override
    public void onResponse(ResponseVersion response, RequestVersion request) {
        // Successful, handle the response
        System.out.println("Version: " + response.getNodeVendor());
    }
    
    @Override
    public void onFailure(RpcException ex, RequestVersion request) {
        // An RPC error occured
        ex.printStackTrace();
    }
    
    @Override
    public void onFailure(IOException ex, RequestVersion request) {
        // An IO (network) error occured
        ex.printStackTrace();
    }
});
```
#### Listening for real-time blocks (WebSocket)
The following will create a WebSocket listener which connects to port 7078 on localhost. For each new block confirmed
 by the node, the hash will be printed to the console.
```java
NanoWebSocketClient ws = new NanoWebSocketClient(); // Defaults to localhost:7078
ws.connect(); // Connect to the websocket
// Register a listener for block confirmations
ws.getTopics().topicConfirmedBlocks().registerListener((message, context) -> {
    // Print the hash of all confirmed blocks
    System.out.println("Confirmed block: " + message.getHash());
});
// Subscribe to the block confirmations topic (and specify an account filter)
ws.getTopics().topicConfirmedBlocks().subscribeBlocking(
        new TopicConfirmation.SubArgs()
                .filterAccounts("nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz")
                .includeBlockContents()
);
```


## Dependencies
This project uses the following dependencies, which are included automatically through Maven:
- [Gson 2.8.6](https://github.com/google/gson)
- [blake2b 1.0.0](https://github.com/rfksystems/blake2b)
- [Java-WebSocket 1.5.1](https://github.com/TooTallNate/Java-WebSocket)

---

<sup><sup>If you found this library useful and would like to support my work, donations may be sent to 
<b>[nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz](nano:nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz)</b>
\- any amount would be greatly appreciated :D</sup></sup>