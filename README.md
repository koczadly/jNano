# <img src="https://svgur.com/i/U0r.svg" height=28/> jNano | Java library for Nano
[![Maven Central](https://img.shields.io/maven-central/v/uk.oczadly.karl/jnano)](https://maven-badges.herokuapp.com/maven-central/uk.oczadly.karl/jnano)

jNano is a Java library used to interface with the official [Nano node](https://github.com/nanocurrency/nano-node)
 via RPC requests. It also includes many built-in utilities so that a node isn't always necessary. 
This library is compatible with Java versions 8 and above (module `uk.oczadly.karl.jnano` for versions 9+).
 
Nano is a crypto-currency which aims to offer instantaneous feeless transactions. For more information, visit
[https://nano.org](https://nano.org).

## Features
This library provides you simple access to the following:
- [RPC Queries](https://github.com/koczadly/jNano/wiki/RPC-Queries)
- [WebSocket notifications](https://github.com/koczadly/jNano/wiki/WebSocket-communication)
- [~~HTTP Block callback server~~](https://github.com/koczadly/jNano/wiki/Block-callback) (deprecated, prefer websockets where possible)
- Various built-in utilities:
  - Block [creation](https://github.com/koczadly/jNano/wiki/Utilities#creation--construction) / [signing](https://github.com/koczadly/jNano/wiki/Utilities#signing) / [hashing](https://github.com/koczadly/jNano/wiki/Utilities#hashing)
  - [Work generation](https://github.com/koczadly/jNano/wiki/Utilities#work-generation) (with support for GPU and [DPoW](https://dpow.nanocenter.org/))
  - Account [parsing](https://github.com/koczadly/jNano/wiki/Utilities#accounts) / [validation](https://github.com/koczadly/jNano/wiki/Utilities#validation)
  - [Unit conversions](https://github.com/koczadly/jNano/wiki/Utilities#unit-conversion)
  - [Constants for Nano (and Banano)](https://github.com/koczadly/jNano/wiki/Utilities#constants)

## Usage
### Download
This project is hosted on Maven Central. You can also download the compiled JAR directly from [here](https://maven-badges.herokuapp.com/maven-central/uk.oczadly.karl/jnano).
#### Maven
```xml
<dependency>
    <groupId>uk.oczadly.karl</groupId>
    <artifactId>jnano</artifactId>
    <version>2.18.0</version>
</dependency>
```
#### Gradle
```gradle
dependencies {
    implementation 'uk.oczadly.karl:jnano:2.18.0'
}
```

### Documentation
- [Javadocs (via Javadoc.io)](https://www.javadoc.io/doc/uk.oczadly.karl/jnano/latest/uk.oczadly.karl.jnano-summary.html)
- [Wiki docs & examples](https://github.com/koczadly/jNano/wiki/)

### Examples
#### RPC Queries [\[Wiki\]](https://github.com/koczadly/jNano/wiki/RPC-Queries)
To make queries to an external Nano node through the RPC system, you will need to use the [RpcQueryNode](https://www.javadoc.io/doc/uk.oczadly.karl/jnano/latest/uk/oczadly/karl/jnano/rpc/RpcQueryNode.html)
 class. You can customize these objects even further by constructing using the nested `Builder` class.
```java
RpcQueryNode rpc = new RpcQueryNode();          // Using localhost:7076
RpcQueryNode rpc = RpcServiceProviders.nanex(); // Using nanex.cc public API
```
This example will print an account's balance to the console using a synchronous (blocking) call.
```java
// Construct and execute the request, and obtain the response
ResponseBalance balance = rpc.processRequest(new RequestAccountBalance(
        "nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz"));

// Handle the result object however you wish (eg. print the balance)
System.out.println("Account balance: " + balance.getTotal());
```

#### WebSockets (listening for blocks) [\[Wiki\]](https://github.com/koczadly/jNano/wiki/WebSocket-communication)
The following will create a WebSocket listener which connects to port `7078` on `localhost`. For each new block
 confirmed by the node, the hash and type will be printed to the console.
```java
NanoWebSocketClient ws = new NanoWebSocketClient(); // Defaults to localhost:7078
ws.connect(); // Connect to the endpoint

// Register a listener (will be called for each new block)
ws.getTopics().topicConfirmedBlocks().registerListener((message, context) -> {
    // Print the hash and type of all confirmed blocks
    System.out.printf("Confirmed block: %s (%s)%n",
            message.getHash(), message.getBlock().getType());
});

// Subscribe to the block confirmations topic
ws.getTopics().topicConfirmedBlocks().subscribeBlocking(new TopicConfirmation.SubArgs()
        .includeBlockContents() // Include block info in messages
        .filterAccounts( // Only receive blocks for these accounts
                "nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz",
                "nano_1ipx847tk8o46pwxt5qjdbncjqcbwcc1rrmqnkztrfjy5k7z4imsrata9est"));
```

#### Block Creation [\[Wiki\]](https://github.com/koczadly/jNano/wiki/Utilities#creation--construction)
The following sample will create a new `state` block. The block will be signed using the provided private key, and
 work will be generated in the JVM using the CPU.
```java
WorkGenerator workGenerator = new CPUWorkGenerator();

StateBlock block = new StateBlockBuilder(StateBlockSubType.OPEN)
        .setLink("BF4A559FEF44D4A9C9CEF4972886A51FC83AD1A2BEE4CDD732F62F3C166D6D4F")
        .setBalance("123000000000000000000000000")
        .generateWork(workGenerator)
        .buildAndSign("A3293644AC105DEE5A0202B7EF976A06E790908EE0E8CC43AEF845380BFF954E"); // Private key

String hash = block.getHash().toHexString(); // Hashes the block
String blockJson = block.toJsonString(); // Serializes the block to a JSON object
```


## Dependencies
This project uses the following dependencies, which are included automatically through Maven:
- [Gson 2.8.6](https://github.com/google/gson)
- [blake2b 1.0.0](https://github.com/rfksystems/blake2b)
- [ed25519-java 0.3.0](https://github.com/str4d/ed25519-java)
- [Java-WebSocket 1.5.1](https://github.com/TooTallNate/Java-WebSocket)
- [JOCL 2.0.2](https://github.com/gpu/JOCL)

---

<sup>If you found this library useful and would like to support my work, donations may be sent to 
<b>`nano_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz`</b> - any amount would be greatly
 appreciated :D</sup>