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
  - `ServiceDocs`:
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
  }
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


## Running the Application

1. **Start the servlet container** (e.g., Apache Tomcat).
2. **Deploy the WAR file** generated by your build tool.
3. **Access the services** via a web browser or API client (e.g., Postman).

Example URLs:
- To add a student: `http://localhost:8080/StudentService/student/add?name=John&age=22`
- To get a student: `http://localhost:8080/StudentService/student/get?id=1`

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0) file for more details.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request with your changes.
