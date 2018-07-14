package com.example.lab.android.nuc.criminallntent.datebase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.lab.android.nuc.criminallntent.crime.Crime;

import java.util.Date;
import java.util.UUID;


public class CrimeCursorWrapper extends CursorWrapper{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));
//        String suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setDate(new Date(date));
        crime.setTitle(title);
        crime.setSolved(isSolved != 0);
//        crime.setSuspect(suspect);
        return crime;
    }
}
