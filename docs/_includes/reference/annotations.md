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
