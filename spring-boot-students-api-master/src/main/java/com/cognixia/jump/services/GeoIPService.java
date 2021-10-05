package com.cognixia.jump.services;


import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cognixia.jump.RestMySqlApplication;
import com.cognixia.jump.model.GeoIP;
import com.cognixia.jump.repository.GeoIPRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

@Service
public class GeoIPService {

	@Autowired
	private GeoIPRepository geoIPRepository;
	
//	@Autowired
//	private static BeanFactory beanFactory;
	
	@Autowired
	DatabaseReader dbReader;
	
	@Value("${geoliteDBPath}")
	private static String DB_PATH_STRING;
	
	public GeoIP getGeoIPfromIPAddress(HttpServletRequest req) throws Exception {
		String location = req.getHeader("x-forwarded-for");
		if( location == null) {
			location = req.getRemoteAddr();
		}
		
		InetAddress inetAddress = InetAddress.getByName(location);
		 
		CityResponse dbPull = dbReader.city(inetAddress);
		String city = dbPull.getCity().getName();
		String country = dbPull.getCountry().getName();
		
		return new GeoIP(
				inetAddress.getHostAddress(), 
				city, 
				country, 
				dbPull.getLocation().getLatitude().toString(),
				dbPull.getLocation().getLongitude().toString(), 
				null);
	}
	
//	private static DatabaseReader getDB() throws Exception {
//		return beanFactory.getBean(RestMySqlApplication.class, "dbReader").dbReader(DB_PATH_STRING);
//	}
	
}
