package com.marcomnrq.consultation.domain.repository;

import com.marcomnrq.consultation.domain.model.Device;
import com.marcomnrq.consultation.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByUserAndDeviceDetailsAndLocation (User user, String deviceDetails, String location);
}
