## Introduction
    
This validation framework does not implement the [JSR-303](https://jcp.org/aboutJava/communityprocess/final/jsr303/index.html)
specification. The main features of this implementation are:

- Ability to modify (correct) test values.
- Support for validation of localized values.
- Conversion of values into and from text representation.
- Formating of error messages, depending on the context.
- Support for metadata, which allows to check any entity types.
- Long list of predefined validation components.
- Good opportunities for expansion.

In chapters 3 and 4, you will find detailed information on all of the above features.

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

## Architecture

On the following class diagram you will see the key classes of this framework.

![Class Diagram](images/class-diagram.png)

Next sections describe all these classes in more details.

### Validation Components

Validation components are basic blocks of this framework. The [Validation](api/org/foxlabs/validation/Validation.html)
interface defines generic abstraction of the validation component. For the moment
there are two component types: [Constraint](api/org/foxlabs/validation/constraint/Constraint.html)
and [Converter](api/org/foxlabs/validation/converter/Converter.html).

The [ValidationContext](api/org/foxlabs/validation/ValidationContext.html) interface
provides information about current validation state to the components. Implementation
of that interface passed to all validation methods of the components.

The [Constraint](api/org/foxlabs/validation/constraint/Constraint.html) interface
defines component, suitable for check whether a test value conforms to some rules or not.
Also it allows to modify (correct) test value if it is possible. If for some reason
test value doesn't fit the validation rules and it cannot be corrected then the result
of validation should be [ConstraintViolationException](api/org/foxlabs/validation/constraint/ConstraintViolationException.html).
In other cases constraint should return (possibly modified) test value.

The [Converter](api/org/foxlabs/validation/converter/Converter.html) interface defines
component, suitable for convert value to and from text representation. If for some
reason value cannot be converted from string representation then the result of conversion
should be [MalformedValueException](api/org/foxlabs/validation/converter/MalformedValueException.html).
Conversion of a value to string representation doesn't assume any exceptions.
The [ValidationContext.isLocalizedConvert()](api/org/foxlabs/validation/ValidationContext.html#isLocalizedConvert())
method specifies if localized representation is required.

### Validation Messages

Validation component may provide error message that will be used when validation process fails.
That message may depend on validation context or component parameters. Because of that
[Validation](api/org/foxlabs/validation/Validation.html) interface defines two methods that
allow to obtain required information for error message generation. The
[Validation.getMessageTemplate()](api/org/foxlabs/validation/Validation.html#getMessageTemplate(org.foxlabs.validation.ValidationContext))
method allows to obtain localized error message template. The
[Validation.appendMessageArguments()](api/org/foxlabs/validation/Validation.html#appendMessageArguments(org.foxlabs.validation.ValidationContext, java.util.Map))
method provides arguments to be substituted into the error message template. Locale of the
error message and its arguments should be obtained from the
[ValidationContext.getMessageLocale()](api/org/foxlabs/validation/ValidationContext.html#getMessageLocale()) method.

The [MessageResolver](api/org/foxlabs/validation/message/MessageResolver.html) is used
for resolving localized message templates. That abstraction allows to store error messages
anywhere. By default `org/foxlabs/validation/resource/validation-messages`
resource bundle is used. Validation components could use
[ValidationContext.resolveMessage()](api/org/foxlabs/validation/ValidationContext.html#resolveMessage(java.lang.String))
to obtain error message templates. Fully qualified class name of the validation component
is usually used as message key.

Having localized error message template and its arguments, goal of the
[MessageBuilder](api/org/foxlabs/validation/message/MessageBuilder.html) is to build
error message. By default the [DefaultMessageBuilder](api/org/foxlabs/validation/message/DefaultMessageBuilder.html)
is used.

### Validation Exceptions

Each validation component can throw [ViolationException](api/org/foxlabs/validation/ViolationException.html)
that contains necessary information about validation state on the moment of violation,
invalid value and source component which generates exception. In case of multiple violations the
[ValidationException](api/org/foxlabs/validation/ValidationException.html) represents full stack
of violations with detailed information about each violation.

Errors in the validation component declaration propagated as the
[ValidationDeclarationException](api/org/foxlabs/validation/ValidationDeclarationException.html)
and its subclasses.

### Entity Metadata

Metadata needed to describe the entity and its properties, constraints and converters imposed on them.

The [EntityMetaData](api/org/foxlabs/validation/metadata/EntityMetaData.html) interface defines
entity metadata. The [PropertyMetaData](api/org/foxlabs/validation/metadata/PropertyMetaData.html)
interface defines property metadata. For Java Beans and POJOs there is the
[BeanMetaData](api/org/foxlabs/validation/metadata/BeanMetaData.html) class that allows to
gather all information about entity from annotations.
The [MapMetaData](api/org/foxlabs/validation/metadata/MapMetaData.html) class can be used
to describe metadata for validation of the `java.util.Map` entities.

### Validator And Its Factory

All architecture components binded together through the [Validator](api/org/foxlabs/validation/Validator.html)
class. That class has a set of shortcut methods to perform validation with default parameters.
If other configuration is required then the [Validator.newContext()](api/org/foxlabs/validation/Validator.html#newContext())
method should be used. Returned [Validator.ContextBuilder](api/org/foxlabs/validation/Validator.ContextBuilder.html)
instance allows to configure validation parameters as you need.

To create a new validator the [ValidatorFactory](api/org/foxlabs/validation/ValidatorFactory.html)
should be used. The main purpose of that factory is maintaining validators configuration.
You can configure factory once and then create validators with different metadata.

```java
...
ValidatorFactory factory = ValidatorFactory.getDefault();
Validator<Account> validator = factory.newValidator(Account.class);
Validator<Account>.ContextBuilder context = validator.newContext();
context.setLocale(Locale.ROOT).setFailFast(true).validateEntity(account);
...
```

### Validating Groups

Validating groups are string identifiers that can be applied to constraints only and
allow to check certain constraints defined on the element. If constraint is not binded
to any validating group then it will be binded to default group (empty string) automatically.
By default validation will be performed for all defined constraints.

The [Validator.ContextBuilder.setValidatingGroups()](api/org/foxlabs/validation/Validator.ContextBuilder.html#setValidatingGroups(java.lang.String...))
method allows to change validating groups.
            
```java
...
Validator<Account>.ContextBuilder context = validator.newContext();
context.setValidatingGroups("correct", "update").validateEntity(account);
...
```

## Annotations

For Java Beans and POJOs validation components can be defined through annotations.

### Constraint Annotations

Each constraint annotation should be annotated by the [@ConstrainedBy](api/org/foxlabs/validation/constraint/ConstrainedBy.html)
annotation that specifies constraint implementation classes. Also constraint annotation
can define the following elements:

- The `message` property of the `java.lang.String` type. This property allows to
  override default error message template of the constraint.
- The `groups` property of the `java.lang.String[]` type. This property defines
  validating groups the constraint is applied on.
- The `targets` property of the [ValidationTarget[]](api/org/foxlabs/validation/ValidationTarget.html)
  type. This property defines an object part to which constraint should be applied.
- The inner `@List` annotation with `value` property of the outer constraint annotation
  array type. This annotation allows to apply outer constraint annotation several times on
  the same element.

For example, you can take a look at the [@NotNull](api/org/foxlabs/validation/constraint/NotNull.html) annotation.

There are several specific annotations.

| Annotation                                                             | Description                                                        |
|------------------------------------------------------------------------|--------------------------------------------------------------------|
| [@Composition](api/org/foxlabs/validation/constraint/Composition.html) | Defines composition (AND) of constraints of the annotated element. |
| [@Conjunction](api/org/foxlabs/validation/constraint/Conjunction.html) | Defines conjunction (AND) of constraints of the annotated element. |
| [@Disjunction](api/org/foxlabs/validation/constraint/Disjunction.html) | Defines disjunction (OR) of constraints of the annotated element.  |
| [@Negation](api/org/foxlabs/validation/constraint/Negation.html)       | Defines negation (NOT) of constraints of the annotated element.    |

```java
@NotNull @NotEmpty
@Url(targets = ValidationTarget.ELEMENTS)
@EmailAddress(targets = ValidationTarget.ELEMENTS)
@Disjunction(targets = ValidationTarget.ELEMENTS)
public List<String> getContactList() {
    ...
}
```

In the example above we define constraint for `java.util.List` property type.
This list cannot be `null` or empty, each element of the list must be a well-formed
URL or email address.

> Also elements of the list in example above can be `null` because
> [@Url](api/org/foxlabs/validation/constraint/Url.html) and
> [@EmailAddress](api/org/foxlabs/validation/constraint/EmailAddress.html)
> constraints allow `null`s.

### Converter Annotations

Each converter annotation should be annotated by the [@ConvertedBy](api/org/foxlabs/validation/converter/ConvertedBy.html)
annotation that specifies converter implementation classes. Also converter annotation
can define the following elements:

- The `message` property of the `java.lang.String` type. This property allows to
  override default error message template of the converter.
- The `targets` property of the [ValidationTarget[]](api/org/foxlabs/validation/ValidationTarget.html)
  type. This property defines an object part to which converter should be applied.
- The inner `@List` annotation with `value` property of the outer converter annotation
  array type. This annotation allows to apply outer converter annotation several times on
  the same element.

For example, you can take a look at the [@NumberPattern](api/org/foxlabs/validation/converter/NumberPattern.html)
annotation.

```java
@DateStyle(date = DateFormat.SHORT)
public Date getCurrentDate() {
    ...
}
```            

In the example above we define converter for `java.util.Date` property type with the specified date style.

> Also you can use converter annotations in conjunction with the
> [Tokenizer](api/org/foxlabs/validation/converter/Tokenizer.html) annotations for arrays,
> `java.util.Collection` and `java.util.Map` types (for example, you can use the
> [@TokenDelimiters](api/org/foxlabs/validation/converter/TokenDelimiters.html) annotation).

### Custom Annotations

Custom annotations are annotations constructed from other annotations. Custom annotations
must not be annotated by the [@ConstrainedBy](api/org/foxlabs/validation/constraint/ConstrainedBy.html)
or [@ConvertedBy](api/org/foxlabs/validation/converter/ConvertedBy.html) annotations.

Lets create custom constraint annotation that will check whether the specified string
is well-formed URL or email address.

```java
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.foxlabs.validation.ValidationTarget;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Disjunction @Url @EmailAddress
public @interface ContactAddress {
    
    String message() default "";
    
    String[] groups() default {};
    
    ValidationTarget[] targets() default {};
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    public static @interface List {
        
        ContactAddress[] value();
        
    }
    
}
```

Now you can apply `@ContactAddress` annotation for any property of the string type.
