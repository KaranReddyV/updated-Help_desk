package com.ojas.ticket.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority
{
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
	/*BDM("ROLE_BDM"),
	RECRUITER("ROLE_RECRUITER"),
	MANAGEMENT("ROLE_MANAGEMENT");*/
	
    private String authority;

    Role(String authority)
    {
        this.authority = authority;
    }

    @Override
    public String getAuthority()
    {
        return this.authority;
    }
}
