package com.podio.sdk.client.sqlite;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.podio.sdk.client.QueuedRestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.RestResult;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.parser.CursorParser;
import com.podio.sdk.parser.ItemParser;

public final class SQLiteRestClient extends QueuedRestClient {
	private static final String DATABASE_NAME = "podio.db";
	private static final int DATABASE_VERSION = 1;

	private final DatabaseHelper databaseHelper;

	private CursorParser resultParser;
	private ItemParser contentParser;

	public SQLiteRestClient(Context context, String authority) {
		this(context, authority, 10);
	}

	public SQLiteRestClient(Context context, String authority, int queueCapacity) {
		super("content", authority, queueCapacity);
		databaseHelper = new DatabaseHelper(context, DATABASE_NAME, DATABASE_VERSION);
		resultParser = new CursorParser();
		contentParser = new ItemParser();
	}

	@Override
	protected RestResult handleRequest(RestRequest restRequest) {
		RestResult result = null;

		if (restRequest != null) {
			String path = restRequest.getPath();
			String query = buildQueryString(restRequest.getQueryParameters());
			Uri uri = buildUri(path, query);

			RestOperation operation = restRequest.getOperation();
			Class<?> itemType = restRequest.getItemType();
			Object item = restRequest.getContent();

			Cursor cursor = queryDatabase(operation, uri, item, itemType);
			List<?> items = parseCursor(resultParser, cursor, itemType);

			if (items != null) {
				result = new RestResult(true, null, items);
			} else {
				result = new RestResult(false, "Ohno", null);
			}
		}

		return result;
	}

	public void setContentParser(ItemParser parser) {
		if (parser != null) {
			this.contentParser = parser;
		}
	}

	public void setResultParser(CursorParser parser) {
		if (parser != null) {
			this.resultParser = parser;
		}
	}

	private ContentValues buildContentValues(Object item, Class<?> classOfItem) {
		List<ContentValues> values = contentParser.parse(item, classOfItem);
		ContentValues result = values != null && values.size() > 0 ? values.get(0) : null;

		return result;
	}

	private String buildQueryString(Map<String, List<String>> query) {
		String result = "";

		if (query != null && query.size() > 0) {
			StringBuilder stringBuilder = new StringBuilder();
			Set<String> keys = query.keySet();

			for (String key : keys) {
				List<String> values = query.get(key);

				for (String value : values) {
					stringBuilder.append("&").append(key).append("=").append(value);
				}
			}

			result = stringBuilder.substring("&".length());
		}

		return result;
	}

	private Uri buildUri(String path, String query) {
		String authority = getAuthority();
		String scheme = getScheme();

		Uri uri = new Uri.Builder().scheme(scheme).authority(authority).path(path).query(query)
				.build();

		return uri;
	}

	private Cursor queryDatabase(RestOperation operation, Uri uri, Object content, Class<?> itemType) {
		switch (operation)
			{
			case GET: {
				return databaseHelper.query(uri);
			}
			case POST: {
				ContentValues values = buildContentValues(content, itemType);
				return databaseHelper.insert(uri, values);
			}
			case DELETE: {
				return databaseHelper.delete(uri);
			}
			case PUT: {
				ContentValues values = buildContentValues(content, itemType);
				return databaseHelper.update(uri, values);
			}
			default:
				return null;
			}
	}

	private List<?> parseCursor(CursorParser parser, Cursor cursor, Class<?> itemType) {
		List<?> result = null;

		if (parser != null && cursor != null && itemType != null) {
			result = parser.parse(cursor, itemType);
		}

		return result;
	}
}
