## Customization

In this chapter, we will consider opportunities to customize components of the proposed framework.

### Implementing Your Own Constraint

To define a new constraint you need to implement the [Constraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/Constraint.html)
interface. Depending on what kind of constraint you need to, you can use one of the existing classes.

| Class                                                                                                         | Description                                                                                                                                                     |
|---------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [CorrectConstraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/CorrectConstraint.html)                             | Allows to modify (correct) test value and never throws [ConstraintViolationException](/foxlabs-validation/api/org/foxlabs/validation/constraint/ConstraintViolationException.html). |
| [CheckConstraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/CheckConstraint.html)                                 | Only checks a test value and doesn't modify it.                                                                                                                 |
| [RegexConstraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/RegexConstraint.html)                                 | Checks whether a test string matches the regular expression.                                                                                                    |
| [EnumerationConstraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/EnumerationConstraint.html)                     | Checks whether a test value is one of the allowed constants.                                                                                                    |
| [IgnoreCaseEnumerationConstraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/IgnoreCaseEnumerationConstraint.html) | Checks whether a test string is one of the allowed strings using ignore case comparison.                                                                        |
| [SizeConstraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/SizeConstraint.html)                                   | Checks whether the size of a test value is within allowed minimum and maximum bounds.                                                                           |

> You can also extend the [AbstractValidation](/foxlabs-validation/api/org/foxlabs/validation/AbstractValidation.html)
> class in conjunction with [Constraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/Constraint.html) interface.

Lets implement `StartsWithConstraint` that will check whether a test string starts with some prefix.

```java
import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.constraint.CheckConstraint;

import org.foxlabs.util.Assert;

public class StartsWithConstraint extends CheckConstraint<String> {
    
    private final String prefix;
    
    public StartsWithConstraint(String prefix) {
        this.prefix = Assert.notEmpty(prefix, "Prefix cannot be null or empty!");
    }
    
    @Override
    public Class<?> getType() {
        return String.class;
    }
    
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("prefix", prefix);
        return true;
    }
    
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        return value == null || value.startsWith(prefix);
    }
    
}
```

> Don't forget to add constraint error message template in the default message bundle
> (see <a href="#validation-messages">Validation Messages</a>).

If you plan to use `StartsWithConstraint` for validating Java beans or POJOs then you need to
define constraint annotation and add corresponding constructor in the constraint implementation class.

```java
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.foxlabs.validation.ValidationTarget;
import org.foxlabs.validation.constraint.ConstrainedBy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@ConstrainedBy(StartsWithConstraint.class)
public @interface StartsWith {
    
    String value(); // prefix
    
    String message() default "";
    
    String[] groups() default {};
    
    ValidationTarget[] targets() default {};
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
    public static @interface List {
        
        StartsWith[] value();
        
    }
    
}
```

Also you need to change `StartsWithConstraint`.

```java
...

public class StartsWithConstraint extends CheckConstraint<String> {
    
    ...
    
    public StartsWithConstraint(StartsWith annotation) {
        this(annotation.value());
    }
    
    ...
    
}
```

That's it. Now you can apply `@StartsWith` annotation for any property of the string type.

### Implementing Your Own Converter

To define a new converter you need to implement the [Converter](/foxlabs-validation/api/org/foxlabs/validation/converter/Converter.html)
interface.

> You can extend the [AbstractConverter](/foxlabs-validation/api/org/foxlabs/validation/converter/AbstractConverter.html)
> class or [AbstractValidation](/foxlabs-validation/api/org/foxlabs/validation/AbstractValidation.html) class in conjunction
> with [Converter](/foxlabs-validation/api/org/foxlabs/validation/converter/Converter.html) interface.

Lets implement `RgbColorConverter` that will convert `#RRGGBB` color text representation
into the `java.awt.Color` and vice versa.

```java
import java.awt.Color;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.converter.AbstractConverter;
import org.foxlabs.validation.converter.MalformedValueException;

public class RgbColorConverter extends AbstractConverter<Color> {
    
    @Override
    public Class<Color> getType() {
        return Color.class;
    }
    
    @Override
    protected <T> Color doDecode(String value, ValidationContext<T> context) {
        if (value.length() == 7 && value.charAt(0) == '#') {
            try {
                int r = Integer.parseInt(value.substring(1, 3), 16);
                int g = Integer.parseInt(value.substring(3, 5), 16);
                int b = Integer.parseInt(value.substring(5, 7), 16);
                
                return new Color(r, g, b);
            } catch (NumberFormatException e) {}
        }
        throw new MalformedValueException(this, context, value);
    }
    
    @Override
    protected <T> String doEncode(Color value, ValidationContext<T> context) {
        StringBuilder rgb = new StringBuilder(7);
        rgb.append('#');
        
        String r = Integer.toHexString(value.getRed());
        if (r.length() < 2) {
            rgb.append('0');
        }
        rgb.append(r);
        
        String g = Integer.toHexString(value.getGreen());
        if (g.length() < 2) {
            rgb.append('0');
        }
        rgb.append(g);
        
        
        String b = Integer.toHexString(value.getBlue());
        if (b.length() < 2) {
            rgb.append('0');
        }
        rgb.append(b);
        
        return rgb.toString();
    }
    
}
```

> Don't forget to add converter error message template in the default message bundle
> (see <a href="#validation-messages">Validation Messages</a>).

If you want to use `RgbColorConverter` as default converter for `java.awt.Color` value types you
need to register it in the [ConverterFactory](/foxlabs-validation/api/org/foxlabs/validation/converter/ConverterFactory.html).

```java
ConverterFactory.addDefaultConverter(new RgbColorConverter());
```

But if you want to use `RgbColorConverter` for Java Beans or POJOs and you have
different converters for `java.awt.Color` type or you want to parameterize your
`RgbColorConverter` (for example, you may want to encode colors in upper or lower case)
then you need an annotation.

```java
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.foxlabs.validation.ValidationTarget;
import org.foxlabs.validation.converter.ConvertedBy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@ConvertedBy(RgbColorConverter.class)
public @interface RgbColor {
    
    boolean upperCase() default true; // encoding parameter
    
    String message() default "";
    
    ValidationTarget[] targets() default {};
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
    public static @interface List {
        
        RgbColor[] value();
        
    }
    
}
```

Also you need to change the `RgbColorConverter`.

```java
...

public class RgbColorConverter extends AbstractConverter<Color> {
    
    private final boolean upperCase;
    
    public RgbColorConverter(boolean upperCase) {
        this.upperCase = upperCase;
    }
    
    public RgbColorConverter(RgbColor annotation) {
        this(annotation.upperCase());
    }
    
    ...
    
    @Override
    protected <T> String doEncode(Color value, ValidationContext<T> context) {
        ...
        
        String text = rgb.toString();
        return upperCase ? text.toUpperCase() : text.toLowerCase();
    }
    
}
```

That's it. Now you can apply `@RgbColor` annotation for any property of the `java.awt.Color` type.

### Customizing Other Components

In most cases you don't need to customize anything in this framework except
[Constraint](/foxlabs-validation/api/org/foxlabs/validation/constraint/Constraint.html) and
[Converter](/foxlabs-validation/api/org/foxlabs/validation/converter/Converter.html). But in some cases you may need
to customize [EntityMetaData](/foxlabs-validation/api/org/foxlabs/validation/metadata/EntityMetaData.html),
[MessageResolver](/foxlabs-validation/api/org/foxlabs/validation/message/MessageResolver.html) or
[MessageBuilder](/foxlabs-validation/api/org/foxlabs/validation/message/MessageBuilder.html) and even
[ValidatorFactory](/foxlabs-validation/api/org/foxlabs/validation/ValidatorFactory.html).

#### Extending Metadata

Extending metadata is required when your entities is not represented as Java Beans or POJOs,
or you define entities or properties by other criteria (for example, you can have `@Property`
annotation to define entity properties). To define your own metadata you can use existing
abstract classes.

| Class                                                                                         | Description                                            |
|-----------------------------------------------------------------------------------------------|--------------------------------------------------------|
| [AbstractEntityMetaData](/foxlabs-validation/api/org/foxlabs/validation/metadata/AbstractEntityMetaData.html)     | Provides base implementation of the entity metadata.   |
| [AbstractPropertyMetaData](/foxlabs-validation/api/org/foxlabs/validation/metadata/AbstractPropertyMetaData.html) | Provides base implementation of the property metadata. |

After you defined your own metadata, it can be used to create validators.

```java
EntityMetaData<T> myMeta = ...
ValidatorFactory factory = ...
Validator<T> validator = factory.newValidator(myMeta);
```

#### Extending Message Resolver And Builder

If your error message templates is not stored as Java resources (for example,
they can be stored in the database) then you need to provide your own
[MessageResolver](/foxlabs-validation/api/org/foxlabs/validation/message/MessageResolver.html) and
initialize your [ValidatorFactory](/foxlabs-validation/api/org/foxlabs/validation/ValidatorFactory.html).

```java
MessageResolver resolver = ...
ValidatorFactory factory = ...
factory.setMessageResolver(resolver);
```

Also you may want to store your error message templates in another Java resource bundle. If so then
you need to tell about that to [ValidatorFactory](/foxlabs-validation/api/org/foxlabs/validation/ValidatorFactory.html).

```java
ValidatorFactory myFactory = new ValidatorFactory("my/message/bundle/name");
```

> The [MessageResolverChain](/foxlabs-validation/api/org/foxlabs/validation/message/MessageResolverChain.html) class
> can be used to create a chain of the [MessageResolver](/foxlabs-validation/api/org/foxlabs/validation/message/MessageResolver.html)s.

The goal of the [MessageBuilder](/foxlabs-validation/api/org/foxlabs/validation/message/MessageBuilder.html) is to render
error message templates for the current [ValidationContext](/foxlabs-validation/api/org/foxlabs/validation/ValidationContext.html)
and message arguments provided by the validation components.
The [DefaultMessageBuilder](/foxlabs-validation/api/org/foxlabs/validation/message/DefaultMessageBuilder.html) is used by
default to render message templates. If you need more sophisticated message renderer then you should
provide your own [MessageBuilder](/foxlabs-validation/api/org/foxlabs/validation/message/MessageBuilder.html) implementation
(for example, you can use one of template engines to render messages) and tell about that to
[ValidatorFactory](/foxlabs-validation/api/org/foxlabs/validation/ValidatorFactory.html).

```java
MessageBuilder builder = ...
ValidatorFactory factory = ...
factory.setMessageBuilder(builder);
```

> You can also extend the [AbstractMessageBuilder](/foxlabs-validation/api/org/foxlabs/validation/message/AbstractMessageBuilder.html)
> abstract class to provide your own implementation.

#### Extending Validator Factory

The main goal for extending [ValidatorFactory](/foxlabs-validation/api/org/foxlabs/validation/ValidatorFactory.html)
is to provide additional configuration to the [Validator](/foxlabs-validation/api/org/foxlabs/validation/Validator.html).
After, these configuration parameters can be accessed in the validation components. The right way is
to extend [Validator](/foxlabs-validation/api/org/foxlabs/validation/Validator.html) too, because factory configuration
is supposed as mutable.
