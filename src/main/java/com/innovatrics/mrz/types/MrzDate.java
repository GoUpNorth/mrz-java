/**
 * Java parser for the MRZ records, as specified by the ICAO organization.
 * Copyright (C) 2011 Innovatrics s.r.o.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.innovatrics.mrz.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


/**
 * Holds a MRZ date type.
 * @author Martin Vysny
 */
public class MrzDate implements Serializable, Comparable<MrzDate> {
    private static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(MrzDate.class);


    /**
     * Year, 00-99.
     * <p/>
     * Note: I am unable to find a specification of conversion of this value to a full year value.
     */
    public int year;
    /**
     * Month, 1-12.
     */
    public int month;
    /**
     * Day, 1-31.
     */
    public int day;

    /**
     * Is the date valid or not
     */
    private final boolean isDateValid;

    /**
     * Raw year
     */
    private final String rawYear;
    /**
     * Raw month
     */
    private final String rawMonth;
    /**
     * Raw day
     */
    private final String rawDay;


    /**
     *MrzDate
     * @param year Parsed year
     * @param month  Parsed month
     * @param day  Parsed day
     */
    public MrzDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        rawYear = String.format("%02d", year);
        rawMonth = String.format("%02d", month);
        rawDay = String.format("%02d", day);

        isDateValid = check();
    }

    /**
     * MrzDate
     * @param rawYear Raw year to parse
     * @param rawMonth Raw month to parse
     * @param rawDay Raw day to parse
     */
    public MrzDate(String rawYear, String rawMonth, String rawDay){
        this.rawYear = rawYear;
        this.rawMonth = rawMonth;
        this.rawDay = rawDay;

        try {
            this.year = Integer.parseInt(rawYear);
        } catch (NumberFormatException e) {
            log.debug("Failed to parse MRZ date year " + rawYear, e);
            this.year = -1;
        }
        try {
            this.month = Integer.parseInt(rawMonth);
        } catch (NumberFormatException e) {
            log.debug("Failed to parse MRZ date month " + rawMonth, e);
            this.month = -1;
        }

        try {
            this.day = Integer.parseInt(rawDay);
        } catch (NumberFormatException e)  {
            log.debug("Failed to parse MRZ date day " + rawDay, e);
            this.day = -1;
        }

        isDateValid = check();
    }

    @Override
    public String toString() {
        return "{" + day + "/" + month + "/" + year + '}';
    }

    public String toMrz() {
        return String.format("%s%s%s", rawYear, rawMonth, rawDay);
    }

    private boolean check() {
        if (year < 0 || year > 99) {
            log.debug("Parameter year: invalid value " + year + ": must be 0..99");
            return false;
        }
        if (month < 1 || month > 12) {
            log.debug("Parameter month: invalid value " + month + ": must be 1..12");
            return false;
        }
        if (day < 1 || day > 31) {
            log.debug("Parameter day: invalid value " + day + ": must be 1..31");
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MrzDate other = (MrzDate) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        if(!this.rawYear.contentEquals(other.rawYear)) {
            return false;
        }
        if(!this.rawMonth.contentEquals(other.rawMonth)) {
            return false;
        }
        if(!this.rawDay.contentEquals(other.rawDay)){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.year;
        hash = 11 * hash + this.month;
        hash = 11 * hash + this.day;
        return hash;
    }

    public int compareTo(MrzDate o) {
        return Integer.compare(year * 10000 + month * 100 + day, o.year * 10000 + o.month * 100 + o.day);
    }

    /**
     * Returns the date validity
     * @return Returns a boolean true if the parsed date is valid, false otherwise
     */
    public boolean isDateValid() {
        return isDateValid;
    }
}
