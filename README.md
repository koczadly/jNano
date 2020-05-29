# Introduction to jNano
jNano is a Java library used to interface with the official [Nano node](https://github.com/nanocurrency/nano-node)
 via RPC requests. It also includes many native utilities and tools so that a node isn't always necessary.
 
Nano is a crypto-currency which aims to offer instantaneous feeless transactions. For more information, visit
[https://nano.org](https://nano.org).

WebSocket support is currently in development, and will be made publicly available shortly.
## Features
This library provides you simple access to the following:
- Synchronous and asynchronous methods for executing RPC calls (all commands up to V20 are supported)
- A server for listening to real-time block callbacks from the node
- Various utility classes for computing and processing natively, without the need of a node

## Usage
### Maven
This project is hosted on [Maven Central](https://search.maven.org/artifact/uk.oczadly.karl/jnano). To import this
 library, add the following dependency into your `pom.xml`:
```xml
<dependency>
    <groupId>uk.oczadly.karl</groupId>
    <artifactId>jnano</artifactId>
    <version>2.4.2-V20</version>
</dependency>
```

### Documentation
The latest Javadoc pages can be [viewed online through Javadoc.io](https://www.javadoc.io/doc/uk.oczadly.karl/jnano).
Additional documentation can be found on the [wiki pages](https://github.com/koczadly/jNano/wiki/).

### Configuring the node
Before using this library, you will need to configure the node correctly.
- If you wish to make RPC requests using this library, 
[view the queries wiki page](https://github.com/koczadly/jNano/wiki/Query-requests#node-configuration).
- If you are wanting to listen for live blocks on the Nano network, 
[view the callback wiki page](https://github.com/koczadly/jNano/wiki/Block-callback#node-configuration).

### Examples
Detailed examples can be found on the associated wiki pages linked below:
- [Query requests and operation execution](https://github.com/koczadly/jNano/wiki/Query-requests#how-to-use-the-library)
- [Real-time network block listener](https://github.com/koczadly/jNano/wiki/Block-callback#how-to-use-the-library)
- [Additional tools and utilities](https://github.com/koczadly/jNano/wiki/Utilities)

#### Creating a node object
The following will define a node on localhost:7076 (default for the empty constructor). Other addresses and ports
 can be specified within the constructor.
```java
RpcQueryNode node = new RpcQueryNode();
```
#### Executing synchronous requests
This example will print an account's balance to the console using a synchronous (blocking) call.
```java
RequestAccountBalance request = new RequestAccountBalance("nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz");
ResponseBalance balance = node.processRequest(request);

System.out.println("Account balance: " + balance.getTotal());
```
#### Executing asynchronous requests
This example will print the node version to the console.
```java
node.processRequestAsync(new RequestVersion(), new QueryCallback<>() {
    @Override
    public void onResponse(ResponseVersion response, RequestVersion request) {
        System.out.println("Version: " + response.getNodeVendor());
    }
    
    @Override
    public void onFailure(RpcException ex, RequestVersion request) {
        ex.printStackTrace();
    }
    
    @Override
    public void onFailure(IOException ex, RequestVersion request) {
        ex.printStackTrace();
    }
});
```
#### Listening for real-time blocks
The following will create a callback server on port 8080, and print the hash and type of all new blocks to the
 console. The node will need to be configured to send to your application's listening address and port for this to work.
```java
BlockCallbackServer server = new BlockCallbackServer(8080);
server.registerListener((block, target, node) -> {
    System.out.println("Block hash: " + block.getBlockHash());
    System.out.println("Block type: " + block.getSubtype());
});
server.start();
```


## Dependencies
The following dependencies are required, and are handled automatically through Maven:
- [Gson 2.8.6](https://github.com/google/gson)

---

<sup><sup>If you found this library useful and would like to support me, my Nano address is 
<b>nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz</b> - 
any amount would be greatly appreciated :D</sup></sup>