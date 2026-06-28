package com.gdg.admin.event;

public class UserSuspendedEvent {
    private Long userId;
    private String email;
    private String motif;
    private Long adminId;

    public UserSuspendedEvent() {}

    public UserSuspendedEvent(Long userId, String email,
            String motif, Long adminId) {
        this.userId = userId;
        this.email = email;
        this.motif = motif;
        this.adminId = adminId;
    }

    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getMotif() { return motif; }
    public Long getAdminId() { return adminId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setMotif(String motif) { this.motif = motif; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
}