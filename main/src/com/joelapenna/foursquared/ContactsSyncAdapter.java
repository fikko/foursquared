package com.joelapenna.foursquared;

import com.joelapenna.foursquare.Foursquare;
import com.joelapenna.foursquare.error.FoursquareError;
import com.joelapenna.foursquare.error.FoursquareException;
import com.joelapenna.foursquare.types.Group;
import com.joelapenna.foursquare.types.User;
import com.joelapenna.foursquared.location.LocationUtils;
import com.joelapenna.foursquared.preferences.Preferences;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class ContactsSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "ContactsSyncAdapter";

    final private Foursquared mFoursquared;
    final private AccountManager mAccountManager;
    final private ContentResolver mContentResolver;
    
    public ContactsSyncAdapter(Foursquared foursquared, Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mFoursquared = foursquared;
        mAccountManager = AccountManager.get(context);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
            SyncResult syncResult) {
        String password = null;
        try {
            Log.i(TAG, "getting password from account manager");
            password = mAccountManager.blockingGetAuthToken(account, AuthenticatorService.ACCOUNT_TYPE, true);
            
        } catch (OperationCanceledException e) {
            Log.w(TAG, "operation cancelled while getting auth token", e);
        } catch (AuthenticatorException e) {
            Log.e(TAG, "authenticator exception while getting auth token", e);
        } catch (IOException e) {
            Log.e(TAG, "ioexception while getting auth token", e);
        }
        
        final Group<User> friends = new Group<User>();
        
        try {
            friends.addAll(mFoursquared.getFoursquare().friends(mFoursquared.getUserId(), LocationUtils.createFoursquareLocation(mFoursquared.getLastKnownLocation())));
        } catch (FoursquareError e) {
            Log.e(TAG, "error fetching friends", e);
        } catch (FoursquareException e) {
            Log.e(TAG, "exception fetching friends", e);
        } catch (IOException e) {
            Log.e(TAG, "ioexception fetching friends", e);
        }
        
        Log.i(TAG, "got " + friends.size() + " friends from server");
        
        for ( User friend : friends ) {
            Log.i(TAG, "adding friend " + friend.getId() + " (" + friend.getFirstname() + " " + friend.getLastname() + ")");
            addContact(account, friend);
        }
        
    }
    
    private void addContact(Account account, User friend) {
        ArrayList<ContentProviderOperation> opList = new ArrayList<ContentProviderOperation>();
 
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
        builder.withValue(RawContacts.ACCOUNT_NAME, account.name);
        builder.withValue(RawContacts.ACCOUNT_TYPE, account.type);
        builder.withValue(RawContacts.SYNC1, friend.getId());
        opList.add(builder.build());
        
        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, friend.getFirstname()+" "+friend.getLastname());
        opList.add(builder.build());
        
        // create a Data record with custom type to point at Foursquare profile
        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/com.joelapenna.foursquared.profile");
        builder.withValue(ContactsContract.Data.DATA1, friend.getId());
        builder.withValue(ContactsContract.Data.DATA2, "Foursquare Profile");
        builder.withValue(ContactsContract.Data.DATA3, "View profile");
        opList.add(builder.build());
        
        try {
            mContentResolver.applyBatch(ContactsContract.AUTHORITY, opList);
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong during creation! " + e);
            e.printStackTrace();
        }        
        
    }

}