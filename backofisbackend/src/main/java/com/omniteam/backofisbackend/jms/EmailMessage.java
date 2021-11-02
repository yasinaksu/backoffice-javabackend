package com.omniteam.backofisbackend.jms;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmailMessage implements Serializable {
    @Builder.Default
    private String queueName = "EmailQueue";

    private String from;
    private String to;
    private String message;

}
