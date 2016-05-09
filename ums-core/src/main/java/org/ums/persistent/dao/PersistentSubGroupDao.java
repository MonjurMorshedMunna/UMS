package org.ums.persistent.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.ums.decorator.SubGroupDaoDecorator;
import org.ums.domain.model.immutable.SubGroup;
import org.ums.domain.model.mutable.MutableSubGroup;
import org.ums.persistent.model.PersistentSubGroup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by My Pc on 5/4/2016.
 */
public class PersistentSubGroupDao extends SubGroupDaoDecorator{

  String SELECT_ALL="SELECT ID,SEMESTER_ID,GROUP_NO,SUB_GROUP_NO,GROUP_ID,POSITION,STUDENT_NUMBER,EXAM_TYPE,LAST_MODIFIED FROM SP_SUB_GROUP";
  String UPDATE_ONE="UPDATE SP_SUB_GROUP SET SEMESTER_ID=?,GROUP_NO=?,SUB_GROUP_NO=?,GROUP_ID=?,POSITION=?,STUDENT_NUMBER=?,EXAM_TYPE=?,LAST_MODIFIED="+getLastModifiedSql() +" ";
  String DELETE_ONE = "DELETE FROM SP_SUB_GROUP ";
  String INSERT_ONE = " INSERT INTO SP_SUB_GROUP(SEMESTER_ID,GROUP_NO,SUB_GROUP_NO,GROUP_ID,POSITION,STUDENT_NUMBER,EXAM_TYPE,LAST_MODIFIED) "+
      " VALUES(?,?,?,?,?,?,"+getLastModifiedSql()+") ";


  String INSERT_ALL=" INSERT INTO SP_SUB_GROUP(SEMESTER_ID,GROUP_NO,SUB_GROUP_NO,GROUP_ID,POSITION,STUDENT_NUMBER,EXAM_TYPE,LAST_MODIFIED) "+
      " VALUES(?,?,?,?,?,?,?,"+getLastModifiedSql()+") ";
  String UPDATE_ALL="UPDATE SP_SUB_GROUP SET SEMESTER_ID=?,GROUP_NO=?,SUB_GROUP_NO=?,GROUP_ID=?,POSITION=?,STUDENT_NUMBER=?,EXAM_TYPE=?,LAST_MODIFIED="+getLastModifiedSql() +" ";
  String DELETE_ALL = "DELETE FROM SP_SUB_GROUP ";


  private JdbcTemplate mJdbcTemplate;

  public PersistentSubGroupDao(JdbcTemplate pJdbcTemplate){
    mJdbcTemplate = pJdbcTemplate;
  }

  @Override
  public List<SubGroup> getByGroupNo(int pGroupNo) {
    String query = SELECT_ALL+" WHERE GROUP_NO = ?";
    return mJdbcTemplate.query(query,new Object[]{pGroupNo},new SubGroupRowMapper());
  }

  @Override
  public List<SubGroup> getBySemesterGroupNoAndType(int pSemesterId, int pGroupNo, int pType) {
    String query = SELECT_ALL+" WHERE SEMESTER_ID=? AND GROUP_NO=? AND EXAM_TYPE=? ORDER BY SUB_GROUP_NO ASC, ID ASC ";
    return mJdbcTemplate.query(query,new Object[]{pSemesterId,pGroupNo,pType},new SubGroupRowMapper());
  }

  @Override
  public int deleteBySemesterAndGroup(int pSemesterId, int pGroupNo) {
    String query = DELETE_ALL+" WHERE SEMESTER_ID=? AND GROUP_NO=? ";
    return mJdbcTemplate.update(query,pSemesterId,pGroupNo);
  }

  @Override
  public int create(List<MutableSubGroup> pMutableList) throws Exception {
    return mJdbcTemplate.batchUpdate(INSERT_ALL,getInsertParamList(pMutableList)).length;
  }

  @Override
  public int getSubGroupNumberOfAGroup(int pSemesterId, int pExamType, int pGroupNo) {
    String query = "select count(distinct sub_group_no) from sp_sub_group where group_no=? and semester_id=? and exam_type=?";
    return mJdbcTemplate.queryForObject(query,Integer.class,pGroupNo,pSemesterId,pExamType);
  }

  @Override
  public List<SubGroup> getSubGroupMembers(int pSemesterId, int pExamTYpe, int pGroupNo, int pSubGroupNo) {
    String query = SELECT_ALL+" WHERE SEMESTER_ID=? AND EXAM_TYPE=? AND GROUP_NO=? AND SUB_GROUP_NO=? ORDER BY ID ASC";
    return mJdbcTemplate.query(query,new Object[]{pSemesterId,pExamTYpe,pGroupNo,pSubGroupNo},new SubGroupRowMapper());
  }

  @Override
  public int create(MutableSubGroup pMutable) throws Exception {
    return mJdbcTemplate.update(INSERT_ALL,
        pMutable.getSemester().getId(),
        pMutable.getGroup().getGroupNo(),
        pMutable.subGroupNo(),
        pMutable.getGroup().getId(),
        pMutable.getPosition(),
        pMutable.getStudentNumber(),
        pMutable.getExamType()
        );
  }

  @Override
  public int delete(MutableSubGroup pMutable) throws Exception {
    String query = DELETE_ONE+" WHERE ID=?";
    return mJdbcTemplate.update(query,pMutable.getId());
  }

  @Override
  public int update(MutableSubGroup pMutable) throws Exception {
    String query = UPDATE_ALL+" WHERE ID=?";
    return mJdbcTemplate.update(query,pMutable.getId());
  }

  @Override
  public SubGroup get(Integer pId) throws Exception {
    String query = SELECT_ALL+" WHERE ID=?";
    return mJdbcTemplate.queryForObject(query,new Object[]{pId},new SubGroupRowMapper());
  }

  @Override
  public List<SubGroup> getAll() throws Exception {
    String query = SELECT_ALL;
    return mJdbcTemplate.query(query,new SubGroupRowMapper());
  }



  private List<Object[]> getInsertParamList(List<MutableSubGroup> pSubGroups) throws Exception{
    List<Object[]> params = new ArrayList<>();

    for(SubGroup subGroup: pSubGroups){
      params.add(new Object[]{
          subGroup.getSemester().getId(),
          subGroup.getGroup().getGroupNo(),
          subGroup.subGroupNo(),
          subGroup.getGroup().getId(),
          subGroup.getPosition(),
          subGroup.getStudentNumber(),
          subGroup.getExamType()
      });
    }

    return params;

  }

  class SubGroupRowMapper implements RowMapper<SubGroup>{
    @Override
    public SubGroup mapRow(ResultSet pResultSet, int pI) throws SQLException {
      PersistentSubGroup mSubGroup = new PersistentSubGroup();
      mSubGroup.setId(pResultSet.getInt("ID"));
      mSubGroup.setSemesterId(pResultSet.getInt("SEMESTER_ID"));
      mSubGroup.setGroupNo(pResultSet.getInt("GROUP_NO"));
      mSubGroup.setSubGroupNo(pResultSet.getInt("SUB_GROUP_NO"));
      mSubGroup.setGroupId(pResultSet.getInt("GROUP_ID"));
      mSubGroup.setPosition(pResultSet.getInt("POSITION"));
      mSubGroup.setStudentNumber(pResultSet.getInt("STUDENT_NUMBER"));
      mSubGroup.setExamType(pResultSet.getInt("EXAM_TYPE"));
      mSubGroup.setLastModified(pResultSet.getString("LAST_MODIFIED"));
      return mSubGroup;
    }
  }
}
