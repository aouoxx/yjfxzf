/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.supconit.kqfx.web.fxzf.avro;
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class redis_BoardInfo extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"redis_BoardInfo\",\"namespace\":\"com.supconit.kqfx.web.fxzf.avro\",\"fields\":[{\"name\":\"sQbbName\",\"type\":\"string\"},{\"name\":\"sQbbItems\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"qbbItemInfo\",\"fields\":[{\"name\":\"sCnt\",\"type\":\"string\"},{\"name\":\"sWay\",\"type\":\"string\"},{\"name\":\"sFont\",\"type\":\"string\"},{\"name\":\"sDelay\",\"type\":\"string\"},{\"name\":\"sSpeed\",\"type\":\"string\"},{\"name\":\"sColor\",\"type\":\"string\"},{\"name\":\"sDataType\",\"type\":\"string\"}]}}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public CharSequence sQbbName;
  @Deprecated public java.util.List<qbbItemInfo> sQbbItems;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public redis_BoardInfo() {}

  /**
   * All-args constructor.
   */
  public redis_BoardInfo(CharSequence sQbbName, java.util.List<qbbItemInfo> sQbbItems) {
    this.sQbbName = sQbbName;
    this.sQbbItems = sQbbItems;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public Object get(int field$) {
    switch (field$) {
    case 0: return sQbbName;
    case 1: return sQbbItems;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: sQbbName = (CharSequence)value$; break;
    case 1: sQbbItems = (java.util.List<qbbItemInfo>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'sQbbName' field.
   */
  public CharSequence getSQbbName() {
    return sQbbName;
  }

  /**
   * Sets the value of the 'sQbbName' field.
   * @param value the value to set.
   */
  public void setSQbbName(CharSequence value) {
    this.sQbbName = value;
  }

  /**
   * Gets the value of the 'sQbbItems' field.
   */
  public java.util.List<qbbItemInfo> getSQbbItems() {
    return sQbbItems;
  }

  /**
   * Sets the value of the 'sQbbItems' field.
   * @param value the value to set.
   */
  public void setSQbbItems(java.util.List<qbbItemInfo> value) {
    this.sQbbItems = value;
  }

  /** Creates a new redis_BoardInfo RecordBuilder */
  public static Builder newBuilder() {
    return new Builder();
  }
  
  /** Creates a new redis_BoardInfo RecordBuilder by copying an existing Builder */
  public static Builder newBuilder(Builder other) {
    return new Builder(other);
  }
  
  /** Creates a new redis_BoardInfo RecordBuilder by copying an existing redis_BoardInfo instance */
  public static Builder newBuilder(redis_BoardInfo other) {
    return new Builder(other);
  }
  
  /**
   * RecordBuilder for redis_BoardInfo instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<redis_BoardInfo>
    implements org.apache.avro.data.RecordBuilder<redis_BoardInfo> {

    private CharSequence sQbbName;
    private java.util.List<qbbItemInfo> sQbbItems;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.sQbbName)) {
        this.sQbbName = data().deepCopy(fields()[0].schema(), other.sQbbName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.sQbbItems)) {
        this.sQbbItems = data().deepCopy(fields()[1].schema(), other.sQbbItems);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing redis_BoardInfo instance */
    private Builder(redis_BoardInfo other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.sQbbName)) {
        this.sQbbName = data().deepCopy(fields()[0].schema(), other.sQbbName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.sQbbItems)) {
        this.sQbbItems = data().deepCopy(fields()[1].schema(), other.sQbbItems);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'sQbbName' field */
    public CharSequence getSQbbName() {
      return sQbbName;
    }
    
    /** Sets the value of the 'sQbbName' field */
    public Builder setSQbbName(CharSequence value) {
      validate(fields()[0], value);
      this.sQbbName = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'sQbbName' field has been set */
    public boolean hasSQbbName() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'sQbbName' field */
    public Builder clearSQbbName() {
      sQbbName = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'sQbbItems' field */
    public java.util.List<qbbItemInfo> getSQbbItems() {
      return sQbbItems;
    }
    
    /** Sets the value of the 'sQbbItems' field */
    public Builder setSQbbItems(java.util.List<qbbItemInfo> value) {
      validate(fields()[1], value);
      this.sQbbItems = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'sQbbItems' field has been set */
    public boolean hasSQbbItems() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'sQbbItems' field */
    public Builder clearSQbbItems() {
      sQbbItems = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public redis_BoardInfo build() {
      try {
        redis_BoardInfo record = new redis_BoardInfo();
        record.sQbbName = fieldSetFlags()[0] ? this.sQbbName : (CharSequence) defaultValue(fields()[0]);
        record.sQbbItems = fieldSetFlags()[1] ? this.sQbbItems : (java.util.List<qbbItemInfo>) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
