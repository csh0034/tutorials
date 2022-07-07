package com.ask.prometheus.repository;

import com.ask.prometheus.entity.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointRepository extends JpaRepository<Endpoint, String> {

}
