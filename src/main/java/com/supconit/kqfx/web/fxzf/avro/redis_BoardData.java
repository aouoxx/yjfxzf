/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.supconit.kqfx.web.fxzf.avro;
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class redis_BoardData extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"redis_BoardData\",\"namespace\":\"com.supconit.kqfx.web.fxzf.avro\",\"fields\":[{\"name\":\"sQbbName\",\"type\":\"string\"},{\"name\":\"sQbb\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public CharSequence sQbbName;
  @Deprecated public CharSequence sQbb;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public redis_BoardData() {}

  /**
   * All-args constructor.
   */
  public redis_BoardData(CharSequence sQbbName, CharSequence sQbb) {
    this.sQbbName = sQbbName;
    this.sQbb = sQbb;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public Object get(int field$) {
    switch (field$) {
    case 0: return sQbbName;
    case 1: return sQbb;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: sQbbName = (CharSequence)value$; break;
    case 1: sQbb = (CharSequence)value$; break;
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
   * Gets the value of the 'sQbb' field.
   */
  public CharSequence getSQbb() {
    return sQbb;
  }

  /**
   * Sets the value of the 'sQbb' field.
   * @param value the value to set.
   */
  public void setSQbb(CharSequence value) {
    this.sQbb = value;
  }

  /** Creates a new redis_BoardData RecordBuilder */
  public static Builder newBuilder() {
    return new Builder();
  }
  
  /** Creates a new redis_BoardData RecordBuilder by copying an existing Builder */
  public static Builder newBuilder(Builder other) {
    return new Builder(other);
  }
  
  /** Creates a new redis_BoardData RecordBuilder by copying an existing redis_BoardData instance */
  public static Builder newBuilder(redis_BoardData other) {
    return new Builder(other);
  }
  
  /**
   * RecordBuilder for redis_BoardData instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<redis_BoardData>
    implements org.apache.avro.data.RecordBuilder<redis_BoardData> {

    private CharSequence sQbbName;
    private CharSequence sQbb;

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
      if (isValidValue(fields()[1], other.sQbb)) {
        this.sQbb = data().deepCopy(fields()[1].schema(), other.sQbb);
        fieldSetFlags()[1] = true;
      }
    }
    
    /** Creates a Builder by copying an existing redis_BoardData instance */
    private Builder(redis_BoardData other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.sQbbName)) {
        this.sQbbName = data().deepCopy(fields()[0].schema(), other.sQbbName);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.sQbb)) {
        this.sQbb = data().deepCopy(fields()[1].schema(), other.sQbb);
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

    /** Gets the value of the 'sQbb' field */
    public CharSequence getSQbb() {
      return sQbb;
    }
    
    /** Sets the value of the 'sQbb' field */
    public Builder setSQbb(CharSequence value) {
      validate(fields()[1], value);
      this.sQbb = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'sQbb' field has been set */
    public boolean hasSQbb() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'sQbb' field */
    public Builder clearSQbb() {
      sQbb = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public redis_BoardData build() {
      try {
        redis_BoardData record = new redis_BoardData();
        record.sQbbName = fieldSetFlags()[0] ? this.sQbbName : (CharSequence) defaultValue(fields()[0]);
        record.sQbb = fieldSetFlags()[1] ? this.sQbb : (CharSequence) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
