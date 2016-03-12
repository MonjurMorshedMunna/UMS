package org.ums.decorator;

import org.ums.cache.ContentDaoDecorator;
import org.ums.domain.model.mutable.MutableClassRoom;
import org.ums.domain.model.immutable.ClassRoom;
import org.ums.manager.ClassRoomManager;

import java.util.List;

/**
 * Created by Ifti on 13-Feb-16.
 */
public class ClassRoomDaoDecorator extends ContentDaoDecorator<ClassRoom, MutableClassRoom, Integer, ClassRoomManager> implements ClassRoomManager {
  @Override
  public List<ClassRoom> getRoomList() throws Exception {
    return getManager().getRoomList();
  }
}