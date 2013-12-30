package com.damon.database;





import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	/** ���ݿ����� */
	private final static String NAME = "player.db";
	private final static int DATABASE_VERSION = 1;
	private final static String SQL_CREATE_FILES = "CREATE TABLE \"files\" (" + //
	    "id"+ " INTEGER PRIMARY KEY AUTOINCREMENT," + //
	    "path" + " TEXT," + //   
	    "position" + " INTEGER," + //
	    "status" + " INTEGER" + //
	    
	    ")";

	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public SQLiteHelper(Context context) {

		super(context, NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_FILES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	

	/**
	 * �������ݿ�
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean update(String table, ContentValues values, String whereClause, String... whereArgs) {
		filterWhereArgs(whereArgs);
		SQLiteDatabase db = getWritableDatabase();
		final int affectedRows = db.update(table, values, whereClause, whereArgs);
		closeDb(db, null);
		return affectedRows > 0;
	}

	/**
	 * �������ݿ�
	 * 
	 * @param table
	 * @param values
	 * @return
	 */
	public long insert(String table, ContentValues values) {
		long result = 0L;
		SQLiteDatabase db = getWritableDatabase();
		result = db.insert(table, null, values);
		closeDb(db, null);
		return result;
	}

	/**
	 * ��������
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean insertOrUpdate(String table, ContentValues values, String whereClause, String... whereArgs) {
		filterWhereArgs(whereArgs);
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(table, null, whereClause, whereArgs, null, null, null);
		int count = c.getCount();
		closeDb(db, c);
		if (count > 0) {
			return update(table, values, whereClause, whereArgs);
		} else {
			return insert(table, values) > 0L;
		}
	}

	/**
	 * ɾ�����ݿ�
	 * 
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean delete(String table, String whereClause, String... whereArgs) {
		boolean result = true;
		filterWhereArgs(whereArgs);
		SQLiteDatabase db = getWritableDatabase();
		try {
			result = db.delete(table, whereClause, whereArgs) > 0;
		} catch (SQLException e) {
			result = false;
		} catch (Exception e) {
			result = false;
		} finally {
			closeDb(db, null);
		}
		return result;
	}

	

	public boolean exsit(String sql, String... whereArgs) {
		boolean result = false;
		filterWhereArgs(whereArgs);
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery(sql, whereArgs);
		if (c.moveToNext()) {
			result = true;
		}
		closeDb(db, c);
		return result;
	}

	/**
	 * ����whereArgs��Ϊnull��������
	 * 
	 * @param whereArgs
	 */
	private static void filterWhereArgs(String... whereArgs) {
		if (whereArgs != null && whereArgs.length > 0) {
			for (int i = 0, j = whereArgs.length; i < j; i++) {
				if (whereArgs[i] == null) {
					whereArgs[i] = "";
				}
			}
		}
	}

	/**
	 * �ͷ����ݿ���Դ
	 **/
	public void closeDb(SQLiteDatabase db, Cursor c) {
		if (c != null) {
			c.close();
		}
		if (db != null) {
			db.close();
		}
		close();
	}
}
