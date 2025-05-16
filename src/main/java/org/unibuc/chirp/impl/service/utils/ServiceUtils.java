package org.unibuc.chirp.impl.service.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ServiceUtils {
    public boolean actionExecutedSuccessfully(final Runnable runnable) {
        try {
            runnable.run();
            log.debug("Runnable executed successfully!");
            return true;
        } catch (Exception e) {
            log.info("Exception occurred while executing runnable: {}", e.getMessage(), e);
            return false;
        }

    }
}
