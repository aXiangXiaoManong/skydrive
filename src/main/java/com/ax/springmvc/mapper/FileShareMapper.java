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
	 ** 添加一次分享记录
	 */
	int addShareFile(@Param("fileShare") FileShare fileShare, @Param("shareDay") Integer shareDay);

	/**
	 ** 得到一个用户的所有分享记录
	 */
	@Select("SELECT * FROM FileShare WHERE userId = #{userId}")
	List<FileShare> queryByUserId(@Param("userId") int userId);

	/**
	 ** 得到一个用户的被分享记录
	 */
	//@Select("SELECT * FROM FileShare F,shareuser S WHERE S.fileShareId = F.id AND S.beuserId=#{userId}")
	List<FileShare> queryByBeUserId(@Param("userId") int userId);

	/**
	 ** 得到一个用户的分享享记录
	 */
	//@Select("SELECT * FROM FileShare F,shareuser S WHERE fileShareId = F.id AND F.userId=#{userId}")
	List<FileShare> queryByToUserId(@Param("userId") int userId);

	/**
	 ** 得到一个用户且是否有效状态的分享记录
	 */
	@Select("select * from FileShare where userId = #{userId} and isNotEffective=#{isNotEffective}")
	List<FileShare> queryByUserIdAndIsEff(@Param("userId") int userId, @Param("isNotEffective") int isNotEffective);

	/**
	 ** 得到一个用户和另外一个用户的所有分享记录
	 */
	@Select("SELECT * FROM FileShare WHERE (userId = #{userId} AND id IN(SELECT fileShareId FROM `shareuser` WHERE `beUserId`=#{beUserId})) OR (userId = #{beUserId} AND id IN (SELECT fileShareId FROM `shareuser` WHERE `beUserId`=#{userId}))")
	List<FileShare> queryByUserIdAndBeUserId(@Param("userId") int userId, @Param("beUserId") int beUserId);

	/**
	 ** 得到一条分享记录
	 */
	@Select("select * from FileShare where id=#{id}")
	FileShare queryOne(@Param("id") int id);

	/**
	 ** 修改分享记录
	 * 
	 * @param id
	 * @return
	 */
	@Update("UPDATE `skydrive`.`fileshare` SET `userId` = #{userId},`isNotEffective` = #{isNotEffective},`downloadNum` = #{downloadNum} where `id` = #{id}")
	int update(FileShare fileShare);
}
