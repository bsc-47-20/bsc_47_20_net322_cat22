# NET322 Continuous Assessment Test II

## Task

You are required to implement a simple Java NIO-based concurrent HTTP server.
_Lookout for **TODO** directive declarations in the source files_

### Milestones

1. **Objective 1** : Implement the constructor in the `SimpleNIOHTTPServer` source file.

   - Marks : 4

2. **Objective 2** : Implement the `run()` method in the `SimpleNIOHTTPServer` source file such that once called on a `SimpleHTTPServer` instance, the concurrent server should start.
   The server implementation is supposed to make use of `java.nio` channels,buffers and selector to achieve concurrency.

The server should be able to handle HTTP client requests that serve the HTML template resources found in the `templates` directory.

#### Expectation

1. When the server program is running,for instance on localhost:8085, should a Web browser fetch `http://localhost:8085/`, the server should respond with contents of the `index.html` template resource.
2. Similarly, if browser requests for `http://localhost:8085/register`, the server should respond with the contents of the `register.html` template resource.
3. Finally when the client (Web browser) is served registration template when calling `http://localhost:8085/register`, on submission of form shown on the page, the server should capture the form details and write the `username` and `email` to a TEXT file called `db.txt`. [HINT : Trap the POST method request].

   The username and email address fields should be written to the `db.txt` as a single line in this format.
    ```bash
        username email_address
    ``` 
Each entry should be written as a single line, any subsequent entires should be written on a separate line in the same `db.txt` file.
    
Achieving this objective fetches a total of 50 marks.

3. **Objective 3** : Complete the `HTTPServerRunner` main method such that on execution the server program should take two commandline arguments `bindAddress` and `bindPort`.
    - Example exec command
        ```bash
            java HTTPServerRunner localhost 8085
        ```
    - Mark : 6

## Instructions
Refer to the source files provided.

**Submission of unoriginal work shall be penalized accordingly.**
````
