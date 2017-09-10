package org.ums.employee.publication;

import org.ums.decorator.ContentDaoDecorator;
import org.ums.employee.publication.PublicationInformation;
import org.ums.employee.publication.MutablePublicationInformation;
import org.ums.manager.registrar.PublicationInformationManager;

import java.util.List;

public class PublicationInformationDaoDecorator extends
    ContentDaoDecorator<PublicationInformation, MutablePublicationInformation, Long, PublicationInformationManager>
    implements PublicationInformationManager {

  @Override
  public int savePublicationInformation(List<MutablePublicationInformation> pMutablePublicationInformation) {
    return getManager().savePublicationInformation(pMutablePublicationInformation);
  }

  @Override
  public List<PublicationInformation> getEmployeePublicationInformation(String pEmployeeId) {
    return getManager().getEmployeePublicationInformation(pEmployeeId);
  }

  @Override
  public List<PublicationInformation> getPublicationInformationWithPagination(String pEmployeeId,
      String pPublicationStatus, int pPageNumber, int pItemPerPage) {
    return getManager().getPublicationInformationWithPagination(pEmployeeId, pPublicationStatus, pPageNumber,
        pItemPerPage);
  }

  @Override
  public List<PublicationInformation> getPublicationInformationWithPagination(String pEmployeeId, int pPageNumber,
      int pItemPerPage) {
    return getManager().getPublicationInformationWithPagination(pEmployeeId, pPageNumber, pItemPerPage);
  }

  @Override
  public List<PublicationInformation> getEmployeePublicationInformation(String pEmployeeId, String pStatus) {
    return getManager().getEmployeePublicationInformation(pEmployeeId, pStatus);
  }

  @Override
  public List<PublicationInformation> getPublicationInformation(String pPublicationStatus) {
    return getManager().getPublicationInformation(pPublicationStatus);
  }

  @Override
  public int updatePublicationStatus(MutablePublicationInformation pMutablePublicationInformation) {
    return getManager().updatePublicationStatus(pMutablePublicationInformation);
  }

  @Override
  public int deletePublicationInformation(String pEmployeeId) {
    return getManager().deletePublicationInformation(pEmployeeId);
  }

  @Override
  public int deletePublicationInformation(List<MutablePublicationInformation> pMutablePublicationInformation) {
    return getManager().deletePublicationInformation(pMutablePublicationInformation);
  }

  @Override
  public int updatePublicationInformation(List<MutablePublicationInformation> pMutablePublicationInformation) {
    return getManager().updatePublicationInformation(pMutablePublicationInformation);
  }

  @Override
  public int getLengthOfPublicationList(String pEmployeeId, String pPublicationStatus) {
    return getManager().getLengthOfPublicationList(pEmployeeId, pPublicationStatus);
  }
}
