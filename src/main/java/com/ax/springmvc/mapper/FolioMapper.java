package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ax.springmvc.pojo.Folio;

public interface FolioMapper {
	/**
	 * �õ�һ�����ڸ�Ŀ¼�����ɵ��ļ���
	 * 
	 * @param userId    �ĸ��û���
	 * @param parentDir ���ļ���Id
	 * @param isNotDel  �Ƿ�ɾ��
	 * @return
	 */
	List<Folio> selectByUser(@Param("userId") int userId, @Param("parentDir") int parentDir,
			@Param("isNotDel") int isNotDel,@Param("isNotShare") int isNotShare);

	/**
	 * ��id�õ�һ������
	 * 
	 * @param id
	 * @return
	 */
	@Select("SELECT * FROM `folio` WHERE id=#{id}")
	Folio selectOne(@Param("id") int id);

	/**
	 * �����ļ���
	 * 
	 * @param folio
	 * @return
	 */
	int createFolio(Folio folio);

	/**
	 * �޸��ļ���
	 * 
	 * @param folio
	 * @return
	 */
	@Update("UPDATE `skydrive`.`folio` SET `parentDir` = #{parentDir},`folioName` = #{folioName},`fileUpdateTime`=NOW(),`isNotDel`=#{isNotDel},`showName`=#{showName} WHERE `id` = #{id}")
	int updateFolio(Folio folio);

	/**
	 * ɾ���ļ���
	 * 
	 * @param id
	 * @return
	 */
	@Delete("delete from folio where id = #{id}")
	int deleteFolio(@Param("id") int id);

	/**
	 * �����޸��ļ���Ϣ
	 */
	int updateFolios(@Param("folios") List<Folio> folios);

	/**
	 * �������ļ���ɾ��������վ
	 */
	int updateFolisTodel(@Param("ids") List<Integer> ids, @Param("isNotDel") int isNotDel);

	/**
	 * ������ѯ�ļ�����Ϣ
	 * 
	 * @param ids
	 * @return
	 */
	List<Folio> selectByIds(@Param("ids") List<Integer> ids);

	/**
	 * ����ɾ���ļ���
	 * 
	 * @param ids
	 * @return
	 */
	int deleteFolios(@Param("ids") List<Integer> ids);

	/**
	 * �����û�Id�õ�һ��Ŀ¼�µ�,��rname�����ļ���������ͬ���ļ���
	 * 
	 * @return
	 */
	@Select("select * from folio where userId=#{userId} and parentDir=#{parentDir} and isNotDel=1 and isNotShare=1 and (folioName=#{folioName} or folioName=(SELECT folioName FROM folio WHERE showName=#{folioName} AND userId=#{userId} and parentDir=#{parentDir} and isNotDel=1 LIMIT 0,1)) ORDER BY showName DESC LIMIT 0,1")
	Folio selectOneByFolioNmae(@Param("userId") int userId, @Param("parentDir") int parentDir,
			@Param("folioName") String fname);
}
