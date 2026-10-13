package com.agriconnect.agriconnect.repository.metier;

import com.agriconnect.agriconnect.entity.metier.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {}

