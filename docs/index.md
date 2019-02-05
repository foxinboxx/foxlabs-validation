## Table Of Contents

1. <a href="#introduction">Introduction</a>
1. <a href="#getting-started">Getting Started</a>
  * <a href="#maven-setup">Maven Setup</a>
  * <a href="#writing-bean">Writing Bean</a>
  * <a href="#validating-bean">Validating Bean</a>
1. <a href="#architecture">Architecture</a>
  * <a href="#validation-components">Validation Components</a>
  * <a href="#validation-messages">Validation Messages</a>
  * <a href="#validation-exceptions">Validation Exceptions</a>
  * <a href="#entity-metadata">Entity Metadata</a>
  * <a href="#validator-and-its-factory">Validator And Its Factory</a>
  * <a href="#validating-groups">Validating Groups</a>
1. <a href="#annotations">Annotations</a>
  * <a href="#constraint-annotations">Constraint Annotations</a>
  * <a href="#converter-annotations">Converter Annotations</a>
  * <a href="#custom-annotations">Custom Annotations</a>
1. <a href="#customization">Customization</a>
  * <a href="#implementing-your-own-constraint">Implementing Your Own Constraint</a>
  * <a href="#implementing-your-own-converter">Implementing Your Own Converter</a>
  * <a href="#customizing-other-components">Customizing Other Components</a>
    * <a href="#extending-metadata">Extending Metadata</a>
    * <a href="#extending-message-resolver-and-builder">Extending Message Resolver And Builder</a>
    * <a href="#extending-validator-factory">Extending Validator Factory</a>
1. <a href="#constrained-map">Constrained Map</a>
1. <a href="https://github.com/foxinboxx/foxlabs-validation">Source Code</a>

{% include_relative introduction.md %}
{% include_relative getting-started.md %}
{% include_relative architecture.md %}
{% include_relative annotations.md %}
{% include_relative customization.md %}
{% include_relative constrained-map.md %}
