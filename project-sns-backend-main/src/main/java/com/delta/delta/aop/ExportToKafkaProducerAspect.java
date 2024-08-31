package com.delta.delta.aop;


import com.delta.delta.DTO.NotificationsDto;
import com.delta.delta.entity.PostLike;
import com.delta.delta.service.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class ExportToKafkaProducerAspect {

    private final KafkaMessagePublisher kafkaMessagePublisher;

    @AfterReturning(pointcut = "execution(* com.delta.delta.service.*.addFollowing(..))",
            returning = "result")
    public void afterReturningExportToKafkaProducerAspectByUser(JoinPoint joinPoint, Object result) {
        if (!(result instanceof Exception)) {
            Object[] args = joinPoint.getArgs();
            Long userId = (Long) args[0];
            Long followingId = (Long) args[1];

            NotificationsDto dto = new NotificationsDto();
            dto.setEventType("following");
            dto.setSenderId(userId);
            dto.setReceiverId(followingId);
            dto.setIsRead("N");
            dto.setSenderName((String) result);

            System.out.println("Specific service executed successfully. Result: " + dto.getReceiverId());
            kafkaMessagePublisher.sendObjectToTopic(dto.getReceiverId(), dto);
        }

    }

    @AfterReturning(pointcut = "execution(* com.delta.delta.service.*.likePost(..)) || " +
            "execution(* com.delta.delta.service.*.unlikePost(..))",
            returning = "result")
    public void afterReturningExportToKafkaProducerAspect(JoinPoint joinPoint, Object result) {
        if (!(result instanceof Exception)) {

            if (result instanceof PostLike postLikeInstance) {
                // sender가 post를 좋아함. dto interface?
                Long postId = postLikeInstance.getPost().getPostId();
                Long senderId = postLikeInstance.getUser().getUserId();
                Long receiverId = postLikeInstance.getPost().getUser().getUserId();


                String sender = postLikeInstance.getUser().getUsername();
                NotificationsDto dto = new NotificationsDto();
                dto.setPostId(postId);
                dto.setSenderId(senderId);
                dto.setReceiverId(receiverId);
                dto.setEventType("postLike");
                dto.setIsRead("N");
                dto.setSenderName(sender);
                //dto.setPostFileName(postFileName);



                System.out.println("Specific service executed successfully. Result: " + dto.getReceiverId());
                kafkaMessagePublisher.sendObjectToTopic(dto.getReceiverId(), dto);

            } else if (result instanceof Long unlikePostId) {
                NotificationsDto dto = new NotificationsDto();
                dto.setPostId(unlikePostId);
                dto.setEventType("postUnlike");
                dto.setIsRead("N");

                System.out.println("Specific service executed successfully. Result: " + dto.getPostId());
                kafkaMessagePublisher.sendObjectToTopic(dto.getPostId(), dto);

            }



        }
    }
}

