## Getting Started

In this chapter, we consider a simple example that will give basic knowledge on the
use of this framework. For this you need [JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
1.6 or higher and [Apache Maven](https://maven.apache.org/).

### Maven Setup

In the `pom.xml` file you need to add the following dependency:

```xml
<dependencies>
    <dependency>
        <groupId>org.foxlabs</groupId>
        <artifactId>foxlabs-validation</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```

No other dependencies are required.

### Writing Bean

Lets create an `Account` class:
                
```java
import org.foxlabs.validation.constraint.*;

public class Account {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    
    @NotNull
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Coalesce @NotEmpty
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Despace @NotEmpty @EmailAddress
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @NotEmpty @Size(min = 6, max = 32)
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
}
```

Here you define the following constraints:

- `id` should never be `null`.
- `username` should never be empty, all extra whitespaces will be removed.
- `email` should be non-empty valid email address, all whitespaces will be removed.
- `password` should be non-empty with length from 6 to 32 characters.

### Validating Bean

In order to validate bean you need a few lines of code.

```java
Account account = new Account();
account.setId(1L);
account.setUsername("Fox Mulder");
account.setEmail("foxinboxx@gmail.com");
account.setPassword("The truth is out there");

Validator<Account> validator = ValidatorFactory.getDefault().newValidator(Account.class);
try {
    validator.validateEntity(account);
} catch (ValidationException e) {
    e.printViolations();
}
```

In the example above, we create a new `Account`, initialize its properties and
perform validation. All violations will be printed to the `System.err`. You can
experiment with the values ​​of the properties to see validation results.
