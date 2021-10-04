package com.ask.springcore.conditional.component;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(ConditionalComponent.class)
public class DependComponent {

}
