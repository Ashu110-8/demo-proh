package com.springrest.fnt.springrestfnt.service;

public class DistanceOfTwoCoord {
		double lat1;
		double long1;
		double lat2;
		double long2;
		double deg;
		double getDistanceFromLatLonInKm(double lat1,double long1,double lat2,double long2) {
			  int R = 6371; // Radius of the earth in km
			  double dLat = deg2rad(lat2-lat1);  // deg2rad below
			  double dLon = deg2rad(long2-long1); 
			  var a = 
			    Math.sin(dLat/2) * Math.sin(dLat/2) +
			    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
			    Math.sin(dLon/2) * Math.sin(dLon/2)
			    ; 
			  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
			  var d = R * c; // Distance in km
			  return (double) d;
			}
		double deg2rad(double deg) {
			  return deg * (Math.PI/180);
			}
		public static void main(String args[])
		{
			
			
			DistanceOfTwoCoord d = new DistanceOfTwoCoord();
			//d.getDistanceFromLatLonInKm(13.3924,62.5757,20.3924,52.5757);
			double distnaceinmeter = (d.getDistanceFromLatLonInKm(52.5748401640,13.3954305794,52.5739273296,13.3972598459)*1000);
			System.out.println(distnaceinmeter);
			
		}
	}

