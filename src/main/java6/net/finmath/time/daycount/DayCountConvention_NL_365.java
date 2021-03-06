/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 07.09.2013
 */

package net.finmath.time.daycount;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.chrono.IsoChronology;

/**
 * Implementation of NL/365.
 *
 * Calculates the day count by calculating the actual number of days between startDate (excluding) and endDate (including), excluding a leap day (February 29th) if present.
 *
 * A fractional day is rounded to the approximately nearest day.
 *
 * The day count fraction is calculated using NL/365 convention, that is, the
 * day count is divided by 365.
 *
 * @author Christian Fries
 * @version 1.0
 */
public class DayCountConvention_NL_365 implements DayCountConventionInterface {

	/**
	 * Create an NL/365 day count convention.
	 */
	public DayCountConvention_NL_365() {
	}

	@Override
	public double getDaycount(LocalDate startDate, LocalDate endDate) {
		if(startDate.isAfter(endDate)) {
			return -getDaycount(endDate,startDate);
		}

		// Get actual number of days
		double daycount = DayCountConvention_ACT.daysBetween(startDate, endDate);

		/*
		 * Remove leap days, if any.
		 */
		for(int year = startDate.getYear() ; year <= endDate.getYear(); year++) {
			if (IsoChronology.INSTANCE.isLeapYear(year)) {
				LocalDate leapDay = LocalDate.of(year,Month.FEBRUARY, 29);
				if(startDate.isBefore(leapDay) && !endDate.isBefore(leapDay)) {
					daycount -= 1.0;
				}
			}
		}

		if(daycount < 0.0) {
			throw new AssertionError("Daycount is negative for startDate not after endDate.");
		}

		return daycount;
	}

	@Override
	public double getDaycountFraction(LocalDate startDate, LocalDate endDate) {
		if(startDate.isAfter(endDate)) {
			return -getDaycountFraction(endDate,startDate);
		}

		double daycountFraction = getDaycount(startDate, endDate) / 365.0;

		return daycountFraction;
	}
}
