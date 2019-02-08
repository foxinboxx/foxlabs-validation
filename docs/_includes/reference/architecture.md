## Architecture

On the following class diagram you will see the key classes of this framework.

![Class Diagram](assets/images/class-diagram.png)

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
The [ValidationContext.isLocalizedConvert()](api/org/foxlabs/validation/ValidationContext.html#isLocalizedConvert--)
method specifies if localized representation is required.

### Validation Messages

Validation component may provide error message that will be used when validation process fails.
That message may depend on validation context or component parameters. Because of that
[Validation](api/org/foxlabs/validation/Validation.html) interface defines two methods that
allow to obtain required information for error message generation. The
[Validation.getMessageTemplate()](api/org/foxlabs/validation/Validation.html#getMessageTemplate-org.foxlabs.validation.ValidationContext-)
method allows to obtain localized error message template. The
[Validation.appendMessageArguments()](api/org/foxlabs/validation/Validation.html#appendMessageArguments-org.foxlabs.validation.ValidationContext, java.util.Map-)
method provides arguments to be substituted into the error message template. Locale of the
error message and its arguments should be obtained from the
[ValidationContext.getMessageLocale()](api/org/foxlabs/validation/ValidationContext.html#getMessageLocale--) method.

The [MessageResolver](api/org/foxlabs/validation/message/MessageResolver.html) is used
for resolving localized message templates. That abstraction allows to store error messages
anywhere. By default `org/foxlabs/validation/resource/validation-messages`
resource bundle is used. Validation components could use
[ValidationContext.resolveMessage()](api/org/foxlabs/validation/ValidationContext.html#resolveMessage-java.lang.String-)
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
If other configuration is required then the [Validator.newContext()](api/org/foxlabs/validation/Validator.html#newContext--)
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

The [Validator.ContextBuilder.setValidatingGroups()](api/org/foxlabs/validation/Validator.ContextBuilder.html#setValidatingGroups-java.lang.String...-)
method allows to change validating groups.

```java
...
Validator<Account>.ContextBuilder context = validator.newContext();
context.setValidatingGroups("correct", "update").validateEntity(account);
...
```
