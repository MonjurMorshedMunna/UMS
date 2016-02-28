package org.ums.academic.dao;

import org.ums.domain.model.mutable.MutableNavigation;
import org.ums.domain.model.readOnly.Navigation;
import org.ums.manager.NavigationManager;

import java.util.List;
import java.util.Set;

public class NavigationDaoDecorator extends ContentDaoDecorator<Navigation, MutableNavigation, Integer, NavigationManager> implements NavigationManager {
  @Override
  public List<Navigation> getByPermissions(Set<String> pPermissions) {
    return getManager().getByPermissions(pPermissions);
  }
}
