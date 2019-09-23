package it.eng.unipa.filesharing.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class UserRole {
	
	@EmbeddedId
	UserRoleOid oid;
	
	
	@ManyToOne
	@MapsId("uuid")
	private Team team;

	
	@Column(nullable = true)
	private String longName;
	
	private boolean admin;
	
	private boolean active;
	
	public UserRole() {
		
	}
	
	public UserRole(Team team,String email) {
		this(team, email, false);
	}
	
	public UserRole(Team team,String email,boolean admin) {
		this(team, email, admin, false);
	}
	
	public UserRole(Team team,String email,boolean admin,boolean active) {
		this.oid = new UserRoleOid();
		this.oid.setEmail(email);
		this.oid.setUuid(team.getUuid());
		this.team = team;
		this.admin = admin;
		this.active = active;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void enableAdmin(){
		this.admin = true;
	}
	
	public void disableAdmin(){
		this.admin = false;
	}
	
	public String getLongName() {
		return longName;
	}
	
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public UserRoleOid getOid() {
		return oid;
	}
	
	public void setOid(UserRoleOid oid) {
		this.oid = oid;
	}
	
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserRole)) {
            return false;
        }
        UserRole that = (UserRole) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.getOid(), that.getOid());
        return eb.isEquals();
    }
	
	@Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(this.getOid());
        return hcb.toHashCode();
    }

	public void clearTeam() {
		this.team = null;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void active(String longName) {
		this.setLongName(longName);
		this.active = true;
	}
	
}
