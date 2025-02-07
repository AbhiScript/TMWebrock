# TM Web Rock Project

## Overview

TM Web Rock is a Java-based web application framework designed to simplify the development of web applications. This repository contains the essential configuration and source code for the TM Web Rock project.

## Project Structure

### Root Directory (`TMWebRock`)

The root directory contains the main resources for the web application:

```
C:\tomcat9\webapps\TMWebRock
│
├── index.html
├── jquery/
├── list.jsp
├── login.jsp
├── student.js
├── styles.css
└── WEB-INF/
```

- **index.html**: The main HTML file for the web application.
- **jquery/**: Directory containing jQuery files.
- **list.jsp**: JSP file for listing content.
- **login.jsp**: JSP file for login functionality.
- **student.js**: JavaScript file for student-related functionalities.
- **styles.css**: CSS file for styling.

### `WEB-INF` Directory

The `WEB-INF` directory contains configuration files and compiled classes:

```
C:\tomcat9\webapps\TMWebRock\WEB-INF
│
├── classes/
│
├── js/
│
├── lib/
│
├── serviceDocs/
└── web.xml
```

- **classes/**: Directory containing compiled Java classes.
- **js/**: Directory for JavaScript files.
- **lib/**: Directory for library files (JARs).
- **serviceDocs/**: Directory for service documentation.
- **web.xml**: Deployment descriptor for the web application.

### `classes` Directory

The `classes` directory contains the compiled Java classes and packages:

```
C:\tomcat9\webapps\TMWebRock\WEB-INF\classes
│
├── bobby/
├── com/
│   └── thinking/
│       └── machines/
│           └── webrock/
│               ├── annotations/
│               ├── JSFileServlet.class
│               ├── JSFileServlet.java
│               ├── model/
│               ├── pojo/
│               ├── scope/
│               ├── ServiceDocs.class
│               ├── ServiceDocs.java
│               ├── ServiceException.class
│               ├── ServiceException.java
│               ├── TMWebRock$Scope.class
│               ├── TMWebRock$Token.class
│               ├── TMWebRock.class
│               ├── TMWebRock.java
│               ├── TMWebRockStarter.class
│               ├── TMWebRockStarter.java
│               └── tokenManager/
```

- **bobby/**: Custom package for application-specific classes.
- **com/thinking/machines/webrock/**: Core package for the TM Web Rock framework.
  - **annotations/**: Contains annotations used in the framework.
  - **JSFileServlet**: Servlet for handling JavaScript files.
  - **model/**: Contains data models.
  - **pojo/**: Contains Plain Old Java Objects (POJOs).
  - **scope/**: Contains scope-related classes.
  - **ServiceDocs**: Servlet for generating service documentation.
  - **ServiceException**: Custom exception class for services.
  - **TMWebRock**: Core servlet class for the framework.
  - **TMWebRockStarter**: Starter servlet class for initializing the framework.
  - **tokenManager/**: Contains classes related to token management.

## Configuration Details

### `web.xml`

The `web.xml` file is the central configuration file for the web application. Below are the main components defined in this file:

- **Description and Display Name**:
  ```xml
  <description>TM Web Rock</description>
  <display-name>TM Web Rock</display-name>
  ```

- **Character Encoding**:
  ```xml
  <request-character-encoding>UTF-8</request-character-encoding>
  ```

- **Servlets and Mappings**:
  - `TMWebRockStarter`:
    ```xml
    <servlet>
      <servlet-name>TMWebRockStarter</servlet-name>
      <servlet-class>com.thinking.machines.webrock.TMWebRockStarter</servlet-class>
      <init-param>
        <param-name>SERVICE_PACKAGE_PREFIX</param-name>
        <param-value>bobby</param-value>
      </init-param>
      <load-on-startup>0</load-on-startup>
    </servlet>
    <servlet-mapping>
      <servlet-name>TMWebRockStarter</servlet-name>
      <url-pattern>/starter</url-pattern>
    </servlet-mapping>
    ```
   - `TMWebRock`:
    ```xml
    <servlet>
      <servlet-name>TMWebRock</servlet-name>
      <servlet-class>com.thinking.machines.webrock.TMWebRock</servlet-class>
    </servlet>
    <servlet-mapping>
      <servlet-name>TMWebRock</servlet-name>
      <url-pattern>/StudentService/*</url-pattern>
    </servlet-mapping>
    ```

### `TMWebRockStarter.java`

This file contains the implementation of the `TMWebRockStarter` servlet, which is responsible for initializing the TM Web Rock framework. It loads the necessary configurations and sets up the environment for the application to run.

### `TMWebRock.java`

This file contains the core implementation of the `TMWebRock` servlet. It handles the main functionalities of the TM Web Rock framework, including processing requests and managing responses.

## Getting Started

To get started with the TM Web Rock project:

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   ```

2. **Build the project** using your preferred build tool (e.g., Maven, Gradle).

3. **Deploy the application** to a compatible servlet container (e.g., Apache Tomcat).

4. **Access the application** via the defined URL patterns:
   - `/starter`
   - `/JSFile`
   - `/serviceDocs`
   - `/StudentService/*`

## Usage

### Using Annotations

#### @Path Annotation

The `@Path` annotation is used to map a method or class to a specific URL path. This annotation is essential for routing requests to the appropriate service methods in your application.

##### Example Usage

**Class-Level Path Mapping**:
```java
import com.thinking.machines.webrock.annotations.*;

@Path("/exampleService")
public class ExampleService {
    @Path("/exampleMethod")
    public void exampleMethod() {
        // This method is mapped to /exampleService/exampleMethod
    }
}
```

**Method-Level Path Mapping**:
```java
import com.thinking.machines.webrock.annotations.*;

public class ExampleService {
    @Path("/exampleService")
    public void exampleMethod() {
        // This method is mapped to /exampleService
    }
}
```

### @GET Annotation

The `@GET` annotation is used to specify that a class or method should only handle HTTP GET requests. If a GET request is not received, an error is returned.

##### Example Usage

**Class-Level GET Request Handling**:
```java
import com.thinking.machines.webrock.annotations.*;

@GET
public class ExampleService {
    @Path("/example")
    public void exampleMethod() {
        // This method will only process GET requests
    }
}
```

**Method-Level GET Request Handling**:
```java
import com.thinking.machines.webrock.annotations.*;

public class ExampleService {
    @Path("/example")
    @GET
    public void exampleMethod() {
        // This method will only process GET requests
    }
}
```

### @POST Annotation

The `@POST` annotation is used to specify that a class or method should only handle HTTP POST requests. If a POST request is not received, an error is returned.

##### Example Usage

**Class-Level POST Request Handling**:
```java
import com.thinking.machines.webrock.annotations.*;

@POST
public class ExampleService {
    @Path("/example")
    public void exampleMethod() {
        // This method will only process POST requests
    }
}
```

**Method-Level POST Request Handling**:
```java
import com.thinking.machines.webrock.annotations.*;

public class ExampleService {
    @Path("/example")
    @POST
    public void exampleMethod() {
        // This method will only process POST requests
    }
}
```

### @ForwardTo Annotation

The `@ForwardTo` annotation is used to forward a request from one service method to another. When a method annotated with `@ForwardTo` completes its processing, the request is forwarded to the method specified in the annotation's parameter.

If the path specified in the `@ForwardTo` annotation does not exist, a 500 error is returned. If the method returns a value, and the forwarded method accepts a parameter, the returned value is passed as an argument. If the types do not match, an exception is raised, and a 500 error is returned.

##### Example Usage

```java
import com.thinking.machines.webrock.annotations.*;

public class ExampleService {
    @Path("/initialService")
    @ForwardTo("/nextService")
    public String initialMethod() {
        // Processing logic
        return "Forwarded Value";
    }

    @Path("/nextService")
    public void nextMethod(String value) {
        // This method will process the forwarded request with the passed value
        System.out.println("Received value: " + value);
    }
}
```

### @OnStartup Annotation

The `@OnStartup` annotation is used to execute tasks on server startup with a specific priority. When the server starts and the entire framework is ready, all methods marked with `@OnStartup` will be invoked. These methods must have a return type of `void` and should not accept any parameters. If a method returns a value or has parameters, it will be ignored. Forwarding checks are not performed after these methods are executed.

##### Example Usage

```java
import com.thinking.machines.webrock.annotations.*;

public class ExampleService {

    @OnStartup(priority = 0)
    public void initialize() {
        // Initialization logic that runs on server startup
        System.out.println("Server startup task executed with priority 0");
    }
    
    @OnStartup(priority = 1)
    public void anotherStartupTask() {
        // Another startup task
        System.out.println("Server startup task executed with priority 1");
    }
}
```

### `@InjectSessionScope` Annotation

The `@InjectSessionScope` annotation is used to inject a `SessionScope` object into a class, allowing the class to access and manipulate session-scoped attributes. This is achieved by following the Inversion of Control (IoC) principle, where the framework sets the session scope object before invoking any service methods.

#### Usage:
1. Annotate the class with `@InjectSessionScope`.
2. Create a `SessionScope` property in the class.
3. Implement setter and getter methods for the `SessionScope` property.
4. The framework will call the `setSessionScope` method to inject the session scope object.
5. Use the `SessionScope` object to set or retrieve session-scoped attributes.

#### Example:
```java
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.*;

@Path("/classpath")
@InjectSessionScope
public class MyClass {
    private SessionScope sessionScope;

    public void setSessionScope(SessionScope sessionScope) {
        this.sessionScope = sessionScope;
    }

    @Path("/somePath1")
    public void exampleMethod1() {
        Object value = "someValue";
        sessionScope.setAttribute("key", value);
    }

    @Path("/somePath2")
    public void exampleMethod2() {
        Object value = sessionScope.getAttribute("key");
        System.out.println(value);
    }
}
```

### `@getSessionScope` Annotation

The `@getSessionScope` annotation is used to retrieve a session-scoped attribute directly into a method parameter.

#### Usage:
1. Annotate the method parameter with `@getSessionScope("key")`.
2. The type of the parameter should match the type of the value stored in the session scope.

#### Example:
```java
import com.thinking.machines.webrock.annotations.*;

@Path("/examplePath")
public class MyClass {

    @Path("/somePath")
    public void exampleMethod(@getSessionScope("key") Object value) {
        // Now this "value" can be used as a method variable in this service
        System.out.println(value);
    }
}
```

### `@InjectApplicationScope` Annotation

The `@InjectApplicationScope` annotation is used to inject an `ApplicationScope` object into a class, allowing the class to access and manipulate application-scoped attributes. This is achieved by following the Inversion of Control (IoC) principle, where the framework sets the application scope object before invoking any service methods.

#### Usage:
1. Annotate the class with `@InjectApplicationScope`.
2. Create an `ApplicationScope` property in the class.
3. Implement setter and getter methods for the `ApplicationScope` property.
4. The framework will call the `setApplicationScope` method to inject the application scope object.
5. Use the `ApplicationScope` object to set or retrieve application-scoped attributes.

#### Example:
```java
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.*;

@Path("/classpath")
@InjectApplicationScope
public class MyClass {
    private ApplicationScope applicationScope;

    public void setApplicationScope(ApplicationScope applicationScope) {
        this.applicationScope = applicationScope;
    }

    @Path("/somePath1")
    public void exampleMethod1() {
        Object value = "someValue";
        applicationScope.setAttribute("key", value);
    }

    @Path("/somePath2")
    public void exampleMethod2() {
        Object value = applicationScope.getAttribute("key");
        System.out.println(value);
    }
}
```

### `@getApplicationScope` Annotation

The `@getApplicationScope` annotation is used to retrieve an application-scoped attribute directly into a method parameter.

#### Usage:
1. Annotate the method parameter with `@getApplicationScope("key")`.
2. The type of the parameter should match the type of the value stored in the application scope.

#### Example:
```java
import com.thinking.machines.webrock.annotations.*;

@Path("/examplePath")
public class MyClass {

    @Path("/somePath")
    public void exampleMethod(@getApplicationScope("key") Object value) {
        // Now this "value" can be used as a method variable in this service
        System.out.println(value);
    }
}
```

### `@InjectRequestScope` Annotation

The `@InjectRequestScope` annotation is used to inject a `RequestScope` object into a class, allowing the class to access and manipulate request-scoped attributes. This is achieved by following the Inversion of Control (IoC) principle, where the framework sets the request scope object before invoking any service methods.

#### Usage:
1. Annotate the class with `@InjectRequestScope`.
2. Create a `RequestScope` property in the class.
3. Implement setter and getter methods for the `RequestScope` property.
4. The framework will call the `setRequestScope` method to inject the request scope object.
5. Use the `RequestScope` object to set or retrieve request-scoped attributes.

#### Example:
```java
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.*;

@Path("/classpath")
@InjectRequestScope
public class MyClass {
    private RequestScope requestScope;

    public void setRequestScope(RequestScope requestScope) {
        this.requestScope = requestScope;
    }

    @Path("/somePath1")
    public void exampleMethod1() {
        Object value = "someValue";
        requestScope.setAttribute("key", value);
    }

    @Path("/somePath2")
    public void exampleMethod2() {
        Object value = requestScope.getAttribute("key");
        System.out.println(value);
    }
}
```

### `@getRequestScope` Annotation

The `@getRequestScope` annotation is used to retrieve a request-scoped attribute directly into a method parameter.

#### Usage:
1. Annotate the method parameter with `@getRequestScope("key")`.
2. The type of the parameter should match the type of the value stored in the request scope.

#### Example:
```java
import com.thinking.machines.webrock.annotations.*;

@Path("/examplePath")
public class MyClass {

    @Path("/somePath")
    public void exampleMethod(@getRequestScope("key") Object value) {
        // Now this "value" can be used as a method variable in this service
        System.out.println(value);
    }
}
```

These annotations ensure that your services can manage different scopes (session, application, and request) effectively, allowing for a cleaner and more organized way to handle scoped data.

### `@Autowired(name="key")` Annotation

The `@Autowired(name="key")` annotation is designed to facilitate dependency injection at the class-level for properties. When this annotation is used on a property, the framework will inject the corresponding object from a predefined pool of instances mapped by the specified key.

#### Usage Guidelines

1. **Class-level Injection:**
   To inject a dependency into a class-level property, you annotate the property with `@Autowired(name="key")`. The framework will automatically set the property to the corresponding instance from its pool.

   **Example:**
   ```java
   import com.thinking.machines.webrock.annotations.*;

   @Path("/examplePath")
   public class MyClass
   {
       @Autowired(name="someService")
       private SomeService someService;

       @Path("/someMethod")
       public void someMethod()
       {
           // Use the injected service
           someService.performAction();
       }
   }
   ```

2. **Dependency Injection Process:**
   - The framework maintains a pool of instances mapped by keys.
   - When a service is requested, the framework checks for any properties annotated with `@Autowired(name="key")`.
   - The corresponding instance from the pool is then injected based on the specified key.

#### Important Considerations

- Ensure that the specified key in the `@Autowired` annotation matches the key used to register the instance in the framework's pool.
- The type of the injected property must match the type of the instance associated with the key.
- This annotation simplifies dependency management, ensuring that dependencies are automatically resolved and injected where needed.

### Example in Context

In a real-world scenario, the `@Autowired(name="key")` annotation might be used to inject service instances into controllers or other services. Here’s a comprehensive example:

```java
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.*;

@Path("/user")
public class UserController
{
   @Autowired(name="userService")
   private UserService userService;

   @Path("/getUserDetails")
   public User getUserDetails()
   {
       // Using the injected UserService
       return userService.findUserById(123); // Example user ID
   }
}
```

In this example, the `UserController` class has a dependency on `UserService`, which is injected using the `@Autowired(name="userService")` annotation. This approach ensures a clean and maintainable codebase with minimal manual wiring of dependencies.

### `@RequestParameter("key")` Annotation

The `@RequestParameter("key")` annotation is used to inject values from the request URL query string into method parameters. When a method parameter is annotated with `@RequestParameter("key")`, the framework will automatically extract the corresponding value from the query string and inject it into the parameter. This allows for easy access to request parameters within your service methods.

#### Usage Guidelines

1. **Method Parameter Injection:**
   To inject a request parameter value into a method parameter, annotate the parameter with `@RequestParameter("key")`. The `key` should match the name of the query string parameter in the request URL.

   **Example:**
   ```java
   import com.thinking.machines.webrock.annotations.*;

   @Path("/example")
   public class ExampleService
   {
       @Path("/process")
       public void processRequest(@RequestParameter("id") String id)
       {
           // Use the injected request parameter
           System.out.println("Received ID: " + id);
       }
   }
   ```

2. **Request Handling:**
   - The framework parses the query string of the incoming request.
   - It then matches the query string parameters with the keys specified in the `@RequestParameter` annotations.
   - The corresponding values are injected into the annotated method parameters.

#### Important Considerations

- Ensure that the `key` in the `@RequestParameter` annotation matches the name of the query string parameter in the request URL.
- The type of the method parameter should be compatible with the query string value. For instance, if the query string value is a number, the parameter should be of a suitable numeric type (e.g., `int` or `Integer`).

### Example in Context

Here is a more comprehensive example demonstrating how to use `@RequestParameter("key")` in a real-world scenario:

```java
import com.thinking.machines.webrock.annotations.*;

@Path("/user")
public class UserService
{
   @Path("/getDetails")
   public User getUserDetails(@RequestParameter("userId") int userId)
   {
       // Retrieve user details based on the provided userId
       User user = findUserById(userId);
       return user;
   }

   private User findUserById(int userId)
   {
       // Logic to find and return a user by their ID
       // For illustration purposes, returning a dummy user
       return new User(userId, "John Doe", "john.doe@example.com");
   }
}
```

In this example, the `UserService` class has a method `getUserDetails` that takes a user ID from the request query string and retrieves the corresponding user details. The `userId` parameter is annotated with `@RequestParameter("userId")`, indicating that the value should be extracted from the query string of the request URL.

By using the `@RequestParameter("key")` annotation, you can easily access request parameters in your service methods, making your code cleaner and more maintainable.

#### `@CreatePOJO`

- **Description**: Automatically creates and injects a POJO.
- **Usage**:
  ```java
  @CreatePOJO
  public class MyPOJO {
    // POJO properties
  }
  ```

#### `@CreateService`

- **Description**: Marks a class as a service that should be created and managed by the framework.
- **Usage**:
  ```java
  @CreateService
  public class MyService {
    // Service methods
  }```
  
#### Example HTML Usage

Include the following script tag in your HTML file to generate and load the `student.js` file:

```html
<script src="http://localhost:8080/TMWebRock/JSFile?name=student.js"></script>
```

#### Explanation

1. **URL**: `http://localhost:8080/TMWebRock/JSFile?name=student.js`
   - `http://localhost:8080` is the base URL of your web application running on the local server.
   - `/TMWebRock` is the context path of your web application.
   - `/JSFile` is the mapping of the `JSFileServlet` in your web application.
   - `?name=student.js` is a query parameter that specifies the name of the JavaScript file to be generated.

2. **Servlet Processing**:
   - When this script tag is encountered, the browser sends a GET request to the specified URL.
   - The `JSFileServlet` processes the request, generates the `student.js` file based on the annotated Java classes (`CreatePOJO` and `CreateService`), and serves the JavaScript file as the response.

3. **JavaScript File Generation**:
   - The `JSFileServlet` scans the specified package for classes annotated with `CreatePOJO` and `CreateService`.
   - It generates JavaScript classes for POJOs and service methods according to the annotations.
   - The generated `student.js` file is then served to the client.

#### Complete Example

Here’s how you can set up the servlet and include the script in an HTML file:

**web.xml Configuration**:

```xml
<web-app>
  <servlet>
    <servlet-name>JSFileServlet</servlet-name>
    <servlet-class>com.thinking.machines.webrock.JSFileServlet</servlet-class>
    <init-param>
      <param-name>SERVICE_PACKAGE_PREFIX</param-name>
      <param-value>com.thinking.machines.services</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>JSFileServlet</servlet-name>
    <url-pattern>/JSFile</url-pattern>
  </servlet-mapping>
</web-app>
```
### `CreatePOJO` Annotation

The `CreatePOJO` annotation is used to mark a class as a Plain Old Java Object (POJO) for the framework to generate a corresponding JavaScript class.

**Usage Example:**

```java
import com.thinking.machines.webrock.annotations.CreatePOJO;

@CreatePOJO
public class Student {
    private int rollNumber;
    private String name;
    
    // Constructors, getters, and setters
    public Student() {}
    
    public Student(int rollNumber, String name) {
        this.rollNumber = rollNumber;
        this.name = name;
    }
    
    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

### `CreateService` Annotation

The `CreateService` annotation is used to mark a class or method as a service, which the framework will expose as JavaScript functions.

**Usage Example at Class Level:**

```java
import com.thinking.machines.webrock.annotations.CreateService;
import com.thinking.machines.webrock.annotations.Path;
import com.thinking.machines.webrock.annotations.Get;
import com.thinking.machines.webrock.annotations.Post;

@CreateService
@Path("/student")
public class StudentService {

    @Get
    @Path("/getAll")
    public List<Student> getAllStudents() {
        // Implementation to get all students
        return new ArrayList<Student>();
    }

    @Post
    @Path("/add")
    public void addStudent(Student student) {
        // Implementation to add a student
    }
}
```

**Usage Example at Method Level:**

```java
import com.thinking.machines.webrock.annotations.CreateService;
import com.thinking.machines.webrock.annotations.Path;
import com.thinking.machines.webrock.annotations.Get;

public class StudentService {

    @CreateService
    @Get
    @Path("/getAll")
    public List<Student> getAllStudents() {
        // Implementation to get all students
        return new ArrayList<Student>();
    }

    @CreateService
    @Post
    @Path("/add")
    public void addStudent(Student student) {
        // Implementation to add a student
    }
}
```
These classes are essential for managing different scopes within the TM Web Rock framework, allowing developers to store and retrieve data within the appropriate context (application, request, or session).

### Token Management

The `Token.java` file is part of the `com.thinking.machines.webrock.tokenManager` package and is used for managing tokens, likely for purposes such as session management, authentication, or authorization.

**Usage:**

- **Creating a token:**
  ```java
  Token token = Token.getInstance();
  double generatedToken = token.generateToken();
  ```

- **Setting a token for a service method:**
  ```java
  token.setToken("methodName", generatedToken);
  ```

### Secured Access

The `SecuredAccess.java` file is part of the `com.thinking.machines.webrock.annotations` package and is used for restricting access to methods or classes based on custom logic.

**Usage:**

1. **Implementing the `checkPost` class:**
   ```java
   package abc.pqr.lmn;

   public class SecurityCheck {
       public void efgh() throws SecurityException {
           // Custom logic to check if the user is authenticated
           // Throw SecurityException if not authenticated
       }
   }
   ```

2. **Using `SecuredAccess` annotation in a service:**
   ```java
   @SecuredAccess(checkPost="abc.pqr.lmn.SecurityCheck", guard="efgh")
   public class SomeService {
       public void someMethod() {
           // This method is secured and can only be accessed if the user is authenticated
       }
   }
   ```

3. **Generating and setting a token in a login service:**
   ```java
   public class LoginService {
       public void login() {
           // Perform login logic
           Token token = Token.getInstance();
           double generatedToken = token.generateToken();
           token.setToken("someMethod", generatedToken);
           // Store token in session or application scope
       }
   }
   ```

In the example above, if a request goes to someMethod, an object of abc.pqr.lmn.SecurityCheck class is created, and its efgh method is called. This method may throw a SecurityException if the user is not authenticated, in which case a 404 error should be sent. If no exception is thrown, the service method is processed. This annotation can be applied to both classes and methods, and the efgh method should execute in the session scope or request scope if those scopes are present in its parameters. If scope injection is required, the scope should be injectable.

### `ServiceDocs`

The `ServiceDocs` servlet is designed to scan all the files in a specified package and generate documentation in PDF format. This PDF file will be created in the `WEB-INF` folder of your web application.

#### Configuration

To configure the `ServiceDocs` servlet, you need to add the following entries to your `web.xml` file:

```xml
<servlet>
  <servlet-name>ServiceDocs</servlet-name>
  <servlet-class>com.thinking.machines.webrock.ServiceDocs</servlet-class>
  <init-param>
    <param-name>CREATE_SERVICE_DOCS</param-name>
    <param-value>bobby</param-value>
  </init-param>
  <load-on-startup>2</load-on-startup>
</servlet>
<servlet-mapping>
  <servlet-name>ServiceDocs</servlet-name>
  <url-pattern>/serviceDocs</url-pattern>
</servlet-mapping>
```

#### Explanation

- **`<servlet-name>`**: Specifies the name of the servlet (`ServiceDocs`).
- **`<servlet-class>`**: Specifies the fully qualified class name of the servlet (`com.thinking.machines.webrock.ServiceDocs`).
- **`<init-param>`**: Specifies initialization parameters for the servlet.
  - **`<param-name>`**: The name of the parameter (`CREATE_SERVICE_DOCS`).
  - **`<param-value>`**: The value of the parameter (`bobby`), which specifies the package to be scanned.
- **`<load-on-startup>`**: Specifies the order in which the servlet should be loaded. A lower number indicates higher priority.
- **`<servlet-mapping>`**: Maps the servlet to a specific URL pattern (`/serviceDocs`).

#### Usage

When the `ServiceDocs` servlet is accessed via the URL pattern `/serviceDocs`, it will scan all the classes in the specified package (`bobby` in this case) and generate a PDF documentation file in the `WEB-INF` folder. This documentation includes details about the services, methods, and annotations used in the specified package, providing an easy reference for developers.

## Running the Application

1. **Start the servlet container** (e.g., Apache Tomcat).
2. **Deploy the WAR file** generated by your build tool.
3. **Access the services** via a web browser or API client (e.g., Postman).

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0) file for more details.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request with your changes.
