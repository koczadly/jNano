# Introduction to jNano
jNano is a Java library used to interface with the official Nano node via RPC requests.
It also includes many native utilities and tools so that a node isn't always necessary.

Almost all of the features have been implemented, however it is still currently a work in progress.
Expecting full completion of the project around the end of March.


## Configuring the node
Before using this library, you will need to configure the node correctly.
- If you wish to make RPC requests using this library, [view the queries wiki page](https://github.com/koczadly/jNano/wiki/Query-requests#node-configuration).
- If you are wanting to listen for live blocks on the Nano network, [view the callback wiki page](https://github.com/koczadly/jNano/wiki/Block-callback#node-configuration).

## Examples
Examples on how to use this library can be found on the associated [wiki pages](https://github.com/koczadly/jNano/wiki/):
- For examples on how to use the request/query functions, [click here](https://github.com/koczadly/jNano/wiki/Query-requests#how-to-use-the-library).
- For examples on how to use the real-time block callback listener, [click here](https://github.com/koczadly/jNano/wiki/Block-callback#how-to-use-the-library).
- For other information on the utilities offered by this library, [click here](https://github.com/koczadly/jNano/wiki/Utilities).

## Use in your project
### Maven
To import jNano into your own project, the dependency can be imported as demonstrated:

- Add the JitPack repository in your pom.xml:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
- Import the dependency into your pom.xml:
```xml
<dependency>
    <groupId>com.github.koczadly</groupId>
    <artifactId>jnano</artifactId>
    <version>1.0.0</version>
</dependency>
```


## Dependencies
- [Gson 2.8.2](https://github.com/google/gson)


## Future development
A few features or commands may be currently outdated, although most should remain functional. Additional features
are present on the `feature/util/account` branch

---

<sup><sup><sup>If you found this library useful and would like to make a donation, my account is <b>xrb_34qjpc8t1u6wnb584pc4iwsukwa8jhrobpx4oea5gbaitnqafm6qsgoacpiz</b> - Any donations are greatly appreciated :D</sup></sup></sup>