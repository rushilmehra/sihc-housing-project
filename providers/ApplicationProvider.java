package com.scu.housing.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


public class ApplicationProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "com.scu.housing";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/applications");
    private static final int HOUSES = 1;
    private static final int HOUSE_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "applications", HOUSES);
        uriMatcher.addURI(PROVIDER_NAME, "applications/#", HOUSE_ID);
        return uriMatcher;
    }

    private ApplicationDatabase imageDataBase = null;

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case HOUSES:
                return "vnd.android.cursor.dir/vnd.com.scu.housing.provider.applications";
            case HOUSE_ID:
                return "vnd.android.cursor.item/vnd.com.scu.housing.provider.applications";

        }
        return "";
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        imageDataBase = new ApplicationDatabase(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;
        if(uriMatcher.match(uri) == HOUSE_ID) {
            //Query is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }
        return imageDataBase.getImages(id, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = imageDataBase.addNewImage(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == HOUSE_ID) {
            //Delete is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return imageDataBase.deleteImages(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == HOUSE_ID) {
            //Update is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return imageDataBase.updateImages(id, values);
    }
}