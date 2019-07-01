# Introduction to jNano
jNano is a Java library used to interface with the official Nano node via RPC requests.
It also includes many native utilities and tools so that a node isn't always necessary.

Almost all of the features have been implemented, however it is still currently a work in progress.


## Usage
### Maven
This project is hosted on Maven Central. To import this library, add the following dependency into your pom.xml:
```xml
<dependency>
    <groupId>uk.oczadly.karl</groupId>
    <artifactId>jnano</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Documentation
The latest Javadoc pages can be [viewed online through Javadoc.io](https://www.javadoc.io/doc/uk.oczadly.karl/jnano).
Additional documentation can be found on the [wiki pages](https://github.com/koczadly/jNano/wiki/).

### Configuring the node
Before using this library, you will need to configure the node correctly.
- If you wish to make RPC requests using this library, [view the queries wiki page](https://github.com/koczadly/jNano/wiki/Query-requests#node-configuration).
- If you are wanting to listen for live blocks on the Nano network, [view the callback wiki page](https://github.com/koczadly/jNano/wiki/Block-callback#node-configuration).

### Examples
Examples can be found on the associated wiki pages linked below:
- [Query requests and operation execution](https://github.com/koczadly/jNano/wiki/Query-requests#how-to-use-the-library).
- [Real-time network block listener](https://github.com/koczadly/jNano/wiki/Block-callback#how-to-use-the-library).
- [Additional tools and utilities](https://github.com/koczadly/jNano/wiki/Utilities).


## Dependencies
The following dependencies are required, and are handled automatically through Maven:
- [Gson 2.8.2](https://github.com/google/gson)


## Future development
A few features or commands may be currently outdated, although most should remain functional. Additional features
can be found on the `feature/util/account` branch, although have no guaranteed functionality or documentation.

---

<sup><sup>If you found this library useful and would like to offer a donation, my Nano address is <b>xrb_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz</b> - any amount would be greatly appreciated :D</sup></sup>