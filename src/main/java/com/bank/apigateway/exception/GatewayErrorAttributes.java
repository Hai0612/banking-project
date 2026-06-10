package com.bank.apigateway.exception;


import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;

@Component
public class GatewayErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(
            ServerRequest request,
            ErrorAttributeOptions options) {

        Map<String, Object> errorAttributes =
                super.getErrorAttributes(request, options);

        Throwable ex = getError(request);

        errorAttributes.put("traceId",
                request.headers().firstHeader("X-Trace-Id"));

        if (ex != null) {
            errorAttributes.put("exception", ex.getClass().getSimpleName());
        }

        return errorAttributes;
    }
}