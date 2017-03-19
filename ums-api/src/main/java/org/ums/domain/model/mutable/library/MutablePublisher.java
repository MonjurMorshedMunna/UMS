package org.ums.domain.model.mutable.library;

import org.ums.domain.model.common.Editable;
import org.ums.domain.model.common.MutableIdentifier;
import org.ums.domain.model.immutable.common.Country;
import org.ums.domain.model.immutable.library.Publisher;
import org.ums.domain.model.mutable.MutableLastModifier;

/**
 * Created by Ifti on 04-Feb-17.
 */
public interface MutablePublisher extends Publisher, Editable<Integer>, MutableLastModifier,
    MutableIdentifier<Integer> {

  void setName(final String pName);

  void setCountryId(final int pCountryId);

  void setCountry(final Country pCountry);

}
