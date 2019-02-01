package org.mayanjun.myjack.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author mayanjun
 * @since 17/03/2017
 */
public class JSONER {

    private JSONER() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(JSONER.class);

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss.SSS"));
    }

    public static String se(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.error("Serialize object error", e);
        }
        return null;
    }

    public static <T> T de(String json, Class<T> cls) {
        try {
            if (StringUtils.isBlank(json)) return null;
            return MAPPER.readValue(json, cls);
        } catch (IOException e) {
            LOG.error("Deserialize object error", e);
        }
        return null;
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }
}