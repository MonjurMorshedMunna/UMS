package org.ums.fee;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;

public interface FeeType extends Serializable, EditType<MutableFeeType>, LastModifier, Identifier<Integer> {

  String getName();

  String getDescription();

  enum Types {
    SEMESTER_FEE(1, "SEMESTER_FEE"),
    CERTIFICATE_FEE(2, "CERTIFICATE_FEE"),
    DUES(3, "DUES"),
    PENALTY(4, "PENALTY"),
    OTHERS(0, "OTHERS");

    private String label;
    private int id;

    Types(int id, String label) {
      this.id = id;
      this.label = label;
    }

    private static final Map<Integer, Types> lookup = new HashMap<>();

    static {
      for(Types c : EnumSet.allOf(Types.class)) {
        lookup.put(c.getId(), c);
      }
    }

    public static Types get(final int pId) {
      return lookup.get(pId);
    }

    public String getLabel() {
      return this.label;
    }

    public int getId() {
      return this.id;
    }

  }
}
