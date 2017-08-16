package org.ums.domain.model.immutable.meeting;

import org.ums.domain.model.common.EditType;
import org.ums.domain.model.common.Identifier;
import org.ums.domain.model.common.LastModifier;
import org.ums.domain.model.mutable.meeting.MutableSchedule;
import org.ums.enums.meeting.MeetingType;

import java.io.Serializable;
import java.sql.Timestamp;

public interface Schedule extends Serializable, EditType<MutableSchedule>, Identifier<Long>, LastModifier {

  MeetingType getMeetingType();

  int getMeetingTypeId();

  int getMeetingNo();

  String getMeetingRefNo();

  Timestamp getMeetingDateTime();

  String getMeetingRoomNo();

}
