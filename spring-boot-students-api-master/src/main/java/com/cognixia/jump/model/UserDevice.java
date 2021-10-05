package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UserDevice implements Serializable {

	private static final long serialVersionUID = 2147846600362237096L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToMany(mappedBy = "userDevices")
	Set<User> users;
	
	private String deviceDetails;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "geoip_id", referencedColumnName = "id")
	private GeoIP geoIP;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoggedIn;
	
	public UserDevice() {}

	public UserDevice(Long id, Set<User> users, String deviceDetails, GeoIP geoIP, Date lastLoggedIn) {
		super();
		this.id = id;
		this.users = users;
		this.deviceDetails = deviceDetails;
		this.geoIP = geoIP;
		this.lastLoggedIn = lastLoggedIn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getDeviceDetails() {
		return deviceDetails;
	}

	public void setDeviceDetails(String deviceDetails) {
		this.deviceDetails = deviceDetails;
	}

	public GeoIP getGeoIP() {
		return geoIP;
	}

	public void setGeoIP(GeoIP geoIP) {
		this.geoIP = geoIP;
	}

	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserDevice [id=" + id + ", users=" + users + ", deviceDetails=" + deviceDetails + ", geoIP=" + geoIP
				+ ", lastLoggedIn=" + lastLoggedIn + "]";
	}
	
	public String toJson() {
		return "{id=" + id + ", users=" + users + ", deviceDetails=" + deviceDetails + ", geoIP=" + geoIP.toJson()
				+ ", lastLoggedIn=" + lastLoggedIn + "}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceDetails == null) ? 0 : deviceDetails.hashCode());
		result = prime * result + ((geoIP == null) ? 0 : geoIP.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDevice other = (UserDevice) obj;
		if (deviceDetails == null) {
			if (other.deviceDetails != null)
				return false;
		} else if (!deviceDetails.equals(other.deviceDetails))
			return false;
		if (geoIP == null) {
			if (other.geoIP != null)
				return false;
		} else if (!geoIP.equals(other.geoIP))
			return false;
		return true;
	}

}
