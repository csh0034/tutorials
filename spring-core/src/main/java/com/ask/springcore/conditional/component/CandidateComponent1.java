package com.ask.springcore.conditional.component;

import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnSingleCandidate(Candidate.class)
public class CandidateComponent1 implements Candidate {

}
