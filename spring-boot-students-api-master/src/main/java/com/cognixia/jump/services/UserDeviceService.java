package com.cognixia.jump.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.jump.model.GeoIP;
import com.cognixia.jump.model.UserDevice;
import com.cognixia.jump.repository.UserDeviceRepository;

@Service
public class UserDeviceService {
	
	@Autowired
	UserDeviceRepository userDeviceRepository;
	
	@Autowired
	GeoIPService geoIpService;
	
	public UserDevice getUserDeviceDetails(HttpServletRequest req) throws Exception {
		GeoIP geoIP = geoIpService.getGeoIPfromIPAddress(req);
		String device = req.getHeader("User-Agent");
		
		return new UserDevice(
				1L, 
				null, 
				device, 
				geoIP, 
				null);
	}

}
