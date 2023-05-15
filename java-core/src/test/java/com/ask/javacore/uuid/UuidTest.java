package com.ask.javacore.uuid;

import com.fasterxml.uuid.Generators;
import com.github.f4b6a3.tsid.TsidCreator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class UuidTest {

  @Test
  void fasterxml() {
    System.out.println("uuid1 = " + Generators.timeBasedGenerator().generate());
    System.out.println("uuid4 = " + Generators.randomBasedGenerator().generate());
    System.out.println("uuid5 = " + Generators.nameBasedGenerator().generate("string to hash"));
    System.out.println("uuid6 = " + Generators.timeBasedReorderedGenerator().generate());
    System.out.println("uuid7 = " + Generators.timeBasedEpochGenerator().generate());
  }

  @RepeatedTest(10)
  void tsid() {
    System.out.println("tsid to long = " + TsidCreator.getTsid().toLong());
    System.out.println("tsid to String = " + TsidCreator.getTsid());
  }

}
