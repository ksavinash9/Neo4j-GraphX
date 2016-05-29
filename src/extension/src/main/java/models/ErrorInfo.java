package models;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ErrorInfo {
    private String message;
    private HttpStatus status;

    public ErrorInfo(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Response toResponse() {
        return Response.status(status.value())
                .entity(new Gson().toJson(this))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
