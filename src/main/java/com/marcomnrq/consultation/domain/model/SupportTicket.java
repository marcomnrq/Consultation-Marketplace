package com.marcomnrq.consultation.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
public class SupportTicket {
    private Long id;

    private String name;

    private String email;

    private String topic;

    private String message;
}
