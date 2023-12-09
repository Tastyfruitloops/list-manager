package com.dev.listmanager.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

class RequestWrapperTest {

    @Test
    public void wrapperCopiesWrapped() throws IOException {
        byte[] bytes = HexFormat.ofDelimiter(":").parseHex("e0:4f:d0:20:ea:3a:69:10:a2:d8:08:00:2b:30:30:9d");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "sample/uri");
        request.setContent(bytes);

        RequestWrapper wrapper = new RequestWrapper(request);
        Assertions.assertEquals(wrapper.getBody(), new String(bytes, StandardCharsets.UTF_8));
    }

}