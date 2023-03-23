/*
Copyright Ann Barcomb and Emily Marasco, 2021-2023
Licensed under GPL v3
See LICENSE.txt for more information.
*/

package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.*;

public class NewFeatureTest {

    @Test
    /*
     * Both constructors and both addVisitorReservation() should throw an IllegalArgumentException for an invalid licence as defined in the Parking class.
    */
    public void testThrowsExceptionWithInvalidLicence() {
        boolean passed = false;

        // Test data - values may be changed in actual tests
        String badLicence = "76543210";
        var aDate = LocalDate.of(2222, 9, 26);

        // Check new VisitorParking(licence)
        try {
            var vp = new VisitorParking(badLicence);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 1-argument constructor did not throw IllegalArgumentException when given invalid licence", passed);

        // Check new VisitorParking(licence, date)
        passed = false;
        try {
            var vp = new VisitorParking(badLicence, aDate);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 2-argument constructor did not throw IllegalArgumentException when given invalid licence", passed);

        // Check addVisitorReservation(licence)
        passed = false;
        var vp = new VisitorParking();
        try {
            vp.addVisitorReservation(badLicence);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 1-argument addVisitorReservation did not throw IllegalArgumentException when given invalid licence", passed);

        // Check addVisitorReservation(licence, date)
        passed = false;
        try {
            vp.addVisitorReservation(badLicence, aDate);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 2-argument addVisitorReservation did not throw IllegalArgumentException when given invalid licence", passed);
    }



    @Test
    /*
     * The constructor and addVisitorReservation() should throw an IllegalArgumentException when provided with a date in the past.
    */
    public void testThrowsExceptionWithInvalidDate() {
        boolean passed = false;

        // Test data - values may be changed in actual tests
        String licence = "5678T";
        var badDate = LocalDate.of(2022, 2, 28);

        // Check new VisitorParking(licence, date)
        try {
            var vp = new VisitorParking(licence, badDate);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 2-argument constructor did not throw IllegalArgumentException when given past date", passed);

        // Check addVisitorReservation(licence, date)
        passed = false;
        var vp = new VisitorParking();
        try {
            vp.addVisitorReservation(licence, badDate);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 2-argument addVisitorReservation did not throw IllegalArgumentException when given past date", passed);
    }

    @Test
    /*
     * addVisitorReservation() should throw an IllegalArgumentException when a licence is reserved.
    */
    public void testThrowsExceptionWithOverlapDate() {
        // Test data - values may be changed in actual tests
        String licence = "VROOM";
        var aDate = LocalDate.of(2038, 3, 11);

        var datePlusOne = aDate.plusDays(1);
        boolean passed = false;
        var vp = new VisitorParking();
        vp.addVisitorReservation(licence, aDate);

        // Try with specifying a date
        try {
            vp.addVisitorReservation(licence, datePlusOne);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 2-argument addVisitorReservation did not throw IllegalArgumentException when two reservations for the same licence include the same day", passed);

       // Try without specifying a date
        passed = false;
        vp.addVisitorReservation(licence);
        try {
            vp.addVisitorReservation(licence);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 1-argument addVisitorReservation did not throw IllegalArgumentException when two reservations for the same licence include the same day", passed);
    }


    @Test
    /*
     * addVisitorReservation() should throw an exception when trying to make a third reservation on the same day.
    */
    public void testThrowsExceptionTooManyPermits() {
        boolean passed = false;

        // Test data - values may be changed in actual tests
        var date1 = LocalDate.of(2040, 1, 1); // Reservation covers dates 1, 2, 3
        var date2 = LocalDate.of(2040, 1, 2); // Reservation covers dates 2, 3, 4
        var date3 = LocalDate.of(2040, 1, 3); // Reservation covers dates 3, 4, 5
        String[] licences = {"one", "two", "three", "four", "five", "six"};

        // Try with specifying a date
        var vp = new VisitorParking();
        vp.addVisitorReservation(licences[0], date1);
        vp.addVisitorReservation(licences[1], date2);
 
        try {
            vp.addVisitorReservation(licences[2], date3);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 2-argument addVisitorReservation did not throw IllegalArgumentException when attempting a third reservation on the same date", passed);

        // Try without specifying a date
        passed = false;
        vp.addVisitorReservation(licences[3]);
        vp.addVisitorReservation(licences[4]);
        try {
            vp.addVisitorReservation(licences[5]);
        }
        catch (IllegalArgumentException e) {
            passed = true;
        }
        catch (Exception e) { }
        assertTrue("VisitorParking 1-argument addVisitorReservation did not throw IllegalArgumentException when attempting a third reservation on the same date", passed);
    }


    /*
     * It should be possible to add three reservations when the first and third don't overlap in time with each other, and the second overlaps with both.
    */
    @Test
    public void testAllowsNonOverlappingReservations() {
        boolean passed = true;
  
        // Test data - values may be changed in actual tests
        var date1 = LocalDate.of(2040, 1, 1); // Reservation covers dates 1, 2, 3
        var date2 = LocalDate.of(2040, 1, 2); // Reservation covers dates 2, 3, 4
        var date3 = LocalDate.of(2040, 1, 4); // Reservation covers dates 4, 5, 6
        String[] licences = {"zero", "medium", "max"};
        
        var vp = new VisitorParking();
        vp.addVisitorReservation(licences[0], date1);
        vp.addVisitorReservation(licences[1], date2);
        
        try {
            vp.addVisitorReservation(licences[2], date3);
        }
        catch (Exception e) {
            passed = false;
        }
        assertTrue("VisitorParking addVisitorReservation threw an exception when attempting a third non-overlapping reservation", passed);
    }


    /*
     * It should be possible to make two reservations for a single licence, where the second reservation starts the day after the first reservation ends.
    */
    @Test
    public void testAllowsSequentialReservationsForLicence() {
        boolean passed = true;

        // Test data - values may be changed in actual tests
        String licence = "my num";
        var date1 = LocalDate.of(2040, 1, 1); // Reservation covers dates 1, 2, 3
        var date2 = LocalDate.of(2040, 1, 4); // Reservation covers dates 4, 5, 6

        var vp = new VisitorParking();
        vp.addVisitorReservation(licence, date1);

        try {
            vp.addVisitorReservation(licence, date2);
        }
        catch (Exception e) {
            passed = false;
        }
        assertTrue("VisitorParking addVisitorReservation threw an exception when attempting non-overlapping reservations for one licence", passed);
    }


    /*
     * It should be possible to make a lot of reservations, and those reservations don't have to be made in order.
     * When making reservations and using licenceIsRegisteredForDate(), licences are standardized, consistent with Parking
    */
    @Test
    public void testManyReservations() {
        boolean success = true;

        // Test data - values may be changed in actual tests
        var date1 = LocalDate.of(2040, 1, 1); 
        var date2 = LocalDate.of(2040, 1, 3);  // Overlap with date1
        var timesToLoop = 18;
        String licence1 = "62092";
        String licence2 = "abc0-s"; 

        // Reservations are being made out of order to ensure there is no limitation related to ordering
        var today = LocalDate.now();
        var vp = new VisitorParking(licence1);
        vp.addVisitorReservation(licence2, date2);
        vp.addVisitorReservation(licence2);
        vp.addVisitorReservation(licence1, date1);


        // Insert a lot of reservations and check that they are inserted
        LocalDate tmp = date2.plusDays(3);
        for (int i=0; i< timesToLoop; i++) {
            vp.addVisitorReservation(licence1, tmp);
            success = (success && vp.licenceIsRegisteredForDate(licence1, tmp));
            tmp = tmp.plusDays(3); 
        }
        
        // Check that earlier reservations were stored as well
        success = (success && vp.licenceIsRegisteredForDate(licence1, today));
        success = (success && vp.licenceIsRegisteredForDate(licence2));
        success = (success && vp.licenceIsRegisteredForDate(licence1, date1));
        success = (success && vp.licenceIsRegisteredForDate(licence2, date2));

        assertTrue("VisitorParking addVisitorReservation() didn't correctly add two licences (one requiring standardization) across multiple dates", success);
    }


    /*
     * getLicencesRegisteredForDate() should give all licences registered for today when called without an argument.
    */
    @Test 
    public void testLicencesRegisteredForToday() {
        // Test data - values may be changed in actual tests
        String licence1 = "bicycle";
        String licence2 = "my-car";

        // Get comparision values
        var today = LocalDate.now();
        String expected1 = Parking.standardizeAndValidateLicence(licence1);
        String expected2 = Parking.standardizeAndValidateLicence(licence2);

        // Insert some reservations, get a list of all reservations
        var vp = new VisitorParking(licence1);
        vp.addVisitorReservation(licence2);
        ArrayList<String> result = vp.getLicencesRegisteredForDate();
        Iterator<String> it = result.iterator();

        // Variables to check the results
        int count = 0; 
        boolean success1 = false; 
        boolean success2 = false; 
        boolean noOtherData = true;

        // Compare contents of ArrayList against expectations:
        // expected1 found once
        // expected2 found once
        // nothing else found
        while (it.hasNext()) {
            String tmp = it.next();
            if (tmp.equals(expected1)) {
                success1 = !success1; // toggle value
                count++;
            } else if (tmp.equals(expected2)) {
                success2 = !success2;
                count++;
            } else {
                noOtherData = false;
            }
        }

        assertTrue("VisitorParking 0-argument getLicencesRegisteredForDate() does not include expected licence", (success1 && success2));
        assertTrue("VisitorParking 0-argument getLicencesRegisteredForDate() does not include only one instance of expected licence", (count <= 2));
        assertTrue("VisitorParking 0-argument getLicencesRegisteredForDate() includes unexpected entry", noOtherData);
    }


    /*
     * getLicencesRegisteredForDate() should give all licences registered for a specified day.
    */
    @Test
    public void testLicencesRegisteredForDate() {
        // Test data - values may be changed in actual tests
        String licence1 = "t o t a l ";
        String licence2 = "ChAoS";
        var date1 = LocalDate.of(2040, 1, 1); 
        var date2 = LocalDate.of(2040, 1, 2); 
        var commonDate = LocalDate.of(2040, 1, 3);  // date1 and date2 have this date in common

        // Values that we are expecting
        String expected1 = Parking.standardizeAndValidateLicence(licence1);
        String expected2 = Parking.standardizeAndValidateLicence(licence2);

        // Add some reservations, get a list of all reservations for the overlapping date
        var vp = new VisitorParking(licence1, date1);
        vp.addVisitorReservation(licence2, date2);
        ArrayList<String> result = vp.getLicencesRegisteredForDate(commonDate);
        Iterator<String> it = result.iterator();

        // Variables to check the results 
        int count = 0;
        boolean success1 = false;
        boolean success2 = false;
        boolean noOtherData = true;

        // Compare contents of ArrayList against expectations:
        // expected1 found once
        // expected2 found once
        // nothing else found
        while (it.hasNext()) {
            String tmp = it.next();
            if (tmp.equals(expected1)) {
                success1 = !success1; // toggle value
                count++;
            } else if (tmp.equals(expected2)) {
                success2 = !success2;
                count++;
            } else {
                noOtherData = false;
            }
        }

        assertTrue("VisitorParking 1-argument getLicencesRegisteredForDate() does not include expected licence", (success1 && success2));
        assertTrue("VisitorParking 1-argument getLicencesRegisteredForDate() does not include only one instance of expected licence", (count <= 2));
        assertTrue("VisitorParking 1-argument getLicencesRegisteredForDate() includes unexpected entry", noOtherData);
    }


    /*
     * Each reservation is made for the day of the reservation, plus two additional days only.
    */
    @Test
    public void testReservationsForThreeDaysOnly() {
        // Test data - values may be changed in actual tests
        String licence = "y";
        var registeredDate = LocalDate.of(2040, 1, 1); 

        // Values for comparision
        var afterDate = registeredDate.plusDays(3);
        var beforeDate = registeredDate.minusDays(1);

        // Insert the reservation
        var vp = new VisitorParking(licence, registeredDate);

        // Check that there is no reservation after the period ends
        ArrayList <String> result = vp.getLicencesRegisteredForDate(afterDate);
        assertTrue("VisitorParking registers reservations for more than date plus 2 days", result.isEmpty());

        // Check that there are no reservations before the period starts
        boolean beforeFails = vp.licenceIsRegisteredForDate(licence, beforeDate);
        assertFalse("VisitorParking registers reservations for before the provided date", beforeFails);

        // Check that there are reservations for the three days that are expected
        boolean successDay1 = vp.licenceIsRegisteredForDate(licence, registeredDate);
        boolean successDay2 = vp.licenceIsRegisteredForDate(licence, registeredDate.plusDays(1));
        boolean successDay3 = vp.licenceIsRegisteredForDate(licence, registeredDate.plusDays(2));
        assertTrue("VisitorParking does not register reservation for three days", (successDay1 && successDay2 && successDay3));
    }

    /*
     * The method getAllDaysLicenceIsRegistered() returns an ordered list of the entire period for which a licence is registered.
    */
    @Test
    public void testAllDaysLicenceIsRegistered() {
        // Test data - values may be changed in actual tests
        String licence = "nope";
        var registeredDate1 = LocalDate.of(2046, 5, 15);
        var registeredDate2 = LocalDate.of(2046, 5, 30);
        String expectedString = "[2046-05-15, 2046-05-16, 2046-05-17, 2046-05-30, 2046-05-31, 2046-06-01]"; // Used to confirm list is ordered

        // Generate additional expected values
        var secondDay1 = registeredDate1.plusDays(1);
        var thirdDay1 = registeredDate1.plusDays(2);
        var secondDay2 = registeredDate2.plusDays(1);
        var thirdDay2 = registeredDate2.plusDays(2);

        // Keeping track of testing results
        boolean success1 = false;
        boolean success2 = false;
        boolean success3 = false;
        boolean success4 = false;
        boolean success5 = false;
        boolean success6 = false;
        boolean onlyExpectedDays = true;

        // Make the reservations and retrieve the ArrayList
        var vp = new VisitorParking(licence, registeredDate1);
        vp.addVisitorReservation(licence, registeredDate2);
        ArrayList<LocalDate> actualDates = vp.getAllDaysLicenceIsRegistered(licence);
        Iterator <LocalDate> iter = actualDates.iterator();

        // Check that for each day (day1 and day2), the first, second, and third days are included, and no other days are included
        while(iter.hasNext()) {
            LocalDate tmp = iter.next();
            if (tmp.equals(registeredDate1)) {
                success1 = true;
            } else if (tmp.equals(secondDay1)) {
                success2 = true;
            } else if (tmp.equals(thirdDay1)) {
                success3 = true;
            } else if (tmp.equals(registeredDate2)) {
                success4 = true;
            } else if (tmp.equals(secondDay2)) {
                success5 = true;
            } else if (tmp.equals(thirdDay2)) {
                success6 = true;
            } else {
                onlyExpectedDays = false;
            }
        }

        assertTrue("VisitorParking getAllDaysLicenceIsRegistered() does not contain all days the licence is registered (first registration)", (success1 && success2 && success3));
        assertTrue("VisitorParking getAllDaysLicenceIsRegistered() does not contain all days the licence is registered (second registration)", (success4 && success5 && success6));
        assertTrue("VisitorParking getAllDaysLicenceIsRegistered() contains days which are not registered", onlyExpectedDays);
        assertEquals("VisitorParking getAllDaysLicenceIsRegistered() does not return an ordered list", expectedString, actualDates.toString());
    }

    /*
     * The method getStartDaysLicenceIsRegistered() returns an ordered list consisting of the starting days which a licence is registered.
    */
    @Test
    public void testStartDaysLicenceIsRegistered() {
        // Test data - values may be changed in actual tests
        String licence = "why not";
        var registeredDate1 = LocalDate.of(2052, 11, 30);
        String expectedString = "[2052-11-30, 2052-12-03, 2052-12-10]"; // Used to confirm list is ordered

        // Generate additional expected values
        var registeredDate2 = registeredDate1.plusDays(3);
        var registeredDate3 = registeredDate1.plusDays(10);

        // Keeping track of results
        boolean success1 = false;
        boolean success2 = false;
        boolean success3 = false;
        boolean onlyExpectedDays = true;

        // Make the reservations and retrieve the ArrayList
        var vp = new VisitorParking(licence, registeredDate3);
        vp.addVisitorReservation(licence, registeredDate1);
        vp.addVisitorReservation(licence, registeredDate2);
        ArrayList<LocalDate> actualDates = vp.getStartDaysLicenceIsRegistered(licence);
        Iterator <LocalDate> iter = actualDates.iterator();

        // Check that each day (registeredDates 1, 2, and 3) is present, and no other days are included
        while(iter.hasNext()) {
            LocalDate tmp = iter.next();
            if (tmp.equals(registeredDate1)) {
                success1 = true;
            } else if (tmp.equals(registeredDate2)) {
                success2 = true;
            } else if (tmp.equals(registeredDate3)) {
                success3 = true;
            } else {
                onlyExpectedDays = false;
            }
        }

        assertTrue("VisitorParking startDaysLicenceIsregistered() does not contain all starting registration dates", (success1 && success2 && success3));
        assertTrue("VisitorParking startDaysLicenceIsregistered() contains days which are not registered", onlyExpectedDays);
        assertEquals("VisitorParking startDaysLicenceIsregistered() did not return an ordered list", expectedString, actualDates.toString());
    }

    /*
     * HouseholdParking has just one resident licence. That licence is standardized.
     * An exception is thrown with an invalid licence.
    */
    @Test
    public void testOnlyOneResidentLicence() {
        // Test data - values may be changed in actual tests
        HouseholdParking hp = new HouseholdParking(111, "residential", "Schulich Street", 4, "T4R 9U2");
        String givenLicence = "ABC DEF-1";
        String givenLicence2 = "123456";
        String invalidLicence = "";

        // Test adding
        String expectedLicence = Parking.standardizeAndValidateLicence(givenLicence);
        hp.addOrReplaceResidentLicence(givenLicence);
        String actualLicence = hp.getResidentLicence();
        assertEquals("Licence is not set or retrieved correctly for resident", expectedLicence, actualLicence);

        // Test replacing
        expectedLicence = givenLicence2;
        hp.addOrReplaceResidentLicence(givenLicence2);
        actualLicence = hp.getResidentLicence();
        assertEquals("Resident licence can't be replaced", expectedLicence, actualLicence);
 
        // Test deleting
        expectedLicence = "";
        hp.removeResidentLicence();
        actualLicence = hp.getResidentLicence();
        assertEquals("Unable to remove resident licence", expectedLicence, actualLicence);

        // Test invalid
        boolean exceptionThrown = false;
        try {
            hp.addOrReplaceResidentLicence(invalidLicence);
        }
        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        catch (Exception e) { }
        assertTrue("addOrReplaceResidentLicence() does not throw an IllegalArgumentException with invalid input", exceptionThrown); 
    }


    /*
     * It is possible to use addVisitorReservation() using a Household object.
     * The method getVisitors() can be used to access the VisitorParking object.
     * Include a date which goes into the next month.
    */
    @Test
    public void testAddViaHouseholdParking() {
        // Test data - values may be changed in actual tests
        HouseholdParking hp = new HouseholdParking(30215, "residential", "University Lane", 42, "T4R 9N2", "A");
        var date1 = LocalDate.of(2040, 6, 29);
        String licence = "LICENSE";

        // Ensure the VisitorParking object can be accessed
        VisitorParking vp = hp.getVisitors();
        assertTrue("HouseholdParking getVisitors() doesn't return a VisitorParking object", (vp instanceof VisitorParking));
 
        // Add reservations, both with the date provided and with it assumed
        hp.addVisitorReservation(licence);
        hp.addVisitorReservation(licence, date1);
        var today = LocalDate.now();

        boolean success = vp.licenceIsRegisteredForDate(licence, date1);
        assertTrue("Visitor licence for specific date can't be added with HouseholdParking", success);

        success = vp.licenceIsRegisteredForDate(licence, today);
        assertTrue("Visitor licence without specified date can't be added with HouseholdParking", success);
    }


    /*
     * Check all the getters and boolean methods in VisitorParking can be accessed via HouseholdParking.
     * Include a date which goes into the next year.
    */
    @Test
    public void testAccessViaHouseholdParking() {
        // Test data - values may be changed in actual tests
        HouseholdParking hp = new HouseholdParking(1, "residential", "University Drive", 16, "T4R9N2");
        var date1 = LocalDate.of(2040, 1, 1);
        var date2 = LocalDate.of(2040, 1, 2);  
        var commonDate = LocalDate.of(2040, 1, 3);  // Reservations for date1 and date2 include this day
        String licence1 = "SNEK";
        String licence2 = "DOG";
        String licence3 = "CAT";

        // Retrieve VisitorParking object and add reservations using it
        var today = LocalDate.now();
        VisitorParking vp = hp.getVisitors();
        vp.addVisitorReservation(licence1, date1);
        vp.addVisitorReservation(licence2, date2);
        vp.addVisitorReservation(licence3);

        // Access data via the HouseholdParking object getLicencesRegisteredForDate(commonDate)
        ArrayList<String> result = hp.getLicencesRegisteredForDate(commonDate);
        Iterator<String> it = result.iterator();

        // Keep track of outcome
        int count = 0;
        boolean success1 = false;
        boolean success2 = false;
        boolean noOtherData = true;

        // Compare contents of ArrayList against expectations:
        // licence1 found once
        // licence2 found once
        // nothing else found
        while (it.hasNext()) {
            String tmp = it.next();
            if (tmp.equals(licence1)) {
                success1 = !success1; // toggle value
                count++;
            } else if (tmp.equals(licence2)) {
                success2 = !success2;
                count++;
            } else {
                noOtherData = false;
            }
        }

        assertTrue("HouseholdParking 1-argument getLicencesRegisteredForDate() does not include expected licence", (success1 && success2));
        assertTrue("HouseholdParking 1-argument getLicencesRegisteredForDate() does not include only one instance of expected licence", (count <= 2));
        assertTrue("HouseholdParking 1-argument getLicencesRegisteredForDate() includes unexpected entry", noOtherData);

        // Access data via HouseholdParking object, getLicencesRegisteredForDate()
        ArrayList<String> result2 = hp.getLicencesRegisteredForDate();
        String todayString = result2.toString();
        assertEquals("HouseholdParking 1-argument getLicencesRegisteredForDate() does not contain expected data", "[CAT]", todayString);

        // Access data via HouseholdParking object, licenceIsRegisteredForDate()
        boolean found0 = hp.licenceIsRegisteredForDate(licence3);
        assertTrue("HouseholdParking 0-argument licenceIsRegisteredForDate() does not return true for expected licence", found0);
        boolean found1 = hp.licenceIsRegisteredForDate(licence1, date1);
        assertTrue("HouseholdParking 1-argument licenceIsRegisteredForDate() does not return true for expected licence", found1);

        // Add a bunch of other reservations, out of order and one of them across a year
        vp.addVisitorReservation(licence1, LocalDate.of(2040, 12, 13));
        vp.addVisitorReservation(licence1, LocalDate.of(2040, 12, 30));
        vp.addVisitorReservation(licence3, LocalDate.of(2040, 12, 26));
        vp.addVisitorReservation(licence3, LocalDate.of(2040, 9, 21));
        vp.addVisitorReservation(licence3, LocalDate.of(2040, 9, 11));

        // Check that getStartDaysLicenceIsRegistered() and getAllDaysLicenceIsRegistered() work with access via HouseholdParking
        String actualAllDays = hp.getAllDaysLicenceIsRegistered(licence1).toString();
        String actualStartDays = hp.getStartDaysLicenceIsRegistered(licence3).toString();
        String expectedStartDays = "[" + today.toString() + ", 2040-09-11, 2040-09-21, 2040-12-26]";
        String expectedAllDays = "[2040-01-01, 2040-01-02, 2040-01-03, 2040-12-13, 2040-12-14, 2040-12-15, 2040-12-30, 2040-12-31, 2041-01-01]";
        assertEquals("HouseholdParking getStartDaysLicenceIsRegistered() does not return a correct ordered list", expectedStartDays, actualStartDays);
        assertEquals("HouseholdParking getAllDaysLicenceIsRegistered() does not return a correct ordered list", expectedAllDays, actualAllDays);
    }
}

