package org.mayanjun.myjack.zookeeper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZooKeeperSerializer
 *
 * @author mayanjun(6/9/16)
 */
public class JsonZkSerializer implements ZkSerializer {

    private static final Logger LOG = LoggerFactory.getLogger(JsonZkSerializer.class);

    private ObjectMapper mapper;

    private String encoding = "utf-8";

    public JsonZkSerializer() {
        mapper = newObjectMapper();
    }

    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        byte bytes[] = new byte[0];
        try {
            String valueAsString = mapper.writeValueAsString(data);
            return valueAsString.getBytes(encoding);
        } catch (Exception e) {
            LOG.error("Can't serialize object:" + data, e);
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        if(bytes == null || bytes.length == 0) return null;
        String json;
        try {
            json = new String(bytes, encoding);
            return json;
        } catch (Exception e) {
            LOG.error("Can't deserialize bytes", e);
        }
        return null;
    }

    public static ObjectMapper newObjectMapper() {
        return new ObjectMapper();
    }
}
