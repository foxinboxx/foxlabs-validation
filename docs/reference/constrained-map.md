## Constrained Map

This validation framework is not tied only to validating Java Beans or POJOs. You can validate any
entity having [EntityMetaData](api/org/foxlabs/validation/metadata/EntityMetaData.html) descriptor.
So it is a good idea to validate `java.util.Map` entities.

The [ConstrainedMap](api/org/foxlabs/validation/ConstrainedMap.html) class allows to maintain
a set of properties with constraints. It also provides modifications in transaction-like manner
and convertation of property values into and from string representation. More over, this class
is thread-safe and suitable for maintaining configuration properties in multithreaded environment
such as web application.

The following example will show how you can create such map.

```java
import org.foxlabs.validation.ConstrainedMap;
import org.foxlabs.validation.ValidatorFactory;
import org.foxlabs.validation.metadata.MapMetaData;

import static org.foxlabs.validation.constraint.ConstraintFactory.*;

@SuppressWarnings("unchecked")
public class Configuration {
    
    public static final ConstrainedMap SETTINGS;
    static {
        MapMetaData.Builder builder = new MapMetaData.Builder();
        
        builder.property("admin.email", String.class, join(despace(), notBlank(), emailAddress()), "x@y.z")
               .property("session.timeout", Integer.class, join(notNull(), range(10, 1440)), 30)
               .property("output.encoding", String.class, supportedEncoding(), "ISO-8859-1");
        
        SETTINGS = new ConstrainedMap(ValidatorFactory.getDefault().newValidator(builder.build()));
    }
    
}
```

In the example above, we build `SETTINGS` map with 3 constrained properties.

The next step is to modify values.

```java
// changing single property
try {
    Configuration.SETTINGS.setValue("session.timeout", 60);
} catch (ValidationException e) {
    e.printViolations();
}

// changing multiple properties
ConstrainedMap.Transaction tx = Configuration.SETTINGS.newTransaction();
tx.setValue("admin.email", "foxinboxx@gmail.com");
tx.setValue("session.timeout", 5);
tx.setValue("output.encoding", "UTF-8");
try {
    tx.commit(false);
    System.out.println(Configuration.SETTINGS);
} catch (ValidationException e) {
    e.printViolations();
}
```

> [ConstrainedMap](api/org/foxlabs/validation/ConstrainedMap.html) validates property values
> internally when you change them. So if you need to change multiple properties it is better
> to use transaction. Also transaction needed if you want to read a number of properties and
> avoid inconsistency in multithreaded environment.

If you need to store (or restore) property values in a file or stream the code below can be helpful.

```java
File file = new File("settings.properties");
Configuration.SETTINGS.save(file, "My configuration");
try {
    Configuration.SETTINGS.load(file);
} catch (ValidationException e) {
    e.printViolations();
}
```
