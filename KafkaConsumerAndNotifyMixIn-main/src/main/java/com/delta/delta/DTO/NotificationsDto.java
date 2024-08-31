package com.delta.delta.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class NotificationsDto {

    // type case -> redirect in front? else include URL
    private String eventType;

    private Long senderId;
    private Long receiverId;

    private Long postId;
    //private String postFileName;
    private String isRead;
    private String isSent;
    private String senderName;
}

// Actions : follow , postLike(To postRedirect, commentRedirectInPost)