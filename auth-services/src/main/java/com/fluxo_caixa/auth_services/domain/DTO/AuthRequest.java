package com.fluxo_caixa.auth_services.domain.DTO;

// import java.util.List;

public class AuthRequest {
    private Long id;
    private String username;
    private String email;
    private String password;
    // private List<String> roles;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    // public List<String> getRoles() {
    //     return roles;
    // }
    
    // public void setRoles(List<String> roles) {
    //     this.roles = roles;
    // }
}
