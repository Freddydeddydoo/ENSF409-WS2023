/*
Copyright Ann Barcomb and Emily Marasco, 2021-2023
Licensed under GPL v3
See LICENSE.txt for more information.
*/

package edu.ucalgary.oop;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.time.LocalDate;

public class BonusTest {
    /* ***  Tests, written as ONE and TWO to generate the desired number of points *** */

    @Test
    public void bonusValuesAreReverseTreeSetByLicenceONE() {
        String[] values = valuesAreReverseTreeSetsByLicence();
        assertEquals("The values stored in parkingRecord are not stored in a reverse-ordered TreeSet per licence", values[0], values[1]); 
    }

    @Test
    public void bonusValuesAreReverseTreeSetByLicenceTWO() {
        String[] values = valuesAreReverseTreeSetsByLicence();
        assertEquals("The values stored in parkingRecord are not stored in a reverse-ordered TreeSet per licence", values[0], values[1]);        
    }

    @Test
    public void bonusKeysAreLicencesONE() {
        String[] values = keysAreLicences();
        assertEquals("Keys stored in parkingRecord are not as expected", values[0], values[1]);
    }

    @Test
    public void bonusKeysAreLicencesTWO() {
        String[] values = keysAreLicences();
        assertEquals("Keys stored in parkingRecord are not as expected", values[0], values[1]);
    }

    @Test
    public void bonusValuesAreTreeSetsONE() {
        String[] values = valuesAreTreeSets();
        assertEquals("The values stored in parkingRecord do not match the first days of all reservations", values[0], values[1]); 
    }

    @Test
    public void bonusValuesAreTreeSetsTWO() {
        String[] values = valuesAreTreeSets();
        assertEquals("The values stored in parkingRecord do not match the first days of all reservations", values[0], values[1]);
    }


    /* ***  Actual work of testing is in private methods  *** */

    /*
     * parkingRecord keys are normalized licences.
    */
    private static String[] keysAreLicences() {
        // Test data - values may be changed in actual tests
        String[] licences = {"uno", "dos", "tres", "quatro"};
        String expectedString = "[DOS, QUATRO, TRES, UNO]";
        var aDate = LocalDate.of(2056, 12, 24);

        // Insert several licences on different days
        var vp = new VisitorParking();
        for (int i=0; i < licences.length; i++) {
            vp.addVisitorReservation(licences[i], aDate);
            aDate = aDate.plusDays(4);
        } 

        // Insert the same licences for other days, some licences on the same day
        for (int i=0; i < licences.length; i++) {
            vp.addVisitorReservation(licences[i], aDate);
            aDate = aDate.plusDays(6);
            vp.addVisitorReservation(licences[i], aDate);
        }

        // Retrieve data structure, create a list of keys
        HashMap<String, TreeSet<LocalDate>> data = vp.getParkingRecord();
        ArrayList<String> actualKeys = new ArrayList<String>();
        for (String key : data.keySet()) {
            actualKeys.add(key); 
        }
        
        // Sort, get String to compare
        Collections.sort(actualKeys);
        String actualString = actualKeys.toString();
        String[] outcome = {expectedString, actualString};
        return outcome;
    }

    
    /*
    * parkingRecord values are only the first day of the reservation.
    */
    private static String[] valuesAreTreeSets() {
        // Use a TreeSet to check for unique values, in a consistent order
        TreeSet<LocalDate> expectedDays = new TreeSet<LocalDate>();

        // Test data - values may be changed in actual tests
        String[] licences = {"een", "twee", "drie", "vier", "vijf"};
        LocalDate aDate = LocalDate.of(2130, 7, 19);

        // Insert several licences on different days
        var vp = new VisitorParking();
        for (int i=0; i < licences.length; i++) {
            expectedDays.add(aDate);
            vp.addVisitorReservation(licences[i], aDate);
            aDate = aDate.plusDays(4);
        }

        // Insert the same licences for other days, some licences on the same day
        for (int i=0; i < licences.length; i++) {
            expectedDays.add(aDate);
            vp.addVisitorReservation(licences[i], aDate);
            aDate = aDate.plusDays(6);
            expectedDays.add(aDate);
            vp.addVisitorReservation(licences[i], aDate);
        }

        // Retrieve data structure, create a collection of dates
        HashMap<String, TreeSet<LocalDate>> data = vp.getParkingRecord();
        TreeSet<LocalDate> actualDays = new TreeSet<LocalDate>();
        for (TreeSet<LocalDate> value : data.values()) {
            Iterator<LocalDate> iter = value.iterator();
            while(iter.hasNext()) {
                actualDays.add(iter.next());
            }
        }

        // Compare by String
        String actualString = actualDays.toString();
        String expectedString = expectedDays.toString();
        String[] outcome = {expectedString, actualString};
        return outcome;
    }


    /*
     * parkingRecord values are stored in a reverse-ordered TreeSet, one per licence.
    */
    private static String[] valuesAreReverseTreeSetsByLicence() {
        // List of dates inserted
        ArrayList<LocalDate> expectedDays = new ArrayList<LocalDate>();

        // Test data - values may be changed in actual tests
        String licence = "Ei n";
        String extraLicence = "ZWEI";
        LocalDate aDate = LocalDate.of(2119, 8, 10);
        int counter = 7;

        // Insert reservations for a particular licence for multiple days, out of order
        var vp = new VisitorParking();
        for (int i=0; i < counter; i++) {
            expectedDays.add(aDate);
            vp.addVisitorReservation(licence, aDate);
            aDate = aDate.minusDays(3);
        }
        aDate = aDate.plusDays(1365);
        vp.addVisitorReservation(licence, aDate);
        expectedDays.add(aDate);

        // Add in values for another licence - these should not be included in the data structure for desired licence
        vp.addVisitorReservation(extraLicence, aDate); 
        aDate = aDate.minusDays(1360);
        vp.addVisitorReservation(extraLicence, aDate); 

        // Put expectedDays in reverse order
        Collections.sort(expectedDays, Collections.reverseOrder());

        // Retrieve the data structure, create an ArrayList of dates
        ArrayList<LocalDate> actualDays = new ArrayList<LocalDate>();
        HashMap<String, TreeSet<LocalDate>> data = vp.getParkingRecord();
        TreeSet<LocalDate> registeredDays = data.get(Parking.standardizeAndValidateLicence(licence));
        Iterator<LocalDate> iter = registeredDays.iterator();
        while(iter.hasNext()) {
            actualDays.add(iter.next());
        }

        // Compare String values
        String expectedString = expectedDays.toString();
        String actualString = actualDays.toString();
        String[] outcome = {expectedString, actualString};
        return outcome;
    }

}

