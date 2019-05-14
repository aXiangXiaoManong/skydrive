package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ax.springmvc.pojo.FileShare;

/**
 * @author asus
 *
 */
public interface FileShareMapper {
	/**
	 ** ���һ�η����¼
	 */
	int addShareFile(@Param("fileShare") FileShare fileShare, @Param("shareDay") Integer shareDay);

	/**
	 ** �õ�һ���û������з����¼
	 */
	@Select("SELECT * FROM FileShare WHERE userId = #{userId}")
	List<FileShare> queryByUserId(@Param("userId") int userId);

	/**
	 ** �õ�һ���û��ı������¼
	 */
	//@Select("SELECT * FROM FileShare F,shareuser S WHERE S.fileShareId = F.id AND S.beuserId=#{userId}")
	List<FileShare> queryByBeUserId(@Param("userId") int userId);

	/**
	 ** �õ�һ���û��ķ������¼
	 */
	//@Select("SELECT * FROM FileShare F,shareuser S WHERE fileShareId = F.id AND F.userId=#{userId}")
	List<FileShare> queryByToUserId(@Param("userId") int userId);

	/**
	 ** �õ�һ���û����Ƿ���Ч״̬�ķ����¼
	 */
	@Select("select * from FileShare where userId = #{userId} and isNotEffective=#{isNotEffective}")
	List<FileShare> queryByUserIdAndIsEff(@Param("userId") int userId, @Param("isNotEffective") int isNotEffective);

	/**
	 ** �õ�һ���û�������һ���û������з����¼
	 */
	@Select("SELECT * FROM FileShare WHERE (userId = #{userId} AND id IN(SELECT fileShareId FROM `shareuser` WHERE `beUserId`=#{beUserId})) OR (userId = #{beUserId} AND id IN (SELECT fileShareId FROM `shareuser` WHERE `beUserId`=#{userId}))")
	List<FileShare> queryByUserIdAndBeUserId(@Param("userId") int userId, @Param("beUserId") int beUserId);

	/**
	 ** �õ�һ�������¼
	 */
	@Select("select * from FileShare where id=#{id}")
	FileShare queryOne(@Param("id") int id);

	/**
	 ** �޸ķ����¼
	 * 
	 * @param id
	 * @return
	 */
	@Update("UPDATE `skydrive`.`fileshare` SET `userId` = #{userId},`isNotEffective` = #{isNotEffective},`downloadNum` = #{downloadNum} where `id` = #{id}")
	int update(FileShare fileShare);
}
