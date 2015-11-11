package com.epicsquad.datakeeper.model.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.epicsquad.datakeeper.common.CommonExceptionHandler;
import com.epicsquad.datakeeper.model.ContactDao;
import com.epicsquad.datakeeper.model.domain.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {

    private final Context context;

    public ContactDaoImpl(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(new CommonExceptionHandler());
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> result = new ArrayList<Contact>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] fieldsToSelect = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts._ID};

        Cursor people = context.getContentResolver().query(uri, fieldsToSelect, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        while (people.moveToNext()){
            String foundName = people.getString(indexName);
            List<String> numbers = findNumbersByName(foundName);
            Contact contact = new Contact();
            contact.setName(foundName);
            contact.setNumber(numbers);
            result.add(contact);
        }
        people.close();
        return result;
    }

    private List<String> findNumbersByName(String contactName){
        List<String> result = new ArrayList<String>();
        ContentResolver cr = context.getContentResolver();
        Cursor contact = cr.query( ContactsContract.Contacts.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = '" + contactName + "'", null, null);
        if (contact.moveToFirst()) {
            String contactId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {
                String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                result.add(number);
            }
            phones.close();
        }
        contact.close();
        return result;
    }

}
