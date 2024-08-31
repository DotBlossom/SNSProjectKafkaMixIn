package com.delta.delta.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class NotificationsDto {

    // type case -> redirect in front? else include URL
    private String eventType;

    private Long senderId;
    private Long receiverId;

    private Long postId;

    private String isRead;
    private String isSent;
    //private String postFileName;
    private String senderName;
}



// post -> 역참조 users?
// post -> redirect in front