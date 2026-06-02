package app.dtos;

import java.time.LocalDateTime;

public class RequestSummaryDTO {

    private int           rqId;
    private LocalDateTime createdAt;
    private String        status;        // PENDING | SENT | ACCEPTED | REJECTED
    private double        carpWidth;
    private double        carpLength;
    private double        carpHeight;
    private String        roofTypeName;
    private double        roofAngle;
    private String        firstName;
    private String        lastName;
    private String        email;
    private String        phone;
    private boolean       hasShed;
    private boolean       flagged;

    public RequestSummaryDTO(int rqId, LocalDateTime createdAt, String status,
                             double carpWidth, double carpLength, double carpHeight, String roofTypeName,
                             double roofAngle, String firstName, String lastName,
                             String email, String phone,
                             boolean hasShed, boolean flagged) {
        this.rqId         = rqId;
        this.createdAt    = createdAt;
        this.status       = status;
        this.carpWidth    = carpWidth;
        this.carpLength   = carpLength;
        this.carpHeight   = carpHeight;
        this.roofTypeName = roofTypeName;
        this.roofAngle    = roofAngle;
        this.firstName    = firstName;
        this.lastName     = lastName;
        this.email        = email;
        this.phone        = phone;
        this.hasShed      = hasShed;
        this.flagged      = flagged;
    }

    public int           getRqId()        { return rqId; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public String        getStatus()      { return status; }
    public double        getCarpWidth()   { return carpWidth; }
    public double        getCarpLength()  { return carpLength; }
    public double        getCarpHeight()  { return carpHeight; }
    public String        getRoofTypeName(){ return roofTypeName; }
    public double        getRoofAngle()   { return roofAngle; }
    public String        getFirstName()   { return firstName; }
    public String        getLastName()    { return lastName; }
    public String        getEmail()       { return email; }
    public String        getPhone()       { return phone; }
    public boolean       isHasShed()      { return hasShed; }
    public boolean       isFlagged()      { return flagged; }
}