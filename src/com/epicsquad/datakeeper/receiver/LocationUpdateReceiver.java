package com.epicsquad.datakeeper.receiver;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.epicsquad.datakeeper.common.Utils;
import com.epicsquad.datakeeper.service.LocationService;
import com.epicsquad.datakeeper.service.impl.LocationServiceImpl;

public class LocationUpdateReceiver implements LocationListener {

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private Location oldLocation;

    private LocationService locationService = new LocationServiceImpl();

    @Override
    public void onLocationChanged(Location location) {
        if (location != null){
            if (location.getProvider().equals("gps")){
                Utils.log("GPS location received");
            }
        }
        if(isBetterLocation(location, oldLocation)){
            oldLocation = location;
            locationService.save(convert(oldLocation));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    private static com.epicsquad.datakeeper.model.domain.Location convert(Location location){
        com.epicsquad.datakeeper.model.domain.Location converted = new com.epicsquad.datakeeper.model.domain.Location();
        converted.setDate(location.getTime());
        converted.setLatitude(location.getLatitude());
        converted.setLongitude(location.getLongitude());
        converted.setProvider(location.getProvider());
        converted.setAccuracy(location.getAccuracy());
        return converted;
    }

    private static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (location == null){
            return false;
        }

        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
