package org.ums.domain.model.immutable;

import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.mutable.MutableNavigation;

import java.io.Serializable;

public interface Navigation extends Serializable, Identifier<Long>, LastModifier, EditType<MutableNavigation> {
  String getTitle();

  String getPermission();

  Navigation getParent();

  Long getParentId();

  Integer getViewOrder();

  String getLocation();

  String getIconImgClass();

  String getIconColorClass();

  boolean isActive();
}
