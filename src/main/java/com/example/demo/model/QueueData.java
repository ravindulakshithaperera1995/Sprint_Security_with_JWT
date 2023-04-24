package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.amqp.core.Message;

@Builder
@EqualsAndHashCode
@AllArgsConstructor
@Data
public class QueueData {
    private Message message;

    private String fileName;
}
