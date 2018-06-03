package betman.config

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class ExceptionResolver : ResponseBodyAdvice<MutableMap<String, Any>> {

    override fun supports(returnType: MethodParameter?, converterType: Class<out HttpMessageConverter<*>>?): Boolean {
        return returnType?.method?.name == "error"
    }

    override fun beforeBodyWrite(body: MutableMap<String, Any>?, returnType: MethodParameter?, selectedContentType: MediaType?, selectedConverterType: Class<out HttpMessageConverter<*>>?, request: ServerHttpRequest?, response: ServerHttpResponse?): MutableMap<String, Any>? {
        if (body != null && 500 == body["status"] && "User not found" == body["message"]) {
            body["status"] = HttpStatus.FORBIDDEN
            response?.setStatusCode(HttpStatus.FORBIDDEN)
        }
        return body
    }
}


