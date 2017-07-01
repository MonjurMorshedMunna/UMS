package org.ums.resource.holidays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ums.builder.Builder;
import org.ums.cache.LocalCache;
import org.ums.domain.model.immutable.common.Holidays;
import org.ums.domain.model.mutable.common.MutableHolidays;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Created by Monjur-E-Morshed on 17-Jun-17.
 */
@Component
public class HolidaysBuilder implements Builder<Holidays, MutableHolidays> {

  private static final Logger mLogger = LoggerFactory.getLogger(HolidaysBuilder.class);

  @Autowired
  DateFormat mDateFormat;

  @Override
  public void build(JsonObjectBuilder pBuilder, Holidays pReadOnly, UriInfo pUriInfo, LocalCache pLocalCache) {
    pBuilder.add("id", pReadOnly.getId().toString());
    pBuilder.add("holidayTypeId", pReadOnly.getHolidayType().getId());
    pBuilder.add("holidayTypeName", pReadOnly.getHolidayType().getHolidayName());
    pBuilder.add("year", pReadOnly.getYear());
    Format formatter = new SimpleDateFormat("dd-MM-YYYY");
    pBuilder.add("fromDate", formatter.format(pReadOnly.getFromDate()));
    pBuilder.add("toDate", formatter.format(pReadOnly.getToDate()));
  }

  @Override
  public void build(MutableHolidays pMutable, JsonObject pJsonObject, LocalCache pLocalCache) {
    pMutable.setId(Long.valueOf(pJsonObject.getString("id")));
    pMutable.setHolidayTypeId(Long.valueOf(pJsonObject.getString("holidayTypeId")));
    pMutable.setYear(pJsonObject.getInt("year"));
    try {
      pMutable.setFromDate(mDateFormat.parse(pJsonObject.getString("fromDate")));
      pMutable.setToDate(mDateFormat.parse(pJsonObject.getString("toDate")));
    } catch(Exception e) {
      mLogger.error(e.getMessage());
    }

  }
}
