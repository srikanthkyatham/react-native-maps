package com.airbnb.android.react.maps;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

public class AirMapOverlay extends AirMapFeature implements ImageReadable {

  private GroundOverlayOptions groundOverlayOptions;
  private GroundOverlay groundOverlay;
  private LatLngBounds bounds;
  private BitmapDescriptor iconBitmapDescriptor;
  private Bitmap iconBitmap;
  private float zIndex;
  private float transparency;
  private float bearing;

  private final ImageReader mImageReader;
  private GoogleMap map;

  public AirMapOverlay(Context context) {
    super(context);
    this.mImageReader = new ImageReader(context, getResources(), this);
    bearing = 0;
  }

  public void setBounds(ReadableArray bounds) {
    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    LatLng sw = new LatLng(bounds.getArray(1).getDouble(0), bounds.getArray(0).getDouble(1));
    LatLng ne = new LatLng(bounds.getArray(0).getDouble(0), bounds.getArray(1).getDouble(1));
    builder.include(sw);
    builder.include(ne);
    this.bounds = builder.build();
    if (this.groundOverlay != null) {
      this.groundOverlay.setPositionFromBounds(this.bounds);
    }
  }

  public void setZIndex(float zIndex) {
    this.zIndex = zIndex;
    if (this.groundOverlay != null) {
      this.groundOverlay.setZIndex(zIndex);
    }
  }

  // public void setTransparency(float transparency) {
  //     this.transparency = transparency;
  //     if (groundOverlay != null) {
  //         groundOverlay.setTransparency(transparency);
  //     }
  // }

  public void setBearing(float bearing) {
    if (this.bearing != bearing) {
      this.bearing = bearing;
      if (groundOverlay != null) {
        groundOverlay.setBearing(bearing);
      }
    }
  }

  public void setImage(String uri) {
    this.mImageReader.setImage(uri);
  }


  public GroundOverlayOptions getGroundOverlayOptions() {
    if (this.groundOverlayOptions == null) {
      this.groundOverlayOptions = createGroundOverlayOptions();
    }
    return this.groundOverlayOptions;
  }

  private GroundOverlayOptions createGroundOverlayOptions() {
    if (this.groundOverlayOptions != null) {
      return this.groundOverlayOptions;
    }
    if (this.iconBitmapDescriptor != null) {
      GroundOverlayOptions options = new GroundOverlayOptions()
          .anchor(0.0f, 0.0f)
          .image(iconBitmapDescriptor)
          .positionFromBounds(bounds)
          .zIndex(zIndex)
          .bearing(bearing);
      return options;
    }
    return null;
  }

  @Override
  public Object getFeature() {
    return groundOverlay;
  }

  @Override
  public void addToMap(GoogleMap map) {
    GroundOverlayOptions groundOverlayOptions = getGroundOverlayOptions();
    if (groundOverlayOptions != null) {
      this.groundOverlay = map.addGroundOverlay(groundOverlayOptions);
      this.groundOverlay.setClickable(true);
    } else {
      this.map = map;
    }
  }

  @Override
  public void removeFromMap(GoogleMap map) {
    if (this.groundOverlay != null) {
      this.groundOverlay.remove();
      this.groundOverlay = null;
      this.groundOverlayOptions = null;
    }
  }

  @Override
  public void setIconBitmap(Bitmap bitmap) {
    this.iconBitmap = bitmap;
  }

  @Override
  public void setIconBitmapDescriptor(
      BitmapDescriptor iconBitmapDescriptor) {
    this.iconBitmapDescriptor = iconBitmapDescriptor;
  }

  @Override
  public void update() {
    this.groundOverlay = getGroundOverlay();
    if (this.groundOverlay != null) {
      this.groundOverlay.setImage(this.iconBitmapDescriptor);
      this.groundOverlay.setBearing(this.bearing);
      this.groundOverlay.setClickable(true);
    }
  }

  private GroundOverlay getGroundOverlay() {
    if (this.groundOverlay != null) {
      return this.groundOverlay;
    }
    if (this.map == null) {
      return null;
    }
    GroundOverlayOptions groundOverlayOptions = getGroundOverlayOptions();
    if (groundOverlayOptions != null) {
      return this.map.addGroundOverlay(groundOverlayOptions);
    }
    return null;
  }
}
