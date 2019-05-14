package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ax.springmvc.pojo.Folio;

public interface FolioMapper {
	/**
	 * 得到一个用在该目录下所由的文件夹
	 * 
	 * @param userId    哪个用户的
	 * @param parentDir 父文件夹Id
	 * @param isNotDel  是否删除
	 * @return
	 */
	List<Folio> selectByUser(@Param("userId") int userId, @Param("parentDir") int parentDir,
			@Param("isNotDel") int isNotDel,@Param("isNotShare") int isNotShare);

	/**
	 * 按id得到一条数据
	 * 
	 * @param id
	 * @return
	 */
	@Select("SELECT * FROM `folio` WHERE id=#{id}")
	Folio selectOne(@Param("id") int id);

	/**
	 * 创建文件夹
	 * 
	 * @param folio
	 * @return
	 */
	int createFolio(Folio folio);

	/**
	 * 修改文件夹
	 * 
	 * @param folio
	 * @return
	 */
	@Update("UPDATE `skydrive`.`folio` SET `parentDir` = #{parentDir},`folioName` = #{folioName},`fileUpdateTime`=NOW(),`isNotDel`=#{isNotDel},`showName`=#{showName} WHERE `id` = #{id}")
	int updateFolio(Folio folio);

	/**
	 * 删除文件夹
	 * 
	 * @param id
	 * @return
	 */
	@Delete("delete from folio where id = #{id}")
	int deleteFolio(@Param("id") int id);

	/**
	 * 批量修改文件信息
	 */
	int updateFolios(@Param("folios") List<Folio> folios);

	/**
	 * 批量将文件夹删除到回收站
	 */
	int updateFolisTodel(@Param("ids") List<Integer> ids, @Param("isNotDel") int isNotDel);

	/**
	 * 批量查询文件夹信息
	 * 
	 * @param ids
	 * @return
	 */
	List<Folio> selectByIds(@Param("ids") List<Integer> ids);

	/**
	 * 批量删除文件夹
	 * 
	 * @param ids
	 * @return
	 */
	int deleteFolios(@Param("ids") List<Integer> ids);

	/**
	 * 更具用户Id得到一个目录下的,且rname最大的文件夹名称相同的文件夹
	 * 
	 * @return
	 */
	@Select("select * from folio where userId=#{userId} and parentDir=#{parentDir} and isNotDel=1 and isNotShare=1 and (folioName=#{folioName} or folioName=(SELECT folioName FROM folio WHERE showName=#{folioName} AND userId=#{userId} and parentDir=#{parentDir} and isNotDel=1 LIMIT 0,1)) ORDER BY showName DESC LIMIT 0,1")
	Folio selectOneByFolioNmae(@Param("userId") int userId, @Param("parentDir") int parentDir,
			@Param("folioName") String fname);
}
