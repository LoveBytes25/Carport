package app.entities;

import java.time.LocalDateTime;

public class Request {
    int id;
    LocalDateTime timestamp;
    RequestStatus status;

    public Request(int id, LocalDateTime timestamp, RequestStatus status) {
        this.id = id;
        this.timestamp = timestamp;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
