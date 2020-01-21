package com.inn.foresight.core.infra.wrapper;

import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.commons.maps.geometry.gis.GISPoint;

/**
 * The Class Geography.
 */
public class Geography extends LatLng {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The name. */
    protected String name;

    /** The display name. */
    protected String displayName;

    /** The l1 pk. */
    protected Integer l1Pk;

    /** The l2 pk. */
    protected Integer l2Pk;

    /** The l3 pk. */
    protected Integer l3Pk;

    /** The l4 pk. */
    protected Integer l4Pk;

    /** The boundary. */
    protected GISGeometry boundary;

    /** The min lat. */
    protected Double minLat;

    /** The min lng. */
    protected Double minLng;

    /** The max lat. */
    protected Double maxLat;

    /** The max lng. */
    protected Double maxLng;

    /**
     * Contains.
     *
     * @param location the location
     * @return true, if successful
     */
    public boolean contains(LatLng location) {
        return boundary.contains(new GISPoint(location));
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the boundary.
     *
     * @return the boundary
     */
    public GISGeometry getBoundary() {
        return boundary;
    }

    /**
     * Sets the boundary.
     *
     * @param boundary the new boundary
     */
    public void setBoundary(GISGeometry boundary) {
        this.boundary = boundary;
    }

    /**
     * Gets the min lat.
     *
     * @return the min lat
     */
    public Double getMinLat() {
        return minLat;
    }

    /**
     * Sets the min lat.
     *
     * @param minLat the new min lat
     */
    public void setMinLat(Double minLat) {
        this.minLat = minLat;
    }

    /**
     * Gets the min lng.
     *
     * @return the min lng
     */
    public Double getMinLng() {
        return minLng;
    }

    /**
     * Sets the min lng.
     *
     * @param minLng the new min lng
     */
    public void setMinLng(Double minLng) {
        this.minLng = minLng;
    }

    /**
     * Gets the max lat.
     *
     * @return the max lat
     */
    public Double getMaxLat() {
        return maxLat;
    }

    /**
     * Sets the max lat.
     *
     * @param maxLat the new max lat
     */
    public void setMaxLat(Double maxLat) {
        this.maxLat = maxLat;
    }

    /**
     * Gets the max lng.
     *
     * @return the max lng
     */
    public Double getMaxLng() {
        return maxLng;
    }

    /**
     * Sets the max lng.
     *
     * @param maxLng the new max lng
     */
    public void setMaxLng(Double maxLng) {
        this.maxLng = maxLng;
    }

    public String getDisplayName() {
      return displayName;
    }

    public void setDisplayName(String displayName) {
      this.displayName = displayName;
    }

    public Integer getL1Pk() {
      return l1Pk;
    }

    public void setL1Pk(Integer l1Pk) {
      this.l1Pk = l1Pk;
    }

    public Integer getL2Pk() {
      return l2Pk;
    }

    public void setL2Pk(Integer l2Pk) {
      this.l2Pk = l2Pk;
    }

    public Integer getL3Pk() {
      return l3Pk;
    }

    public void setL3Pk(Integer l3Pk) {
      this.l3Pk = l3Pk;
    }

    public Integer getL4Pk() {
      return l4Pk;
    }

    public void setL4Pk(Integer l4Pk) {
      this.l4Pk = l4Pk;
    }
   
    
    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Geography [");
        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }
        if (boundary != null) {
            builder.append("boundary=");
            builder.append(boundary);
            builder.append(", ");
        }
        if (minLat != null) {
            builder.append("minLat=");
            builder.append(minLat);
            builder.append(", ");
        }
        if (minLng != null) {
            builder.append("minLng=");
            builder.append(minLng);
            builder.append(", ");
        }
        if (maxLat != null) {
            builder.append("maxLat=");
            builder.append(maxLat);
            builder.append(", ");
        }
        if (maxLng != null) {
            builder.append("maxLng=");
            builder.append(maxLng);
            builder.append(", ");
        }
        if (displayName != null) {
          builder.append("displayName=");
          builder.append(displayName);
          builder.append(", ");
        }
        if (l1Pk != null) {
          builder.append("l1Pk=");
          builder.append(l1Pk);
          builder.append(", ");
        }
        if (l2Pk != null) {
          builder.append("l2Pk=");
          builder.append(l2Pk);
          builder.append(", ");
        }
        if (l3Pk != null) {
          builder.append("l3Pk=");
          builder.append(l3Pk);
          builder.append(", ");
        }
        if (l4Pk != null) {
          builder.append("l4Pk=");
          builder.append(l4Pk);
        }
        builder.append("]");
        return builder.toString();
    }
}