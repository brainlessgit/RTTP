This is a "client" for second task. This is a "client" for other nodes in this (second task) app, but it could be used as a "server" from another app, like Spring app uses it for saving data.

Client connects to all given servers (Alpha and Beta, but it could be more)  via Sockets and uses protobuf to communicate with them.

Client's interface are in `com.kisel.client.controller.Controller.java` and consists of next methods:
  * `boolean register(Alien alien)`;
  * `Alien auth(String name, String password)`;
  * `List<Alien> search(String name)`.

So it uses for registration, authenticating and searching Aliens.

Data partitioning idea is implemented here. Directly in `register` method. Every Alien have an `address` (integer, maybe ip). With some trivial algorithm "client" chooses a server where Alien must be persisted.

When authenticating or searching methods are working "client" sends messages for each server he is connected and collect all results together.

The "client" is not needed to be run. Spring app just uses Controller class as bean.
