package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.pojo.Folio;

public interface FileInfoMapper {
	/**
	 * 得到一个用在该目录下所由的文件
	 * 
	 * @param userId    哪个用户的
	 * @param parentDir 父文件夹Id
	 * @param isNotDel  是否删除
	 * @return
	 */
	List<FileInfo> selectByUserId(@Param("userId") int userId, @Param("parentDir") int parentDir,
			@Param("isNotDel") int isNotDel, @Param("isNotShare") int isNotShare);

	/**
	 * 更具文件类型得到该用户指定的文件
	 * 
	 * @param userId    哪个用户
	 * @param fileTypes 文件类型
	 * @return
	 */
	List<FileInfo> selectByType(@Param("userId") int userId, @Param("fileTypes") String[] fileTypes,
			@Param("isNotShare") int isNotShare);

	/**
	 * 修改文件信息
	 * 
	 * @param fileInfo
	 * @return
	 */
	@Update("UPDATE `skydrive`.`fileinfo` SET `fileName` = #{fileName},`fileSrc` = #{fileSrc},`parentDir` = #{parentDir},`fileType` = #{fileType},`isNotDel`=#{isNotDel},`fileUpdateTime` = NOW(),`showName`=#{showName} WHERE `id` = #{id}")
	int updateFileInfo(FileInfo fileInfo);

	/**
	 * 更具文件id得到文件信息
	 * 
	 * @param id
	 * @return
	 */
	@Select("select * from fileInfo where id = #{id}")
	FileInfo selectOne(@Param("id") int id);

	/**
	 * 删除文件
	 * 
	 * @param id
	 * @return
	 */
	@Delete("delete from fileinfo where id = #{id}")
	int deleteFileInfo(@Param("id") int id);

	/**
	 * 批量添加文件
	 * 
	 * @param fileInfos
	 * @return
	 */
	int fileUpLoad(@Param("fileInfos") List<FileInfo> fileInfos);

	/**
	 * 添加一条文件记录
	 * 
	 * @param fileInfo
	 * @return
	 */
	int newFile(FileInfo fileInfo);

	/**
	 * 批量修改文件
	 */
	int updateFileInfos(@Param("fileInfos") List<FileInfo> fileInfos);

	/**
	 * 批量将文件删除到回收站
	 */
	int updateFileTodel(@Param("ids") List<Integer> ids, @Param("isNotDel") int isNotDel);

	/**
	 * 批量查询文件
	 */
	List<FileInfo> selectByIds(@Param("ids") List<Integer> ids);

	/**
	 * 批量彻底删除文件
	 */
	int deleteFiles(@Param("ids") List<Integer> ids);

	/**
	 ** 为了解决文件同名问题
	 */
	@Select("select * from fileInfo where userId=#{userId} and parentDir=#{parentDir} and isNotDel=1 and isNotShare=1 and (fileName=#{fileName} or fileName=(SELECT fileName FROM fileInfo WHERE showName=#{fileName} AND userId=#{userId} and parentDir=#{parentDir} and isNotDel=1 LIMIT 0,1)) ORDER BY fileUpdateTime DESC LIMIT 0,1")
	FileInfo selectOneByFileNmae(@Param("userId") int userId, @Param("parentDir") int parentDir,
			@Param("fileName") String fileName);

	/**
	 ** 模糊查询文件
	 * 
	 * @param userId
	 * @param fileName
	 * @return
	 */
	@Select("select * from fileInfo where userId = #{userId} and showName like CONCAT('%',#{fileName,jdbcType = VARCHAR},'%') and isNotDel=1 and isNotShare=1")
	List<FileInfo> listByLikeFileName(@Param("userId") int userId, @Param("fileName") String fileName);
}
