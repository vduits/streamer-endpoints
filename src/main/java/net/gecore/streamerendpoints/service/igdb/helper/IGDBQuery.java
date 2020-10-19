package net.gecore.streamerendpoints.service.igdb.helper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.gecore.streamerendpoints.service.igdb.constants.IGDBField;

public class IGDBQuery {

  private final List<IGDBField> fields = new LinkedList<>();
  private final List<IGDBWhere> where = new LinkedList<>();


  /**
   * Allows you to add fields that will be returned after the query has been performed;
   *
   * @param fields IGDBField
   * @return IGDBQuery
   */

  public IGDBQuery fields(IGDBField ...fields){
    this.fields.addAll(Arrays.asList(fields));
    return this;
  }

  /**
   * Creates a where clause using known IGDBFields and the desired value;
   *
   * @param field IGDBField field to filter
   * @param value String desired value the filter has
   * @return IGDBQuery
   */
  public IGDBQuery where(IGDBField field, Object value){
    this.where.add(new IGDBWhere(field, value));
    return this;
  }

  /**
   * Doesn't actually do anything but is added for semantic readability.
   * @return IGDBQuery
   */
  public IGDBQuery and(){
    return this;
  }

  private String buildFields(){
    StringBuilder builder = new StringBuilder();
    builder.append("fields ");
    int count = 0;
    for(IGDBField field: this.fields){
      if(count >= 1){
        builder.append(",");
      }
      builder.append(field);
      count++;
    }
    builder.append(";");
    return builder.toString();
  }

  private String buildWhere(){
    StringBuilder builder = new StringBuilder();
    builder.append(" where ");
    int count = 0;
    for(IGDBWhere where: this.where){
      if(count >= 1){
        builder.append(" & ");
      }
      builder.append(where.field);
      builder.append(" = ");
      if(where.value instanceof String){
        builder.append("\"");
        builder.append(where.value);
        builder.append("\"");
      }else{
        builder.append(where.value);
      }

      count++;
    }
    builder.append(";");
    return builder.toString();
  }

  public String build(){
    StringBuilder builder = new StringBuilder();
    builder.append(buildFields());
    builder.append(buildWhere());

    return builder.toString();
  }

  private static class IGDBWhere{
    IGDBField field;
    Object value;

    public IGDBWhere(IGDBField field, Object value) {
      this.field = field;
      this.value = value;
    }
  }


}
