package com.ask.springcore.conditional.component;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "conditional.enabled", havingValue = "true")
@ConditionalOnExpression("${conditional.enabled:false} and '${conditional.name}'.equals('ask')")
public class ConditionalComponent {

}
