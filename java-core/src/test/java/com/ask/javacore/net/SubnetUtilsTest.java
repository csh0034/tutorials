package com.ask.javacore.net;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.junit.jupiter.api.Test;

class SubnetUtilsTest {

  /**
   * CIDR Signature:	[192.168.112.0/24] <br> Netmask: [255.255.255.0] <br> Network: [192.168.112.0] <br> Broadcast:
   * [192.168.112.255] <br> First address: [192.168.112.0] <br> Last address: [192.168.112.255] <br> Address Count: [256]
   */
  @Test
  void subnet() {
    SubnetUtils subnet = new SubnetUtils("192.168.112.0/24");
    subnet.setInclusiveHostCount(true); // Whether to include network and broadcast addresses
    SubnetInfo subnetInfo = subnet.getInfo();

    assertThat(subnetInfo.getNetworkAddress()).isEqualTo("192.168.112.0");
    assertThat(subnetInfo.getBroadcastAddress()).isEqualTo("192.168.112.255");
    assertThat(subnetInfo.getLowAddress()).isEqualTo("192.168.112.0");
    assertThat(subnetInfo.getHighAddress()).isEqualTo("192.168.112.255");

    assertThat(subnetInfo.isInRange("192.168.112.0")).isTrue();
    assertThat(subnetInfo.isInRange("192.168.112.255")).isTrue();

    assertThat(subnetInfo.isInRange("192.168.0.0")).isFalse();

    assertThatIllegalArgumentException().isThrownBy(() -> subnetInfo.isInRange("192.168.112.256"));
  }

}
