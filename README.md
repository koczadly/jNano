# Introduction to jNano
jNano is a Java library for interfacing with the Nano node via RPC requests.

Currently a work in progress, expecting full completion by 3rd March 2018 :)


## Usage
### Sending query requests
```java
try {
    //Initialise an RPC node
    RPCQueryNode node = new RPCQueryNode(); //Defaults to 127.0.0.1:7076, unless otherwise specified
    
    //Request node version
    NodeVersionRequest verRq = new NodeVersionRequest();
    NodeVersionResponse verRs = node.processRequest(verRq);
    
    //Output node version
    System.out.println("Node version: " + verRs.getNodeVendor());
} catch (RPCQueryException | IOException e) {
    e.printStackTrace();
}
```
```text
Node version: RaiBlocks 10.0
```

### Handing live callback requests
Still in development

## Dependencies
- Gson 2.8.2