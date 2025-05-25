package org.unibuc.chirp.impl.service.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.unibuc.chirp.domain.dto.message.get.GetMessageResponseDto;
import org.unibuc.chirp.domain.entity.MessageEntity;

import java.time.format.DateTimeFormatter;

@Slf4j
@UtilityClass
public class ServiceUtils {


    public static GetMessageResponseDto toDetailsDto(MessageEntity messageEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new GetMessageResponseDto(
                messageEntity.getId(),
                messageEntity.getContent(),
                messageEntity.getSender().getUsername(),
                formatter.format(messageEntity.getTimestamp())
        );
    }
}
