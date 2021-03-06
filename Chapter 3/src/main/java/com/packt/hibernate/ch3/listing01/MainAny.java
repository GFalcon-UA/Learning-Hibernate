package com.packt.hibernate.ch3.listing01;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class MainAny {

	public static void main(String[] args) {
		try {

			Traveler traveler = new Traveler();
			traveler.setFirstname("David");
			traveler.setLastname("Smith");
			
			Car car = new Car();
			car.setCost(350.00);
			car.setPickupLocation("Washington");
			car.setDropOffLocation("Baltimore");
			
			Airplane plane = new Airplane();
			plane.setCost(500.00);
			plane.setDepartureAirport("BWI");
			plane.setArrivalAirport("JFK");
			
			traveler.getTransportation().add(car);
			traveler.getTransportation().add(plane);
			
			
			Session session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			Transaction transaction = session.beginTransaction();
			try {
				session.save(traveler);
				session.save(car);
				session.save(plane);

				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				// log stack trace
				e.printStackTrace();

			} finally {
				if (session.isOpen())
					session.close();
			}
			
			session = HibernateUtil.getSessionFactory()
					.getCurrentSession();
			transaction = session.beginTransaction();
			try {
				traveler = (Traveler) session.get(Traveler.class, new Long(1));
				
				Iterator<Transportation> iterator = traveler.getTransportation().iterator();
				
				while (iterator.hasNext()) {
					Transportation transportation = iterator.next();
					if (transportation instanceof Car) {
						System.out.println("car: " + transportation);
					}
					else {
						System.out.println("airplane: " + transportation);						
					}
				}

				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				// log stack trace
				e.printStackTrace();

			} finally {
				if (session.isOpen())
					session.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		HibernateUtil.stopService();
	}

}
