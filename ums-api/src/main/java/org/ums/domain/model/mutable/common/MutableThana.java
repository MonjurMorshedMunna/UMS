package org.ums.domain.model.mutable.common;

import org.ums.domain.model.common.Editable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.common.Thana;
import org.ums.domain.model.mutable.MutableLastModifier;

public interface MutableThana extends Thana, Editable<String>, MutableLastModifier, MutableIdentifier<String> {

  void setThanaId(final String pThanaId);

  void setDistrictId(final String pDistrictId);

  void setThanaName(final String pThanaName);
}
