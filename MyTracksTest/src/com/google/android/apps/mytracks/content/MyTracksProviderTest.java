/*
 * Copyright 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.android.apps.mytracks.content;

import com.google.android.apps.mytracks.content.MyTracksProvider.DatabaseHelper;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * A unit test for {@link MyTracksProvider}.
 * 
 * @author Youtao Liu
 */
public class MyTracksProviderTest extends AndroidTestCase {

  private SQLiteDatabase db;
  private MyTracksProvider myTracksProvider;

  @Override
  protected void setUp() throws Exception {
    recreatedDatabase();
    myTracksProvider = new MyTracksProvider();
    super.setUp();
  }

  /**
   * Tests the method {@link MyTracksProvider.DatabaseHelper#onCreate()}.
   */
  public void testDatabaseHelper_OnCreate() {
    assertTrue(checkTable(TrackPointsColumns.TABLE_NAME));
    assertTrue(checkTable(TracksColumns.TABLE_NAME));
    assertTrue(checkTable(WaypointsColumns.TABLE_NAME));
  }

  /**
   * Tests the method
   * {@link MyTracksProvider.DatabaseHelper#onUpgrade(SQLiteDatabase, int, int)}
   * when version is less than 17.
   */
  public void testDatabaseHelper_onUpgrade_Version16() {
    DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
    dropTable(TrackPointsColumns.TABLE_NAME);
    dropTable(TracksColumns.TABLE_NAME);
    dropTable(WaypointsColumns.TABLE_NAME);
    databaseHelper.onUpgrade(db, 16, 20);
    assertTrue(checkTable(TrackPointsColumns.TABLE_NAME));
    assertTrue(checkTable(TracksColumns.TABLE_NAME));
    assertTrue(checkTable(WaypointsColumns.TABLE_NAME));
  }
  
  /**
   * Tests the method
   * {@link MyTracksProvider.DatabaseHelper#onUpgrade(SQLiteDatabase, int, int)}
   * when version is 17.
   */
  public void testDatabaseHelper_onUpgrade_Version17() {
    DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
    
    // Make two table is only contains one normal integer column.
    dropTable(TrackPointsColumns.TABLE_NAME);
    dropTable(TracksColumns.TABLE_NAME);
    createEmptyTable(TrackPointsColumns.TABLE_NAME);
    createEmptyTable(TracksColumns.TABLE_NAME);
    databaseHelper.onUpgrade(db, 17, 20);
    assertTrue(checkColumn(TrackPointsColumns.TABLE_NAME, TrackPointsColumns.SENSOR));
    assertTrue(checkColumn(TracksColumns.TABLE_NAME, TracksColumns.TABLEID));
    assertTrue(checkColumn(TracksColumns.TABLE_NAME, TracksColumns.ICON));
  }
  
  /**
   * Tests the method
   * {@link MyTracksProvider.DatabaseHelper#onUpgrade(SQLiteDatabase, int, int)}
   * when version is 18.
   */
  public void testDatabaseHelper_onUpgrade_Version18() {
    DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
    
    // Make two table is only contains one normal integer column.
    dropTable(TrackPointsColumns.TABLE_NAME);
    dropTable(TracksColumns.TABLE_NAME);
    createEmptyTable(TrackPointsColumns.TABLE_NAME);
    createEmptyTable(TracksColumns.TABLE_NAME);
    databaseHelper.onUpgrade(db, 18, 20);
    assertFalse(checkColumn(TrackPointsColumns.TABLE_NAME, TrackPointsColumns.SENSOR));
    assertTrue(checkColumn(TracksColumns.TABLE_NAME, TracksColumns.TABLEID));
    assertTrue(checkColumn(TracksColumns.TABLE_NAME, TracksColumns.ICON));
  }
  
  /**
   * Tests the method
   * {@link MyTracksProvider.DatabaseHelper#onUpgrade(SQLiteDatabase, int, int)}
   * when version is 19.
   */
  public void testDatabaseHelper_onUpgrade_Version19() {
    DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
    
    // Make two table is only contains one normal integer column.
    dropTable(TrackPointsColumns.TABLE_NAME);
    dropTable(TracksColumns.TABLE_NAME);
    createEmptyTable(TrackPointsColumns.TABLE_NAME);
    createEmptyTable(TracksColumns.TABLE_NAME);
    databaseHelper.onUpgrade(db, 19, 20);
    assertFalse(checkColumn(TrackPointsColumns.TABLE_NAME, TrackPointsColumns.SENSOR));
    assertFalse(checkColumn(TracksColumns.TABLE_NAME, TracksColumns.TABLEID));
    assertTrue(checkColumn(TracksColumns.TABLE_NAME, TracksColumns.ICON));
  }

  /**
   * Tests the method {@link MyTracksProvider#onCreate()}.
   */
  public void testOnCreate() {
    assertTrue(myTracksProvider.onCreate(getContext()));
  }

  /**
   * Recreates the database of MyTracks.
   */
  private void recreatedDatabase() {
    assertTrue(getContext().deleteDatabase(MyTracksProvider.DATABASE_NAME));
    db = (new DatabaseHelper(getContext())).getWritableDatabase();
  }

  /**
   * Creates an table only contains one column.
   * 
   * @param table the name of table
   */
  private void createEmptyTable(String table) {
    db.execSQL( "CREATE TABLE " + table + " (test INTEGER)");
  }
  
  /**
   * Drops a table in database.
   * @param table
   */
  private void dropTable(String table) {
    db.execSQL("Drop TABLE " + table);
  }
  
  /**
   * Checks whether a table is existed.
   * 
   * @param table the name of table
   * @return true means the table has existed
   */
  private boolean checkTable(String table) {
    try {
      db.rawQuery("select count(*) from " + table, null);
      return true;
    } catch (Exception e) {
     return false;
    }
    
  }
  
  /**
   * Checks whether a column in a table is existed.
   * 
   * @param table the name of table
   * @param column the name of column
   * @return true means the column has existed
   */
  private boolean checkColumn(String table, String column) {
    try {
      db.execSQL("ALTER TABLE " + table + " add  " + column);
    } catch (Exception e) {
      if(e.getMessage().indexOf("duplicate column") > -1) {
        return true;
      }
    }
    return false;
  }

}
