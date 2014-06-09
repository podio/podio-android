package com.podio.sdk.client.cache;

import com.podio.sdk.internal.utils.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteCacheClient implements CacheClient {

	private final SQLiteHelper helper;

	public SQLiteCacheClient(Context context, String name, int version) {
		this.helper = new SQLiteHelper(context, name, version);
	}

	@Override
	public byte[] load(String key) {
		if (Utils.isEmpty(key)) {
			throw new IllegalArgumentException("key cannot be empty");
		}
		
		String[] projection = { SQLiteHelper.DATA_COLUMN };
		String selection = SQLiteHelper.KEY_COLUMN + " = ?";
		String[] selectionArgs = { key };

		Cursor cursor = helper.getReadableDatabase().query(SQLiteHelper.TABLE,
				projection, selection, selectionArgs, null, null, null);
		try {
			if (cursor == null || !cursor.moveToFirst()) {
				// TODO: Perhaps raise exception instead?
				return null;
			}

			return cursor.getBlob(0);
		} finally {
			cursor.close();
		}
	}

	@Override
	public void save(String key, byte[] data) {
		if (Utils.isEmpty(key)) {
			throw new IllegalArgumentException("key cannot be empty");
		}
		
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.KEY_COLUMN, key);
		values.put(SQLiteHelper.DATA_COLUMN, data);

		helper.getWritableDatabase().insertWithOnConflict(SQLiteHelper.TABLE, null,
				values, SQLiteDatabase.CONFLICT_REPLACE);

	}

	@Override
	public boolean delete(String key) {
		if (Utils.isEmpty(key)) {
			throw new IllegalArgumentException("key cannot be empty");
		}
		
		String whereClause = SQLiteHelper.KEY_COLUMN + " = ?";
		String[] whereArgs = { key };

		int counts = helper.getWritableDatabase().delete(SQLiteHelper.TABLE, whereClause, whereArgs);
		
		return counts > 0;
	}
 
    public SQLiteDatabase getReadableDatabase() {
        return helper.getReadableDatabase();
    }
 
    public SQLiteDatabase getWritableDatabase() {
        return helper.getWritableDatabase();
    }

}
