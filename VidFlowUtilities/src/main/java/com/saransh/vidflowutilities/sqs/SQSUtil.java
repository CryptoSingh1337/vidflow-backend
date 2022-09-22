package com.saransh.vidflowutilities.sqs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author saranshk04
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "aws.sqs.queue")
public class SQSUtil {

    private static String base;
    private static String suffix;

    private static String createQueueUri(String queueName) {
        return String.format("%s/%s/%s", base, suffix, queueName);
    }

    public static String get(String name) {
        return createQueueUri(name);
    }

    public void setBase(String base) {
        SQSUtil.base = base;
    }

    public void setSuffix(String suffix) {
        SQSUtil.suffix = suffix;
    }
}
